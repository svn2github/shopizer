package com.salesmanager.web.shop.controller.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.common.model.Billing;
import com.salesmanager.core.business.common.model.Delivery;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderSummary;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.payments.service.PaymentService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.shipping.model.ShippingProduct;
import com.salesmanager.core.business.shipping.model.ShippingQuote;
import com.salesmanager.core.business.shipping.service.ShippingService;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.shop.controller.ControllerConstants;

@Controller
@RequestMapping(Constants.SHOP_URI)
public class ShoppingOrderController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private ShippingService shippingService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private ZoneService zoneService;
	
	@RequestMapping("/order/checkout.html")
	public String displayCheckout(@CookieValue("cart") String cookie, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		Customer customer = (Customer)request.getSession().getAttribute(Constants.CUSTOMER);
		
		
		
		
		/**
		 * Shopping cart
		 * 
		 * ShoppingCart should be in the HttpSession
		 * Otherwise the cart id is in the cookie
		 * Otherwise the customer is in the session and a cart exist in the DB
		 * Else -> Nothing to display
		 */
		
		//TODO or should we get the shopping cart id from the database only
		
		String shoppingCartCode = null;
		
		ShoppingCartData shoppingCart = (ShoppingCartData)request.getSession().getAttribute(Constants.SHOPPING_CART);
		com.salesmanager.core.business.shoppingcart.model.ShoppingCart cart = null;
		if(shoppingCart==null) {//cookie
			if(!StringUtils.isBlank(cookie)) {
				shoppingCartCode = new String(cookie.getBytes());//TODO validate
			}
		} else {
			shoppingCartCode = shoppingCart.getCode();
		}
		
		if(shoppingCart==null) {//customer
			if(customer!=null) {
				cart=shoppingCartService.getByCustomer(customer);
			}
		}
		
		if(cart==null && shoppingCartCode!=null) {
			cart = shoppingCartService.getByCode(shoppingCartCode, store);
		}
		
		if(shoppingCartCode==null && cart==null) {//error
			return "redirect:/shop/shoppingCart.html";
		}
		

		if(customer!=null) {
			if(cart.getCustomerId()!=customer.getId().longValue()) {
				//TODO error ?
			}
		}

		Set<ShoppingCartItem> items = cart.getLineItems();
		if(CollectionUtils.isEmpty(items)) {
			return "redirect:/shop/shoppingCart.html";
		}
		
		//determine if shipping
		List<ShippingProduct> shippingProducts = null;
		boolean freeShipping = false;
		for(ShoppingCartItem item : items) {
			Product product = item.getProduct();
			//TODO determine if free
			if(!product.isProductVirtual() && product.isProductShipeable()) {
				if(shippingProducts==null) {
					shippingProducts = new ArrayList<ShippingProduct>();
				}
				ShippingProduct shippingProduct = new ShippingProduct(product);
				shippingProduct.setQuantity(item.getQuantity());
			}
		}
		
		if(shippingProducts!=null) {//get shipping methods
			ShippingQuote quote = shippingService.getShippingQuote(store, customer, shippingProducts, language);
			model.addAttribute("shippingQuote", quote);
		}

		//get payment methods
		Map<String,IntegrationConfiguration> paymentMethods = paymentService.getPaymentModulesConfigured(store);
		model.addAttribute("paymentMethods", paymentMethods);
		
		
		//TODO no payments configured
		
		//order total
		
		OrderSummary summary = new OrderSummary();
		List<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> productsList = new ArrayList<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem>();
		productsList.addAll(cart.getLineItems());
		summary.setProducts(productsList);
		//summary.setShippingSummary(shippingSummary)
		
		OrderTotalSummary orderTotalSummary = orderService.caculateOrderTotal(summary, customer, store, language);
		model.addAttribute("orderTotalSummary", orderTotalSummary);

		if(customer==null) {
			customer = new Customer();
			Billing billing = new Billing();
			billing.setCountry(store.getCountry());
			billing.setZone(store.getZone());
			billing.setState(store.getStorestateprovince());
			customer.setBilling(billing);
			
			Delivery delivery = new Delivery();
			delivery.setCountry(store.getCountry());
			delivery.setZone(store.getZone());
			delivery.setState(store.getStorestateprovince());
			customer.setDelivery(delivery);
		}
		
		//get countries
		List<Country> countries = countryService.getCountries(language);
		
		//get list of zones
		List<Zone> zones = zoneService.list();
		
		model.addAttribute("zones", zones);
		model.addAttribute("countries", countries);
		model.addAttribute("customer",customer);
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Checkout.checkout).append(".").append(store.getStoreTemplate());
		return template.toString();

		
	}

}
