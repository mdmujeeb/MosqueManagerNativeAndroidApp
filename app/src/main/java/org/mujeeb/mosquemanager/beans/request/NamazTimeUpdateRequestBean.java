package org.mujeeb.mosquemanager.beans.request;

public class NamazTimeUpdateRequestBean extends BaseRequestBean {

	private static final long serialVersionUID = 1L;

	private String name;
	private String time;
	
	public NamazTimeUpdateRequestBean() {}
	
	public NamazTimeUpdateRequestBean(String name, String time) {
		this.name = name;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
