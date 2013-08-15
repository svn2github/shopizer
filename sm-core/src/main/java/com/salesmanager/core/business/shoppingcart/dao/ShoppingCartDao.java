package com.salesmanager.core.business.shoppingcart.dao;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;

public interface ShoppingCartDao extends SalesManagerEntityDao<Long, ShoppingCart> {

	ShoppingCart getById(Long id, MerchantStore store);

	ShoppingCart getByCustomer(Customer customer);


	
}
