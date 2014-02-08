package com.salesmanager.web.entity.customer;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.Size;

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
	@Size(message="{registration.firstName.invalid}")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}
	
	@NotEmpty(message="{registration.lastName.invalid}")
	@Size(message="{registration.lastName.invalid}")
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



    @NotEmpty(message="{registration.country.invalid}")
    @Size(min=1,message="{registration.country.invalid}")
    public String getCountry()
    {
        return country;
    }

    public void setCountry( String country )
    {
        this.country = country;
    }

    @NotEmpty(message="{registration.province.invalid}")
    @Size(min=1, message="{registration.province.invalid}")
    public String getProvince()
    {
        return province;
    }

    public void setProvince( String province )
    {
        this.province = province;
    }

    

}
