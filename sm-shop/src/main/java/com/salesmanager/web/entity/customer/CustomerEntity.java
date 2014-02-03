package com.salesmanager.web.entity.customer;

import java.io.Serializable;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class CustomerEntity extends Customer implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	
	private String firstName;
	
	
	private String lastName;
	
	private String emailAddress;
	private String phone;
	@Valid
	private Address billing;
	private Address delivery;
	private String gender;

	
	private String recaptcha_challenge_field;
	
	private String recaptcha_response_field;

	 private String pwd;
	 private String checkPwd;


	private String language;
	
	private String country;
    private String province;

	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	@Email (message="{registration.invalid.email}")
    @NotEmpty(message="{registration.invalid.email}")
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setPhone(final String phone) {
		this.phone = phone;
	}
	public String getPhone() {
		return phone;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}
	public String getLanguage() {
		return language;
	}
	
	@NotEmpty(message="{registration.firstName.invalid}")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}
	
	@NotEmpty(message="{registration.lastName.invalid}")
	public String getLastName() {
		return lastName;
	}
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}
	public Address getBilling() {
		return billing;
	}
	public void setBilling(final Address billing) {
		this.billing = billing;
	}
	public Address getDelivery() {
		return delivery;
	}
	public void setDelivery(final Address delivery) {
		this.delivery = delivery;
	}
	public void setGender(final String gender) {
		this.gender = gender;
	}
	public String getGender() {
		return gender;
	}
    public String getRecaptcha_challenge_field()
    {
        return recaptcha_challenge_field;
    }
    public void setRecaptcha_challenge_field( final String recaptcha_challenge_field )
    {
        this.recaptcha_challenge_field = recaptcha_challenge_field;
    }
    public String getRecaptcha_response_field()
    {
        return recaptcha_response_field;
    }
    public void setRecaptcha_response_field( final String recaptcha_response_field )
    {
        this.recaptcha_response_field = recaptcha_response_field;
    }
    public String getPwd()
    {
        return pwd;
    }
    public void setPwd( final String pwd )
    {
        this.pwd = pwd;
    }
    public String getCheckPwd()
    {
        return checkPwd;
    }
    public void setCheckPwd( final String checkPwd )
    {
        this.checkPwd = checkPwd;
    }

    @NotEmpty(message="{registration.lastName.invalid}")
    public String getCountry()
    {
        return country;
    }

    public void setCountry( String country )
    {
        this.country = country;
    }

    @NotEmpty(message="{registration.lastName.invalid}")
    public String getProvince()
    {
        return province;
    }

    public void setProvince( String province )
    {
        this.province = province;
    }

    

}
