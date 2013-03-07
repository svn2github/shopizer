package com.salesmanager.core.business.order.dao;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.QOrder;
import com.salesmanager.core.business.order.model.QOrderTotal;
import com.salesmanager.core.business.order.model.orderproduct.QOrderProduct;
import com.salesmanager.core.business.order.model.orderproduct.QOrderProductAttribute;
import com.salesmanager.core.business.order.model.orderstatus.QOrderStatusHistory;

@Repository("orderDao")
public class OrderDaoImpl  extends SalesManagerEntityDaoImpl<Long, Order> implements OrderDao {

	public OrderDaoImpl() {
		super();
	}
	
	@Override
	public Order getById(Long id) {
		
		
		QOrder qOrder = QOrder.order;
		QOrderProduct qOrderProduct = QOrderProduct.orderProduct;
		QOrderTotal qOrderTotal = QOrderTotal.orderTotal;
		QOrderStatusHistory qOrderStatusHistory = QOrderStatusHistory.orderStatusHistory;
		QOrderProductAttribute qOrderProductAttribute = QOrderProductAttribute.orderProductAttribute;
		//OrderAccount not loaded for now
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qOrder)
			.join(qOrder.orderProducts, qOrderProduct).fetch()
			.join(qOrder.orderTotal, qOrderTotal).fetch()
			.leftJoin(qOrder.orderHistory, qOrderStatusHistory).fetch()
			.leftJoin(qOrderProduct.downloads).fetch()
			.leftJoin(qOrderProduct.orderAttributes,qOrderProductAttribute).fetch()
			.leftJoin(qOrderProduct.prices).fetch()
			.leftJoin(qOrderProductAttribute.productOption).fetch()
			.leftJoin(qOrderProductAttribute.productOptionValue).fetch()
			.where(qOrder.id.eq(id));

		
		return query.uniqueResult(qOrder);
		
	}
}
