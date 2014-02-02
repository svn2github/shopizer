package com.salesmanager.web.shop.controller.order;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.attribute.CustomerOptionService;
import com.salesmanager.core.business.customer.service.attribute.CustomerOptionValueService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.payments.model.PaypalPayment;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.payments.service.PaymentService;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.shipping.model.ShippingOption;
import com.salesmanager.core.business.shipping.model.ShippingSummary;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.modules.integration.payment.impl.PayPalExpressCheckoutPayment;
import com.salesmanager.core.modules.integration.payment.model.PaymentModule;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.customer.PersistableCustomer;
import com.salesmanager.web.entity.order.ShopOrder;
import com.salesmanager.web.populator.customer.CustomerPopulator;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.order.facade.OrderFacade;
import com.salesmanager.web.shop.controller.shoppingCart.facade.ShoppingCartFacade;
import com.salesmanager.web.utils.LabelUtils;

@Controller
@RequestMapping(Constants.SHOP_URI+"/order")
public class ShoppingOrderPaymentController extends AbstractController {
	
	private static final Logger LOGGER = LoggerFactory
	.getLogger(ShoppingOrderPaymentController.class);
	
	private final static String INIT_ACTION = "init";
	
	@Autowired
	private ShoppingCartFacade shoppingCartFacade;
	
    @Autowired
    private ShoppingCartService shoppingCartService;
	
    @Autowired
	private LanguageService languageService;
	
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
	
	@Autowired
	private CustomerOptionService customerOptionService;
	
	@Autowired
	private CustomerOptionValueService customerOptionValueService;
	
	/**
	 * Recalculates shipping and tax following a change in country or province
	 * @param order
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/paymentAction.html/{action}/{paymentmethod}"}, method=RequestMethod.POST)
	public @ResponseBody AjaxResponse calculateShipping(@ModelAttribute(value="order") ShopOrder order, @PathVariable String action, @PathVariable String paymentmethod, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		String shoppingCartCode  = getSessionAttribute(Constants.SHOPPING_CART, request);
		
		Validate.notNull(shoppingCartCode,"shoppingCartCode does not exist in the session");

		try {
			
			IntegrationConfiguration config = paymentService.getPaymentConfiguration(paymentmethod, store);
			IntegrationModule integrationModule = paymentService.getPaymentMethodByCode(store, paymentmethod);
			
			com.salesmanager.core.business.shoppingcart.model.ShoppingCart cart = shoppingCartFacade.getShoppingCartModel(shoppingCartCode, store);
			OrderTotalSummary orderTotalSummary = orderFacade.calculateOrderTotal(store, order, language);
			
			ShippingSummary summary = (ShippingSummary)request.getSession().getAttribute("SHIPPING_SUMMARY");
			@SuppressWarnings("unchecked")
			List<ShippingOption> options = (List<ShippingOption>)request.getSession().getAttribute("SHIPPING_OPTIONS");
			
			if(summary!=null) {
				order.setShippingSummary(summary);
			}
			
			Customer customer = null;
			PersistableCustomer persistableCustomer = order.getCustomer();
			CustomerPopulator customerPopulator = new CustomerPopulator();
			customerPopulator.setCountryService(countryService);
			customerPopulator.setCustomerOptionService(customerOptionService);
			customerPopulator.setCustomerOptionValueService(customerOptionValueService);
			customerPopulator.setLanguageService(languageService);
			customerPopulator.setZoneService(zoneService);
			
			if(action.equals(this.INIT_ACTION)) {
				if(paymentmethod.equals("paypal-express-checkout")) {
					PaymentModule module = paymentService.getPaymentModule("paypal-express-checkout");
					PayPalExpressCheckoutPayment p = (PayPalExpressCheckoutPayment)module;
					PaypalPayment payment = new PaypalPayment();
					Transaction transaction = p.initTransaction(store, customer, orderTotalSummary.getTotal(), payment, config, integrationModule);
				}
			}
		
		} catch(Exception e) {
			LOGGER.error("Error while performing payment action " + action + " for payment method " + paymentmethod ,e);

		}
		
		return null;
	}

}
