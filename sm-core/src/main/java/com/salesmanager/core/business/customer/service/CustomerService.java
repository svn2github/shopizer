package com.salesmanager.core.business.customer.service;


import java.util.List;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerCriteria;
import com.salesmanager.core.business.customer.model.CustomerList;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;

public interface CustomerService  extends SalesManagerEntityService<Long, Customer> {

	public List<Customer> getByName(String firstName);

	List<Customer> listByStore(MerchantStore store);

	Customer getByNick(String nick);
	void saveOrUpdate(Customer customer) throws ServiceException ;

	CustomerList listByStore(MerchantStore store, CustomerCriteria criteria);

	Customer getByNick(String nick, int storeId);


}
