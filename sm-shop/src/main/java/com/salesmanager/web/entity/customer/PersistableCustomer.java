package com.salesmanager.web.entity.customer;

import java.io.Serializable;

public class PersistableCustomer extends SecuredCustomer implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String billingCountryCode;//ISO code CA, US ...
	private String billingZoneCode;//internal code QC, AL ...
	private String deliveryCountryCode;
	private String deliveryZoneCode;
	private String billingStateProvince;
	private String deliveryStateProvince;
	public String getBillingCountryCode() {
		return billingCountryCode;
	}
	public void setBillingCountryCode(String billingCountryCode) {
		this.billingCountryCode = billingCountryCode;
	}
	public String getBillingZoneCode() {
		return billingZoneCode;
	}
	public void setBillingZoneCode(String billingZoneCode) {
		this.billingZoneCode = billingZoneCode;
	}
	public String getDeliveryCountryCode() {
		return deliveryCountryCode;
	}
	public void setDeliveryCountryCode(String deliveryCountryCode) {
		this.deliveryCountryCode = deliveryCountryCode;
	}
	public String getDeliveryZoneCode() {
		return deliveryZoneCode;
	}
	public void setDeliveryZoneCode(String deliveryZoneCode) {
		this.deliveryZoneCode = deliveryZoneCode;
	}
	public String getBillingStateProvince() {
		return billingStateProvince;
	}
	public void setBillingStateProvince(String billingStateProvince) {
		this.billingStateProvince = billingStateProvince;
	}
	public String getDeliveryStateProvince() {
		return deliveryStateProvince;
	}
	public void setDeliveryStateProvince(String deliveryStateProvince) {
		this.deliveryStateProvince = deliveryStateProvince;
	}

}
