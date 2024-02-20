package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;

import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet1Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;


public class UpdateUser {

    private MainPage mainPage;



    @FXML
    private Button cancelButton;

    @FXML
    private TextField userEmailTextField;

    @FXML
    private TextField userNameTextField;

    @FXML
    private PasswordField userPasswordTextField;

    @FXML
    private TextField userPhoneNumberTextField;

    /** Initilizes the filed userEmailTextField to the value of the email of the selected user
    * @author Marc-Antoine Nadeau - Student ID: 261114549
    * @return void
    */
    @FXML
    public void initialize() {
      if (ViewUserPage.getUserEmail() != null) {
        userEmailTextField.setText(ViewUserPage.getUserEmail());
      } else {
        userEmailTextField.setText("");
      }
    }
    /** Returns to the MainPage.fxml page and move to the right tab (ViewUsers.fxml)
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

    /** Calls the controller update the employee or guest with the info input in the textfield, resets them when it works
    * @author Marc-Antoine Nadeau - Student ID: 261114549
    * @return void
    */

    @FXML
    void updateEmployeeOrGuestClicked(ActionEvent event) {
      String name = userNameTextField.getText();
      String email = userEmailTextField.getText();
      String phoneNumber = userPhoneNumberTextField.getText();
      String password = userPasswordTextField.getText();
      if (ViewUtils.successful(AssetPlusFeatureSet1Controller.updateEmployeeOrGuest(email,password,name,phoneNumber))){
        userNameTextField.setText("");
        userEmailTextField.setText("");
        userPhoneNumberTextField.setText("");
        userPasswordTextField.setText("");
      }

    }
     /** Calls the controller update password value of the manager. Ignore all the field except password and resets them all to null when it works
    * @author Marc-Antoine Nadeau - Student ID: 261114549
    * @return void
    */
    @FXML
    void updateManagerClicked(ActionEvent event) {
      String name = userNameTextField.getText();
      String email = userEmailTextField.getText();
      String phoneNumber = userPhoneNumberTextField.getText();
      String password = userPasswordTextField.getText();
      if (ViewUtils.successful(AssetPlusFeatureSet1Controller.updateManager(password))){
        userNameTextField.setText("");
        userEmailTextField.setText("");
        userPhoneNumberTextField.setText("");
        userPasswordTextField.setText("");
      }
      
    }

}