package com.salesmanager.web.shop.controller.orders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.salesmanager.core.business.payments.service.PaymentService;
import com.salesmanager.core.business.shipping.service.ShippingService;

@Controller
public class ShoppingCheckoutController {
	
	
	@Autowired
	private ShippingService shippingService;
	
	@Autowired
	private PaymentService paymentService;
	
	
	@RequestMapping("/shop/orders/checkout.html")
	public String displayCheckout(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		//Customer
		
		//Transform shopping cart to OrderProduct ??? [should use the same methods than the shopping cart]
		
		//Calculate order total
		
		//Create order
		
		//Shipping
		
		//Payment
		
		return "";
	}

}
