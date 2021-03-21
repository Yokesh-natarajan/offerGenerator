package com.kognitiv.offer.beans.model.request;

import java.util.Date;

public class Offer {
	
	private String name;
	
	private Date validFrom;
	
	private Date validTill;
	
	private String location;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTill() {
		return validTill;
	}

	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Offer [name=" + name + ", validFrom=" + validFrom + ", validTill=" + validTill + ", location="
				+ location + "]";
	}
	
}
