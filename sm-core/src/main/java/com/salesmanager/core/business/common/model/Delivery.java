package com.salesmanager.core.business.common.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.zone.model.Zone;

@Embeddable
public class Delivery {
	
	@Column (name ="DELIVERY_LAST_NAME", length=64)
	private String lastName;




	@Column (name ="DELIVERY_FIRST_NAME", length=64)
	private String firstName;

	


	@Column (name ="DELIVERY_COMPANY", length=100)
	private String company;
	
	@Column (name ="DELIVERY_STREET_ADDRESS", length=256)
	private String address;

	@Column (name ="DELIVERY_CITY", length=100)
	private String city;
	
	@Column (name ="DELIVERY_POSTCODE", length=20)
	private String postalCode;
	
	@Column (name ="DELIVERY_STATE", length=100)
	private String state;
	
	@Column(name="DELIVERY_TELEPHONE", length=32)
	private String telephone;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Country.class)
	@JoinColumn(name="DELIVERY_COUNTRY_ID", nullable=true)
	private Country country;
	

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Zone.class)
	@JoinColumn(name="DELIVERY_ZONE_ID", nullable=true)
	private Zone zone;
	


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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}
	

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getTelephone() {
		return telephone;
	}	
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
}
