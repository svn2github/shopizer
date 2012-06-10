package com.salesmanager.core.business.customer.service;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;

public interface CustomerService  extends SalesManagerEntityService<Long, Customer> {

	public Customer getByName(String firstName);
}
