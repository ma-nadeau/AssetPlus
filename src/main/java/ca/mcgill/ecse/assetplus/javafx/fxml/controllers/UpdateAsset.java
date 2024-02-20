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
 * The UpdateAsset view controller provides methods for the user to update an asset through
 * the AssetPlus controller
 */
public class UpdateAsset {

    private MainPage mainPage;

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

    @FXML
    private Button updateAsset;

    /**
     * @author Jatin Patel
     * @return void
     * Initializes the assetTypeTextField with all the asset types in the model
     */
    @FXML
    public void initialize() {
      Integer assetID = ViewAssetsPage.getAssetID();
      purchaseDateDatePicker.setEditable(false);
      purchaseDateDatePicker.setValue(LocalDate.now());
      if (assetID != -1) {
          assetNumberTextField.setText(String.valueOf(ViewAssetsPage.getAssetID()));
      } else {
          assetNumberTextField.setText("");
      }
      assetTypeTextField.setItems(FXCollections.observableArrayList(ExtraFeaturesController.getAllAssetTypes()));
    }
    
    
    /** Changes the scene to the main page, and selects the asset tab 3 (ViewAssets.fxml)
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

    /** Calls the controller method updateSpecificAsset with the info input in the textfield, resets them when it works
    * @author Jatin Patel
    * @return void
    */
    @FXML
    void updateAsset(ActionEvent event) {
      int assetNumber;
      int newFloorNumber;
      int newRoomNumber=-1;
      Date newPurchaseDate;
      try{
       assetNumber = Integer.parseInt(assetNumberTextField.getText());
       newFloorNumber = Integer.parseInt(floorNumberTextField.getText());
       if(roomNumberTextField.getText()!=""){
          newRoomNumber = Integer.parseInt(roomNumberTextField.getText());
       }
       newPurchaseDate = Date.valueOf(purchaseDateDatePicker.getValue());}
      catch(NumberFormatException e){
        ViewUtils.showError("Please enter an integer for the asset number, floor number, and room number");
        return;
      }
      String newAssetType = assetTypeTextField.getValue();
      if(newAssetType==null){
        ViewUtils.showError("Select an asset type");
        return;
      }

      String result = AssetPlusFeatureSet3Controller.updateSpecificAsset(assetNumber, newFloorNumber, newRoomNumber, newPurchaseDate, newAssetType);
      if (result.isBlank()){
        assetNumberTextField.setText("");
        floorNumberTextField.setText("");
        roomNumberTextField.setText("");
        purchaseDateDatePicker.setValue(null);
        assetTypeTextField.setValue(null);
      }else{
        ViewUtils.showError(result);
      }
    }

}
