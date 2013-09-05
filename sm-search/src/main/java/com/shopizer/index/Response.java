package com.shopizer.index;

public class Response {

	protected int result = 0;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Response() {
		super();
	}

}