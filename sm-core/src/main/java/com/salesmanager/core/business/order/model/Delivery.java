package com.salesmanager.core.business.order.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Delivery {
	
	

	@Column (name ="DELIVERY_NAME")
	private String name;
	
	@Column (name ="DELIVERY_COMPANY")
	private String company;
	
	@Column (name ="DELIVERY_STREET_ADDRESS")
	private String address;

	@Column (name ="DELIVERY_CITY")
	private String city;
	
	@Column (name ="DELIVERY_POSTCODE")
	private String postalCode;
	
	@Column (name ="DELIVERY_STATE")
	private String state;
	
	@Column (name ="DELIVERY_COUNTRY")
	private String countryCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}



	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


}