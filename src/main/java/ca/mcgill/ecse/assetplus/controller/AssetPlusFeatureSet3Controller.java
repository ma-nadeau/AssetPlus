package ca.mcgill.ecse.assetplus.controller;

import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import ca.mcgill.ecse.assetplus.model.AssetType;
import ca.mcgill.ecse.assetplus.model.SpecificAsset;
import ca.mcgill.ecse.assetplus.persistence.AssetPlusPersistence;
import java.sql.Date;

public class AssetPlusFeatureSet3Controller {

  private static AssetPlus assetPlus = AssetPlusApplication.getAssetPlus();

  /**
   * Checks constraints for asset number, floor number, and room number.
   *
   * @param assetNumber the asset number
   * @param floorNumber the floor number
   * @param roomNumber the room number
   * @return the message indicating any constraint violations
   * @author Anastasiia Nemyrovska
   */

  private static String checkConstraints(int assetNumber, int floorNumber, int roomNumber) {
    StringBuilder message = new StringBuilder();
    boolean constraintsMet = true;

    if (assetNumber < 1) {
      message.append("The asset number shall not be less than 1");
      constraintsMet = false;
    }

    if (floorNumber < 0) {
      message.append("The floor number shall not be less than 0");
      constraintsMet = false;
    }

    if (roomNumber < -1) {
      message.append("The room number shall not be less than -1");
      constraintsMet = false;
    }

    return constraintsMet ? "" : message.toString();
  }


  /**
   * Adds a specific asset to the AssetPlus system.
   *
   * @param assetNumber the asset number
   * @param floorNumber the floor number
   * @param roomNumber the room number
   * @param purchaseDate the purchase date
   * @param assetTypeName the name of the asset type
   * @return the error message if any error occurs during asset addition
   * @author Anastasiia Nemyrovska
   */

  public static String addSpecificAsset(int assetNumber, int floorNumber, int roomNumber,
      Date purchaseDate, String assetTypeName) {
    String inputValidation = checkConstraints(assetNumber, floorNumber, roomNumber);
    if (!inputValidation.isEmpty()) {
      return inputValidation;
    }
    AssetType assetType = AssetType.getWithName(assetTypeName.toLowerCase());
    if (assetType == null) {
      return "The asset type does not exist";
    }
    SpecificAsset assetToAdd;
    try {
      assetToAdd = new SpecificAsset(assetNumber, floorNumber, roomNumber, purchaseDate, assetPlus, assetType);
      System.out.println("Worked1");
    } catch (RuntimeException e) {
      String error = e.getMessage();
      if (error.startsWith("Cannot create due to duplicate assetNumber.")) {
        error = "The asset with the given asset number already exists.";
      }
      if (error.startsWith("Unable to create specificAsset due to assetPlus.")) {
        error = "The AssetPlus does not exist.";
      }
      return error;
    }
    //TODO FIX ME 
    //boolean added = assetPlus.addSpecificAsset(assetToAdd);
    //if (!added) {
    //  return "The asset was not added.";
    //}
    try {
          System.out.println("Worked");
          AssetPlusPersistence.save();
        } catch (RuntimeException e){
        }
    return "";
  }

  /**
   * Updates a specific asset in the AssetPlus system.
   *
   * @param assetNumber the asset number
   * @param newFloorNumber the new floor number
   * @param newRoomNumber the new room number
   * @param newPurchaseDate the new purchase date
   * @param newAssetTypeName the new asset type name
   * @return the error message if any error occurs during asset update
   * @author Anastasiia Nemyrovska
   */

  public static String updateSpecificAsset(int assetNumber, int newFloorNumber, int newRoomNumber,
      Date newPurchaseDate, String newAssetTypeName) {
    String error = "";
    String inputValidation = checkConstraints(assetNumber, newFloorNumber, newRoomNumber);
    if (!inputValidation.isEmpty()) {
      return inputValidation;
    }
    SpecificAsset assetToUpdate = SpecificAsset.getWithAssetNumber(assetNumber);
    if (assetToUpdate == null) {
      error = "The asset with asset number " + assetNumber + " was not found.";
    }
    AssetType newAssetType = AssetType.getWithName(newAssetTypeName.toLowerCase());
    if (newAssetType == null) {
      error += "The asset type does not exist";
    }
    if (error.length() > 0) {
      return error;
    }
    boolean floorNumberSet = assetToUpdate.setFloorNumber(newFloorNumber);
    boolean roomNumberSet = assetToUpdate.setRoomNumber(newRoomNumber);
    boolean purchaseDateSet = assetToUpdate.setPurchaseDate(newPurchaseDate);
    boolean assetTypeSet = assetToUpdate.setAssetType(newAssetType);
    if (!(floorNumberSet && roomNumberSet && purchaseDateSet && assetTypeSet)) {
      return "Asset update failed.";
    }

    try {
      AssetPlusPersistence.save();
    } catch (RuntimeException e){
    }
    
    return "";
  }

  /**
   * Deletes a specific asset from the AssetPlus system.
   *
   * @param assetNumber the asset number
   * @author Anastasiia Nemyrovska
   */

  public static void deleteSpecificAsset(int assetNumber) {
    try {
      SpecificAsset assetToDelete = SpecificAsset.getWithAssetNumber(assetNumber);
      assetToDelete.delete();
    } catch (RuntimeException e) {}

    try {
      AssetPlusPersistence.save();
    } catch (RuntimeException e){
    }
    
  }

}
