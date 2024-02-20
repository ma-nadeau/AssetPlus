package ca.mcgill.ecse.assetplus.controller;

import ca.mcgill.ecse.assetplus.model.MaintenanceTicket;
import ca.mcgill.ecse.assetplus.model.TicketImage;
import ca.mcgill.ecse.assetplus.persistence.AssetPlusPersistence;


    /**
  * The controller for the adding an image and delete an image to/from a maitenance ticket
  * @since 1.0
  * @author Dmytro Martyniuk - Student ID: 261118276
  */
public class AssetPlusFeatureSet5Controller {

    /**
   * Adds a specific image to a specific Maintenance Ticket .
   *
   * @param imageURL which is the url of the image the user wants to add to a Maitenance ticket
   * @param ticketID the id of the Maintenance to which the user wants to add an image

   * @return the error message if any error occurs during asset addition or returns an empty string if successfully added.
   * @author Dmytro Martyniuk

   */
    public static String addImageToMaintenanceTicket(String imageURL, int ticketID) {
        // Validattion of the inputs 
        StringBuilder error = new StringBuilder();

        if (imageURL == null || imageURL == "") {
            error.append("Image URL cannot be empty\n'");
        } else if (!imageURL.startsWith("http://") && !imageURL.startsWith("https://")) {
            error.append("Image URL must start with http:// or https://\n");
        }
        if (ticketID < 0) {
            error.append("Ticket does not exist\n'");
        }
        if( !MaintenanceTicket.hasWithId(ticketID) ){
            error.append("Ticket does not exist\n ");
        }

        if (!error.isEmpty()){
            String result = error.toString();
            return result;
        }
        
        try{
            //Initialize a new ticket object. We know there is one since no errors.
            MaintenanceTicket ticket = MaintenanceTicket.getWithId(ticketID);

            // Check if the imageURL is already in images in the ticket.
            for (TicketImage image : ticket.getTicketImages()) {
                if (image.getImageURL().equals(imageURL)) {
                    error.append("Image already exists for the ticket ");
                    String result = error.toString();
                    return result;
                }
            }
            // Create a new ticket image object with an existing ticket
            TicketImage ticketImage = new TicketImage(imageURL, ticket);

            // Add the ticket image to the ticket
            ticket.addTicketImage(ticketImage);
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
   * Deletes a specific image from a specific Maintenance Ticket .
   *
   * @param imageURL which is the url of the image the user wants to delete from a Maitenance ticket
   * @param ticketID the id of the Maintenance from which the user wants to delete an image

   * @author Dmytro Martyniuk

   */
    public static void deleteImageFromMaintenanceTicket(String imageURL, int ticketID) {

        var error = "";
        if (imageURL == null || imageURL == "") {
            error = "Image URL cannot be empty ";
        }else if (!imageURL.startsWith("http://") && !imageURL.startsWith("https://")) {
            error = "Image URL must start with http:// or https://";  
        }
        if (ticketID < 0) {
            error = "Ticket does not exist";     
        }
        if( !MaintenanceTicket.hasWithId(ticketID) ){
            error ="Ticket does not exist ";
        }

        if (error.isEmpty()){
            try {
                MaintenanceTicket ticket = MaintenanceTicket.getWithId(ticketID);
                // Searches for the correct image to be removed from the ticket. By cheking the
                // Urls of all iamges in the ticket
                for (TicketImage image : ticket.getTicketImages()) {
                    if (image.getImageURL().equals(imageURL)) {
                        // Remove the image from the maintenace ticket
                        ticket.removeTicketImage(image);
                        // Deletes the image
                        image.delete();
                        // Exits from the loop since the image is found
                        break; 
                    }
                }
            } catch (RuntimeException e) {
                error = e.getMessage();
            }
            try {
      AssetPlusPersistence.save();
    } catch (RuntimeException e){
    }
        }
    }
}
