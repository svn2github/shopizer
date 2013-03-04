package com.salesmanager.web.admin.controller.shipping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shipping.model.ShippingConfiguration;
import com.salesmanager.core.business.shipping.model.ShippingType;
import com.salesmanager.core.business.shipping.service.ShippingService;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.modules.integration.IntegrationException;
import com.salesmanager.core.modules.integration.shipping.model.CustomShippingQuotesConfiguration;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.utils.LabelUtils;

@Controller
public class CustomShippingMethodsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomShippingMethodsController.class);
	

	public final static String WEIGHT_BASED_SHIPPING_METHOD = "weightBased";
	
	@Autowired
	private ShippingService shippingService;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	LabelUtils messages;
	

	
	@RequestMapping(value="/admin/shipping/weightBased.html", method=RequestMethod.GET)
	public String getWeightBasedShippingMethod(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		this.setMenu(model, request);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		ShippingConfiguration shippingConfiguration =  shippingService.getShippingConfiguration(store);
		
		if(shippingConfiguration==null) {
			shippingConfiguration = new ShippingConfiguration();
			shippingConfiguration.setShippingType(ShippingType.INTERNATIONAL);
		}
		

		//get configured shipping modules
		Map<String,IntegrationConfiguration> configuredModules = shippingService.getShippingModulesConfigured(store);
		IntegrationConfiguration configuration = new IntegrationConfiguration();
		if(configuredModules!=null) {
			for(String key : configuredModules.keySet()) {
				if(key.equals(WEIGHT_BASED_SHIPPING_METHOD)) {
					configuration = configuredModules.get(key);
					break;
				}
			}
		}
		
		configuration.setModuleCode(WEIGHT_BASED_SHIPPING_METHOD);
		
		//get custom information
		CustomShippingQuotesConfiguration customConfiguration = (CustomShippingQuotesConfiguration)shippingService.getCustomShippingConfiguration(WEIGHT_BASED_SHIPPING_METHOD, store);

		//get supported countries
		List<String> includedCountries = shippingService.getSupportedCountries(store);
		List<Country> shippingCountries = new ArrayList<Country>();
		if(shippingConfiguration.getShippingType().equals(ShippingType.INTERNATIONAL.name())){
			Map<String,Country> countries = countryService.getCountriesMap(language);
			for(String key : countries.keySet()) {
				Country country = (Country)countries.get(key);
				if(!includedCountries.contains(key)) {
					shippingCountries.add(country);
				}
			}
		} else {//if national only store country
			if(!includedCountries.contains(store.getCountry().getIsoCode())) {
				shippingCountries.add(store.getCountry());
			}
		}
		
		
		List<String> environments = new ArrayList<String>();
		environments.add(Constants.TEST_ENVIRONMENT);
		environments.add(Constants.PRODUCTION_ENVIRONMENT);
		
		model.addAttribute("configuration", configuration);
		model.addAttribute("customConfiguration", customConfiguration);
		model.addAttribute("shippingCountries", shippingCountries);
		return ControllerConstants.Tiles.Shipping.shippingMethod;
		
		
	}
	
	@RequestMapping(value="/admin/shipping/saveWeightBasedShippingMethod.html", method=RequestMethod.POST)
	public String saveShippingMethod(@ModelAttribute("configuration") CustomShippingQuotesConfiguration configuration, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {


		this.setMenu(model, request);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		String moduleCode = configuration.getModuleCode();
		LOGGER.debug("Saving module code " + moduleCode);
		
		List<String> environments = new ArrayList<String>();
		environments.add(Constants.TEST_ENVIRONMENT);
		environments.add(Constants.PRODUCTION_ENVIRONMENT);

		model.addAttribute("environments", environments);
		model.addAttribute("configuration", configuration);

		try {
			shippingService.saveShippingQuoteModuleConfiguration(configuration, store);
			shippingService.saveCustomShippingConfiguration(WEIGHT_BASED_SHIPPING_METHOD, configuration, store);

			
			
		} catch (Exception e) {
			if(e instanceof IntegrationException) {
				if(((IntegrationException)e).getErrorCode()==IntegrationException.ERROR_VALIDATION_SAVE) {
					
					List<String> errorCodes = ((IntegrationException)e).getErrorFields();
					for(String errorCode : errorCodes) {
						model.addAttribute(errorCode,messages.getMessage("message.fielderror", locale));
					}
					return ControllerConstants.Tiles.Shipping.shippingMethod;
				}
			} else {
				throw new Exception(e);
			}
		}
		

		model.addAttribute("success","success");
		return ControllerConstants.Tiles.Shipping.shippingMethod;
		
		
	}
	

	
	@RequestMapping(value="/admin/shipping/deleteWeightBasedShippingMethod.html", method=RequestMethod.POST)
	public String deleteShippingMethod(BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		this.setMenu(model, request);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		//shippingService.removeShippingQuoteModuleConfiguration(code, store);
		
		return ControllerConstants.Tiles.Shipping.shippingMethods;
		
	}

	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("shipping", "shipping");
		activeMenus.put("shipping-methods", "shipping-methods");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("shipping");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}


}
