package com.salesmanager.core.business.shoppingcart.dao;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;

@Repository("shoppingCartDao")
public class ShoppingCartDaoImpl extends SalesManagerEntityDaoImpl<Long, ShoppingCart>
		implements ShoppingCartDao {
	


}
