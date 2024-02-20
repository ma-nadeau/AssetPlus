package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;

import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket;
import ca.mcgill.ecse.assetplus.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import ca.mcgill.ecse.assetplus.controller.*;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Pei Yan Geng
 * The addUpdateDeleteTicket controller provides methods for the user to interact with
 * the model through the AssetPlus controller and navigate through the pages.
 */
public class addUpdateDeleteTicket {
    private static AssetPlus assetPlus = AssetPlusApplication.getAssetPlus();

    private MainPage mainPage;

    @FXML
    public Button backButton;

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
    
    @FXML
    private CheckBox addImage;

    /**
     * @author Pei Yan Geng
     * Initializes the controller, setting default values for the ticket date, making it non-editable,
     * and updating the ticket ID field if available.
     */
    @FXML
    public void initialize() {
    // Set the value of userEmailTextField from ViewUserPage.currentUser
    ticketDateDatePicker.setValue(LocalDate.now());
    ticketDateDatePicker.setEditable(false);
    
    if(ticketIdTextField!=null){
    if (ViewTicketsPage.getTicketID() != -1) {
        ticketIdTextField.setText(String.valueOf(ViewTicketsPage.getTicketID()));
    } else {
        ticketIdTextField.setText("");
    }}
    }

    /**
     * @author Pei Yan Geng
     * Handles the addition of a new maintenance ticket when the corresponding button is clicked.
     * Validates user input and displays error messages if needed.
     *
     * @param event The event triggering the add ticket action.
     */
    @FXML
    void addTicket(ActionEvent event) {
        List<TOMaintenanceTicket> allTickets = AssetPlusFeatureSet6Controller.getTickets();
        int newID=1;
        for (TOMaintenanceTicket ticket : allTickets) {
            if(ticket.getId()>=newID){
                newID=ticket.getId()+1;
            }
        }

        int ticketId = newID;
        LocalDate localRaisedOn =  ticketDateDatePicker.getValue();

        if(localRaisedOn==null){
            ViewUtils.showError("Enter a date");
            return;
        }
        Date raisedOn = Date.valueOf(localRaisedOn);        
        String description = descriptionTextField.getText();
        if(description==""){
            ViewUtils.showError("Enter a description");
            return;
        }
        String email = emailTextField.getText();
        if(!User.hasWithEmail(email) || email=="") {
            ViewUtils.showError("Enter a valid email");
            return;
        }
        int assetId = -1;
        String assetIdString = assetIdTextField.getText();
        if (!assetIdString.isEmpty()) {
            try{assetId = Integer.parseInt(assetIdString);
            }catch(NumberFormatException e){
                ViewUtils.showError("Please enter a valid asset ID");
                return;
            }
            if (assetId < 0){
                ViewUtils.showError("Please enter a valid asset ID");
                return;}
        }
        String result = AssetPlusFeatureSet4Controller.addMaintenanceTicket(ticketId, raisedOn, description, email, assetId);
        if (result.contains("Ticket added successfully")){
            assetIdTextField.setText("");
            emailTextField.setText("");
            descriptionTextField.setText("");
            ticketDateDatePicker.setValue(null);
        }

        /*if (addImage.isSelected() && MaintenanceTicket.hasWithId(ticketId)){
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/addImage.fxml"));
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
        }*/
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
        String result = AssetPlusFeatureSet4Controller.updateMaintenanceTicket(ticketId, raisedOn, description, email, assetId);

        if (result.contains("Ticket modified successfully")) {
            assetIdTextField.setText("");
            emailTextField.setText("");
            descriptionTextField.setText("");
            ticketDateDatePicker.setValue(null);
        }else{
            ViewUtils.showError(result);
        }
    }
     */

    /*
    @FXML
    void deleteTicket(ActionEvent actionEvent) { //useless (see ViewTicketsPage controller)
        int ticketId = Integer.parseInt(ticketIdTextField.getText());
        AssetPlusFeatureSet4Controller.deleteMaintenanceTicket(ticketId);
        ticketIdTextField.setText("");
    }
    */
    /*
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

     */

}