package com.salesmanager.web.shop.controller.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderTotal;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.payments.model.PaymentMethod;
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
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.order.ReadableOrderTotal;
import com.salesmanager.web.entity.order.ReadableShippingSummary;
import com.salesmanager.web.entity.order.ReadableShopOrder;
import com.salesmanager.web.entity.order.ShopOrder;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.populator.order.ReadableOrderTotalPopulator;
import com.salesmanager.web.populator.order.ReadableShippingSummaryPopulator;
import com.salesmanager.web.populator.order.ReadableShopOrderPopulator;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.controller.order.facade.OrderFacade;
import com.salesmanager.web.shop.controller.shoppingCart.facade.ShoppingCartFacade;
import com.salesmanager.web.utils.LabelUtils;

@Controller
@RequestMapping(Constants.SHOP_URI+"/order")
public class ShoppingOrderController extends AbstractController {
	
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
	
	@Autowired
	private LabelUtils messages;
	
	@Autowired
	private PricingService pricingService;
	
	@RequestMapping("/checkout.html")
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
		
		//check if an existing order exist
		ShopOrder order = null;
		
		order = super.getSessionAttribute(Constants.ORDER, request);
		

		
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
		
	     if(order==null) {
	
	
			order = orderFacade.initializeOrder(store, customer, cart, language);
		
		  }

		boolean freeShoppingCart = shoppingCartService.isFreeShoppingCart(cart);
		
		/** shipping **/
		ShippingQuote quote = orderFacade.getShippingQuote(cart, order, store, language);
		model.addAttribute("shippingQuote", quote);

