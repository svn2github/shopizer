package com.salesmanager.web.shop.controller.order;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
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
import com.salesmanager.web.entity.order.ShopOrder;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.order.facade.OrderFacade;
import com.salesmanager.web.shop.controller.shoppingCart.facade.ShoppingCartFacade;

@Controller
@RequestMapping(Constants.SHOP_URI)
public class ShoppingOrderController {
	
	private static final Logger LOGGER = LoggerFactory
	.getLogger(ShoppingOrderController.class);
	
	@Autowired
	private ShoppingCartFacade shoppingCartFacade;
	
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
	
	@Autowired
	private OrderFacade orderFacade;
	
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
		
		//Get the cart from the DB
		
		String shoppingCartCode  = (String)request.getSession().getAttribute(Constants.SHOPPING_CART);
		com.salesmanager.core.business.shoppingcart.model.ShoppingCart cart = null;

		if(!StringUtils.isBlank(shoppingCartCode)) {
			cart = shoppingCartFacade.getShoppingCartModel(shoppingCartCode, store);
		}

		if(cart==null && customer!=null) {
			cart=shoppingCartFacade.getShoppingCartModel(customer, store);
		}
		

		
		if(shoppingCartCode==null && cart==null) {//error
			return "redirect:/shop/shoppingCart.html";
		}
		

		if(customer!=null) {
			if(cart.getCustomerId()!=customer.getId().longValue()) {
				return "redirect:/shop/shoppingCart.html";
			}
		}

		Set<ShoppingCartItem> items = cart.getLineItems();
		if(CollectionUtils.isEmpty(items)) {
			return "redirect:/shop/shoppingCart.html";
		}
		
		ShopOrder order = orderFacade.initializeOrder(store, customer, cart, language);

		

		//create shipping products
		List<ShippingProduct> shippingProducts = shoppingCartService.createShippingProduct(cart);

		
		if(shippingProducts!=null) {//get shipping methods
			ShippingQuote quote = shippingService.getShippingQuote(store, customer, shippingProducts, language);
			model.addAttribute("shippingQuote", quote);
		}
		
		//determine if all free items
		boolean freeShoppingCart = shoppingCartService.isFreeShoppingCart(cart);

		//get payment methods
		Map<String,IntegrationConfiguration> paymentMethods = paymentService.getPaymentModulesConfigured(store);
		
		//not free and no payment methods
		if(paymentMethods==null && !freeShoppingCart) {
			model.addAttribute("errorMessages", "No payments configured");
		}

		//order total
		OrderTotalSummary orderTotalSummary = orderFacade.calculateOrderTotal(store, order, language);
		order.setOrderTotalSummary(orderTotalSummary);

		
		//get countries
		List<Country> countries = countryService.getCountries(language);
		
		//get list of zones
		List<Zone> zones = zoneService.list();
		
		model.addAttribute("zones", zones);
		model.addAttribute("countries", countries);
		model.addAttribute("shopOrder",order);
		model.addAttribute("paymentMethods", paymentMethods);
		//may have shippingquote
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Checkout.checkout).append(".").append(store.getStoreTemplate());
		return template.toString();

		
	}
	
	
	/**
	 * Method that saves the order to the database
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/order/commit.html")
	public String commitOrder(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		
		
		try {
			
			Language language = (Language)request.getAttribute("LANGUAGE");
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
			Customer customer = (Customer)request.getSession().getAttribute(Constants.CUSTOMER);
			ShopOrder order = (ShopOrder)request.getSession().getAttribute(Constants.ORDER);
			
			
			if(order==null) {
				//redirect
			}

			//transform ShoppingCartItem to OrderProduct
			
		} catch (Exception e) {
			LOGGER.error("An error occured while commiting the order",e);
		}
		
		return null;
		
	}

}
