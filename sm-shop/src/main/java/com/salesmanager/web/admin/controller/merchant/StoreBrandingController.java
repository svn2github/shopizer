package com.salesmanager.web.admin.controller.merchant;


import java.util.ArrayList;
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
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.currency.service.CurrencyService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.web.admin.entity.web.Menu;

@Controller
public class StoreBrandingController {
	
	@Autowired
	MerchantStoreService merchantStoreService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	ZoneService zoneService;
	
	
	@Autowired
	LanguageService languageService;
	
	@Autowired
	CurrencyService currencyService;
	
	@RequestMapping(value="/admin/store/storeBranding.html", method=RequestMethod.GET)
	public String displayStoreBranding(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		
		//TODO place key in constant
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		//get countries
		List<Country> countries = countryService.getCountries(language);
		
		
		//TODO use multiple store
		MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
		
		model.addAttribute("store", store);
		
		
		List<Language> languages = languageService.getLanguages();
		
		List<Currency> currencies = currencyService.list();
		

		model.addAttribute("countries", countries);
		model.addAttribute("languages",languages);
		model.addAttribute("currencies",currencies);
		
		return "admin-store";
	}
	
	@RequestMapping(value="/admin/store/saveBranding.html", method=RequestMethod.POST)
	public String saveStoreBranding(@Valid @ModelAttribute("store") MerchantStore store, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		
		
		MerchantStore sessionStore = (MerchantStore)request.getAttribute("MERCHANT_STORE");

		if(store.getId()>0) {
			if(store.getId().intValue() != sessionStore.getId().intValue()) {
				return "redirect:/admin/store/store.html";
			}
		}
		
		List<Currency> currencies = currencyService.list();
		
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		List<Language> languages = languageService.getLanguages();
		
		//get countries
		List<Country> countries = countryService.getCountries(language);
		
		if (result.hasErrors()) {
			return "admin-store";
		}
		
		//get country
		Country country = store.getCountry();
		country = countryService.getByCode(country.getIsoCode());
		Zone zone = store.getZone();
		if(zone!=null) {
			zone = zoneService.getByCode(zone.getCode());
		}
		Currency currency = store.getCurrency();
		currency = currencyService.getById(currency.getId());

		List<Language> supportedLanguages = store.getLanguages();
		List<Language> supportedLanguagesList = new ArrayList<Language>();
		Map<String,Language> languagesMap = languageService.getLanguagesMap();
		for(Language lang : supportedLanguages) {
			
			Language l = languagesMap.get(lang.getCode());
			if(l!=null) {
				
				supportedLanguagesList.add(l);
				
			}
			
		}
		
		Language defaultLanguage = store.getDefaultLanguage();
		defaultLanguage = languageService.getById(defaultLanguage.getId());
		
		if(!MerchantStore.DEFAULT_STORE.equals(sessionStore.getCode())) {
			
			sessionStore.setCode(store.getCode());
			
		}
		

		
		sessionStore.setCountry(country);
		sessionStore.setZone(zone);
		sessionStore.setStorestateprovince(store.getStorestateprovince());
		sessionStore.setCurrency(currency);
		sessionStore.setDefaultLanguage(defaultLanguage);
		sessionStore.setDomainName(store.getDomainName());
		sessionStore.setInBusinessSince(store.getInBusinessSince());
		sessionStore.setLanguages(supportedLanguagesList);
		sessionStore.setStoreaddress(store.getStoreaddress());
		sessionStore.setStorecity(store.getStorecity());
		sessionStore.setStoreEmailAddress(store.getStoreEmailAddress());
		
		//merchantStoreService.update(sessionStore);
		//update session store
		request.getSession().setAttribute("MERCHANT_STORE", sessionStore);


		model.addAttribute("success","success");
		model.addAttribute("countries", countries);
		model.addAttribute("languages",languages);
		model.addAttribute("languages",languages);
		model.addAttribute("currencies",currencies);
		
		return "admin-store";
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("store", "store");
		activeMenus.put("branding", "branding");

		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("store");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
