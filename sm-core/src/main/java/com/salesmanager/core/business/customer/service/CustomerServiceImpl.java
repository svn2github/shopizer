package com.salesmanager.core.business.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.dao.CustomerDAO;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.Customer_;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("customerService")
public class CustomerServiceImpl extends SalesManagerEntityServiceImpl<Long, Customer> implements CustomerService {

	@Autowired
	public CustomerServiceImpl(CustomerDAO customerDAO) {
		super(customerDAO);
	}

	@Override
	public Customer getByName(String firstName) {
		return getByField(Customer_.firstname, firstName);
	}

}
