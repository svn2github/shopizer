package com.salesmanager.web.utils;

import org.springframework.stereotype.Component;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;

@Component
public class CustomerUtils {

	public com.salesmanager.web.entity.customer.Customer buildProxyCustomer(Customer customer, MerchantStore store) {
		com.salesmanager.web.entity.customer.Customer customerProxy = new com.salesmanager.web.entity.customer.Customer();
		customerProxy.setUserName(customer.getFirstname() +" "+ customer.getLastname());
		customerProxy.setPassword(customer.getPassword());
		customerProxy.setStoreCode(store.getCode());
		customerProxy.setId(customer.getId());
		customerProxy.setLanguage(customer.getDefaultLanguage().getCode());
		
		return customerProxy;
	}
	
}
