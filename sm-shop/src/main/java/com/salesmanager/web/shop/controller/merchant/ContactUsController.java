package com.salesmanager.web.shop.controller.merchant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.shop.controller.ControllerConstants;


@Controller
public class ContactUsController {
	
	
	@RequestMapping(value={"/shop/contactus.html"}, method=RequestMethod.GET)
	public String displayContactUs(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		Language language = (Language)request.getAttribute("LANGUAGE");
		
		//todo breadcrumb
		
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Merchant.contactUs).append(".").append(store.getStoreTemplate());

		return template.toString();
		
	}

}
