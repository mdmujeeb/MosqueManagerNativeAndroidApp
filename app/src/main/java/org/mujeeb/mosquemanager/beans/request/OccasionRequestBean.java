package org.mujeeb.mosquemanager.beans.request;

public class OccasionRequestBean extends BaseRequestBean {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String date;
	private String description;
	private Integer masjidId;
	
	public OccasionRequestBean() {}

	public Integer getMasjidId() {
		return masjidId;
	}

	public void setMasjidId(Integer masjidId) {
		this.masjidId = masjidId;
	}

	public OccasionRequestBean(Long id, String date, String description) {
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

	public String getName() {
		return date;
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
