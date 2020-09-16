package org.mujeeb.mosquemanager.beans.request;

public class UpdateRefreshRequiredBean extends BaseRequestBean {

	private static final long serialVersionUID = 1L;

	private String refreshRequired;

	public UpdateRefreshRequiredBean() {}

	public UpdateRefreshRequiredBean(String userId, String password, String refreshRequired) {
		super(userId, password);
		this.refreshRequired = refreshRequired;
	}

	public String getRefreshRequired() {
		return refreshRequired;
	}

	public void setRefreshRequired(String refreshRequired) {
		this.refreshRequired = refreshRequired;
	}
}
