package com.salesmanager.web.shop.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.customer.Customer;
import com.salesmanager.web.shop.controller.ControllerConstants;

/**
 * Registration of a new customer
 * @author Carl Samson
 *
 */
@Controller
public class CustomerRegistrationController {
	

	
	@RequestMapping(value="/shop/customer/registration.html", method=RequestMethod.GET)
	public String displayRegistration(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		
		//TODO object will change
		Customer customer = new Customer();
		
		model.addAttribute("customer", customer);
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.register).append(".").append(store.getStoreTemplate());

		return template.toString();
		
		
	}

}
