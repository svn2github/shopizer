package com.salesmanager.web.shop.controller.merchant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shop.ContactForm;
import com.salesmanager.web.shop.controller.ControllerConstants;


@Controller
public class ContactUsController {
	
	/**
	 * Method for displaying contact us form
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/shop/contactUs.html"}, method=RequestMethod.GET)
	public String displayContactUs(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		Language language = (Language)request.getAttribute("LANGUAGE");
		
		model.addAttribute("contact", new ContactForm());

		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Merchant.contactUs).append(".").append(store.getStoreTemplate());
		return template.toString();
		
	}
	
	/**
	 * Submit contact us form
	 * @param model
	 * @param result
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/shop/submitContactUs.html"}, method=RequestMethod.POST)
	public String submitContactUs(@Valid @ModelAttribute("contact") ContactForm contact, Model model, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		Language language = (Language)request.getAttribute("LANGUAGE");

		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Merchant.contactUs).append(".").append(store.getStoreTemplate());
		return template.toString();
		
	}

}
