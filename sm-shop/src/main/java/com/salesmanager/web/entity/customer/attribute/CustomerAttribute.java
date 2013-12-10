package com.salesmanager.web.entity.customer.attribute;

import java.io.Serializable;

import com.salesmanager.web.entity.Entity;


public class CustomerAttribute extends Entity implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CustomerOption customerOption;
	private CustomerOptionValue customerOptionValue;
	private String textValue;
	public void setCustomerOptionValue(CustomerOptionValue customerOptionValue) {
		this.customerOptionValue = customerOptionValue;
	}
	public CustomerOptionValue getCustomerOptionValue() {
		return customerOptionValue;
	}
	public void setCustomerOption(CustomerOption customerOption) {
		this.customerOption = customerOption;
	}
	public CustomerOption getCustomerOption() {
		return customerOption;
	}
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
	public String getTextValue() {
		return textValue;
	}
}
