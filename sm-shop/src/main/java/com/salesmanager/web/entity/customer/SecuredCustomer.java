package com.salesmanager.web.entity.customer;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.salesmanager.web.shop.constraints.FieldMatch;


@FieldMatch.List({
    @FieldMatch(first="password",second="checkPassword",message="password.notequal")
    
})
public class SecuredCustomer extends CustomerEntity implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="{NotEmpty.customer.userName}")
	private String userName;
	private String storeCode;
	@Size(min=6, message="{registration.password.not.empty}")
	private String password;
	
	@Size(min=6, message="{registration.password.not.empty}")
	private String checkPassword;
	


	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}





	public void setStoreCode(final String storeCode) {
		this.storeCode = storeCode;
	}


	public String getStoreCode() {
		return storeCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public String getCheckPassword()
    {
        return checkPassword;
    }

    public void setCheckPassword( String checkPassword )
    {
        this.checkPassword = checkPassword;
    }
	
	

}
