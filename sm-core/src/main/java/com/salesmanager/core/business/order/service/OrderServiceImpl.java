package com.salesmanager.core.business.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.dao.OrderDao;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.Order_;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;

@Service("orderService")
public class OrderServiceImpl  extends SalesManagerEntityServiceImpl<Long, Order> implements OrderService {
	
	@Autowired
	public OrderServiceImpl(OrderDao orderDao) {
		super(orderDao);
	}

	@Override
	public Order getOrder(Long orderId ) {
		return getById(orderId);
	}
	
	public List<Order> getMerchantOrders(MerchantStore merchantStore) {
		return listByField(Order_.merchant, merchantStore);
	}
	
	public List<Order> getCustomerOrders(Customer customer) {
		return listByField(Order_.customer, customer);
	}
	
	public void addOrderStatusHistory(Order order, OrderStatusHistory history) throws ServiceException {
		order.getOrderHistory().add(history);
		history.setOrder(order);
		update(order);
	}
}
