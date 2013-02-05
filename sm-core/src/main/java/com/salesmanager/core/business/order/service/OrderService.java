package com.salesmanager.core.business.order.service;

import java.math.BigDecimal;
import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;

public interface OrderService extends SalesManagerEntityService<Long, Order> {
	
	Order getOrder(Long id);
	
	public List<Order> listByStore(MerchantStore merchantStore);

	/**
	 * Calculates the total that would cost an order based on a list of Product
	 * Does not include shipping and other additional costs
	 * @param products
	 * @param store
	 * @return
	 * @throws ServiceException
	 */
	BigDecimal getTotal(List<Product> products, MerchantStore store)
			throws ServiceException;
	

}
