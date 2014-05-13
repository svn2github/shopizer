package com.salesmanager.core.business.order.dao;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.OrderList;

public interface OrderDao extends SalesManagerEntityDao<Long, Order> {
	
	OrderList listByStore(MerchantStore store, OrderCriteria criteria);
	
	/**
	 * <p>DAO method will will be responsible for fetching all orders associated with customer.
	 * In case customer has placed no order.
	 * </p>	
	 * @param store
	 * @param customerId
	 * @return {@link OrderList}
	 */
	OrderList getOrdersByCustomer(MerchantStore store, final OrderCriteria criteria);
}