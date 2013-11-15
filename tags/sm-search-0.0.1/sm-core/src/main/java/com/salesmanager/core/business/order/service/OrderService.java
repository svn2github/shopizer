package com.salesmanager.core.business.order.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.OrderList;
import com.salesmanager.core.business.order.model.OrderSummary;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;
import com.salesmanager.core.business.reference.language.model.Language;

public interface OrderService extends SalesManagerEntityService<Long, Order> {
	
	Order getOrder(Long id);
	
	public OrderList listByStore(MerchantStore store, OrderCriteria criteria);
	
	public List<Order> listByStore(MerchantStore merchantStore);


	void saveOrUpdate(Order order) throws ServiceException;

	void addOrderStatusHistory(Order order, OrderStatusHistory history)
			throws ServiceException;

	ByteArrayOutputStream generateInvoice(MerchantStore store, Order order,
			Language language) throws ServiceException;

	/**
	 * Can be used to calculates the final prices of all items contained in a ShoppingCart
	 * @param orderSummary
	 * @param store
	 * @param language
	 * @return
	 * @throws ServiceException
	 */
	OrderTotalSummary caculateOrderTotal(OrderSummary orderSummary,
			MerchantStore store, Language language) throws ServiceException;

	/**
	 * Can be used to calculates the final prices of all items contained in checkout page
	 * @param orderSummary
	 * @param customer
	 * @param store
	 * @param language
	 * @return
	 * @throws ServiceException
	 */
	OrderTotalSummary caculateOrderTotal(OrderSummary orderSummary,
			Customer customer, MerchantStore store, Language language)
			throws ServiceException;


	

}
