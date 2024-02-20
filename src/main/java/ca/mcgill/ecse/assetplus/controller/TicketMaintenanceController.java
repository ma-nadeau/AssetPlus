package ca.mcgill.ecse.assetplus.controller;

import ca.mcgill.ecse.assetplus.model.*;
import ca.mcgill.ecse.assetplus.persistence.AssetPlusPersistence;

public class TicketMaintenanceController {

  /**
   * @author Jatin Patel & Anastasiia Nemyrovska
   * @param ticketID String ID of the maintenance ticket
   * @param staffEmail String email of the staff to be assigned
   * @param timeEstimate String time estimate for the ticket
   * @param priorityLevel String priority level of the ticket
   * @param approvalRequired String indicating whether approval is required
   * @return String error message if any are encountered
   */
  public static String assignStaffToTicket(String ticketID, String staffEmail, String timeEstimate,
      String priorityLevel, String approvalRequired) {
    if(ticketID.matches(".*\\D.*")){
      return "Ticket ID should be a number.";
    }
    if(!staffEmail.endsWith("@ap.com")){
      return "Can only assign ticket to hotel staff";
    }
    if (!MaintenanceTicket.hasWithId(Integer.parseInt(ticketID))) {
      return "Maintenance ticket does not exist.";
    }
    if (!HotelStaff.hasWithEmail(staffEmail)) {
      return "Staff to assign does not exist.";
    }

    MaintenanceTicket ticket = MaintenanceTicket.getWithId(Integer.parseInt(ticketID));
    try {
      ticket.assignStaff(staffEmail, MaintenanceTicket.PriorityLevel.valueOf(priorityLevel),
          MaintenanceTicket.TimeEstimate.valueOf(timeEstimate), Boolean.parseBoolean(approvalRequired));
    } catch (Exception e) {
      return e.getMessage();
    }
    try {
      AssetPlusPersistence.save();
    } catch (RuntimeException e) {
    }
    return "";
  }
  /**
   * @author Pei Yan Geng & Laurence Perreault & Dmytro Martyniuk
   * @param ticketID String ID of the maintenance ticket
   * @return String error message if any are encountered
   */
  public static String beginTicketWork(String ticketID) {
    String errorMsg = "";
    int intTicketID = Integer.parseInt(ticketID);
    if (!MaintenanceTicket.hasWithId(intTicketID)) {
      errorMsg += "Maintenance ticket does not exist.";
    }

    MaintenanceTicket aTicket = MaintenanceTicket.getWithId(intTicketID);
    try {
      aTicket.beginWork();
    } catch (Exception e) {
      errorMsg += e.getMessage();
    }

    try {
      AssetPlusPersistence.save();
    } catch (RuntimeException e) {
    }
    return errorMsg;

  }
  /**
   * @author Dmytro Martyniuk & Pei Yan Geng & Laurence Perreault
   * @param ticketID String ID of target ticket
   * @return String errors if any are encountered
   */
  public static String completeTicketWork(String ticketID) {
    int intTicketID = Integer.parseInt(ticketID);
    if (!MaintenanceTicket.hasWithId(intTicketID)) {
      return "Maintenance ticket does not exist.";
    }

    MaintenanceTicket aTicket = MaintenanceTicket.getWithId(intTicketID);
    try {
      aTicket.completeWork();
    } catch (Exception e) {
      return e.getMessage();
    }

    try {
      AssetPlusPersistence.save();
    } catch (RuntimeException e) {
    }

    return "";
  }
  /**
   * @author Behrad Rezaie & Marc-Antoine Nadeau
   * @param ticketID String ID of ticket to approve
   * @return String error if any are encountered
   */
  public static String approveTicketWork(String ticketID){
    if(!MaintenanceTicket.hasWithId(Integer.parseInt(ticketID))){
      return "Maintenance ticket does not exist.";
    }
    MaintenanceTicket ticket = MaintenanceTicket.getWithId(Integer.parseInt(ticketID));

    try {
      ticket.approve();
    } catch (Exception e) {
      return e.getMessage();
    }

    try {
      AssetPlusPersistence.save();
    } catch (RuntimeException e) {
    }

    return "";
  }
  /**
   * @author Behrad Rezaie & Marc-Antoine Nadeau
   * @param ticketID String ID of ticket to disapprove
   * @param date String date of disapproval
   * @param reason String reason for disapproval
   * @return String error if any are encountered
   */
  public static String disapproveTicketWork(String ticketID, String date, String reason){
    if(!MaintenanceTicket.hasWithId(Integer.parseInt(ticketID))){
      return "Maintenance ticket does not exist.";
    }

    MaintenanceTicket ticket = MaintenanceTicket.getWithId(Integer.parseInt(ticketID));
    try {
      ticket.disapprove(ticketID, date, reason);
    } catch (Exception e) {
      return e.getMessage();
    }
    try {
      AssetPlusPersistence.save();
    } catch (RuntimeException e) {
    }
    return "";
  }

}
