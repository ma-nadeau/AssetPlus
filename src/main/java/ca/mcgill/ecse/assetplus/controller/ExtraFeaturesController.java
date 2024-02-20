package ca.mcgill.ecse.assetplus.controller;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import ca.mcgill.ecse.assetplus.model.AssetType;
import ca.mcgill.ecse.assetplus.model.Employee;
import ca.mcgill.ecse.assetplus.model.Guest;
import ca.mcgill.ecse.assetplus.model.Manager;
import ca.mcgill.ecse.assetplus.model.SpecificAsset;
import javafx.application.Application;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;

/**
 * List of extra feature controller methods made for bonus features
 * @author Behrad Rezaie
 */
public class ExtraFeaturesController {

    /**
     * Calls the getGuests, getEmployees, and getManager functions and returns a comprehensive
     * list of all users in the system.
     * @author Behrad Rezaie
     * @return List<TOUser> list of all users as Transfer Object
     */
    public static List<TOUser> getUsers(){
      List<TOUser> users = new ArrayList<TOUser>();
      
      users.addAll(getGuests());
      users.addAll(getEmployees());
      users.addAll(getManager());

      return users;

    }
    /**
     * Returns a list of guests in the system as transfer objects
     * @author Behrad Rezaie
     * @return List<TOUser> list of guests
     */
    public static List<TOUser> getGuests(){
      List<TOUser> users = new ArrayList<TOUser>();
      
      List<Guest> guests = AssetPlusApplication.getAssetPlus().getGuests();
      for (Guest guest : guests) {
        String name = guest.getName();
        String email = guest.getEmail();
        String phoneNum = guest.getPhoneNumber();
        String password = guest.getPassword();
        String userType = "Guest";
        users.add(new TOUser(userType, name, phoneNum, email, password));
      }
      return users;
    }
    /**
     * Returns a list of employees in the system as transfer objects
     * @author Behrad Rezaie
     * @return List<TOUser> list of employees
     */
    public static List<TOUser> getEmployees(){
      List<TOUser> users = new ArrayList<TOUser>();
      
      List<Employee> employees = AssetPlusApplication.getAssetPlus().getEmployees();
      for (Employee employee : employees) {
        String name = employee.getName();
        String email = employee.getEmail();
        String phoneNum = employee.getPhoneNumber();
        String password = employee.getPassword();
        String userType = "Employee";
        users.add(new TOUser(userType, name, phoneNum, email, password));
    
      }
      return users;
    }
    
    /**
     * Returns a list of users containing the Manager as the sole item in that list
     * @author Behrad Rezaie
     * @return List<TOUser> list containing Manager as TO
     */
    public static List<TOUser> getManager(){
      List<TOUser> users = new ArrayList<TOUser>();
      Manager manager = AssetPlusApplication.getAssetPlus().getManager();
      users.add(new TOUser("Manager", manager.getName(), manager.getPhoneNumber(), manager.getEmail(), manager.getPassword()));

      return users;
    }

    /**
     * Takes an integer as input, and returns a list of Transfer Object Assets containing only 
     * the asset with ID same as the input
     * @param number integer asset ID.
     * @return List<TOAsset> List containing the asset with given ID 
     * @author Behrad Rezaie
     */
    public static List<TOAsset> getAssetByNumber(int number){
      List<SpecificAsset> listOfAsset = new ArrayList<SpecificAsset>();
      if(SpecificAsset.hasWithAssetNumber(number)){
        listOfAsset.add(SpecificAsset.getWithAssetNumber(number));
      }

      return convertToTransferObject(listOfAsset);
    }
    
    /**
     * Takes an integer as input as the floor number, and returns a list of Transfer Object Assets
     * that are on that floor
     * @param floorNumber integer floor number
     * @return List<TOAsset> List containing assets on the specified floor
     * @author Behrad Rezaie
     */
    public static List<TOAsset> getAssetsByFloor(int floorNumber){
      List<SpecificAsset> allAssets = AssetPlusApplication.getAssetPlus().getSpecificAssets();
      List<SpecificAsset> filteredAsset = new ArrayList<SpecificAsset>();
      for (SpecificAsset asset : allAssets) {
        if(asset.getFloorNumber() == floorNumber){
          filteredAsset.add(asset);
        }
      }
      return convertToTransferObject(filteredAsset);
    }

    /**
     * Takes a string as input as the asset type, and returns a list of Transfer Object Asssets 
     * that are of the same type as the input.
     * @param assetType String type of asset
     * @return List<TOAsset> list of assets of a specified type
     * @author Behrad Rezaie
     */
    public static List<TOAsset> getAssetsByType(String assetType){
      List<SpecificAsset> allAssets = AssetPlusApplication.getAssetPlus().getSpecificAssets();
      List<SpecificAsset> filteredAsset = new ArrayList<SpecificAsset>();
      
      for (SpecificAsset asset : allAssets) {
        if(asset.getAssetType().toString().contains(assetType)){
          filteredAsset.add(asset);
        }
      }


      return convertToTransferObject(filteredAsset);
    }

    /**
     * Returns all assets in the system as Transfer Objects.
     * @return List<TOAsset> all assets in the system
     * @author Behrad Rezaie
     */
    public static List<TOAsset> getAllAssets(){
      return convertToTransferObject(AssetPlusApplication.getAssetPlus().getSpecificAssets());
    }

    /**
     * Takes a list of Specific Assets in their model form and returns a list of the equivalent
     * Transfer Object Assets. 
     * @param listToConvert List<SpecificAsset> list of assets to convert to Transfer Objects
     * @return List<TOAsset> List of converted specific assets to Transfer Objects
     * @author Behrad Rezaie
     */
    public static List<TOAsset> convertToTransferObject(List<SpecificAsset> listToConvert){
      List<TOAsset> convertedList = new ArrayList<TOAsset>();
      for (SpecificAsset specificAsset : listToConvert) {
        int floorNumber = specificAsset.getFloorNumber();
        int roomNumber = specificAsset.getRoomNumber();
        int id = specificAsset.getAssetNumber();
        Date purchaseDate = specificAsset.getPurchaseDate();
        String type = specificAsset.getAssetType().getName();

        convertedList.add(new TOAsset(id, floorNumber, roomNumber, purchaseDate,type));
      }

      return convertedList;

    }
  
    /**
     * Finds all assetTypes in the system and returns their names in a list of strings
     * @return List<String> name of all assetTypes in the System.
     * @author Behrad Rezaie
     */
    public static List<String> getAllAssetTypes(){
      AssetPlus assetPlus = AssetPlusApplication.getAssetPlus();
      List<AssetType> assetTypes = assetPlus.getAssetTypes();

      List<String> allTypes = new ArrayList<String>();
      for (AssetType type : assetTypes) {
        allTypes.add(type.getName());
      }
      return allTypes;

    }
  }
