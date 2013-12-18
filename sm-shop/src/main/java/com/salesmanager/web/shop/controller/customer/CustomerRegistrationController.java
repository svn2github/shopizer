package com.salesmanager.web.shop.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.customer.Customer;
import com.salesmanager.web.entity.customer.PersistableCustomer;
import com.salesmanager.web.populator.customer.CustomerPopulator;
import com.salesmanager.web.shop.controller.ControllerConstants;

/**
 * Registration of a new customer
 * @author Carl Samson
 *
 */
@Controller
@RequestMapping("/shop/customer")
public class CustomerRegistrationController {
	
	@Autowired
	private CustomerService customerService;
	

	
	@RequestMapping(value="/registration.html", method=RequestMethod.GET)
	public String displayRegistration(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		
		//TODO object will change
		Customer customer = new Customer();
		
		model.addAttribute("customer", customer);
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.register).append(".").append(store.getStoreTemplate());

		return template.toString();
		
		
	}
	
	@RequestMapping( value="/register.html", method=RequestMethod.POST)
	public String registerCustomer(@Valid @ModelAttribute PersistableCustomer customer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		
		//manual validations
		/** check if the customer exists **/
		
		com.salesmanager.core.business.customer.model.Customer cust = new com.salesmanager.core.business.customer.model.Customer();
		
		CustomerPopulator populator = new CustomerPopulator();
		populator.populate(customer, cust, merchantStore, merchantStore.getDefaultLanguage());
		

		customerService.save(cust);
		customer.setId(cust.getId());
		
		//set customer in http session
		
		
		return null;

	}

}
