package com.pfc.ballots.entities;

import java.util.Date;
/**
 * EditLog entity that contains the information of 
 * a ballot edition
 *
 *  
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2015
 */
public class EditLog {

	private String email;
	private Date editDate;
	private String ballotId;
	private String id;
	private String data;
	
	public Date getEditDate() {
		return editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	public String getBallotId() {
		return ballotId;
	}

	public void setBallotId(String ballotId) {
		this.ballotId = ballotId;
	}

	public String getNewData() {
		return data;
	}

	public void setNewData(String newData) {
		this.data = newData;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
