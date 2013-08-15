package com.salesmanager.core.business.shoppingcart.service;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;


public interface ShoppingCartService {
	
	ShoppingCart getShoppingCart(Customer customer) throws ServiceException;
	
	void saveOrUpdate(ShoppingCart shoppingCart) throws ServiceException;

	ShoppingCart getById(Long id, MerchantStore store) throws ServiceException;
	




}