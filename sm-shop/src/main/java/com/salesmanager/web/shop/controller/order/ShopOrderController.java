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
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.payments.service.PaymentService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shipping.model.ShippingProduct;
import com.salesmanager.core.business.shipping.model.ShippingQuote;
import com.salesmanager.core.business.shipping.service.ShippingService;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shoppingcart.ShoppingCart;

@Controller
public class ShopOrderController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private ShippingService shippingService;
	
	@Autowired
	private PaymentService paymentService;
	
	@RequestMapping("/shop/order/checkout.html")
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
		
		String shoppingCartCode = null;
		
		ShoppingCart shoppingCart = (ShoppingCart)request.getSession().getAttribute(Constants.SHOPPING_CART);
		com.salesmanager.core.business.shoppingcart.model.ShoppingCart cart = null;
		if(shoppingCart==null) {//cookie
			if(!StringUtils.isBlank(cookie)) {
				
			}
		} else {
			shoppingCartCode = shoppingCart.getCode();
		}
		
		if(shoppingCart==null) {//customer
			if(customer!=null) {
				cart=shoppingCartService.getByCustomer(customer);
			}
		}
		
		if(shoppingCartCode==null && cart==null) {//error
			//TODO error
		}
		
		//calculate total
		
		//TODO or should we get the shopping cart id from the database only
		if(cart==null) {
			cart = shoppingCartService.getByCode(shoppingCartCode, store);
		}

		
		if(cart==null) {
			//TODO error
		}
		
		Set<ShoppingCartItem> items = cart.getLineItems();
		
		if(CollectionUtils.isEmpty(items)) {
			//TODO error
		}
		
		//determine if shipping
		List<ShippingProduct> shippingProducts = null;
		
		for(ShoppingCartItem item : items) {
			Product product = item.getProduct();
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
		
		//TODO determine if free
		
		Map<String,IntegrationConfiguration> paymentMethods = paymentService.getPaymentModulesConfigured(store);
		
		//TODO no payments configured
		
		return null;
		
	}

}
