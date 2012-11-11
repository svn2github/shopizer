package com.salesmanager.core.business.customer.dao;

import java.util.List;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;

public interface CustomerDAO extends SalesManagerEntityDao<Long, Customer> {
	
	public List<Customer> getByName(String name);
	
	
}