		if(quote!=null) {

			if(StringUtils.isBlank(quote.getShippingReturnCode())) {
			
				ShippingSummary summary = orderFacade.getShippingSummary(quote, store, language);
				order.setShippingSummary(summary);
				if(order.getSelectedShippingOption()==null) {
					order.setSelectedShippingOption(quote.getSelectedShippingOption());
				}
				
				//save quotes in HttpSession
				List<ShippingOption> options = quote.getShippingOptions();
				request.getSession().setAttribute("SHIPPING_SUMMARY", summary);
				request.getSession().setAttribute("SHIPPING_OPTIONS", options);
			
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
		List<PaymentMethod> paymentMethods = paymentService.getAcceptedPaymentMethods(store);

		//not free and no payment methods
		if(CollectionUtils.isEmpty(paymentMethods) && !freeShoppingCart) {
			LOGGER.error("No payment method configured");
			model.addAttribute("errorMessages", "No payments configured");
		}
		
		if(!CollectionUtils.isEmpty(paymentMethods)) {//select default payment method
			PaymentMethod defaultPaymentSelected = null;
			for(PaymentMethod paymentMethod : paymentMethods) {
				if(paymentMethod.isDefaultSelected()) {
					defaultPaymentSelected = paymentMethod;
					break;
				}
			}
			
			if(defaultPaymentSelected==null) {//forced default selection
				defaultPaymentSelected = paymentMethods.get(0);
				defaultPaymentSelected.setDefaultSelected(true);
			}
			
			
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
	@RequestMapping("/commit.html")
	public String commitOrder(Model model, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		
		ShopOrder order = super.getSessionAttribute(Constants.ORDER, request);
		if(order==null) {
			throw new Exception("Time out in transaction order is null");
		}
		
		try {
			
			return this.commitOrder(order, bindingResult, model, request, response, locale);
			
		} catch(Exception e) {
			LOGGER.error("Error while commiting order",e);
			throw e;		
			
		}
		

		
	}
	
	@RequestMapping("/commitShopOrder.html")
	public String commitShopOrder(@Valid @ModelAttribute(value="order") ShopOrder order, BindingResult bindingResult, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		try {
			
	        if ( bindingResult.hasErrors() )
	        {
	            LOGGER.info( "found {} validation error while validating in customer registration ",
	                         bindingResult.getErrorCount() );
	    		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Checkout.checkout).append(".").append(store.getStoreTemplate());
	    		return template.toString();

	        }
			
			return this.commitOrder(order, bindingResult, model, request, response, locale);
			
		} catch(Exception e) {
			LOGGER.error("Error while commiting order",e);
			throw e;		
			
		}
		
	}
	
	
	private String commitOrder(ShopOrder order, BindingResult bindingResult, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		

			

			MerchantStore store = super.getSessionAttribute(Constants.MERCHANT_STORE, request);

			//validate order shipping and billing
			if(StringUtils.isBlank(order.getCustomer().getBilling().getFirstName())) {
				FieldError error = new FieldError("customer.billing.firstName","customer.billing.firstName",messages.getMessage("NotEmpty.customer.firstName", locale));
            	bindingResult.addError(error);
			}
			
			if(StringUtils.isBlank(order.getCustomer().getBilling().getLastName())) {
				FieldError error = new FieldError("customer.billing.lastName","customer.billing.lastName",messages.getMessage("NotEmpty.customer.lastName", locale));
            	bindingResult.addError(error);
			}
			
			if(StringUtils.isBlank(order.getCustomer().getEmailAddress())) {
				FieldError error = new FieldError("customer.emailAddress","customer.emailAddress",messages.getMessage("NotEmpty.customer.emailAddress", locale));
            	bindingResult.addError(error);
			}
			
			if(StringUtils.isBlank(order.getCustomer().getBilling().getAddress())) {
				
			}
			
			if(StringUtils.isBlank(order.getCustomer().getBilling().getCity())) {
				
			}
			
			if(StringUtils.isBlank(order.getCustomer().getBilling().getCountry())) {
				
			}
			
			if(StringUtils.isBlank(order.getCustomer().getBilling().getZone()) && StringUtils.isBlank(order.getCustomer().getBilling().getStateProvince())) {
				
			}
			
			if(StringUtils.isBlank(order.getCustomer().getBilling().getPhone())) {
				
			}
			
			if(StringUtils.isBlank(order.getCustomer().getBilling().getPostalCode())) {
				
			}
			
			//validate payment
			
			//validate shipping
			
			//validate credit card
	
			//validate additional fields
			
	        if ( bindingResult.hasErrors() )
	        {
	            LOGGER.info( "found {} validation error while validating in customer registration ",
	                         bindingResult.getErrorCount() );
	    		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Checkout.checkout).append(".").append(store.getStoreTemplate());
	    		return template.toString();

	        }
			
			return null;

	}
	
	/**
	 * Recalculates shipping and tax following a change in country or province
	 * @param order
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/shippingQuotes.html"}, method=RequestMethod.POST)
	public @ResponseBody ReadableShopOrder calculateShipping(@ModelAttribute(value="order") ShopOrder order, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		String shoppingCartCode  = getSessionAttribute(Constants.SHOPPING_CART, request);
		
		Validate.notNull(shoppingCartCode,"shoppingCartCode does not exist in the session");
		
		ReadableShopOrder readableOrder = new ReadableShopOrder();
		try {

			//re-generate cart
			com.salesmanager.core.business.shoppingcart.model.ShoppingCart cart = shoppingCartFacade.getShoppingCartModel(shoppingCartCode, store);
	
			
			
			ReadableShopOrderPopulator populator = new ReadableShopOrderPopulator();
			populator.populate(order, readableOrder, store, language);
			
			ShippingQuote quote = orderFacade.getShippingQuote(cart, order, store, language);
			
			if(quote!=null) {
				if(StringUtils.isBlank(quote.getShippingReturnCode())) {
					ShippingSummary summary = orderFacade.getShippingSummary(quote, store, language);
					order.setShippingSummary(summary);//for total calculation
					
					
					ReadableShippingSummary readableSummary = new ReadableShippingSummary();
					ReadableShippingSummaryPopulator readableSummaryPopulator = new ReadableShippingSummaryPopulator();
					readableSummaryPopulator.setPricingService(pricingService);
					readableSummaryPopulator.populate(summary, readableSummary, store, language);
					
					readableSummary.setSelectedShippingOption(quote.getSelectedShippingOption());

					//save quotes in HttpSession
					List<ShippingOption> options = quote.getShippingOptions();
					readableSummary.setShippingOptions(options);
					
					readableOrder.setShippingSummary(readableSummary);
					request.getSession().setAttribute("SHIPPING_SUMMARY", summary);
					request.getSession().setAttribute("SHIPPING_OPTIONS", options);
				
				}

				if(quote.getShippingReturnCode()!=null && quote.getShippingReturnCode().equals(ShippingQuote.NO_SHIPPING_MODULE_CONFIGURED)) {
					LOGGER.error("Shipping quote error " + quote.getShippingReturnCode());
					readableOrder.setErrorMessage(messages.getMessage("message.noshipping", locale));
				}
				
				if(quote.getShippingReturnCode()!=null && quote.getShippingReturnCode().equals(ShippingQuote.NO_SHIPPING_TO_SELECTED_COUNTRY)) {
					LOGGER.error("Shipping quote error " + quote.getShippingReturnCode());
					readableOrder.setErrorMessage(messages.getMessage("message.noshipping", locale));
				}
				
				if(!StringUtils.isBlank(quote.getQuoteError())) {
					LOGGER.error("Shipping quote error " + quote.getQuoteError());
					readableOrder.setErrorMessage(messages.getMessage("message.noshippingerror", locale));
				}
				
				
			}
			
			//set list of shopping cart items for core price calculation
			List<ShoppingCartItem> items = new ArrayList<ShoppingCartItem>(cart.getLineItems());
			order.setShoppingCartItems(items);
			
			OrderTotalSummary orderTotalSummary = orderFacade.calculateOrderTotal(store, order, language);
			
			
			ReadableOrderTotalPopulator totalPopulator = new ReadableOrderTotalPopulator();
			totalPopulator.setMessages(messages);
			totalPopulator.setPricingService(pricingService);

			List<ReadableOrderTotal> subtotals = new ArrayList<ReadableOrderTotal>();
			for(OrderTotal total : orderTotalSummary.getTotals()) {
				if(!total.getOrderTotalCode().equals("order.total.total")) {
					ReadableOrderTotal t = new ReadableOrderTotal();
					totalPopulator.populate(total, t, store, language);
					subtotals.add(t);
				} else {//grand total
					ReadableOrderTotal ot = new ReadableOrderTotal();
					totalPopulator.populate(total, ot, store, language);
					readableOrder.setGrandTotal(ot.getTotal());
				}
			}
			
			
			readableOrder.setSubTotals(subtotals);
		
		} catch(Exception e) {
			LOGGER.error("Error while getting shipping quotes",e);
			readableOrder.setErrorMessage(messages.getMessage("message.error", locale));
		}
		
		return readableOrder;
	}
	
	/**
	 * Calculates the order total following price variation like changing a shipping option
	 * @param order
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/calculateOrderTotal.html"}, method=RequestMethod.POST)
	public @ResponseBody ReadableShopOrder calculateOrderTotal(@ModelAttribute(value="order") ShopOrder order, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		String shoppingCartCode  = getSessionAttribute(Constants.SHOPPING_CART, request);
		
		Validate.notNull(shoppingCartCode,"shoppingCartCode does not exist in the session");
		
		ReadableShopOrder readableOrder = new ReadableShopOrder();
		try {

			//re-generate cart
			com.salesmanager.core.business.shoppingcart.model.ShoppingCart cart = shoppingCartFacade.getShoppingCartModel(shoppingCartCode, store);

			ReadableShopOrderPopulator populator = new ReadableShopOrderPopulator();
			populator.populate(order, readableOrder, store, language);
			

			//re-create a quote
			//ShippingQuote quote = orderFacade.getShippingQuote(cart, order, store, language);
				
			//if(quote!=null) {
					//if(StringUtils.isBlank(quote.getShippingReturnCode())) {
			if(order.getSelectedShippingOption()!=null) {
						ShippingSummary summary = (ShippingSummary)request.getSession().getAttribute("SHIPPING_SUMMARY");
						@SuppressWarnings("unchecked")
						List<ShippingOption> options = (List<ShippingOption>)request.getSession().getAttribute("SHIPPING_OPTIONS");
						
						
						order.setShippingSummary(summary);//for total calculation
						
						
						ReadableShippingSummary readableSummary = new ReadableShippingSummary();
						ReadableShippingSummaryPopulator readableSummaryPopulator = new ReadableShippingSummaryPopulator();
						readableSummaryPopulator.setPricingService(pricingService);
						readableSummaryPopulator.populate(summary, readableSummary, store, language);
						
						
						if(!CollectionUtils.isEmpty(options)) {
						
							//get submitted shipping option
							ShippingOption quoteOption = null;
							ShippingOption selectedOption = order.getSelectedShippingOption();

							
							
							//check if selectedOption exist
							for(ShippingOption shipOption : options) {
								if(!StringUtils.isBlank(shipOption.getOptionId()) && shipOption.getOptionId().equals(selectedOption.getOptionId())) {
									quoteOption = shipOption;
								}
							}
							
							if(quoteOption==null) {
								quoteOption = options.get(0);
							}
							
							
							readableSummary.setSelectedShippingOption(quoteOption);
							readableSummary.setShippingOptions(options);
							

							summary.setShippingOption(quoteOption.getOptionId());
							summary.setShipping(quoteOption.getOptionPrice());
						
						}

						
						readableOrder.setShippingSummary(readableSummary);

					
					//}
					
/*					if(quote.getShippingReturnCode()!=null && quote.getShippingReturnCode().equals(ShippingQuote.NO_SHIPPING_MODULE_CONFIGURED)) {
						LOGGER.error("Shipping quote error " + quote.getShippingReturnCode());
						readableOrder.setErrorMessage(messages.getMessage("message.noshipping", locale));
					}
					
					if(!StringUtils.isBlank(quote.getQuoteError())) {
						LOGGER.error("Shipping quote error " + quote.getQuoteError());
						readableOrder.setErrorMessage(messages.getMessage("message.noshipping", locale));
					}
					
					if(quote.getShippingReturnCode()!=null && quote.getShippingReturnCode().equals(ShippingQuote.NO_SHIPPING_TO_SELECTED_COUNTRY)) {
						LOGGER.error("Shipping quote error " + quote.getShippingReturnCode());
						readableOrder.setErrorMessage(messages.getMessage("message.noshipping", locale));
					}*/
			}
			
