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
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.shipping.model.ShippingOption;
import com.salesmanager.core.business.shipping.model.ShippingQuote;
import com.salesmanager.core.business.shipping.model.ShippingSummary;
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
			return "redirect:/shop/cart/shoppingCart.html";
		}
		

		if(customer!=null) {
			if(cart.getCustomerId()!=customer.getId().longValue()) {
				return "redirect:/shop/shoppingCart.html";
			}
		} else {
			customer = orderFacade.initEmptyCustomer(store);
		}

		Set<ShoppingCartItem> items = cart.getLineItems();
		if(CollectionUtils.isEmpty(items)) {
			return "redirect:/shop/shoppingCart.html";
		}

		
		ShopOrder order = (ShopOrder)request.getSession().getAttribute("SHOP_ORDER");
		if(order==null) {
			order = orderFacade.initializeOrder(store, customer, cart, language);
		} else {
			//orderFacade.refreshOrder(order, store, customer, cart, language);
		}
		request.getSession().setAttribute("SHOP_ORDER", order);
		
		boolean freeShoppingCart = shoppingCartService.isFreeShoppingCart(cart);
		
		/** shipping **/
		ShippingQuote quote = orderFacade.getShippingQuote(cart, order, store, language);
		model.addAttribute("shippingQuote", quote);

		if(quote!=null) {

			if(StringUtils.isBlank(quote.getShippingReturnCode())) {
			
				ShippingSummary summary = orderFacade.getShippingSummary(quote, store, language);
				order.setShippingSummary(summary);
				order.setSelectedShippingOption(quote.getSelectedShippingOption());
				
				//save quotes in HttpSession
				List<ShippingOption> options = quote.getShippingOptions();
				request.getSession().setAttribute("SHIPPING_QUOTES", options);
			
			}
			
			
			//get shipping countries
			List<Country> shippingCountriesList = orderFacade.getShipToCountry(store, language);
			model.addAttribute("countries", shippingCountriesList);
		} else {
			//get all countries
			List<Country> countries = countryService.getCountries(language);
			model.addAttribute("countries", countries);
		}
		
		if(quote!=null && quote.getShippingReturnCode()!=null && quote.getShippingReturnCode().equals(ShippingQuote.NO_SHIPPING_MODULE_CONFIGURED)) {
			LOGGER.error("Shipping quote error " + quote.getShippingReturnCode());
			model.addAttribute("errorMessages", quote.getShippingReturnCode());
		}
		
		if(quote!=null && !StringUtils.isBlank(quote.getQuoteError())) {
			LOGGER.error("Shipping quote error " + quote.getQuoteError());
			model.addAttribute("errorMessages", quote.getQuoteError());
		}
		
		if(quote!=null && quote.getShippingReturnCode()!=null && quote.getShippingReturnCode().equals(ShippingQuote.NO_SHIPPING_TO_SELECTED_COUNTRY)) {
			LOGGER.error("Shipping quote error " + quote.getShippingReturnCode());
			model.addAttribute("errorMessages", quote.getShippingReturnCode());
		}
		/** end shipping **/


		//get payment methods
		Map<String,IntegrationConfiguration> paymentMethods = paymentService.getPaymentModulesConfigured(store);
		
		//not free and no payment methods
		if((paymentMethods==null || CollectionUtils.isEmpty(paymentMethods.keySet())) && !freeShoppingCart) {
			LOGGER.error("No payment method configured");
			model.addAttribute("errorMessages", "No payments configured");
		}
		
		//readable shopping cart items for order summary box
        ShoppingCartData shoppingCart = shoppingCartFacade.getShoppingCartData(cart);
        model.addAttribute( "cart", shoppingCart );
		


		//order total
		OrderTotalSummary orderTotalSummary = orderFacade.calculateOrderTotal(store, order, language);
		order.setOrderTotalSummary(orderTotalSummary);

		model.addAttribute("order",order);
		model.addAttribute("paymentMethods", paymentMethods);
		
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
