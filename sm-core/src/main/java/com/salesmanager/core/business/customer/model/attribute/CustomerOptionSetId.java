package com.salesmanager.core.business.customer.model.attribute;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class CustomerOptionSetId implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CustomerOption customerOption;
	private CustomerOptionValue customerOptionValue;
	
	
	@ManyToOne
	public CustomerOption getCustomerOption() {
		return customerOption;
	}
	public void setCustomerOption(CustomerOption customerOption) {
		this.customerOption = customerOption;
	}
	
	@ManyToOne
	public CustomerOptionValue getCustomerOptionValue() {
		return customerOptionValue;
	}
	public void setCustomerOptionValue(CustomerOptionValue customerOptionValue) {
		this.customerOptionValue = customerOptionValue;
	}


}
