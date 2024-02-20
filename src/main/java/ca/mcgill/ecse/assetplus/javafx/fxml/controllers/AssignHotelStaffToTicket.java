package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;

import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet1Controller;
import ca.mcgill.ecse.assetplus.controller.TicketMaintenanceController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller class for assigning hotel staff to a ticket in the application.
 * This class handles the user interface and interacts with the TicketMaintenanceController.
 *
 * @author Anastasiia Nemyrovska
 */

public class AssignHotelStaffToTicket implements Initializable {

    private MainPage mainPage;

    @FXML
    private Button backButton;

    @FXML
    private CheckBox approvalCheckBox;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField idTextField;

    @FXML
    private ChoiceBox<String> priorityChoiceBox;

    private String priorityChoice[] = {"Urgent", "Normal", "Low"};

    @FXML
    private Button submitButton;

    @FXML
    private ChoiceBox<String> timeChoiceBox;

    private String timeChoice[] = {"< day", "1 - 3 days", "3 - 7 days", "1 - 3 weeks", "> 3 weeks"};

    private static Map<String, String> timeChoiceMap;

    static {
        String[] originalChoices = {"LessThanADay", "OneToThreeDays", "ThreeToSevenDays", "OneToThreeWeeks", "ThreeOrMoreWeeks"};
        timeChoiceMap = new HashMap<>();
        String[] uiChoices = {"< day", "1 - 3 days", "3 - 7 days", "1 - 3 weeks", "> 3 weeks"};
        for (int i = 0; i < originalChoices.length; i++) {
            timeChoiceMap.put(uiChoices[i], originalChoices[i]);
        }
    }

    /**
     * Converts a user interface choice to the corresponding internal time choice.
     *
     * @param uiChoice The user interface choice (e.g., "< day", "1 - 3 days").
     * @return The internal time choice (e.g., "LessThanADay", "OneToThreeDays").
     * @author Anastasiia Nemyrovska
     */

    public static String getTimeChoiceFromUi(String uiChoice) {
        return timeChoiceMap.get(uiChoice);
    }

    /**
     * Handles the assignment of hotel staff to a ticket when the submit button is clicked.
     *
     * @param event The ActionEvent triggered by the submit button.
     * @author Anastasiia Nemyrovska
     */

    public void assignTicket(ActionEvent event) {
        String ticketID = idTextField.getText();
        String staffEmail = emailTextField.getText();
        String timeEstimate = getTimeChoiceFromUi(timeChoiceBox.getValue());
        String priorityLevel = priorityChoiceBox.getValue();
        String approvalRequired = String.valueOf(approvalCheckBox.isSelected());
        if (ticketID.isEmpty() || staffEmail.isEmpty() || timeEstimate == null || priorityLevel == null || approvalRequired.isEmpty()) {
            ViewUtils.showError("Please fill in the listed fields.");
            return;
        } else {
            String result = TicketMaintenanceController.assignStaffToTicket(ticketID, staffEmail, timeEstimate, priorityLevel, approvalRequired);
            if (result.equals("")) {
                idTextField.setText("");
                emailTextField.setText("");
                timeChoiceBox.setValue(null);
                priorityChoiceBox.setValue(null);
                approvalCheckBox.setSelected(false);
            }
            else {
                ViewUtils.showError(result);
            }
        }
    }

    /**
     * Initializes the controller with the necessary components and data.
     *
     * @param location   The location used to resolve relative paths for the root object.
     * @param resources  The resources used to localize the root object.
     * @author Anastasiia Nemyrovska
     */

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        priorityChoiceBox.getItems().addAll(priorityChoice);
        timeChoiceBox.getItems().addAll(timeChoice);

        if(ViewTicketsPage.getTicketID() != -1){
            idTextField.setText(String.valueOf(ViewTicketsPage.getTicketID()));}    }

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