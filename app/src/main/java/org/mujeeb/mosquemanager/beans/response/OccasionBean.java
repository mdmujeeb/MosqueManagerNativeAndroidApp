package org.mujeeb.mosquemanager.beans.response;

import java.io.Serializable;
import java.util.Date;

public class OccasionBean implements Serializable {
	
	private static final long serialVersionUID = -6427145784371855347L;
	
	private Long id;
	private String  date;
	private String  description;

	public OccasionBean() {}
	
	public OccasionBean(Long id, Date dtDate, String date, String description) {
		this.id = id;
		this.date = date;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
