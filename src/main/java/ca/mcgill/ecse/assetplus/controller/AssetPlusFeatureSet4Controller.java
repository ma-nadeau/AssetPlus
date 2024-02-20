package ca.mcgill.ecse.assetplus.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import ca.mcgill.ecse.assetplus.model.*;
import ca.mcgill.ecse.assetplus.persistence.AssetPlusPersistence;
import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;

/**
 * @author Pei Yan Geng
 * The AssetPlusFeatureSet4Controller class provides methods for managing maintenance tickets
 * and their associated data (ticket ID, date, email and asset number), including the addition,
 * update, and deletion of maintenance tickets.
 * This controller class interacts with the AssetPlus model.
 */

public class AssetPlusFeatureSet4Controller {

  private static AssetPlus assetPlus = AssetPlusApplication.getAssetPlus();

  /**
   * Checks if an email address is valid.
   *
   * @param email The email address to validate.
   * @return True if the email is valid; false otherwise.
   */
  private static boolean isValidEmail(String email) {
    String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+(\\.[A-Za-z]+)*$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches() && User.hasWithEmail(email);
  }

  /**
   * Checks if a ticket ID is valid.
   *
   * @param id The ID to validate.
   * @return True if the ID is valid; false otherwise.
   */
  private static boolean isValidID(int id) {
    return !(MaintenanceTicket.hasWithId(id));
  }

  /**
   * Checks if an asset ID is valid.
   *
   * @param assetNumber The asset ID to validate.
   * @return True if the ID is valid; false otherwise.
   */
  private static boolean isValidAssetNumber(int assetNumber) {
    return SpecificAsset.getWithAssetNumber(assetNumber) != null || assetNumber == -1;
  }

  /**
   * Generates error messages.
   *
   * @param id The ticket ID to validate.
   * @param raisedOnDate The date to validate.
   * @param description The description to validate.
   * @param email The email to validate.
   * @param assetNumber The asset ID to validate.
   * @return StringBuilder containing error messages
   */
  private static StringBuilder errorGenerator(int id, Date raisedOnDate, String description, String email, int assetNumber) {
    StringBuilder errorMessage = new StringBuilder();
    if (id < 0) {
      errorMessage.append("Invalid ID ");
    }
    if (description.isEmpty()) {
      errorMessage.append("Ticket description cannot be empty");
    }
    if (!isValidEmail(email)) {
      errorMessage.append("The ticket raiser does not exist");
    }
    if (!isValidAssetNumber(assetNumber)) {
      errorMessage.append("The asset does not exist");
    }
    return errorMessage;
  }
  /**
   * Adds a maintenance ticket to AssetPlus.
   *
   * @param id The ID of the maintenance ticket.
   * @param raisedOnDate The date when the ticket was raised.
   * @param description The description of the ticket.
   * @param email The email of the user associated with the ticket.
   * @param assetNumber The asset number associated with the ticket, or -1 if none.
   * @return A string containing any error messages, if applicable, or a success message.
   */
  public static String addMaintenanceTicket(int id, Date raisedOnDate, String description, String email, int assetNumber) {
    StringBuilder errorMessage = errorGenerator(id, raisedOnDate, description, email, assetNumber);
    if (errorMessage.length() > 0) {
      System.out.println(errorMessage);
      return errorMessage.toString();
    }
    try {
      User aUser = User.getWithEmail(email);
      MaintenanceTicket aTicket = assetPlus.addMaintenanceTicket(id, raisedOnDate, description, aUser);

      assetPlus.addMaintenanceTicket(aTicket);

      if (assetNumber != -1) {
        SpecificAsset aAsset = SpecificAsset.getWithAssetNumber(assetNumber);
        aTicket.setAsset(aAsset);
      }
      else {
        aTicket.setAsset(null);
      }
    }
    catch (Exception e) {
      String eString = e.getMessage();
      if (eString.startsWith("Cannot create due to duplicate id")) {
        errorMessage.append("Ticket id already exists");
      }
    }
    if (errorMessage.length() == 0) {
      errorMessage.append("Ticket added successfully");
    }
    System.out.println(errorMessage);

    try {
      AssetPlusPersistence.save();
    } catch (RuntimeException e){
    }

    return errorMessage.toString();
  }

  /**
   * Updates an existing maintenance ticket.
   *
   * @param id The ID of the ticket to update.
   * @param newRaisedOnDate The new date when the ticket was raised.
   * @param newDescription The new description of the ticket.
   * @param newEmail The new email of the user associated with the ticket.
   * @param newAssetNumber The new asset number associated with the ticket, or -1 if none.
   * @return A string containing any error messages, if applicable, or a success message.
   */
  public static String updateMaintenanceTicket(int id, Date newRaisedOnDate, String newDescription, String newEmail, int newAssetNumber) {
    StringBuilder errorMessage = errorGenerator(id, newRaisedOnDate, newDescription, newEmail, newAssetNumber);
    if (errorMessage.length() > 0) {
      System.out.println(errorMessage);
      return errorMessage.toString();
    }
    try {
      List<MaintenanceTicket> maintenanceTickets = assetPlus.getMaintenanceTickets();
      MaintenanceTicket aTicket = maintenanceTickets.stream().filter(ticket -> ticket.getId() == id).findFirst().orElse(null);
      if (aTicket != null) {
        aTicket.setRaisedOnDate(newRaisedOnDate);
        aTicket.setDescription(newDescription);
        User aUser = User.getWithEmail(newEmail);
        aTicket.setTicketRaiser(aUser);
        if (newAssetNumber != -1) {
          SpecificAsset aAsset = SpecificAsset.getWithAssetNumber(newAssetNumber);
          aTicket.setAsset(aAsset);
        }
        else {
          aTicket.setAsset(null);
        }
      }
      else {
        errorMessage.append("Ticket not found");
      }
    }
    catch (Exception e) {
      errorMessage.append(e.getMessage()).append("");
    }
    if (errorMessage.length() == 0) {
      errorMessage.append("Ticket modified successfully");
    }
    System.out.println(errorMessage);

    try {
      AssetPlusPersistence.save();
    } catch (RuntimeException e){
    }
    return errorMessage.toString();
  }


  /**
   * Deletes a maintenance ticket from the system.
   *
   * @param id The ID of the ticket to delete.
   */
  public static void deleteMaintenanceTicket(int id) {
    try {
      List<MaintenanceTicket> maintenanceTickets = assetPlus.getMaintenanceTickets();
      MaintenanceTicket ticketToDelete = maintenanceTickets.stream().filter(ticket -> ticket.getId() == id).findFirst().orElse(null);
      if (ticketToDelete != null) {
        ticketToDelete.delete();
        System.out.println("Ticket deleted successfully");
      }
      else {
        System.out.println("Ticket not found ");
      }
    }
    catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }

    try {
      AssetPlusPersistence.save();
    } catch (RuntimeException e){
    }
  }
}
