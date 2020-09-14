package org.mujeeb.mosquemanager.beans.request;

import java.io.Serializable;

public class BaseRequestBean implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String userId;
	protected String password;
	
	public BaseRequestBean() {}
	
	public BaseRequestBean(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
