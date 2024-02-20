package ca.mcgill.ecse.assetplus.controller;

import java.sql.Date;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import ca.mcgill.ecse.assetplus.model.AssetType;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket;
import ca.mcgill.ecse.assetplus.model.TicketImage;
import ca.mcgill.ecse.assetplus.model.User;
import ca.mcgill.ecse.assetplus.persistence.AssetPlusPersistence;
import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.model.HotelStaff;
import ca.mcgill.ecse.assetplus.model.MaintenanceNote;

public class AssetPlusFeatureSet7Controller {

    private static AssetPlus assetplus = AssetPlusApplication.getAssetPlus();

    /**
     * @author Laurence Perreault
     * @param date the date the maintenance note was written
     * @param description the description of what is broken/report on fixing the issue
     * @param email the email of the person writing the note
     * @param ticketID the ID number of the associated ticket
     * @return the error message (if there is an error in any of the input)
     */
    public static String addMaintenanceNote(Date date, String description, int ticketID, String email) {
        StringBuilder error = new StringBuilder();

        if (description == null || description == "") {
            error.append("Ticket description cannot be empty");
        } 
        
        if (ticketID < 0) {
            error.append("Ticket id does not exist'");
        }
        
        if(!(MaintenanceTicket.hasWithId(ticketID))){
            error.append("Ticket does not exist");
        }
        else {
            MaintenanceTicket maintenanceTicket = MaintenanceTicket.getWithId(ticketID);
            
            if (date.before(maintenanceTicket.getRaisedOnDate())) {
                error.append("The date is incorrect");
            }
        }

        if( !User.hasWithEmail(email)){
            error.append("Hotel staff does not exist");
        }
        else {
            User user = User.getWithEmail(email);

            if (!(user instanceof HotelStaff)) {
                error.append("User cannot write a maintenance note");
            }
        }

        if (!error.isEmpty()){
            String result = error.toString();
            return result;
        }
        
        try {
            MaintenanceTicket ticket = MaintenanceTicket.getWithId(ticketID);
            User noteTaker = User.getWithEmail(email);

            for (MaintenanceNote note : ticket.getTicketNotes()) {
                if (note.getDescription().equals(description) && note.getNoteTaker().equals(noteTaker) && note.getDate().equals(date)) {
                    error.append("Note already exists for the ticket");
                    String result = error.toString();
                    return result;
                }
            }
            
            MaintenanceNote note = new MaintenanceNote (date, description, ticket, (HotelStaff) noteTaker);

            ticket.addTicketNote(note);
        } catch (Exception e){
            error.append(e.getMessage());
            String result = error.toString();
            return result;
        }
        
        String result = error.toString();

        try {
            AssetPlusPersistence.save();
          } catch (RuntimeException e){
        }
        return result;
    }

    /**
     * @author Laurence Perreault
     * @param newDate date the new maintenance note that was written
     * @param newDescription the new description of what is broken/report on fixing the issue
     * @param newEmail the email of the person writing the note
     * @param ticketID the ID number of the associated ticket
     * @return the error message (if there is an error in any of the input)
     */

    // index starts at 0
    public static String updateMaintenanceNote(int ticketID, int index, Date newDate, String newDescription, String newEmail) {

        StringBuilder error = new StringBuilder();

        if (newDescription == null || newDescription == "") {
            error.append("Ticket description cannot be empty");
        } 
        
        if (ticketID < 0) {
            error.append("The ticket id is incorrect");
        }
        
        if(!(MaintenanceTicket.hasWithId(ticketID))){
            error.append("Ticket does not exist");
        }
        else {
            MaintenanceTicket maintenanceTicket = MaintenanceTicket.getWithId(ticketID);
            
            if (newDate.before(maintenanceTicket.getRaisedOnDate())) {
                error.append("The date is incorrect");
            }
        }

        if( !User.hasWithEmail(newEmail)){
            error.append("Hotel staff does not exist");
        }
        else {
            User user = User.getWithEmail(newEmail);

            if (!(user instanceof HotelStaff)) {
                error.append("The user cannot write a maintenance note");
            }
        }

        if (!error.isEmpty()){
            String result = error.toString();
            return result;
        }
        
        try {
            MaintenanceTicket ticket = MaintenanceTicket.getWithId(ticketID);
            User noteTaker = User.getWithEmail(newEmail);

            if (index >= ticket.numberOfTicketNotes()) {
                error.append("Note does not exist");

                String result = error.toString();
                return result;
            }

            for (MaintenanceNote note : ticket.getTicketNotes()) {
                if (note.getDescription().equals(newDescription) && note.getNoteTaker().equals(noteTaker) && note.getDate().equals(newDate)) {
                    error.append("The maintenance note already exists");
                    String result = error.toString();
                    return result;
                }
            }
            
            MaintenanceNote updatedNote = ticket.getTicketNote(index);

            updatedNote.setDate(newDate);
            updatedNote.setDescription(newDescription);
            updatedNote.setNoteTaker((HotelStaff) noteTaker);
            
        } catch (Exception e){
            error.append(e.getMessage());
            String result = error.toString();
            return result;
        }
        
        String result = error.toString();

        try {
      AssetPlusPersistence.save();
    } catch (RuntimeException e){
    }
        return result;
    }

    /**
     * @author Laurence Perreault
     * @param index the index of the maintenance note that we wish to delete
     * @param ticketID the ID number of the associated ticket
     */

    // index starts at 0
    public static void deleteMaintenanceNote(int ticketID, int index) {

        StringBuilder error = new StringBuilder();
        
        if (ticketID < 0) {
            error.append("Ticket does not exist\n'");
        }

        if(!(MaintenanceTicket.hasWithId(ticketID))){
            error.append("Ticket does not exist\n ");
        }

        try {
            MaintenanceTicket ticket = MaintenanceTicket.getWithId(ticketID);
            MaintenanceNote deletedMaintenanceNote = ticket.getTicketNote(index);
            deletedMaintenanceNote.delete();
            ticket.removeTicketNote(deletedMaintenanceNote);
        } catch (RuntimeException e) {
    }

    try {
        AssetPlusPersistence.save();
      } catch (RuntimeException e){
      }
    }
}