			//set list of shopping cart items for core price calculation
			List<ShoppingCartItem> items = new ArrayList<ShoppingCartItem>(cart.getLineItems());
			order.setShoppingCartItems(items);
			
			OrderTotalSummary orderTotalSummary = orderFacade.calculateOrderTotal(store, order, language);
			
			
			ReadableOrderTotalPopulator totalPopulator = new ReadableOrderTotalPopulator();
			totalPopulator.setMessages(messages);
			totalPopulator.setPricingService(pricingService);

			List<ReadableOrderTotal> subtotals = new ArrayList<ReadableOrderTotal>();
			for(OrderTotal total : orderTotalSummary.getTotals()) {
				if(!total.getOrderTotalCode().equals("order.total.total")) {
					ReadableOrderTotal t = new ReadableOrderTotal();
					totalPopulator.populate(total, t, store, language);
					subtotals.add(t);
				} else {//grand total
					ReadableOrderTotal ot = new ReadableOrderTotal();
					totalPopulator.populate(total, ot, store, language);
					readableOrder.setGrandTotal(ot.getTotal());
				}
			}
			
			
			readableOrder.setSubTotals(subtotals);
		
		} catch(Exception e) {
			LOGGER.error("Error while getting shipping quotes",e);
			readableOrder.setErrorMessage(messages.getMessage("message.error", locale));
		}
		
		return readableOrder;
	}
	


}
