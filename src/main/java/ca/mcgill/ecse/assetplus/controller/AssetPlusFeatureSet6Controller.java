package ca.mcgill.ecse.assetplus.controller;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.model.*;
import ca.mcgill.ecse.assetplus.persistence.AssetPlusPersistence;


  /**
 * <h1> Feature Set 6 Controller Methods</h1>
 * @author Behrad Rezaie
 * @since 2023-10-12
 *  
 * Contains deleteEmployeeOrGuest and getTickets methods
 **/
public class AssetPlusFeatureSet6Controller {
/** <h2> deleteEmployeeOrGuest(String s) </h2>
 * Finds user by email. Deletes the object from the assetPlus instance
 * if the user is an employee or a guest.
 * @param email the user email
 * @return void
 * */
  public static void deleteEmployeeOrGuest(String email) {
    
    User queriedUser = User.getWithEmail(email);
    if(queriedUser instanceof Guest || queriedUser instanceof Employee){
      queriedUser.delete();
    } 
    
    try {
      AssetPlusPersistence.save();
    } catch (RuntimeException e){
    }
  }

  /**<h2> getTickets() </h2>

 * Gets all maintenance tickets in the assetPlus instance and returns them as
 * a List<TOMaintenanceTicket>. Retrieves each individual ticket information, 
 * creates its TO-object, and adds it to the list, iterating to the next ticket.
* @return List< TOMaintenanceTicket >
*/ 
  public static List<TOMaintenanceTicket> getTickets() {
      AssetPlus assetPlus = AssetPlusApplication.getAssetPlus();
      List<TOMaintenanceTicket> listOfTickets = new ArrayList<TOMaintenanceTicket>(); //Return value
      List<MaintenanceTicket> maintenanceTickets = assetPlus.getMaintenanceTickets();
      
      for (MaintenanceTicket ticket : maintenanceTickets) {
        
        //Getting Primitive Ticket attributes
        int aID = ticket.getId();
        Date aRaisedOnDate = ticket.getRaisedOnDate();
        String aDescription = ticket.getDescription();
        String emailRaiser = ticket.getTicketRaiser().getEmail();
        String status = ticket.getTicketStatusFullName();
        
        String priorityLevel = null;
        String timeEstimate = null;
        String ticketFixer = null;
        
        
        
        
        MaintenanceTicket.PriorityLevel priority = ticket.getPriority();
        if(priority != null){
          priorityLevel = priority.name();
        }
        MaintenanceTicket.TimeEstimate estimate = ticket.getTimeToResolve();
        if(estimate != null){
          timeEstimate = estimate.name();
        }
        if(ticket.hasTicketFixer()){
          ticketFixer = ticket.getTicketFixer().getEmail();
        }
        boolean approvalRequired = ticket.hasFixApprover();
        
        //Getting SpecificAsset Information
        String assetName = null;
        int lifeSpan = -1;
        Date purchaseDate = null;
        int aFloorNumber = -1;
        int aRoomNumber = -1;
        String assetNumber = null;
        if (ticket.hasAsset()){
          SpecificAsset asset = ticket.getAsset();
          assetNumber ="" + asset.getAssetNumber();
          System.out.println("selected Asset ID:" + assetNumber);
          
           assetName = asset.getAssetType().getName();
           lifeSpan = asset.getAssetType().getExpectedLifeSpan();
           purchaseDate = asset.getPurchaseDate();
           aFloorNumber = asset.getFloorNumber();
           aRoomNumber = asset.getRoomNumber();
          }
        
        //Getting Ticket Image URLS
        List<String> imageURLS = new ArrayList<String>();
        List<TicketImage> imageList = ticket.getTicketImages();
        for (TicketImage TicketImage : imageList) {
          imageURLS.add(TicketImage.getImageURL());
        }
        
        //Getting Ticket Notes
        TOMaintenanceNote[] allNotes = new TOMaintenanceNote[ticket.numberOfTicketNotes()];
        int i=0;
        //Converting from List type to array
        for (MaintenanceNote note : ticket.getTicketNotes()) {
          Date noteDate = note.getDate();
          String noteDescription = note.getDescription();
          String noteTaker = note.getNoteTaker().getEmail();
          TOMaintenanceNote MaintenanceNote = new TOMaintenanceNote(noteDate, noteDescription, noteTaker);
          allNotes[i] = MaintenanceNote;
          i++;
        }
        
        
        //Constructing TO of current Ticket
        TOMaintenanceTicket TOTicket = new TOMaintenanceTicket(aID, aRaisedOnDate, aDescription, emailRaiser, status,ticketFixer,timeEstimate,priorityLevel,approvalRequired, assetName,lifeSpan,purchaseDate,aFloorNumber,aRoomNumber,imageURLS, assetNumber, allNotes);
        listOfTickets.add(TOTicket);
      }

    return listOfTickets;
  }
  public static List<TOMaintenanceTicket> filterTicketsByDate(Date date) {
    
    List<TOMaintenanceTicket> filteredTickets = new ArrayList<TOMaintenanceTicket>();
    List<TOMaintenanceTicket> allTickets = getTickets();

    for (TOMaintenanceTicket ticket : allTickets) {
      System.out.println(date+" vs "+ticket.getRaisedOnDate());

      if(ticket.getRaisedOnDate().equals(date)){
        filteredTickets.add(ticket);
      }
    }
    return filteredTickets;
  }
  public static List<TOMaintenanceTicket> filterTicketsByEmployee(String employeeName) {
    AssetPlus assetPlus = AssetPlusApplication.getAssetPlus();
    List<TOMaintenanceTicket> filteredTickets = new ArrayList<TOMaintenanceTicket>();
    List<TOMaintenanceTicket> allTickets = getTickets();

    //Find employee's email based on input
    String employeeEmail = null;
    List<Employee> employees = assetPlus.getEmployees();
    List<Employee> filteredEmployees = new ArrayList<Employee>();
    for (Employee employee : employees) {
      if(employee.getName().contains(employeeName)){
        employeeEmail = employee.getEmail();
        filteredEmployees.add(employee);
      }
    }
    //Null check in case employee name not found
    if(employeeEmail == null){
      return filteredTickets;
    }
    //Loop through tickets for tickets fixed by employee
    for (Employee employee : filteredEmployees) {
      for (TOMaintenanceTicket ticket : allTickets) {
        if(ticket.getFixedByEmail()!=null && ticket.getFixedByEmail().equals(employee.getEmail())){
          filteredTickets.add(ticket);
        }
      }
    }

    return filteredTickets;
  }
  public static boolean ticketsAreEqual(TOMaintenanceTicket firstTick, TOMaintenanceTicket secondTick){
    //System.out.println(firstTick.toString());
    int index = firstTick.toString().indexOf('[');
    String subString = String.valueOf(firstTick.toString().subSequence(index+1, firstTick.toString().length()-1));
    String subString2 = String.valueOf(secondTick.toString().subSequence(index+1, secondTick.toString().length()-1));
    System.out.println(subString);
    
    System.out.println("\n");
    //System.out.println(secondTick.toString());
    return (subString.contains(subString2) || subString2.contains(subString));
  
  }
  public static List<TOMaintenanceTicket> filterByBoth(Date dateFilter, String employeeName){
    List<TOMaintenanceTicket> filteredTickets = new ArrayList<TOMaintenanceTicket>();
    List<TOMaintenanceTicket> allTickets = getTickets();
    AssetPlus assetPlus = AssetPlusApplication.getAssetPlus();

    List<TOMaintenanceTicket> employeeTickets = filterTicketsByEmployee(employeeName);
    List<TOMaintenanceTicket> dateTickets = filterTicketsByDate(dateFilter);

    for (TOMaintenanceTicket employeeTicket : employeeTickets) {
      for (TOMaintenanceTicket dateTicket : dateTickets) {
        if(ticketsAreEqual(dateTicket, employeeTicket)){
          filteredTickets.add(employeeTicket);
          System.out.println("equals");
        }
        else{
          System.out.println("not equals");
        }
      }
    }
    return filteredTickets;


    /* 
    String employeeEmail = null;
    List<Employee> employees = assetPlus.getEmployees();
    for (Employee employee : employees) {
      if(employee.getName().contains(employeeName)){
        employeeEmail = employee.getEmail();
        break;
      }
    }
    
    //Null check in case employee name not found
    if(employeeEmail == null){
      return filteredTickets;
    }
    for (TOMaintenanceTicket ticket : allTickets) {
      if(ticket.getFixedByEmail().equals(employeeEmail) && ticket.getRaisedOnDate().equals(dateFilter)){
        filteredTickets.add(ticket);
      }
    }*/


  }



}
