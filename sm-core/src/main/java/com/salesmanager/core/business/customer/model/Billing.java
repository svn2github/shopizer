package com.salesmanager.core.business.customer.model;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.zone.model.Zone;

@Embeddable
public class Billing extends com.salesmanager.core.business.order.model.Billing{
	
	

	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="BILLING_COUNTRY_ID", nullable=false)
	private Country country;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="BILLING_ZONE_ID", nullable=true)
	private Zone zone;



	public void setCountry(Country country) {
		this.country = country;
	}

	public Country getCountry() {
		return country;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public Zone getZone() {
		return zone;
	}
	



}
