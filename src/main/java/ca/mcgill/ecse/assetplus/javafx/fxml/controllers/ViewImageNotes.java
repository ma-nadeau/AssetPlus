package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet5Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet6Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet7Controller;
import ca.mcgill.ecse.assetplus.controller.TOMaintenanceNote;
import ca.mcgill.ecse.assetplus.controller.TOMaintenanceTicket;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller for the ViewImageNotes Page, where the user can view and manage notes and images for a 
 * selected ticket.
 * @author Behrad Rezaie
 */
public class ViewImageNotes {

    private MainPage mainPage;

    @FXML
    private Button addImage;

    @FXML
    private Button addNote;

    @FXML
    private Button backButton;

    @FXML
    private Button deleteImage;

    @FXML
    private Button deleteNote;

    @FXML
    private TableView<String> imagesView;

    @FXML
    private TableView<TOMaintenanceNote> notesView;

    @FXML
    private Label title;

    @FXML
    private Button updateNote;

    public static String selectedURL = null;
    public static TOMaintenanceNote selectedNote = null;
    public static int selectedNoteIndex = -1;

    public static int selectedTicket = ViewTicketsPage.getTicketID();

    /**
     * Initialization of the ViewImageNotes page. Sets the title dynamically to show which 
     * ticket is currently being viewed, as well as creates the appropriate columns for the 
     * image and note tableViews. Sets their default placeholder texts as well in case of
     * empty tables.
     * @author Behrad Rezaie
     */
    public void initialize(){
      selectedTicket = ViewTicketsPage.getTicketID();
      title.setText(title.getText()+": Ticket ID #" + selectedTicket);

      imagesView.setPlaceholder(new Label("No Image URLs Found"));
      notesView.setPlaceholder(new Label("No notes found"));

      TableColumn<TOMaintenanceNote, String> noteWriter = new TableColumn<TOMaintenanceNote, String>("Written by");
      noteWriter.setCellValueFactory(new PropertyValueFactory<TOMaintenanceNote, String>("noteTakerEmail"));
      
      TableColumn<TOMaintenanceNote, Date> dateRaised = new TableColumn<TOMaintenanceNote, Date>("Raised On");
      dateRaised.setCellValueFactory(new PropertyValueFactory<TOMaintenanceNote, Date>("date"));
      
      TableColumn<TOMaintenanceNote, String> description = new TableColumn<TOMaintenanceNote, String>("Note Description");
      description.setCellValueFactory(new PropertyValueFactory<TOMaintenanceNote, String>("description"));
      
      notesView.getColumns().add(dateRaised);
      notesView.getColumns().add(noteWriter);
      notesView.getColumns().add(description);


      TableColumn<String, String> imageURLS = new TableColumn<String, String>("Image URL");
      imageURLS.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()));
      imageURLS.setMaxWidth(600);
      imagesView.getColumns().add(imageURLS);

      setImageNotes();
    }

    /**
     * Method that returns the ticket associated with the current ticket ID. 
     * @author Behrad Rezaie
     * @return TOMaintenanceTicket transfer object of the actual ticket
     */
    TOMaintenanceTicket getTicket(){
      for (TOMaintenanceTicket ticket : AssetPlusFeatureSet6Controller.getTickets()) {
        if(ticket.getId()==selectedTicket){
          return ticket;
        }
      }
      ViewUtils.showError("Could not find ticket (unexpected)");
      return null;

    }
    
    /**
     * Method that sets the row items of the note and image tableViews upon being called.
     * @author Behrad Rezaie
     */
    void setImageNotes(){
      TOMaintenanceTicket ticket = getTicket();
      notesView.setItems(FXCollections.observableList(ticket.getNotes()));
      imagesView.setItems(FXCollections.observableList(ticket.getImageURLs()));
    }

    /**
     * Method that opens the addImage page. Method is called when the associated button is clicked.
     * @author Behrad Rezaie
     */
    @FXML
    void addImage(ActionEvent event) {
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
    }

    /**
     * Method that opens the addNote page. Method is called when the associated button is clicked.
     * @author Behrad Rezaie
     */
    @FXML
    void addNote(ActionEvent event) {
      try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/addNote.fxml"));
            Parent root = loader.load();

            // Get the current Stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new root for the current Scene
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
            ViewUtils.showError("Error: Could not add note page\n");
        }
    }
    /**
     * Method that deletes a selected image based on the private field selectedURL. 
     * Shows a popup error if no image is selected.
     * Method is called when the associated button is clicked.
     * @author Behrad Rezaie
     */
    @FXML
    void deleteImage(ActionEvent event) {
      if(selectedURL!=null){
        AssetPlusFeatureSet5Controller.deleteImageFromMaintenanceTicket(selectedURL, selectedTicket);
      }
      else{
        ViewUtils.showError("Select a URL first");
      }
      setImageNotes();
    }

    /**
     * Method that finds the index of a given note within a Ticket's list of notes.
     * @author Behrad Rezaie
     * @param selectedNote
     * @return integer index of the note in the ticket's list of notes
     */
    public int findIndex(TOMaintenanceNote selectedNote){
      List<TOMaintenanceNote> allNotes = getTicket().getNotes();
        for (TOMaintenanceNote note : allNotes) {
          if(note.getDescription().equals(selectedNote.getDescription())&&
              note.getNoteTakerEmail().equals(selectedNote.getNoteTakerEmail())&&
              note.getDate().equals(selectedNote.getDate())){
                return allNotes.indexOf(note);
              }
        }
        return -1;
    }
    /**
     * Method that deletes a selected note based on the private field selectedNote.
     * Shows a popup error if no note is selected yet.
     * @author Behrad Rezaie
     */
    @FXML
    void deleteNote(ActionEvent event) {
      int noteIndex=-1;
      if(selectedNote!=null){
        noteIndex=findIndex(selectedNote);
        AssetPlusFeatureSet7Controller.deleteMaintenanceNote(selectedTicket,noteIndex);
      }
      else{
        ViewUtils.showError("Select a note first");
      }
      setImageNotes();
    }

    /**
     * Method that returns to the ViewTicketsPage upon being called by having the associated button clicked.
     * @author Behrad Rezaie
     */
    @FXML
    void goBack(ActionEvent event) {
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
    /**
     * Method that checks if a row in either the image or note tableView is selected, and sets the
     * appropriate field to the selected values. Method is called every time a mouse is clicked.
     * @author Behrad Rezaie
     */
    @FXML
    void selectRow(MouseEvent event) {
      String attemptedSelURL = imagesView.getSelectionModel().getSelectedItem();
      TOMaintenanceNote attemptedSelNote = notesView.getSelectionModel().getSelectedItem();

      if(attemptedSelNote!=null){
        System.out.println("Selected note");
        selectedNote = attemptedSelNote;
        selectedNoteIndex = findIndex(attemptedSelNote);
        selectedNote = getTicket().getNote(selectedNoteIndex);
      }
      if(attemptedSelURL!=null){
        System.out.println("Selected url");
        selectedURL = attemptedSelURL;
      }
    }

    /**
     * Method that opens the UpdateNote page upon being called by clicking the associated button.
     * Shows a popup error if no note is selected upon being clicked.
     * @author Behrad Rezaie
     */
    @FXML
    void updateNote(ActionEvent event) {
      if(selectedNoteIndex==-1){
        ViewUtils.showError("Select a note from the list to update first");
        return;
      }

      try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/UpdateNote.fxml"));
            Parent root = loader.load();

            // Get the current Stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new root for the current Scene
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
            ViewUtils.showError("Error: Could not load update note page");
        }
    }

    /**
     * Public method that returns the TOMaintenanceNote set in the private field. Allows for 
     * other pages to call this method to initialize their own fields.
     * @author Behrad Rezaie
     * @return TOMaintenanceNote
     */
    public static TOMaintenanceNote getSelectedNote(){
      return selectedNote;
    }
}
