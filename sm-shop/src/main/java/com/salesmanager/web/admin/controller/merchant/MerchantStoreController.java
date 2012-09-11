package com.salesmanager.web.admin.controller.merchant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;

@Controller
public class MerchantStoreController {
	
	@Autowired
	MerchantStoreService merchantStoreService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	LanguageService languageService;
	
	@RequestMapping(value="/admin/store/store.html", method=RequestMethod.GET)
	public String displayMerchantStore(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//TODO place key in constant
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		//get countries
		List<Country> countries = countryService.getCountries(language);
		
		
		//TODO use multiple store
		MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
		
		model.addAttribute("store", store);
		
		List<Language> languages = languageService.getLanguages();
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("store", "store");
		
		model.addAttribute("activeMenus",activeMenus);
		model.addAttribute("countries", countries);
		model.addAttribute("languages",languages);
		
		return "admin-store";
	}
	
	@RequestMapping(value="/admin/store/save.html", method=RequestMethod.POST)
	public String saveMenrchantStore(@Valid @ModelAttribute("store") MerchantStore store, BindingResult result, Model model) {
		

		
		
		if (result.hasErrors()) {
			return "admin/merchant/merchant";
		}

		
		//model.addAttribute("store", store);
		model.addAttribute("success","success");
		
		return "admin/merchant/merchant";
	}

}
