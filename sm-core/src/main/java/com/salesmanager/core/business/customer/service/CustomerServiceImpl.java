package com.salesmanager.core.business.customer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.dao.CustomerDAO;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;

@Service("customerService")
public class CustomerServiceImpl extends SalesManagerEntityServiceImpl<Long, Customer> implements CustomerService {

	private CustomerDAO customerDAO;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public CustomerServiceImpl(CustomerDAO customerDAO) {
		super(customerDAO);
		this.customerDAO = customerDAO;
	}

	@Override
	public List<Customer> getByName(String firstName) {
		return customerDAO.getByName(firstName);
	}
	
	@Override
	public Customer getById(Long id) {
			return customerDAO.getById(id);		
	}
	
	@Override
	public Customer getByNick(String nick) {
		return customerDAO.getByNick(nick);	
}
	
	@Override
	public List<Customer> listByStore(MerchantStore store) {
		return customerDAO.listByStore(store);
	}

}
