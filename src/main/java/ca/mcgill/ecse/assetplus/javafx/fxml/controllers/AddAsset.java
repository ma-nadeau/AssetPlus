package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;

import java.sql.Date;
import java.time.LocalDate;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet3Controller;
import ca.mcgill.ecse.assetplus.controller.ExtraFeaturesController;
import javafx.scene.control.DatePicker;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * @author Jatin Patel
 * The AddAsset view controller provides methods for the user to add a new asset to the model through
 * the AssetPlus controller
 */

public class AddAsset {

    private MainPage mainPage;

    @FXML
    private Button addAsset;

    @FXML
    private TextField assetNumberTextField;

    @FXML
    private ComboBox<String> assetTypeTextField;

    @FXML
    private TextField floorNumberTextField;

    @FXML
    private Button goBack;

    @FXML
    private DatePicker purchaseDateDatePicker;

    @FXML
    private TextField roomNumberTextField;

    /**
     * @author Jatin Patel
     * @return void
     * Initializes the assetTypeTextField with all the asset types in the model
     */
    @FXML
    public void initialize(){
      assetTypeTextField.setItems(FXCollections.observableArrayList(ExtraFeaturesController.getAllAssetTypes()));
      purchaseDateDatePicker.setValue(LocalDate.now());

    }

    /** Calls the controller method addSpecificAsset with the info input in the textfield, resets them when it works
    * @author Jatin Patel
    * @return void
    */
    @FXML
    void addAsset(ActionEvent event) {
      int assetNumber = Integer.parseInt(assetNumberTextField.getText());
      int floorNumber = Integer.parseInt(floorNumberTextField.getText());
      int roomNumber = Integer.parseInt(roomNumberTextField.getText());
      Date purchaseDate = Date.valueOf(purchaseDateDatePicker.getValue());
      String assetTypeName = assetTypeTextField.getValue();
      
      if(assetTypeName==null){
        ViewUtils.showError("Select an asset type");
        return;
      }

      if (ViewUtils.successful(AssetPlusFeatureSet3Controller.addSpecificAsset(assetNumber, floorNumber, roomNumber, purchaseDate, assetTypeName))){
        assetNumberTextField.setText("");
        floorNumberTextField.setText("");
        roomNumberTextField.setText("");
        purchaseDateDatePicker.setValue(null);
        assetTypeTextField.setValue(null);
      }

    }

    /** Changes the scene to the main page, and selects the asset tab 3 (ViewAssets.fxml).
    * @author Jatin Patel
    * @return void
    */
    @FXML
    void goBack(ActionEvent event) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../MainPage.fxml"));
        Parent root = loader.load();

        // Get the current Stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new root for the current Scene
        stage.getScene().setRoot(root);
        mainPage = loader.getController();

        mainPage.selectTab(3);
        
    
      } catch (IOException e) {
        e.printStackTrace();
        ViewUtils.showError("Error Changing Page\n");
      }
    }

}

