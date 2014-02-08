package com.salesmanager.web.entity.customer;

import java.io.Serializable;

import javax.validation.constraints.Size;



public class SecuredCustomer extends CustomerEntity implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String userName;
	private String storeCode;
	private String password;
	private String checkPassword;


	public void setUserName(final String userName) {
		this.userName = userName;
	}

	//@NotEmpty(message="{registration.username.not.empty}")
	
	@Size(min=2, max=15, message="{registration.username.not.empty}")
	public String getUserName() {
		return userName;
	}


	
    @Size(min=6, message="{registration.password.not.empty}")
	public String getCheckPassword() {
        return checkPassword;
    }
    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }


	public void setStoreCode(final String storeCode) {
		this.storeCode = storeCode;
	}


	public String getStoreCode() {
		return storeCode;
	}

	
    @Size(min=6, message="{registration.password.not.empty}")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
