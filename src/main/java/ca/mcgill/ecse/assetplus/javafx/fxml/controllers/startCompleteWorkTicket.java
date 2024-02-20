package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;

import java.io.IOException;

import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet5Controller;
import ca.mcgill.ecse.assetplus.controller.TicketMaintenanceController;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author: Dmytro Martyuniuk
 * Sets the progress of the opened ticket to
 * in progress when start work pressed or
 * closed when complete work is preseed 
 */

public class startCompleteWorkTicket {
  
  private MainPage mainPage;
  
  @FXML
  private TextField ticketIDTextField;
  @FXML
  private Button startWorkButton;
  @FXML
  private Button completeWorkButton;
  @FXML
  private Button backButton;

    /**
   * @author: Dmytro Martyuniuk
  * Initializes the controller, setting default values for the ticket id
  */ 
  @FXML
  public void initialize() {
    int ticketId = ViewTicketsPage.getTicketID();
    if (ticketId != -1) {
        ticketIDTextField.setText(String.valueOf(ViewTicketsPage.getTicketID()));
    }
  }
  

    /**
     * @author: Dmytro Martyuniuk
     * Handles the start work of a maintenance ticket.
     * Validates user input and displays error messages if needed.
     *
     * @param event The event triggering the start work action.
     */
  @FXML
  public void startWorkButtonClicked(ActionEvent event) {
    String ticketId = ticketIDTextField.getText();

    if (ticketId == null || ticketId.trim().isEmpty()) {
      ViewUtils.showError("Please input a valid ticketID");
    }
    else if (MaintenanceTicket.getWithId(Integer.parseInt(ticketId)).getTicketStatus() == MaintenanceTicket.TicketStatus.Open) {
      ViewUtils.showError("Please assign an user to this ticket first"); //this error message replaces "Cannot start a maintenance ticket which is open"
    }
    else if (ViewUtils.successful(TicketMaintenanceController.beginTicketWork(ticketId))) {

      ticketIDTextField.setText("");
    }
  }

    /**
     * @author: Dmytro Martyuniuk
     * Handles the completion of a maintenance ticket.
     * Validates user input and displays error messages if needed.
     *
     * @param event The event triggering the complete work action.
     */
  @FXML
  public void completeWorkButtonClicked(ActionEvent event) {
    String ticketId = ticketIDTextField.getText();

    if (ticketId == null || ticketId.trim().isEmpty()) {
      ViewUtils.showError("Please input a valid ticketID");
    }
    else if (MaintenanceTicket.getWithId(Integer.parseInt(ticketId)).getTicketStatus() == MaintenanceTicket.TicketStatus.Assigned) {
      ViewUtils.showError("Please start this work first"); //this error message replaces "Cannot start a maintenance ticket which is assigned"
    }
    else if (ViewUtils.successful(TicketMaintenanceController.completeTicketWork(ticketId))) {

      ticketIDTextField.setText("");
    }
  }
      /**
     * @author: Dmytro Martyuniuk
     * Returns to the view tickets page page when the corresponding button is clicked.
     * Acts as a cancel and go back button.
     * @param event The event triggering the cancel action.
     */
  @FXML
  public void backButtonClicked(ActionEvent event) {
    try {

      FXMLLoader loader = new FXMLLoader(getClass().getResource("../MainPage.fxml"));
      Parent root = loader.load();

      // Get the current Stage
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

      // Set the new root for the current Scene
      stage.getScene().setRoot(root);
      mainPage = loader.getController();

      mainPage.selectTab(1);

    } catch (IOException e) {
      e.printStackTrace();
      ViewUtils.showError("Error Changing Page\n");
    }
  }
}
