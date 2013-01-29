package com.salesmanager.web.admin.controller.merchant;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
import com.salesmanager.web.utils.DateUtil;
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
	
	@Secured("STORE")
	@RequestMapping(value="/admin/store/storeCreate.html", method=RequestMethod.GET)
	public String displayMerchantStoreCreate(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		
		
		setMenu(model,request);

		MerchantStore store = new MerchantStore();

		return displayMerchantStore(store, model, request, response, locale);
	}
		

	@Secured("STORE")
	@RequestMapping(value="/admin/store/store.html", method=RequestMethod.GET)
	public String displayMerchantStore(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		setMenu(model,request);


		//TODO use multiple store
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		
		
		return displayMerchantStore(store, model, request, response, locale);
	}
	
	private String displayMerchantStore(MerchantStore store, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		
		setMenu(model,request);
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		List<Language> languages = languageService.getLanguages();
		
		List<Currency> currencies = currencyService.list();
		
		Date dt = store.getInBusinessSince();
		if(dt!=null) {
			store.setDateBusinessSince(DateUtil.formatDate(dt));
		} else {
			store.setDateBusinessSince(DateUtil.formatDate(new Date()));
		}
		
		//get countries
		List<Country> countries = countryService.getCountries(language);
		
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
		model.addAttribute("store", store);
		
		
		return "admin-store";
		
		
	}
	

	@Secured("STORE")
	@RequestMapping(value="/admin/store/save.html", method=RequestMethod.POST)
	public String saveMerchantStore(@Valid @ModelAttribute("store") MerchantStore store, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		setMenu(model,request);
		
		
		MerchantStore sessionStore = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

		if(store.getId()!=null) {
			if(store.getId().intValue() != sessionStore.getId().intValue()) {
				return "redirect:/admin/store/store.html";
			}
		}
		
		Date date = new Date();
		if(!StringUtils.isBlank(store.getDateBusinessSince())) {
			try {
				date = DateUtil.getDate(store.getDateBusinessSince());
				store.setInBusinessSince(date);
			} catch (Exception e) {
				ObjectError error = new ObjectError("dateBusinessSince",messages.getMessage("message.invalid.date", locale));
				result.addError(error);
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
		
		model.addAttribute("weights",weights);
		model.addAttribute("sizes",sizes);
		
		model.addAttribute("countries", countries);
		model.addAttribute("languages",languages);
		model.addAttribute("currencies",currencies);
		
		
		Country c = store.getCountry();
		List<Zone> zonesList = zoneService.getZones(c, language);
		
		if((zonesList==null || zonesList.size()==0) && StringUtils.isBlank(store.getStorestateprovince())) {
			
			ObjectError error = new ObjectError("zone.code",messages.getMessage("merchant.zone.invalid", locale));
			result.addError(error);
			
		}

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
		if(defaultLanguage!=null) {
			store.setDefaultLanguage(defaultLanguage);
		}
		

		store.setCountry(country);
		store.setZone(zone);
		store.setCurrency(currency);
		store.setDefaultLanguage(defaultLanguage);
		store.setLanguages(supportedLanguagesList);
		store.setLanguages(supportedLanguagesList);

		
		merchantStoreService.saveOrUpdate(store);
		
		sessionStore = merchantStoreService.getMerchantStore(sessionStore.getCode());
		
		
		//update session store
		//request.getSession().setAttribute(Constants.ADMIN_STORE, sessionStore);


		model.addAttribute("success","success");
		model.addAttribute("store", sessionStore);

		
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
