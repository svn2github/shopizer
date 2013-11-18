package com.salesmanager.web.entity.customer;

import java.util.List;

import com.salesmanager.web.entity.customer.attribute.CustomerOption;

public class CustomerAttributes {
	
	private List<CustomerOption> customerOptions;

	public List<CustomerOption> getCustomerOptions() {
		return customerOptions;
	}

	public void setCustomerOptions(List<CustomerOption> customerOptions) {
		this.customerOptions = customerOptions;
	}

}
