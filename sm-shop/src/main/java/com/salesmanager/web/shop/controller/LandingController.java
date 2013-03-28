package com.salesmanager.web.shop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.content.model.content.Content;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.constants.Constants;

@Controller
public class LandingController {
	
	
	@Autowired
	ContentService contentService;
	
	//@Autowired
	//CategoryService categoryService;
	
	@RequestMapping(value={"/shop/home.html","/shop/","/shop"}, method=RequestMethod.GET)
	public String displayLanding(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		
		//store.getStorename();
		//store.getLanguages();
		//store.getDefaultLanguage();
		
		Content content = contentService.getByCode("LANDING_PAGE", store, language);

		//content.getDescriptions().get(0).getName();//title
		//content.getDescriptions().get(0).getMetatagDescription();
		//content.getDescriptions().get(0).getMetatagKeywords();
		//content.getDescriptions().get(0).getContent();
		
		//model.addAttribute("activeMenus",activeMenus);
		

		if(content!=null) {
			model.addAttribute("page",content.getDescriptions().get(0));
		}
		
		//root categories
		
		//featured items
		
		StringBuilder template = new StringBuilder().append("landing.").append(store.getStoreTemplate());
		
		return "landing.bootstrap";//template.toString()
	}

}
