package com.salesmanager.core.business.customer.model.attribute;

import java.io.Serializable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

//@Embeddable
public class CustomerOptionSetId implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//@NotNull
	@ManyToOne
	@JoinColumn(name = "CUSTOMER_OPTION_ID")
	private CustomerOption customerOption = null;
	@ManyToOne
	@JoinColumn(name = "CUSTOMER_OPTION_VALUE_ID")
	private CustomerOptionValue customerOptionValue = null;
	
	
	//@ManyToOne
	public CustomerOption getCustomerOption() {
		return customerOption;
	}
	public void setCustomerOption(CustomerOption customerOption) {
		this.customerOption = customerOption;
	}
	
	//@ManyToOne
	public CustomerOptionValue getCustomerOptionValue() {
		return customerOptionValue;
	}
	public void setCustomerOptionValue(CustomerOptionValue customerOptionValue) {
		this.customerOptionValue = customerOptionValue;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customerOption == null) ? 0 : customerOption.hashCode());
		result = prime
				* result
				+ ((customerOptionValue == null) ? 0 : customerOptionValue
						.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerOptionSetId other = (CustomerOptionSetId) obj;
		if (customerOption == null) {
			if (other.customerOption != null)
				return false;
		} else if (!customerOption.equals(other.customerOption))
			return false;
		if (customerOptionValue == null) {
			if (other.customerOptionValue != null)
				return false;
		} else if (!customerOptionValue.equals(other.customerOptionValue))
			return false;
		return true;
	}


}
