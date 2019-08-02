package com.twitter.polls.payload;

public class ApiResponse {
	
	private Boolean success;
	private Boolean message;
		
	public ApiResponse(Boolean success, Boolean message) {
		this.success = success;
		this.message = message;
	}
	
	public Boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	public Boolean getMessage() {
		return message;
	}
	
	public void setMessage(Boolean message) {
		this.message = message;
	}
	
}
