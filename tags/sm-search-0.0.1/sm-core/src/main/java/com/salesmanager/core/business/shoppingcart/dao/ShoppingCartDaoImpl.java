package com.salesmanager.core.business.shoppingcart.dao;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.shoppingcart.model.QShoppingCart;
import com.salesmanager.core.business.shoppingcart.model.QShoppingCartItem;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;

@Repository("shoppingCartDao")
public class ShoppingCartDaoImpl extends SalesManagerEntityDaoImpl<Long, ShoppingCart>
		implements ShoppingCartDao {
	
	
	@Override
	public ShoppingCart getById(Long id) {
		
		
		QShoppingCart qShoppingCart = QShoppingCart.shoppingCart;
		QShoppingCartItem qShoppingCartItem = QShoppingCartItem.shoppingCartItem;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qShoppingCart)
			.leftJoin(qShoppingCart.lineItems, qShoppingCartItem).fetch()
			.leftJoin(qShoppingCartItem.attributes).fetch()
			.leftJoin(qShoppingCart.merchantStore).fetch()
			.where(qShoppingCart.id.eq(id));
		
		return query.uniqueResult(qShoppingCart);
		
	}
	
	@Override
	public ShoppingCart getById(Long id, MerchantStore store) {
		
		
		QShoppingCart qShoppingCart = QShoppingCart.shoppingCart;
		QShoppingCartItem qShoppingCartItem = QShoppingCartItem.shoppingCartItem;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qShoppingCart)
			.leftJoin(qShoppingCart.lineItems, qShoppingCartItem).fetch()
			.leftJoin(qShoppingCartItem.attributes).fetch()
			.leftJoin(qShoppingCart.merchantStore).fetch()
			.where(qShoppingCart.id.eq(id)
					.and(qShoppingCart.merchantStore.id.eq(store.getId())));
		
		return query.uniqueResult(qShoppingCart);
		
	}
	
	@Override
	public ShoppingCart getByCode(String code, MerchantStore store) {
		
		
		QShoppingCart qShoppingCart = QShoppingCart.shoppingCart;
		QShoppingCartItem qShoppingCartItem = QShoppingCartItem.shoppingCartItem;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qShoppingCart)
			.leftJoin(qShoppingCart.lineItems, qShoppingCartItem).fetch()
			.leftJoin(qShoppingCartItem.attributes).fetch()
			.leftJoin(qShoppingCart.merchantStore).fetch()
			.where(qShoppingCart.shoppingCartCode.eq(code)
					.and(qShoppingCart.merchantStore.id.eq(store.getId())));
		
		return query.uniqueResult(qShoppingCart);
		
	}
	
	@Override
	public ShoppingCart getByCustomer(Customer customer) {
		
		
		QShoppingCart qShoppingCart = QShoppingCart.shoppingCart;
		QShoppingCartItem qShoppingCartItem = QShoppingCartItem.shoppingCartItem;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qShoppingCart)
			.leftJoin(qShoppingCart.lineItems, qShoppingCartItem).fetch()
			.leftJoin(qShoppingCartItem.attributes).fetch()
			.leftJoin(qShoppingCart.merchantStore).fetch()
			.where(qShoppingCart.customerId.eq(customer.getId()));
		
		return query.uniqueResult(qShoppingCart);
		
	}
	


}
