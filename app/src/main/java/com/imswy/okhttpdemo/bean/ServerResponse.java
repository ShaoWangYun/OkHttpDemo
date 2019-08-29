package com.imswy.okhttpdemo.bean;

public class ServerResponse {
	private int responseCode;
	private String responseInfo;
	private User user;

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(String responseInfo) {
		this.responseInfo = responseInfo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ServerResponse(int responseCode, String responseInfo, User user) {
		super();
		this.responseCode = responseCode;
		this.responseInfo = responseInfo;
		this.user = user;
	}

	public ServerResponse() {
		super();
	}

	@Override
	public String toString() {
		return "ServerResponse [responseCode=" + responseCode + ", responseInfo=" + responseInfo + ", user=" + user
				+ "]";
	}

}
