package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;

import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet1Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;


public class AddUser {
    
    private MainPage mainPage;

    @FXML
    private MenuItem addEmployeeClicked;

    @FXML
    private MenuItem addGuestClicked;

    @FXML
    private Button goBackButton;

    @FXML
    private TextField userEmailTextField;

    @FXML
    private TextField userNameTextField;

    @FXML
    private PasswordField userPasswordTextField;

    @FXML
    private TextField userPhoneNumberTextField;

    /** Calls the controller create a new employee  with the info input in the textfield, resets them when it works
    * @author Marc-Antoine Nadeau - Student ID: 261114549
    * @return void
    */

    @FXML
    void addEmployeeClicked(ActionEvent event) {
      String name = userNameTextField.getText();
      String email = userEmailTextField.getText();
      String phoneNumber = userPhoneNumberTextField.getText();
      String password = userPasswordTextField.getText();

      if (ViewUtils.successful(AssetPlusFeatureSet1Controller.addEmployeeOrGuest(email,password, name, phoneNumber, true))){
        userNameTextField.setText("");
        userEmailTextField.setText("");
        userPhoneNumberTextField.setText("");
        userPasswordTextField.setText("");
      }

    }
    /** Calls the controller create a new guest  with the info input in the textfield, resets them when it works
    * @author Marc-Antoine Nadeau - Student ID: 261114549
    * @return void
    */
    @FXML
    void addGuestClicked(ActionEvent event) {
      String name = userNameTextField.getText();
      String email = userEmailTextField.getText();
      String phoneNumber = userPhoneNumberTextField.getText();
      String password = userPasswordTextField.getText();

      if (ViewUtils.successful(AssetPlusFeatureSet1Controller.addEmployeeOrGuest(email,password, name, phoneNumber, false))){
        userNameTextField.setText("");
        userEmailTextField.setText("");
        userPhoneNumberTextField.setText("");
        userPasswordTextField.setText("");
      }

    }
    /** Returns to the MainPage.fxml page and move to the right tab (viewUsers)
    * @author Marc-Antoine Nadeau - Student ID: 261114549
    * @return void
    */
  
    @FXML
    void goBackClicked(ActionEvent event) {
       try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("../MainPage.fxml"));
          Parent root = loader.load();

          // Get the current Stage
          Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

          // Set the new root for the current Scene
          stage.getScene().setRoot(root);
          mainPage = loader.getController();

          mainPage.selectTab(2);
          
      
        } catch (IOException e) {
          e.printStackTrace();
          ViewUtils.showError("Error Changing Page\n");
        }
      }
    }