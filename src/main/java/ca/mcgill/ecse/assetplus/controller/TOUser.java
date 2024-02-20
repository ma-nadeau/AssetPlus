/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/

package ca.mcgill.ecse.assetplus.controller;

// line 36 "../../../../../../AssetPlusTransferObjects.ump"
public class TOUser
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOUser Attributes
  private String userType;
  private String name;
  private String phoneNumber;
  private String email;
  private String password;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOUser(String aUserType, String aName, String aPhoneNumber, String aEmail, String aPassword)
  {
    userType = aUserType;
    name = aName;
    phoneNumber = aPhoneNumber;
    email = aEmail;
    password = aPassword;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getUserType()
  {
    return userType;
  }

  public String getName()
  {
    return name;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public String getEmail()
  {
    return email;
  }

  public String getPassword()
  {
    return password;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "userType" + ":" + getUserType()+ "," +
            "name" + ":" + getName()+ "," +
            "phoneNumber" + ":" + getPhoneNumber()+ "," +
            "email" + ":" + getEmail()+ "," +
            "password" + ":" + getPassword()+ "]";
  }
}