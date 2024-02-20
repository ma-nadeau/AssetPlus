package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet7Controller;
import ca.mcgill.ecse.assetplus.controller.TOMaintenanceNote;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateNote {

    
    @FXML
    private Button cancelButton;

    @FXML
    private DatePicker noteDateDatePicker;

    @FXML
    private TextField noteDescriptionTextField;

    @FXML
    private TextField noteTakerTextField;

    @FXML
    private Label title;

    private static TOMaintenanceNote selectedNote = ViewImageNotes.selectedNote;

    /** Initializes the controller and sets the date, the description, the index and the email.
    * @author Laurence Perreault
    * @return void
    */
    @FXML
    public void initialize(){
      title.setText(title.getText()+" #"+String.valueOf(ViewImageNotes.selectedNoteIndex+1));
      
      noteDateDatePicker.setValue(LocalDate.now());
      
      if(ViewImageNotes.getSelectedNote() !=null){
        TOMaintenanceNote thisNote = ViewImageNotes.getSelectedNote();
        noteDescriptionTextField.setText(thisNote.getDescription());
        noteTakerTextField.setText(thisNote.getNoteTakerEmail());
        System.out.println("changing fields");
      }
    }

    /** Cancels the action
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
            ViewUtils.showError("Error viewing images and notes");
        }
      }

    /** Verifies that the inputed string is a numeric value
    * @author Laurence Perreault
    * @return void
    */
    public boolean isNumeric(String input){
      try{
        Integer.parseInt(input);
        return true;
      }catch(NumberFormatException e){
        return false;
      }
    }

    /** Calls the controller method updateMaintenanceNote when all the parameters are valid.
     Resets the fields when the method works.
    * @author Laurence Perreault
    * @return void
    */
    @FXML
    void submitClicked (ActionEvent event) {
      
      if(noteDescriptionTextField.getText()==""){
        ViewUtils.showError("A description must be provided");
        return;
      }
      else if(noteDateDatePicker.getValue()==null){
        ViewUtils.showError("A date must be provided");
        return;
      }
      else if(noteTakerTextField.getText().equals("")){
        ViewUtils.showError("A note taker must be provided");
        return;
      }
      
      Date newDate = Date.valueOf(noteDateDatePicker.getValue());
      String newDescription = noteDescriptionTextField.getText();
      String newTaker = noteTakerTextField.getText();
      //String result = AssetPlusFeatureSet7Controller.updateMaintenanceNote(ViewImageNotes.selectedTicket,ViewImageNotes.selectedNoteIndex , newDate, newDescription, newTaker);
      if(ViewUtils.successful(AssetPlusFeatureSet7Controller.updateMaintenanceNote(ViewImageNotes.selectedTicket,ViewImageNotes.selectedNoteIndex , newDate, newDescription, newTaker))){
        noteDescriptionTextField.setText("");
        noteTakerTextField.setText("");
        noteDateDatePicker.setValue(null);
      }
    }

}
