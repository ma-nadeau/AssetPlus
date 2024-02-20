package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet7Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddNote {

    @FXML
    private Button cancelButton;

    @FXML
    private TextField idTextField;

    @FXML
    private DatePicker noteDateDatePicker;

    @FXML
    private TextField noteDescriptionTextField;

    @FXML
    private TextField noteTakerTextField;

    /** Initializes the controller and sets the date
    * @author Laurence Perreault
    * @return void
    */
    @FXML
    public void initialize(){
        noteDateDatePicker.setValue(LocalDate.now());
      
      if(ViewTicketsPage.getTicketID()!=-1){
        idTextField.setText(String.valueOf(ViewTicketsPage.getTicketID()));
      }
    }
    
    /** Calls the controller method addMaintenanceNote when all the parameters are valid.
     Resets the fields when the method works.
    * @author Laurence Perreault
    * @return void
    */
    @FXML
    void submitClicked (ActionEvent event) {
      String ticketID = idTextField.getText();
      if(ticketID=="" || Integer.parseInt(ticketID)<1){
        ViewUtils.showError("Enter a valid ID (larger than 0)");
        return;
      }
      int ticketId = Integer.parseInt(idTextField.getText());

      String email = noteTakerTextField.getText();
      if(email=="null"){
        ViewUtils.showError("Enter a valid email");
        return;
      }

      if(noteDateDatePicker.getValue()==null){
        ViewUtils.showError("Pick a date");
        return;
      }
      Date date = Date.valueOf(noteDateDatePicker.getValue());

      String description = noteDescriptionTextField.getText();
      if(description==""){
        ViewUtils.showError("Enter a description");
        return;
      }
      //String result = AssetPlusFeatureSet7Controller.addMaintenanceNote(date, description, ticketId, email);
      if(ViewUtils.successful(AssetPlusFeatureSet7Controller.addMaintenanceNote(date, description, ticketId, email))){
        noteDescriptionTextField.setText("");
        noteTakerTextField.setText("");
        noteDateDatePicker.setValue(null);
        idTextField.setText("");
      }
    }

    /** Cancels the action.
    * @author Laurence Perreault
    * @return void
    */
    @FXML
    public void cancelClicked (ActionEvent event) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/ViewImageNotes.fxml"));
        Parent root = loader.load();

        // Get the current Stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new root for the current Scene
        stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            ViewUtils.showError("Error viewing images and notes.");
        }
      }
}
