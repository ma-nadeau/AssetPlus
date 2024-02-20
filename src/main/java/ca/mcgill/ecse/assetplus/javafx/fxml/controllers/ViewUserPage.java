package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Node;
import javafx.stage.Stage;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet6Controller;
import ca.mcgill.ecse.assetplus.controller.ExtraFeaturesController;
import ca.mcgill.ecse.assetplus.controller.TOUser;
import ca.mcgill.ecse.assetplus.javafx.fxml.AssetPlusFxmlView;
import ca.mcgill.ecse.assetplus.model.Employee;
import ca.mcgill.ecse.assetplus.model.Guest;
import ca.mcgill.ecse.assetplus.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.thoughtworks.xstream.mapper.Mapper.Null;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class ViewUserPage {

    private static String currrentUser;
    
    @FXML
    private Button openAddUserButton;

    @FXML
    private Button openUpdateUserButton;

    @FXML
    private Button deleteUserButton;

    @FXML
    private Button refreshUserButton;

    @FXML
    private CheckBox showEmployeesCheckBox;

    @FXML
    private CheckBox showManagerCheckBox;

    @FXML
    private CheckBox showUsersCheckBox;

    @FXML
    private TableView<TOUser> UserView;


  /** Get the email of the current user
   * @author Marc-Antoine Nadeau - Student ID: 261114549
   * @return the email of the current currentUser.
   */
    protected static String getUserEmail(){
        return currrentUser;
    }
    /** Reset the email of the current user to null
    * @author Marc-Antoine Nadeau - Student ID: 261114549
    * @return void
    */
    private static void setCurrentUserToNull() {
        currrentUser = null;
    }

    /** Refreshes the value in the table after an action has been perfomeed
    * @author Marc-Antoine Nadeau - Student ID: 261114549
    * @return void
    */
    @FXML
    void refreshUserClicked(ActionEvent event){
      System.out.println("buttonClicked");
      List<TOUser> users = getFilteredUsers();
      
      UserView.setItems(FXCollections.observableList(users));

      AssetPlusFxmlView.getInstance().refresh();
    }

    /** Initializes the value to be added in the table.
    * @author Marc-Antoine Nadeau && Behrad Rezaie
    * @return void
    */
    public void initialize(){
      System.out.println("initialized");

      
      UserView.setPlaceholder(new Label("No users found"));

      refreshUserClicked(new ActionEvent());
      setCurrentUserToNull();
      TableColumn<TOUser, String> userRole = new TableColumn<TOUser, String>("Role");
      userRole.setCellValueFactory(new PropertyValueFactory<TOUser, String>("userType"));
      
      TableColumn<TOUser, String> userName = new TableColumn<TOUser, String>("Name");
      userName.setCellValueFactory(new PropertyValueFactory<TOUser, String>("name"));

      TableColumn<TOUser, String> userEmail = new TableColumn<TOUser, String>("Email");
      userEmail.setCellValueFactory(new PropertyValueFactory<TOUser, String>("email"));

      TableColumn<TOUser, String> userPhoneNumber = new TableColumn<TOUser, String>("Phone Number");
      userPhoneNumber.setCellValueFactory(new PropertyValueFactory<TOUser, String>("phoneNumber"));

      TableColumn<TOUser, String> userPassword = new TableColumn<TOUser, String>("Password");
      userPassword.setCellValueFactory(new PropertyValueFactory<TOUser, String>("password"));



      UserView.getColumns().add(userRole);
      UserView.getColumns().add(userName);
      UserView.getColumns().add(userEmail);
      UserView.getColumns().add(userPhoneNumber);
      UserView.getColumns().add(userPassword);
      
      //UserView.getItems().add(new TOUser("userEmail","userName","userPassword","userPassword","Guest"));

      UserView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    /** Filters the users present in the table
    * @author Behrad Rezaie
    * @return List<TOUser>
    */


    public List<TOUser> getFilteredUsers(){
      List<TOUser> filteredUsers = new ArrayList<TOUser>();

      boolean employees_shown = showEmployeesCheckBox.isSelected();
      boolean users_shown = showUsersCheckBox.isSelected();
      boolean manager_shown = showManagerCheckBox.isSelected();

      if((!employees_shown && !users_shown && !manager_shown)|| (employees_shown && users_shown && manager_shown)){
        return ExtraFeaturesController.getUsers();
      }
      if(manager_shown){
        filteredUsers.addAll(ExtraFeaturesController.getManager());
      }
      if(employees_shown){
        filteredUsers.addAll(ExtraFeaturesController.getEmployees());
      }
      if(users_shown){
        filteredUsers.addAll(ExtraFeaturesController.getGuests());
      }
      return filteredUsers;
    }


  
    /** Opens the scene UpdateUser.fxml to edit the information of a user
    * @author Marc-Antoine Nadeau - Student ID: 261114549
    * @return void
    */
    @FXML
    void openUpdateUserClicked(ActionEvent event) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/UpdateUser.fxml"));
        Parent root = loader.load();

        // Get the current Stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new root for the current Scene
        stage.getScene().setRoot(root);
      } catch (IOException e) {
      e.printStackTrace();
      ViewUtils.showError("Error opening image upload page\n");
    }
    }
    /** Opens the scene AddUser.fxml to edit the information of a user
    * @author Marc-Antoine Nadeau - Student ID: 261114549
    * @return void
    */
    @FXML
    void openAddUserClicked(ActionEvent event) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/AddUser.fxml"));
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

    /** Sets the value of currentUser to the value selected by clicking in the table
    * @author Marc-Antoine Nadeau - Student ID: 261114549
    * @return void
    */
    @FXML
    void selectUser(MouseEvent event) {
      if (event.getClickCount() == 1) {
        // Get the selected item
        TOUser selectedUser = UserView.getSelectionModel().getSelectedItem();
        currrentUser = selectedUser.getEmail();
        if (currrentUser != null) {
            // Debugging output
            System.out.println("Selected User Email: " + selectedUser.getEmail());
        }
    }
  }

    /** Deletes the currentUser that was selected by clicking in the table
    * @author Marc-Antoine Nadeau - Student ID: 261114549
    * @return void
    */
    @FXML
    void deleteUserClicked(ActionEvent event) {
      System.out.println("Selected User Email: " + currrentUser);
      if (currrentUser != null) {
        if (!currrentUser.equals("manager@ap.com")) {
            AssetPlusFeatureSet6Controller.deleteEmployeeOrGuest(currrentUser);
            refreshUserClicked(new ActionEvent());
            currrentUser = null;
            AssetPlusFxmlView.getInstance().refresh();
        } else {
            ViewUtils.showError("Cannot delete the manager");
        }
    } else {
            ViewUtils.showError("Select User to Delete");
        }

  }
}
