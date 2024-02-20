package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import javafx.scene.Node;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet3Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet6Controller;
import ca.mcgill.ecse.assetplus.controller.ExtraFeaturesController;
import ca.mcgill.ecse.assetplus.controller.TOAsset;
import ca.mcgill.ecse.assetplus.javafx.fxml.AssetPlusFxmlView;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.SysexMessage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.TextField;


/**
 * Controller for the ViewAssets Page, where the user can view, filter, and manage the assets in 
 * the assetPlus system. 
 * @author Behrad Rezaie
 */
public class ViewAssetsPage {

    private static int currentAsset;

    @FXML
    private TableView<TOAsset> AssetView;

    @FXML
    private TextField assetFloorField;

    @FXML
    private TextField assetIDField;

    @FXML
    private ComboBox<String> assetTypeField;

    @FXML
    private Button deleteAsset;

    @FXML
    private Button openAddAsset;

    @FXML
    private Button openUpdateAsset;

    @FXML
    private Button refreshAsset;

    /**
     * Method that takes the selected asset from the tableView, and deletes it from the system.
     * Shows an error if no asset is selected.
     * @author Behrad Rezaie
     */
    @FXML
    void deleteAsset(ActionEvent event) {
      System.out.println("Selected User Email: " + currentAsset);
      if (currentAsset != 0) {

        AssetPlusFeatureSet3Controller.deleteSpecificAsset(currentAsset);
        refreshAsset(new ActionEvent());
        currentAsset = 0;
        
      } else {
            ViewUtils.showError("Select Asset to Delete");
      }

    }
      
    /**
     * Method that opens the AddAsset page. 
     * Method is called when the associated button is clicked.
     * @author Behrad Rezaie
     */
    @FXML
    void openAddAsset(ActionEvent event) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/AddAsset.fxml"));
        Parent root = loader.load();

