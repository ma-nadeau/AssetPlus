package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;
import ca.mcgill.ecse.assetplus.controller.TicketMaintenanceController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller class for approving or disapproving work on a ticket in the application.
 * This class handles the user interface and interacts with the TicketMaintenanceController.
 *
 * @author Anastasiia Nemyrovska
 */

public class ApproveDisapproveWork implements Initializable {

    private MainPage mainPage;

    @FXML
    private Button backButton;

    @FXML
    private ChoiceBox<String> approvalChoiceBox;

    private String status[] = {"approved", "rejected"};

    @FXML
    private TextField idTextField;

    @FXML
    private DatePicker dateDatePicker;

    @FXML
    private TextField reasonTextField;

    @FXML
    private Button submitButton;

    /**
     * Sets the approval status of the ticket based on user input.
     *
     * @param event The ActionEvent triggered by the submit button.
     * @author Anastasiia Nemyrovska
     */

    public void setApprovalStatus(ActionEvent event) {
        String status = approvalChoiceBox.getValue();
        String ticketID = idTextField.getText();
        String rejectionReason = reasonTextField.getText();
        String rejectionDate;
        if (ticketID.isEmpty() || status == null) {
            ViewUtils.showError("Please select the ticket ID and approval status.");
        }
        else if (status.equals("approved")) {
            String result = TicketMaintenanceController.approveTicketWork(ticketID);
            if (result.isBlank()){
                approvalChoiceBox.setValue(null);
                idTextField.setText("");
                reasonTextField.setText("");
                dateDatePicker.setValue(null);
                return;
            }else{
                ViewUtils.showError(result);
                return;
            }
        }
        else {
            if (dateDatePicker.getValue() == null || rejectionReason.isEmpty()) {
                ViewUtils.showError("Please enter the rejection reason and rejection date.");
            } else {
                rejectionDate = dateDatePicker.getValue().toString();
                String result = TicketMaintenanceController.disapproveTicketWork(ticketID, rejectionDate, rejectionReason);
                if (result.equals("")){
                    approvalChoiceBox.setValue(null);
                    idTextField.setText("");
                    reasonTextField.setText("");
                    dateDatePicker.setValue(null);
                }
                else {
                    ViewUtils.showError(result);
                }
            }
        }
    }

    /**
     * Initializes the controller with the necessary components and sets default values.
     *
     * @param location   The location used to resolve relative paths for the root object.
     * @param resources  The resources used to localize the root object, or null if the root object was not localized.
     * @author Anastasiia Nemyrovska
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateDatePicker.setValue(LocalDate.now());

        approvalChoiceBox.getItems().addAll(status);
        if(ViewTicketsPage.getTicketID() != -1){
        idTextField.setText(String.valueOf(ViewTicketsPage.getTicketID()));}
        dateDatePicker.setEditable(false);
    }

    /**
     * Navigates back to the main page when the back button is clicked.
     *
     * @param event The ActionEvent triggered by the back button.
     * @author Anastasiia Nemyrovska
     */

    public void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../MainPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            mainPage = loader.getController();
            mainPage.selectTab(1);
        } catch (IOException e) {
            e.printStackTrace();
            ViewUtils.showError("Error Changing Page\n");
        }
    }
}