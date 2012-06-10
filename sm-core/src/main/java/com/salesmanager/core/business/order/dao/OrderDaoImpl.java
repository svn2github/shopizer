package com.salesmanager.core.business.order.dao;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.order.model.Order;

@Repository("orderDao")
public class OrderDaoImpl  extends SalesManagerEntityDaoImpl<Long, Order> implements OrderDao {

	public OrderDaoImpl() {
		super();
	}
}
