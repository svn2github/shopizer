package com.salesmanager.core.business.order.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.dao.OrderDao;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.core.business.order.model.OrderList;
import com.salesmanager.core.business.order.model.Order_;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;


@Service("orderService")
public class OrderServiceImpl  extends SalesManagerEntityServiceImpl<Long, Order> implements OrderService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	private OrderDao orderDao;
	
	@Autowired
	public OrderServiceImpl(OrderDao orderDao) {
		super(orderDao);
		this.orderDao = orderDao;
	}

	@Override
	public Order getOrder(Long orderId ) {
		return getById(orderId);
	}
	
	@Override
	public List<Order> listByStore(MerchantStore merchantStore) {
		return listByField(Order_.merchant, merchantStore);
	}
	

	@Override
	public void addOrderStatusHistory(Order order, OrderStatusHistory history) throws ServiceException {
		order.getOrderHistory().add(history);
		history.setOrder(order);
		update(order);
	}
	

	
	@Override
	public void delete(Order order) throws ServiceException {
		
		//TODO delete sub objects
		super.delete(order);
	}
	
	@Override	
	public void saveOrUpdate(Order order) throws ServiceException {
				
		if(order.getId()!=null && order.getId()>0) {
			LOGGER.debug("Updating Order");	
			super.update(order);
			
		} else {
			LOGGER.debug("Creating Order");	
			super.create(order);

		}
	}

	@Override
	public OrderList listByStore(MerchantStore store, OrderCriteria criteria) {

		return orderDao.listByStore(store, criteria);
	}
}
