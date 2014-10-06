package com.pfc.ballots.entities;




import javax.validation.constraints.Pattern;

import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.beaneditor.Validate;

import com.pfc.ballots.data.Sex;


/**
 * Profile Entity that contains the data associated to an user
 * 
 * @version 1.0, JUN-2013
 * @author Irene Grande Hernández
 * @version 2.0 FEB-2014
 * @author Mario Temprano Martin
 */

public class Profile {

	
	public Profile()
	{
		
	}
	public Profile(String email)
	{
		this.email=email;
	}
	
	/*********************************************** Profile fields **************************************************************/

	
	//It may be interesting to change the name of this field to any random word, for security reasons. I leave it like this
	//by now so the next developer don't go crazy and try to kill me. Whenever you need to create a new administrator from scratch,
	//just comment the line "NonVisual". But don't forget to change it back!
	@NonVisual
	private boolean isAdmin;
	@NonVisual
	private String Id;
	//this will also be the username
	@Validate("required,regexp=^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
	private String email;
	//Encrypted password of the user
	@Validate("required")
	private String password;

	//Other stuff of the user
	private Sex sex;
	@Validate("required")
	private String firstName;
	@Validate("required")
	private String lastName;
	private String university;
	//private String city;
	//private String country;
	
	
	public String getId()
	{
		return Id;
	}
	public void setId(String Id)
	{
		this.Id=Id;
	}
	public boolean getIsAdmin(){
		
		return isAdmin;
	}
	public void setIsAdmin(boolean isAdmin){
		
		this.isAdmin = isAdmin;
	}
	
	public String getEmail(){
			
		return email;
	}
	public void setEmail(String email){
			
		this.email = email;
	}
	
	public String getPassword(){
		
		return password;
	}
	public void setPassword(String password){
		
		this.password = password;
	}
		

	public Sex getSex(){
		
		return sex;
	}
	public void setSex(Sex sex){
		
		this.sex = sex;
	}

	
	public String getFirstName(){
		
		return firstName;
	}
	public void setFirstName(String firstName){
		
		this.firstName = firstName;
	}


	public String getLastName(){
		
		return lastName;
	}
	public void setLastName(String lastName){
		
		this.lastName = lastName;
	}

	
	
	public String getUniversity(){
		
		return university;
	}
	public void setUniversity(String university){
		
		this.university = university;
	}

	
	public String getCity(){
		
		return city;
	}
	public void setCity(String city){
		
		this.city = city;
	}
	

	public String getCountry(){
		
		return country;
	}
	public void setCountry(String country){
		
		this.country = country;
	}
}
