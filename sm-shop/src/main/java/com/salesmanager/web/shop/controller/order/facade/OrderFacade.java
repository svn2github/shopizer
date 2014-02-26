package com.salesmanager.web.shop.controller.order.facade;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.validation.BindingResult;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shipping.model.ShippingQuote;
import com.salesmanager.core.business.shipping.model.ShippingSummary;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.web.entity.order.PersistableOrder;
import com.salesmanager.web.entity.order.ShopOrder;


public interface OrderFacade {
	
	ShopOrder initializeOrder(MerchantStore store, Customer customer, ShoppingCart shoppingCart, Language language) throws Exception;
	void refreshOrder(ShopOrder order, MerchantStore store, Customer customer, ShoppingCart shoppingCart, Language language) throws Exception;
	/** used in website **/
	OrderTotalSummary calculateOrderTotal(MerchantStore store, ShopOrder order, Language language) throws Exception;
	/** used in the API **/
	OrderTotalSummary calculateOrderTotal(MerchantStore store, PersistableOrder order, Language language) throws Exception;

	/** process a valid order **/
	Order processOrder(ShopOrder order, MerchantStore store, Language language) throws ServiceException;
	/** process a valid order against an initial transaction **/
	Order processOrder(ShopOrder order, Transaction transaction, MerchantStore store, Language language) throws ServiceException;
	
	/** creates a working copy of customer when the user is anonymous **/
	Customer initEmptyCustomer(MerchantStore store);
	List<Country> getShipToCountry(MerchantStore store, Language language)
			throws Exception;
	
	/**
	 * Get a ShippingQuote based on merchant configuration and items to be shipped
	 * @param cart
	 * @param order
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ShippingQuote getShippingQuote(ShoppingCart cart, ShopOrder order,
			MerchantStore store, Language language) throws Exception;
	
	/**
	 * Creates a ShippingSummary object for OrderTotal calculation based on a ShippingQuote
	 * @param quote
	 * @param store
	 * @param language
	 * @return
	 */
	ShippingSummary getShippingSummary(ShippingQuote quote, MerchantStore store, Language language);
	void validateOrder(ShopOrder order, BindingResult bindingResult,
			Map<String, String> messagesResult, MerchantStore store,
			Locale locale) throws ServiceException;
}
