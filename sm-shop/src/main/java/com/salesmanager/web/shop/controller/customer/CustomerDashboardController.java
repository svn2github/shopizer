package com.salesmanager.web.shop.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
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
public class CustomerDashboardController extends AbstractController {
	
	@Autowired
    private AuthenticationManager customerAuthenticationManager;
	
	@RequestMapping(value="/dashboard.html", method=RequestMethod.GET)
	public String displayCustomerDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	    final MerchantStore store = super.<MerchantStore>getSessionValue(Constants.MERCHANT_STORE);
        final Language language=super.<Language>getSessionValue(  Constants.LANGUAGE );
        
	   
		
		//LATEST orders
		
		model.addAttribute("section","dashboard");
		
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customer).append(".").append(store.getStoreTemplate());

		return template.toString();
		
	}

}