        // Get the current Stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new root for the current Scene
        stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            ViewUtils.showError("Error opening Add Asset page");
        }
    }
    /**
     * Method that opens the update asset page. 
     * Method is called when the associated button is clicked.
     * @author Behrad Rezaie
     */
    @FXML
    void openUpdateAsset(ActionEvent event) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/UpdateAsset.fxml"));
        Parent root = loader.load();

        // Get the current Stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new root for the current Scene
        stage.getScene().setRoot(root);
      } catch (IOException e) {
      e.printStackTrace();
      ViewUtils.showError("Error opening Update Asset page\n");
    }
    }

    /**
     * Method that opens the assetType management page. 
     * Method is called when the associated button is clicked.
     * @author Behrad Rezaie
     */
    @FXML
    void openAssetType(ActionEvent event) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/addUpdateDeleteAssetType.fxml"));
        Parent root = loader.load();

        // Get the current Stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new root for the current Scene
        stage.getScene().setRoot(root);
      } catch (IOException e) {
      e.printStackTrace();
      ViewUtils.showError("Error opening Asset Type page\n");
    }
  }

    /**
     * Method checks if an asset row in the tableView is clicked, and sets the 
     * private field accordingly.
     * @author Behrad Rezaie
     */
    @FXML
    void selectAsset(MouseEvent event) {
    TOAsset selectedAsset = AssetView.getSelectionModel().getSelectedItem();
    currentAsset = selectedAsset.getAssetNumber();
    }

    /**
     * Public method that returns the assetID of the selected asset. 
     * Can be accessed by different pages to initialize their own values.
     * @author Behrad Rezaie
     * @return integer ID of the asset
     */
    public static int getAssetID(){
        return currentAsset;
    }
    /**
     * Private method that resets the private field to -1. Serves to reset the selected asset 
     * when required.
     * @author Behrad Rezaie
     */
    private static void setCurrentIDToNull() {
      //setting it to -1
      currentAsset = -1;
  }

    /**
     * Method that refreshes assets upon ActionEvents. Used for when an AssetType filter is selected.
     * @author Behrad Rezaie
     */
    @FXML
    void refreshAsset(ActionEvent event){
      List<TOAsset> assets = getFilteredAssets();
      
      AssetView.setItems(FXCollections.observableList(assets));

      AssetPlusFxmlView.getInstance().refresh();
    }
    /**
     * Method that refreshes assets upon text input. Used for when the user is filtering 
     * by ID or floor number
     * @author Behrad Rezaie
     */
    @FXML
    void refreshAssets(KeyEvent event){
      AssetView.setItems(FXCollections.observableList(getFilteredAssets()));
      AssetPlusFxmlView.getInstance().refresh();
    }
    
    /**
     * Method that clears the selected AssetType filter upon the box being clicked on.
     * @author Behrad Rezaie
     */
    @FXML
    void clearType(MouseEvent event) {
      assetTypeField.setValue(null);
      refreshAsset(new ActionEvent());
    }

    /** 
     * Initilization of the ViewAssets Page. Sets the columns of the assets tableView to the 
     * relevant fields of the TOAsset, as well as its default placeholder message when no assets
     * are found.
     * @author Behrad Rezaie
     */
    public void initialize(){
      System.out.println("initialized");

      refreshAsset(new ActionEvent());
      setCurrentIDToNull();
      
      assetTypeField.setItems(FXCollections.observableArrayList(ExtraFeaturesController.getAllAssetTypes()));

      
      
      AssetView.setPlaceholder(new Label("No assets found"));

      TableColumn<TOAsset, Integer> assetNumber = new TableColumn<TOAsset, Integer>("ID");
      assetNumber.setCellValueFactory(new PropertyValueFactory<TOAsset, Integer>("assetNumber"));
      
      TableColumn<TOAsset, Integer> floorNumber = new TableColumn<TOAsset, Integer>("Floor");
      floorNumber.setCellValueFactory(new PropertyValueFactory<TOAsset, Integer>("floorNumber"));

      TableColumn<TOAsset, Integer> roomNumber = new TableColumn<TOAsset, Integer>("Room");
      roomNumber.setCellValueFactory(new PropertyValueFactory<TOAsset, Integer>("roomNumber"));

      TableColumn<TOAsset, Date> purchaseDate = new TableColumn<TOAsset, Date>("Date Purchased");
      purchaseDate.setCellValueFactory(new PropertyValueFactory<TOAsset, Date>("purchaseDate"));

      TableColumn<TOAsset, String> assetType = new TableColumn<TOAsset, String>("Asset Type");
      assetType.setCellValueFactory(new PropertyValueFactory<TOAsset, String>("type"));

      
      AssetView.getColumns().add(assetNumber);
      AssetView.getColumns().add(assetType);
      AssetView.getColumns().add(floorNumber);
      AssetView.getColumns().add(roomNumber);
      AssetView.getColumns().add(purchaseDate);
      
      AssetView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    /**
     * Public method that returns a list of Asset Transfer Objects based on the active filters on the page.
     * 
     * @author Behrad Rezaie
     * @return List<TOAsset> filtered assets based on active filters
     */
    public List<TOAsset> getFilteredAssets(){
      List<TOAsset> allAssets = ExtraFeaturesController.getAllAssets();
      
      
      Integer filterID = null;
      Integer filterFloor = null;
      String assetType = "";

      List<TOAsset> filteredByID = ExtraFeaturesController.getAllAssets();
      List<TOAsset> filteredByFloor = ExtraFeaturesController.getAllAssets();
      List<TOAsset> filteredByType = ExtraFeaturesController.getAllAssets();
      try{
        if(assetIDField.getText() == ""){
          filterID = null;
        }else{
         filterID = Integer.parseInt(assetIDField.getText());
        }
        if(assetFloorField.getText() == ""){
          filterFloor = null;
        }else{
         filterFloor = Integer.parseInt(assetFloorField.getText());
        }
         assetType = assetTypeField.getValue();
         if(assetType==null){
          assetType="";
         }
      }catch(Exception e){
        ViewUtils.showError("Invalid input: "+e.getMessage());
        return ExtraFeaturesController.getAllAssets();
      }
    
      if(filterID==null && filterFloor==null && assetType==""){
        return filteredByID;
      }

      if(filterID!=null){
        filteredByID = ExtraFeaturesController.getAssetByNumber(filterID);

        if(filteredByID.isEmpty()){
          System.out.println("NO ASSETS TO LOAD");

          return filteredByID;
        }else{
          if(filterFloor != null && filteredByID.get(0).getFloorNumber()!=filterFloor){
            System.out.println("WRONG FLOOR");
            return new ArrayList<TOAsset>();}
          if(assetType != "" && !(filteredByID.get(0).getType().equals(assetType))){
            System.out.println(assetType);
            System.out.println(filteredByID.get(0).getType());
            System.out.println("WRONG TYPE");
            return new ArrayList<TOAsset>();
          }
          return filteredByID;
        }
      }

      if(filterFloor != null){
        filteredByFloor = ExtraFeaturesController.getAssetsByFloor(filterFloor);
        System.out.println("filtering by floor");
        for (TOAsset asset : filteredByFloor) {
          System.out.println(asset);
        }
      }
      if(assetType!=""){
        filteredByType = ExtraFeaturesController.getAssetsByType(assetType);
        System.out.println("filteeed by type");
        for (TOAsset toAsset : filteredByType) {
          System.out.println(toAsset);
        }
      }



      List<TOAsset> filteredAssets = new ArrayList<TOAsset>();
      for (TOAsset floorAsset : filteredByFloor) {
        System.out.println("LOOP");

        for (TOAsset typeAsset : filteredByType) {
          System.out.println(typeAsset);
          if (typeAsset.getAssetNumber()==floorAsset.getAssetNumber()
              && typeAsset.getFloorNumber()==floorAsset.getFloorNumber()
              && typeAsset.getType().equals(floorAsset.getType())
              && typeAsset.getRoomNumber() ==floorAsset.getRoomNumber()
              && typeAsset.getPurchaseDate().equals(floorAsset.getPurchaseDate())){
            filteredAssets.add(typeAsset);
          }
        }
      }

      return filteredAssets;
    }
}