package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;

import java.io.IOException;

import ca.mcgill.ecse.assetplus.controller.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/*
 * @author: Dmytro Martyuniuk
 * Adds image to a maitencnace ticket with the correct TicketID
 */

public class addImage {

  @FXML
  public Button cancelButton;
  @FXML
  private TextField imageURLTextField;
  @FXML
  private TextField idTextField;
  @FXML
  private Button submitButton;
  @FXML
  private ImageView image;

  /**
  * @author: Dmytro Martyuniuk
  * Initializes the controller, setting default values for the ticket id
  */ 
  @FXML
  public void initialize() {
  // Set the value of userEmailTextField from ViewUserPage.currentUser
  if(idTextField!=null){
  if (ViewTicketsPage.getTicketID() != -1) {
      idTextField.setText(String.valueOf(ViewTicketsPage.getTicketID()));
  } else {
      idTextField.setText("");
  }}
  }
  
    /**
     * @author: Dmytro Martyuniuk
     * Handles the addition of a new ticket image to the corresponding ticket id.
     * Validates user input and displays error messages if needed.
     *
     * @param event The event triggering the add image action.
     */
  @FXML
  public void submitButtonClicked(ActionEvent event) {

    String url = imageURLTextField.getText();
    String ticketId = idTextField.getText();
    int ID;

    if (!(ticketId == null || ticketId == "")) {
      ID = Integer.parseInt(ticketId);
    } else {
      ID = -1;// ID is empty
    }
    if (url == null || url.trim().isEmpty()) {
        ViewUtils.showError("Please input a valid image URL");
    } else if (ID < 1) {
        ViewUtils.showError("Please input a valid ticketID");
    } else if (ViewUtils.successful(AssetPlusFeatureSet5Controller.addImageToMaintenanceTicket(url, ID))) {
        // Adds the image to the ticket
        // Resets the fields
        image.setImage(new Image(imageURLTextField.getText()));
        imageURLTextField.setText("");        
    }
  }

      /**
     * @author: Dmytro Martyuniuk
     * Returns to the view page page when the corresponding button is clicked.
     * Acts as a cancel and go back button.
     * @param event The event triggering the cancel action.
     */
  @FXML
  public void cancelButtonClicked(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/ViewImageNotes.fxml"));
      Parent root = loader.load();

      // Get the current Stage
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

      // Set the new root for the current Scene
      stage.getScene().setRoot(root);
      } catch (IOException e) {
          e.printStackTrace();
          ViewUtils.showError("Error opening Add User page");
      }
  }
}
