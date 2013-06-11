package com.salesmanager.core.business.customer.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.dao.CustomerDAO;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerCriteria;
import com.salesmanager.core.business.customer.model.CustomerList;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.modules.email.HtmlEmailSender;

@Service("customerService")
public class CustomerServiceImpl extends SalesManagerEntityServiceImpl<Long, Customer> implements CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	private CustomerDAO customerDAO;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private HtmlEmailSender htmlEmailSender;
	
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
	
	@Override
	public CustomerList listByStore(MerchantStore store, CustomerCriteria criteria) {
		return customerDAO.listByStore(store,criteria);
	}

	@Override	
	public void saveOrUpdate(Customer customer) throws ServiceException {

		LOGGER.debug("Creating Customer");
		
		if(customer.getId()!=null && customer.getId()>0) {
			super.update(customer);
		} else {			
		
			super.create(customer);

		}
	}
	
	public void resetCustomerPassword(Customer customer, MerchantStore store) throws ServiceException {
		
		
		//generate random password
		
		//encode using the passwordEncoder
		
		//set in customer object
		
		//get email template [NEED TO TALK ABOUT THIS]
		
		//get emailConfig - see ConfigurationController in sm-shop
		
		//send new password to the customer [use htmlEmailSender]
		
		//save customer object
		
		
		
	}
}
