package ca.mcgill.ecse.assetplus.javafx.fxml.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

public class MainPage {

  @FXML
  private TabPane tabPane;


  /** Changes the scene to the scene associated with the given index 
  * @author Marc-Antoine Nadeau - Student ID: 261114549
  * @param integer representing the index of the scene you want to access
  * @return void
  */
  public void selectTab(int index) {
    tabPane.getSelectionModel().select(index);
  }

  
}
