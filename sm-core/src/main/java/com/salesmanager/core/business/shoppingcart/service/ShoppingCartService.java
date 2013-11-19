package com.salesmanager.core.business.shoppingcart.service;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.shipping.model.ShippingProduct;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;


public interface ShoppingCartService extends SalesManagerEntityService<Long, ShoppingCart> {
	
	ShoppingCart getShoppingCart(Customer customer) throws ServiceException;
	
	void saveOrUpdate(ShoppingCart shoppingCart) throws ServiceException;

	ShoppingCart getById(Long id, MerchantStore store) throws ServiceException;

	ShoppingCart getByCode(String code, MerchantStore store)
			throws ServiceException;

	ShoppingCart getByCustomer(Customer customer) throws ServiceException;

	/**
	 * Creates a list of ShippingProduct based on the ShoppingCart
	 * @param cart
	 * @return
	 * @throws ServiceException
	 */
	List<ShippingProduct> createShippingProduct(ShoppingCart cart)
			throws ServiceException;
	


	/**
	 * Looks if the items in the ShoppingCart are free of charges
	 * @param cart
	 * @return
	 * @throws ServiceException
	 */
	boolean isFreeShoppingCart(ShoppingCart cart) throws ServiceException;

	/**
	 * Populates a ShoppingCartItem from a Product and attributes if any
	 * @param product
	 * @return
	 * @throws ServiceException
	 */
	ShoppingCartItem populateShoppingCartItem(Product product)
			throws ServiceException;


	




}