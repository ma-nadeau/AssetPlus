package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;

import ca.mcgill.ecse.assetplus.controller.*;
import java.sql.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class addUpdateDeleteAsset {

    @FXML
    private Button addAsset;

    @FXML
    private TextField assetNumberTextField;

    @FXML
    private TextField assetTypeTextField;

    @FXML
    private Button deleteAsset;

    @FXML
    private TextField floorNumberTextField;

    @FXML
    private DatePicker purchaseDateDatePicker;

    @FXML
    private TextField roomNumberTextField;

    @FXML
    private Button updateAsset;

    @FXML
    void addAssetClicked(ActionEvent event) {
      int assetNumber = Integer.parseInt(assetNumberTextField.getText());
      int floorNumber = Integer.parseInt(floorNumberTextField.getText());
      int roomNumber = Integer.parseInt(roomNumberTextField.getText());
      Date purchaseDate = Date.valueOf(purchaseDateDatePicker.getValue());
      String assetTypeName = assetTypeTextField.getText();

      if (ViewUtils.successful(AssetPlusFeatureSet3Controller.addSpecificAsset(assetNumber, floorNumber, roomNumber, purchaseDate, assetTypeName))){
        assetNumberTextField.setText("");
        floorNumberTextField.setText("");
        roomNumberTextField.setText("");
        purchaseDateDatePicker.setValue(null);
        assetTypeTextField.setText("");
      }
    }

    @FXML
    void deleteAssetClicked(ActionEvent event) {
      int assetNumber = Integer.parseInt(assetNumberTextField.getText());

      AssetPlusFeatureSet3Controller.deleteSpecificAsset(assetNumber);
      assetNumberTextField.setText("");
    }

    /*@FXML
    void updateAssetClicked(ActionEvent event) {

      int assetNumber = Integer.parseInt(assetNumberTextField.getText());
      int newFloorNumber = Integer.parseInt(floorNumberTextField.getText());
      int newRoomNumber = Integer.parseInt(roomNumberTextField.getText());
      Date newPurchaseDate = Date.valueOf(purchaseDateDatePicker.getValue());
      String newAssetType = assetTypeTextField.getText();

      if (ViewUtils.successful(AssetPlusFeatureSet3Controller.updateSpecificAsset(assetNumber, newFloorNumber, newRoomNumber, newPurchaseDate, newAssetType))){
        assetNumberTextField.setText("");
        floorNumberTextField.setText("");
        roomNumberTextField.setText("");
        purchaseDateDatePicker.setValue(null);
        assetTypeTextField.setText("");
      }
    }*/
}