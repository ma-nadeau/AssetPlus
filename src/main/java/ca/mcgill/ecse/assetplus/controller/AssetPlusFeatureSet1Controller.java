package ca.mcgill.ecse.assetplus.controller;

import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import ca.mcgill.ecse.assetplus.model.Manager;
import ca.mcgill.ecse.assetplus.model.User;
import ca.mcgill.ecse.assetplus.persistence.AssetPlusPersistence;

/**
  * The controller for the Update Manager Password & Add and Update Employee/Guest
  * @since 1.0
  * @author Marc-Antoine Nadeau - Student ID: 261114549
  */
public class AssetPlusFeatureSet1Controller { 
  
  //get the single instance of the AssetPlus class
  private static AssetPlus assetplus = AssetPlusApplication.getAssetPlus();
  /**
   * Updates the password of the Manager and perfome Input Validation on the password.
   * @param password a string with at least one 3 character long containing at least one of #!$, both uppercase and lowercase letters. 
   * @return An empty string when the password has been succesfully updated, or a String with all the error messages combined in case of failure.
   * @since 1.0 
   * @author Marc-Antoine Nadeau - Student ID: 261114549
   */
  public static String updateManager(String password) {  
    //Input Validation
    var error = "";
    if (password.length() <= 3){
      error = "Password must be at least four characters long";
    } if( ! (password.contains("!") || password.contains("#") || password.contains("$"))){
       error += "Password must contain one character out of !#$";
    } if(password.isBlank()){
      error += "Password cannot be empty";
    }if( ! (password.matches(".*[a-z].*"))){
      error += "Password must contain one lower-case character";
    }if( ! (password.matches(".*[A-Z].*"))){
      error += "Password must contain one upper-case character";
    } if(!assetplus.hasManager()){
      error += "A Manager should exist";
    } 
    if (!error.isEmpty()) {
      return error.trim();
    }
    //Call Model
    Manager manager = assetplus.getManager();
    manager.setPassword(password);

    try {
      AssetPlusPersistence.save();
    } catch (RuntimeException e){
    }
    return error;
  }
  /**
   * Add employee or guest to the assetplus system and performs input validations
   * @param email The email address of an employee or guest following certain guidelines
   * @param password The password of the corresponding employee or guest
   * @param name The name of the following employee or guest
   * @param phoneNumber The phone number of the following employee or guest
   * @param isEmployee Boolean telling if it is an employee or a guest
   * @return An empty string when the employee or guest has been added, or a String with all the error messages combined in case of failure.
   * @since 1.0 
   * @author Marc-Antoine Nadeau - Student ID: 261114549
   */
  public static String addEmployeeOrGuest(String email, String password, String name, String phoneNumber, boolean isEmployee) {
    // Input Validation
    var error = "";
    if (email.equals("manager@ap.com")){
      error += "Email cannot be manager@ap.com";
    } if(!isEmployee && email.endsWith("@ap.com")){
      error += " Email domain cannot be @ap.com";
    } if(isEmployee && ! email.endsWith("@ap.com")){
      error += " Email domain must be @ap.com";
    } if (email.contains(" ")) {
      error += " Email must not contain any spaces";
    }if(email.isBlank()){
      error += " Email cannot be empty";
    } if ( ! email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")){
      error += " Invalid email";
    } if (password == null || password.isBlank()){ 
      error += " Password cannot be empty";
    } if ( phoneNumber == null){ 
      error += " Phone number cannot be empty";
    } if(name == null){
      error += " Name cannot be empty";
    }
    
    if ( ! error.isEmpty()) { 
      return error.trim();
    }
    try {
      // Calls model for employee
      if(isEmployee){  
        assetplus.addEmployee(email, name, password, phoneNumber);
      } 
      // Calls model for Guest
      else {
        assetplus.addGuest(email, name, password, phoneNumber);
      } 
    } catch (RuntimeException e){
      error = e.getMessage();
      if(error.startsWith("Cannot create due to duplicate email.") && isEmployee){
        error = "Email already linked to an employee account";
      } else if (error.startsWith("Cannot create due to duplicate email") && !isEmployee){
        error = "Email already linked to a guest account";
      }
    }

    try {
      AssetPlusPersistence.save();
    } catch (RuntimeException e){
    }

  return error;
 }
 
 /**
  * 
  * Updates employee or guest to the assetplus system and performs input validations
  * @param email The email address of an employee or guest following certain guidelines
  * @param newPassword The new password of the corresponding employee or guest
  * @param newName The new name of the following employee or guest
  * @param newPhoneNumber The new phone number of the following employee or guest
  * @return An empty string when the employee or guest has been updated, or a String with all the error messages combined in case of failure.
  * @since 1.0 
  * @author Marc-Antoine Nadeau - Student ID: 261114549
  */
 public static String updateEmployeeOrGuest(String email, String newPassword, String newName, String newPhoneNumber) {
    var error = "";
    if(!User.hasWithEmail(email)){
      error += "There exist no user with email: " + email +" in the system.";
    } if (newPassword == null || newPassword.isBlank()){ 
      error += "Password cannot be empty";
    } if ( newPhoneNumber == null){ 
      error += "Phone number cannot be empty";
    } if(newName == null){
      error += "Name cannot be empty";
    }
    
    
    if (!error.isEmpty()) {
      return error.trim();
    }
    //Call controller for User (Guest and Employee)
    User specificUser = User.getWithEmail(email);
    specificUser.setPassword(newPassword);
    specificUser.setName(newName);
    specificUser.setPhoneNumber(newPhoneNumber);

    try {
      AssetPlusPersistence.save();
    } catch (RuntimeException e){
    }
    
   return error;
  }
}