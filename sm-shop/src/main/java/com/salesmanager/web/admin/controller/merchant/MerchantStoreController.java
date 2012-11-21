package com.salesmanager.web.admin.controller.merchant;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
import com.salesmanager.web.admin.entity.reference.Size;
import com.salesmanager.web.admin.entity.reference.Weight;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.utils.LabelUtils;

@Controller
public class MerchantStoreController {
	
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
	
	@Autowired
	LabelUtils messages;
	
	@RequestMapping(value="/admin/store/store.html", method=RequestMethod.GET)
	public String displayMerchantStore(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		setMenu(model,request);

		Language language = (Language)request.getAttribute("LANGUAGE");
		
		//get countries
		List<Country> countries = countryService.getCountries(language);
		
		
		//TODO use multiple store
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		model.addAttribute("store", store);
		
		List<Language> languages = languageService.getLanguages();
		
		List<Currency> currencies = currencyService.list();
		
		List<Weight> weights = new ArrayList<Weight>();
		weights.add(new Weight("LB",messages.getMessage("label.generic.weightunit.LB", locale)));
		weights.add(new Weight("KG",messages.getMessage("label.generic.weightunit.KG", locale)));
		
		List<Size> sizes = new ArrayList<Size>();
		sizes.add(new Size("CM",messages.getMessage("label.generic.sizeunit.CM", locale)));
		sizes.add(new Size("IN",messages.getMessage("label.generic.sizeunit.IN", locale)));
		
		//display menu

		model.addAttribute("countries", countries);
		model.addAttribute("languages",languages);
		model.addAttribute("currencies",currencies);
		
		model.addAttribute("weights",weights);
		model.addAttribute("sizes",sizes);
		
		
		return "admin-store";
	}
	
	@RequestMapping(value="/admin/store/save.html", method=RequestMethod.POST)
	public String saveMenrchantStore(@Valid @ModelAttribute("store") MerchantStore store, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		setMenu(model,request);
		
		
		MerchantStore sessionStore = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

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
		
		List<Weight> weights = new ArrayList<Weight>();
		weights.add(new Weight("LB",messages.getMessage("label.generic.weightunit.LB", locale)));
		weights.add(new Weight("KG",messages.getMessage("label.generic.weightunit.KG", locale)));
		
		List<Size> sizes = new ArrayList<Size>();
		sizes.add(new Size("CM",messages.getMessage("label.generic.sizeunit.CM", locale)));
		sizes.add(new Size("IN",messages.getMessage("label.generic.sizeunit.IN", locale)));
		
		//TODO no supported languages
		
		if (result.hasErrors()) {
			
	        Map<String, String> errors = new LinkedHashMap<String, String>();
	        for (FieldError error : result.getFieldErrors()) {            
	        	//errors.put(error.getField(), error.getDefaultMessage());
	        	if(error.getField().equals("languages")) {
	        		
	        		if(store.getLanguages()==null || store.getLanguages().size()==0) {
	        			Language en = languageService.getByCode("en");
	        			List<Language> langs = new ArrayList<Language>();
	        			//langs.add(en);
	        			//store.setLanguages(langs);
	        		}
	        		
	        	}
	        } 
	        
			model.addAttribute("weights",weights);
			model.addAttribute("sizes",sizes);
			
			model.addAttribute("countries", countries);
			model.addAttribute("languages",languages);
			model.addAttribute("currencies",currencies);
			model.addAttribute("store", store);
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
		if(defaultLanguage!=null) {
			store.setDefaultLanguage(defaultLanguage);
		}
		
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
		sessionStore.setLanguages(supportedLanguagesList);
		sessionStore.setWeightunitcode(store.getWeightunitcode());
		sessionStore.setSeizeunitcode(store.getSeizeunitcode());
		
		merchantStoreService.update(sessionStore);
		//update session store
		request.getSession().setAttribute(Constants.ADMIN_STORE, sessionStore);


		model.addAttribute("success","success");
		model.addAttribute("countries", countries);
		model.addAttribute("languages",languages);
		model.addAttribute("store", sessionStore);
		
		model.addAttribute("weights",weights);
		model.addAttribute("sizes",sizes);

		model.addAttribute("currencies",currencies);
		
		return "admin-store";
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("store", "store");
		activeMenus.put("storeDetails", "storeDetails");

		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("store");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
