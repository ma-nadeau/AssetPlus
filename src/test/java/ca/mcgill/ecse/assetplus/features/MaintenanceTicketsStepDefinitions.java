package ca.mcgill.ecse.assetplus.features;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import ca.mcgill.ecse.assetplus.model.AssetType;
import ca.mcgill.ecse.assetplus.model.HotelStaff;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket;
import ca.mcgill.ecse.assetplus.model.Manager;
import ca.mcgill.ecse.assetplus.model.SpecificAsset;
import ca.mcgill.ecse.assetplus.model.TicketImage;
import ca.mcgill.ecse.assetplus.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet6Controller;
import ca.mcgill.ecse.assetplus.controller.TOMaintenanceNote;
import ca.mcgill.ecse.assetplus.controller.TOMaintenanceTicket;
import ca.mcgill.ecse.assetplus.controller.TicketMaintenanceController;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket.PriorityLevel;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket.TicketStatus;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket.TimeEstimate;
public class MaintenanceTicketsStepDefinitions {
  private static AssetPlus assetPlus = AssetPlusApplication.getAssetPlus();
  private String error = "";
  private void callController(String result) {
    if (!result.isEmpty()) {
      error += result;
    }
  }

  /**
   * @author Marc-Antoine Nadeau
   */
  @Given("the following employees exist in the system")
  public void the_following_employees_exist_in_the_system_p11(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> employeesToAdd = dataTable.asMaps();
    for (Map<String, String> employee : employeesToAdd) {
      String email = employee.get("email");
      String name = employee.get("name");
      String password = employee.get("password");
      String phoneNumber = employee.get("phoneNumber");
      assetPlus.addEmployee(email, name, password, phoneNumber);
    }
  }
  /**
   * @author Marc-Antoine Nadeau
   */
  @Given("the following manager exists in the system")
  public void the_following_manager_exists_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> managerToAdd = dataTable.asMaps();
    for (Map<String, String> manager : managerToAdd) {
      String email = manager.get("email");
      String password = manager.get("password");
      new Manager(email, "", password, "", assetPlus);
    }
  }
  /**
   * @author Marc-Antoine Nadeau
   */
  @Given("the following asset types exist in the system")
  public void the_following_asset_types_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> assetTypeToAdd = dataTable.asMaps();
      for (Map<String,String> assetType : assetTypeToAdd) {
        String name = assetType.get("name");
        int lifeSpan = Integer.parseInt(assetType.get("expectedLifeSpan"));
        assetPlus.addAssetType(name, lifeSpan);
        }
  }
  /**
   * @author Marc-Antoine Nadeau
   */

  @Given("the following assets exist in the system")
  public void the_following_assets_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> assetToAdd = dataTable.asMaps();
    for (Map<String, String> asset : assetToAdd) {
      int assetNumber = Integer.parseInt(asset.get("assetNumber"));
      AssetType assetType = AssetType.getWithName(asset.get("type"));
      Date purchaseDate = Date.valueOf(asset.get("purchaseDate"));
      int floorNumber = Integer.parseInt(asset.get("floorNumber"));
      int roomNumber = Integer.parseInt(asset.get("roomNumber"));
      assetPlus.addSpecificAsset(assetNumber, floorNumber, roomNumber, purchaseDate, assetType);
    }
  }

  /**
   * @author Marc-Antoine Nadeau & Pei Yan Geng
   */
  @Given("the following tickets exist in the system")
  public void the_following_tickets_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> ticketToAdd = dataTable.asMaps();
    for (Map<String,String> ticket : ticketToAdd) {
      int id = Integer.parseInt(ticket.get("id"));
      User ticketRaiser = User.getWithEmail(ticket.get("ticketRaiser"));
      Date raisedOnDate = Date.valueOf(ticket.get("raisedOnDate"));
      String description = ticket.get("description");
      int assetNumber;
      if (ticket.get("assetNumber") == null) {
        assetNumber = -1;
      }
      else {
        assetNumber = Integer.parseInt(ticket.get("assetNumber"));
      }
      MaintenanceTicket maintenanceTicket = new MaintenanceTicket(id, raisedOnDate, description, assetPlus, ticketRaiser);
      maintenanceTicket.setAsset(SpecificAsset.getWithAssetNumber(assetNumber));
      TicketStatus ticketStatus = TicketStatus.valueOf(ticket.get("status"));
      maintenanceTicket.setTicketStatus(ticketStatus);
      HotelStaff fixedByEmail = (HotelStaff) User.getWithEmail(ticket.get("fixedByEmail"));
      maintenanceTicket.setTicketFixer(fixedByEmail);
      Boolean approvalRequired = Boolean.parseBoolean(ticket.get("approvalRequired"));
      if(approvalRequired){
        maintenanceTicket.setFixApprover(assetPlus.getManager()); 
        
      }
      String timeToResolve = ticket.get("timeToResolve");
      String priority = ticket.get("priority");
      if(timeToResolve!=null){
        maintenanceTicket.setTimeToResolve(TimeEstimate.valueOf(timeToResolve));
      }
      if(priority != null){
        maintenanceTicket.setPriority(PriorityLevel.valueOf(priority));
      }
    }
  }

  /**
   * @author Marc-Antoine Nadeau
   */
  @Given("the following notes exist in the system")
  public void the_following_notes_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> noteToAdd = dataTable.asMaps();
    for (Map<String, String> note : noteToAdd) {
      MaintenanceTicket maintenanceTicket = MaintenanceTicket.getWithId(Integer.parseInt(note.get("ticketId")));
      maintenanceTicket.addTicketNote(Date.valueOf(note.get("addedOnDate")), note.get("description"), (HotelStaff) HotelStaff.getWithEmail(note.get("noteTaker")));
    }
  }

  /**
   * @author Marc-Antoine Nadeau
   */
  @Given("the following ticket images exist in the system")
  public void the_following_ticket_images_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> ticketImageToAdd= dataTable.asMaps();
    for (var ticketImage : ticketImageToAdd) {
      String imageUrl = ticketImage.get("imageUrl");
      int ticketId = Integer.parseInt(ticketImage.get("ticketId"));
      new TicketImage(imageUrl, MaintenanceTicket.getWithId(ticketId));
    }
  }

  /**
   * @author Marc-Antoine Nadeau
   */
  @Given("ticket {string} is marked as {string} with requires approval {string}")
  public void ticket_is_marked_as_with_requires_approval(String ticketId, String initialState, String requiresApproval) {
    int ticketID = Integer.parseInt(ticketId);
    MaintenanceTicket ticket = MaintenanceTicket.getWithId(ticketID);
    ticket.setTicketStatus(TicketStatus.valueOf(initialState)); 
    if(Boolean.parseBoolean(requiresApproval)){
      ticket.setFixApprover(assetPlus.getManager());
    }
  }

  /**
   * @author Marc-Antoine Nadeau
   */
  @Given("ticket {string} is marked as {string}")
  public void ticket_is_marked_as(String ticketId, String state) {
    int ticketID = Integer.parseInt(ticketId);
    MaintenanceTicket ticket = MaintenanceTicket.getWithId(ticketID);
    ticket.setTicketStatus(TicketStatus.valueOf(state)); 
  }
  /**
   * @author Pei Yan Geng & Behrad Rezaie
   */
  @When("the manager attempts to view all maintenance tickets in the system")
  public void the_manager_attempts_to_view_all_maintenance_tickets_in_the_system() {
    
    List<TOMaintenanceTicket> ticketList = AssetPlusFeatureSet6Controller.getTickets();
  }
  /**
   * @author Behrad Rezaie
   */
  @When("the manager attempts to assign the ticket {string} to {string} with estimated time {string}, priority {string}, and requires approval {string}")
  public void the_manager_attempts_to_assign_the_ticket_to_with_estimated_time_priority_and_requires_approval(
      String ticketID, String staffEmail, String timeEstimate, String priorityLevel, String approvalRequired) {
        
        callController(TicketMaintenanceController.assignStaffToTicket(ticketID,staffEmail,timeEstimate,priorityLevel,approvalRequired));
    
  }
  /**
   * @author Behrad Rezaie
   */
  @When("the hotel staff attempts to start the ticket {string}")
  public void the_hotel_staff_attempts_to_start_the_ticket(String ticketID) {
    callController(TicketMaintenanceController.beginTicketWork(ticketID));
  }
  /**
   * @author Behrad Rezaie
   */
  @When("the manager attempts to approve the ticket {string}")
  public void the_manager_attempts_to_approve_the_ticket(String ticketID) {
    callController(TicketMaintenanceController.approveTicketWork(ticketID));
  }
  /**
   * @author Behrad Rezaie
   */
  @When("the hotel staff attempts to complete the ticket {string}")
  public void the_hotel_staff_attempts_to_complete_the_ticket(String ticketID) {
    callController(TicketMaintenanceController.completeTicketWork(ticketID));
  }
  /**
   * @author Behrad Rezaie
   */
  @When("the manager attempts to disapprove the ticket {string} on date {string} and with reason {string}")
  public void the_manager_attempts_to_disapprove_the_ticket_on_date_and_with_reason(String ticketID,
      String date, String reason) {
        callController(TicketMaintenanceController.disapproveTicketWork(ticketID,date,reason));
  }
  /**
   * @author Behrad Rezaie
   */
  @Then("the ticket {string} shall be marked as {string}")
  public void the_ticket_shall_be_marked_as(String ticketID, String ticketStatus) {
    MaintenanceTicket ticket = MaintenanceTicket.getWithId(Integer.parseInt(ticketID));
    Assertions.assertEquals(ticketStatus, ticket.getTicketStatusFullName());
  }
  /**
   * @author Behrad Rezaie
   */
  @Then("the system shall raise the error {string}")
  public void the_system_shall_raise_the_error(String errorMessage) {
    Assertions.assertTrue(error.contains(errorMessage));
  }
  /**
   * @author Behrad Rezaie
   */
  @Then("the ticket {string} shall not exist in the system")
  public void the_ticket_shall_not_exist_in_the_system(String ticketIDString) {
    
    Assertions.assertEquals(MaintenanceTicket.hasWithId(Integer.parseInt(ticketIDString)),false);

  }
  /**
   * @author Behrad Rezaie
   */
  @Then("the ticket {string} shall have estimated time {string}, priority {string}, and requires approval {string}")
  public void the_ticket_shall_have_estimated_time_priority_and_requires_approval(String ticketIDString,
      String estimatedTimeString, String priorityLevelString, String approvalRequiredString) {
    
    MaintenanceTicket ticket = MaintenanceTicket.getWithId(Integer.parseInt(ticketIDString));
    
    Assertions.assertEquals(ticket.getTimeToResolve().name(), estimatedTimeString);
    Assertions.assertEquals(ticket.getPriority().name(), priorityLevelString);
    Assertions.assertEquals( Boolean.parseBoolean(approvalRequiredString),ticket.hasFixApprover());
  
  }
  /**
   * @author Behrad Rezaie
   */
  @Then("the ticket {string} shall be assigned to {string}")
  public void the_ticket_shall_be_assigned_to(String ticketIDString, String ticketFixerEmail) {
    
    MaintenanceTicket ticket = MaintenanceTicket.getWithId((Integer.parseInt(ticketIDString)));
    String actualfixerEmail = ticket.getTicketFixer().getEmail();
    Assertions.assertEquals(ticketFixerEmail, actualfixerEmail);
  }
  /**
   * @author Behrad Rezaie
   */
  @Then("the number of tickets in the system shall be {string}")
  public void the_number_of_tickets_in_the_system_shall_be(String numTicketsString) {
    
    int numTicketsExpected = Integer.parseInt(numTicketsString);
    Assertions.assertEquals(numTicketsExpected,assetPlus.numberOfMaintenanceTickets());
  }
  /**
   * @author Behrad Rezaie
   */
  @Then("the following maintenance tickets shall be presented")
  public void the_following_maintenance_tickets_shall_be_presented(
      io.cucumber.datatable.DataTable dataTable) {
    List<TOMaintenanceTicket> viewedTickets = AssetPlusFeatureSet6Controller.getTickets();
    
    List<Map<String, String>> ticketList = dataTable.asMaps();
    for (Map<String, String> ticket : ticketList){
      int id = Integer.parseInt(ticket.get("id"));
      
      TOMaintenanceTicket actualTicket = null;
      boolean ticketFound = false; 
      for (TOMaintenanceTicket toMaintenanceTicket : viewedTickets) {
        if(toMaintenanceTicket.getId() == id ){
          actualTicket = toMaintenanceTicket;
          ticketFound = true;
        }
       }

      String raiserEmail = ticket.get("ticketRaiser");
      String raisedDate = ticket.get("raisedOnDate");
      String description = ticket.get("description");
      
      String assetName = ticket.get("assetName");
      String lifeSpanString = ticket.get("expectLifeSpan");
      int lifeSpan = -1;
      if(lifeSpanString!=null){
         lifeSpan = Integer.parseInt(lifeSpanString);
      }
      Date purchaseDate = null; 
      if(ticket.get("purchaseDate") != null){
        purchaseDate = Date.valueOf(ticket.get("purchaseDate"));
      }
      int floorNumber = -1;
      if(ticket.get("floorNumber")!=null){
        floorNumber =  Integer.parseInt(ticket.get("floorNumber"));
      }
      int roomNumber = -1;
      if(ticket.get("roomNumber") != null){
        roomNumber =  Integer.parseInt(ticket.get("roomNumber"));
      }
      String status = ticket.get("status");
      String fixedByEmail = ticket.get("fixedByEmail");
      String timeToResolve = ticket.get("timeToResolve");
      String priority = ticket.get("priority");
      boolean approvalRequired = Boolean.parseBoolean(ticket.get("approvalRequired"));
    
      if(ticketFound){
      Assertions.assertEquals(raiserEmail, actualTicket.getRaisedByEmail());
      Assertions.assertEquals(raisedDate, actualTicket.getRaisedOnDate().toString());
      Assertions.assertEquals(description, actualTicket.getDescription());
      Assertions.assertEquals(assetName, actualTicket.getAssetName());
      Assertions.assertEquals(lifeSpan, actualTicket.getExpectLifeSpanInDays());
      Assertions.assertEquals(purchaseDate, actualTicket.getPurchaseDate());
      Assertions.assertEquals(floorNumber, actualTicket.getFloorNumber());
      Assertions.assertEquals(roomNumber, actualTicket.getRoomNumber());
      Assertions.assertEquals(status, actualTicket.getStatus());
      Assertions.assertEquals(fixedByEmail, actualTicket.getFixedByEmail());
      Assertions.assertEquals(timeToResolve, actualTicket.getTimeToResolve());
      Assertions.assertEquals(priority, actualTicket.getPriority());
      Assertions.assertEquals(approvalRequired, actualTicket.isApprovalRequired());
      }else{
        Assertions.fail();
      }
    }
  }

  /**
   * @author Laurence Perreault & Behrad Rezaie
   *
   */
  @Then("the ticket with id {string} shall have the following notes")
  public void the_ticket_with_id_shall_have_the_following_notes(String ticketIDString,
      io.cucumber.datatable.DataTable dataTable) {
    

    
    int ticketID = Integer.parseInt(ticketIDString);
    TOMaintenanceTicket targetTicket = null;  
    List<TOMaintenanceTicket> TOtickets = AssetPlusFeatureSet6Controller.getTickets();
      for (TOMaintenanceTicket toMaintenanceTicket : TOtickets) {
        if(toMaintenanceTicket.getId() == ticketID){
            targetTicket = toMaintenanceTicket;
        }
      }



    MaintenanceTicket ticket = MaintenanceTicket.getWithId(ticketID);
    List<TOMaintenanceNote> activeNotes = targetTicket.getNotes();
    
    List<Map<String, String>> ticketNoteList = dataTable.asMaps();
    Assertions.assertEquals(ticketNoteList.size(), ticket.numberOfTicketNotes());
    String expectedNotes = "";
    for(Map<String, String> ticketNote : ticketNoteList){
      String description = ticketNote.get("description");
      String date = ticketNote.get("addedOnDate");
      String noteTaker = ticketNote.get("noteTaker");
      
      expectedNotes+= description+date+noteTaker;
    }
    for(TOMaintenanceNote note : activeNotes){
      Assertions.assertTrue(expectedNotes.contains(note.getDate().toString()));
      Assertions.assertTrue(expectedNotes.contains(note.getNoteTakerEmail()));
      Assertions.assertTrue(expectedNotes.contains(note.getDescription()));
    }

  }
  /**
   * @author Behrad Rezaie
   */
  @Then("the ticket with id {string} shall have no notes")
  public void the_ticket_with_id_shall_have_no_notes(String ticketIDString) {
    int ticketID = Integer.parseInt(ticketIDString);
    Assertions.assertEquals(false,MaintenanceTicket.getWithId(ticketID).hasTicketNotes());

  }

  /**
   * @author Jatin Patel
   */
  @Then("the ticket with id {string} shall have the following images")
  public void the_ticket_with_id_shall_have_the_following_images(String ticketIDString,
      io.cucumber.datatable.DataTable dataTable) {

    int ticketID = Integer.parseInt(ticketIDString);

    MaintenanceTicket ticket = MaintenanceTicket.getWithId(ticketID);

    List<TicketImage> actualTicketImages = ticket.getTicketImages();

    List<Map<String, String>> ticketImageList = dataTable.asMaps();
    String imageURLS = "";
    for (TicketImage image : actualTicketImages) {
      imageURLS+=image.getImageURL();
    }
    for(Map<String, String> ticketImage : ticketImageList){

      String imageUrl = ticketImage.get("imageUrl");

      Assertions.assertEquals(true, imageURLS.contains(imageUrl));
    }

  }
  /**
   * @author Jatin Patel
   */
  @Then("the ticket with id {string} shall have no images")
  public void the_ticket_with_id_shall_have_no_images(String ticketIDString) {
    int ticketID = Integer.parseInt(ticketIDString);
    Assertions.assertEquals(false,MaintenanceTicket.getWithId(ticketID).hasTicketImages());
  
  }
}
