package com.salesmanager.core.modules.integration;

import com.salesmanager.core.business.generic.exception.ServiceException;

public class IntegrationException extends ServiceException {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int ERROR_VALIDATION_SAVE = 100;
	
	private int errorCode = 0;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public IntegrationException(Exception e) {
		super(e);
	}
	
	public IntegrationException(String message, Exception e) {
		super(message,e);
	}
	
	public IntegrationException(int code, String message) {
		
		super(message);
		this.errorCode = code;
	}

}
