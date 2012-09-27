package com.salesmanager.web.admin.controller.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PaymentsController {

	
	@RequestMapping(value="/admin/payments/paymentMethods.html", method=RequestMethod.GET)
	public String getPaymentMethods(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		//set menu
		
		//merchant store from request
		
		//locale
		
		//get integration modules for PAYMENT
		
		//get configured modules
		
		
		
		return "payment-methods";

	}
	
	
}
