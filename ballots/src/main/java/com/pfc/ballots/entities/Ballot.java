package com.pfc.ballots.entities;

import java.util.Date;

import org.apache.tapestry5.beaneditor.NonVisual;

import com.pfc.ballots.data.Method;
/**
 * Ballot entity that contains the general information of a ballot
 * 
 * @author Mario Temprano Martin
 * @version 2.0 JUN-2014
 *
 */
public class Ballot {
	
	
	private String id;
	private String name;
	private String description;
	private String idOwner;
	private String idCensus;
	private Method method;
	private String idBallotData;
	@NonVisual
	private boolean teaching;
	@NonVisual
	private boolean privat;
	@NonVisual
	private boolean publica;
	private Date startDate;
	private Date endDate;
	private boolean ended;
	private boolean notStarted;
	private boolean active;
	private boolean counted;
	
	
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getIdOwner() {
			return idOwner;
		}
		public void setIdOwner(String idOwner) {
			this.idOwner = idOwner;
		}
		public String getIdCensus() {
			return idCensus;
		}
		public void setIdCensus(String idCensus) {
			this.idCensus = idCensus;
		}
		public Method getMethod() {
			return method;
		}
		public void setMethod(Method method) {
			this.method = method;
		}
		public String getIdBallotData() {
			return idBallotData;
		}
		public void setIdBallotData(String idBallotData) {
			this.idBallotData = idBallotData;
		}
		public boolean isTeaching() {
			return teaching;
		}
		public void setTeaching(boolean teaching) {
			this.teaching = teaching;
		}
		public boolean isPublica() {
			return publica;
		}
		public void setPublica(boolean publica) {
			this.publica = publica;
		}
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}
		public Date getEndDate() {
			return endDate;
		}
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
		public boolean isCounted() {
			return counted;
		}
		public void setCounted(boolean counted) {
			this.counted = counted;
		}
		public boolean isEnded() {
			return ended;
		}
		public void setEnded(boolean ended) {
			this.ended = ended;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public boolean isActive() {
			return active;
		}
		public void setActive(boolean active) {
			this.active = active;
		}
		public boolean isNotStarted() {
			return notStarted;
		}
		public void setNotStarted(boolean notStarted) {
			this.notStarted = notStarted;
		}
		public boolean isPrivat() {
			return privat;
		}
		public void setPrivat(boolean privat) {
			this.privat = privat;
		}
	
}
