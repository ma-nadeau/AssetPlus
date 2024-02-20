package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;

import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet7Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DeleteNote {

    @FXML
    private TextField ticketIDTextField;

    @FXML
    private TextField ticketIndexTextField;

     /** Calls the controller method deleteMaintenanceNote with the info input in the textfield and deletes the note.
     Resets the fields when the method works.
    * @author Laurence Perreault
    * @return void
    */
    @FXML
    void deleteNote(ActionEvent event) {
      int id = Integer.parseInt(ticketIDTextField.getText());
      int index = Integer.parseInt(ticketIndexTextField.getText());

      AssetPlusFeatureSet7Controller.deleteMaintenanceNote(id, index);
        ticketIDTextField.setText("");
        ticketIndexTextField.setText("");
    }
}
