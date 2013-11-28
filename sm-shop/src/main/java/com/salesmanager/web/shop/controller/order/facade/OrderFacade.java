package com.salesmanager.web.shop.controller.order.facade;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.web.entity.order.PersistableOrder;
import com.salesmanager.web.entity.order.ShopOrder;


public interface OrderFacade {
	
	ShopOrder initializeOrder(MerchantStore store, Customer customer, ShoppingCart shoppingCart, Language language) throws Exception;
	/** used in website **/
	OrderTotalSummary calculateOrderTotal(MerchantStore store, ShopOrder order, Language language) throws Exception;
	/** used in the API **/
	OrderTotalSummary calculateOrderTotal(MerchantStore store, PersistableOrder order, Language language) throws Exception;

	/** saves a valid order **/
	void saveOrder(ShopOrder order, MerchantStore store, Language language) throws Exception;
}
