package com.salesmanager.web.entity.customer;

import java.io.Serializable;

public class SecuredCustomer extends CustomerEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	private String storeCode;


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getUserName() {
		return userName;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getPassword() {
		return password;
	}


	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}


	public String getStoreCode() {
		return storeCode;
	}

}
