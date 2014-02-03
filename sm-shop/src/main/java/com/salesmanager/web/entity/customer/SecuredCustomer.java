package com.salesmanager.web.entity.customer;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

public class SecuredCustomer extends CustomerEntity implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String userName;
	private String storeCode;


	public void setUserName(final String userName) {
		this.userName = userName;
	}

	@NotEmpty(message="{registration.username.not.empty}")
	public String getUserName() {
		return userName;
	}





	public void setStoreCode(final String storeCode) {
		this.storeCode = storeCode;
	}


	public String getStoreCode() {
		return storeCode;
	}

}
