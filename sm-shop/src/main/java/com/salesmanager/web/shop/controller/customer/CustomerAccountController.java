package com.salesmanager.web.shop.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;

/**
 * Entry point for logged in customers
 * @author Carl Samson
 *
 */
@Controller
@RequestMapping("/shop/customer")
public class CustomerAccountController extends AbstractController {

	
	/**
	 * Dedicated customer logon page
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/customLogon.html", method=RequestMethod.GET)
	public String displayLogon(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	    final MerchantStore store = super.<MerchantStore>getSessionValue(Constants.MERCHANT_STORE);
        final Language language=super.<Language>getSessionValue(  Constants.LANGUAGE );
        

		//dispatch to dedicated customer logon
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customerLogon).append(".").append(store.getStoreTemplate());

		return template.toString();
		
	}
	
	
	@RequestMapping(value="/customer/account.html", method=RequestMethod.GET)
	public String displayCustomerAccount(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	    final MerchantStore store = super.<MerchantStore>getSessionValue(Constants.MERCHANT_STORE);
        final Language language=super.<Language>getSessionValue(  Constants.LANGUAGE );
        

		
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customer).append(".").append(store.getStoreTemplate());

		return template.toString();
		
	}
	
	
	@RequestMapping(value="/billing.html", method=RequestMethod.GET)
	public String displayCustomerBillingAddress(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	    final MerchantStore store = super.<MerchantStore>getSessionValue(Constants.MERCHANT_STORE);
        final Language language=super.<Language>getSessionValue(  Constants.LANGUAGE );
        

		
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customer).append(".").append(store.getStoreTemplate());

		return template.toString();
		
	}

}
