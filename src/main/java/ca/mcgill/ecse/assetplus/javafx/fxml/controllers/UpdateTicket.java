package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;

import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import ca.mcgill.ecse.assetplus.controller.*;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

/**
 * @author Pei Yan Geng
 * The UpdateTicket controller provides methods for the user to interact with the model through
 * the AssetPlus controller and navigate through the pages.
 */

public class UpdateTicket {
    private static AssetPlus assetPlus = AssetPlusApplication.getAssetPlus(); // can I do this?
    @FXML
    public Button backButton;

    private MainPage mainPage;

    @FXML
    private TextField assetIdTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private DatePicker ticketDateDatePicker;

    @FXML
    private TextField ticketIdTextField;

    /**
     * @author Pei Yan Geng
     * Initializes the controller, setting default values for the ticket date and ticket ID fields.
     */
    @FXML
    public void initialize() {
        ticketDateDatePicker.setValue(LocalDate.now());
        
        Integer ticketId = ViewTicketsPage.getTicketID();
        if (ticketId != -1) {
            ticketIdTextField.setText(String.valueOf(ViewTicketsPage.getTicketID()));
        } else {
            ticketIdTextField.setText("");
        }
    }

    /**
     * @author Pei Yan Geng
     * Handles the update ticket action when the corresponding button is clicked.
     *
     * @param event The event triggering the update ticket action.
     */
    @FXML
    void updateTicket(ActionEvent event) {

        int ticketId = Integer.parseInt(ticketIdTextField.getText());
        Date raisedOn =  Date.valueOf(ticketDateDatePicker.getValue());
        String description = descriptionTextField.getText();
        String email = emailTextField.getText();
        int assetId = -1;
        String assetIdString = assetIdTextField.getText();
        if (!assetIdString.isEmpty()) {
            assetId = Integer.parseInt(assetIdString);
            if (assetId < 0)
                ViewUtils.showError("Please enter a valid asset ID");
        }
        if (MaintenanceTicket.getWithId(ticketId).getTicketStatus() == MaintenanceTicket.TicketStatus.Closed) {
            ViewUtils.showError("Cannot update a closed ticket");
        }

        String result = AssetPlusFeatureSet4Controller.updateMaintenanceTicket(ticketId, raisedOn, description, email, assetId);
        if (result.contains("Ticket modified successfully")){
            assetIdTextField.setText("");
            emailTextField.setText("");
            descriptionTextField.setText("");
            ticketIdTextField.setText("");
            ticketDateDatePicker.setValue(null);
        }else{
            ViewUtils.showError(result);
        }

        /*

        if (ViewUtils.successful(AssetPlusFeatureSet4Controller.updateMaintenanceTicket(ticketId, raisedOn, description,
                email, assetId))) {
            assetIdTextField.setText("");
            emailTextField.setText("");
            descriptionTextField.setText("");
            ticketDateDatePicker.setValue(null);
        }

         */
    }

    /**
     * @author Pei Yan Geng
     * Opens the add/delete image page when the corresponding button is clicked.
     *
     * @param event The event triggering the open add image page action.
     */
    @FXML
    void openAddImagePage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/addDeleteImage.fxml"));
            Parent root = loader.load();

            // Get the current Stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new root for the current Scene
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
            ViewUtils.showError("Error opening image upload page\n");
        }
    }

    /**
     * @author Pei Yan Geng
     * Returns to the main page when the corresponding button is clicked.
     *
     * @param event The event triggering the go back action.
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

            mainPage.selectTab(1);


        } catch (IOException e) {
            e.printStackTrace();
            ViewUtils.showError("Error Changing Page\n");
        }
    }
    /*
    @FXML
    void deleteTicket(ActionEvent actionEvent) { //useless (see ViewTicketsPage controller)
        int ticketId = Integer.parseInt(ticketIdTextField.getText());
        AssetPlusFeatureSet4Controller.deleteMaintenanceTicket(ticketId);
        ticketIdTextField.setText("");
    }
     */
}