package com.salesmanager.web.admin.controller.shipping;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.system.model.MerchantConfiguration;
import com.salesmanager.core.business.system.service.MerchantConfigurationService;
import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.web.admin.entity.shipping.ShippingConfiguration;

@Controller
public class ShippingMethodsController {
	
	@Autowired
	MerchantConfigurationService merchantConfigurationService;
	
	/**
	 * Configures the shipping mode, shows shipping methods
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/shipping/shippingMethods.html", method=RequestMethod.GET)
	public String getShippingMethods(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		ShippingConfiguration shippingConfiguration = new ShippingConfiguration();
		
		
		MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
		
		
		//get key SHP_ZONES_SHIPPING (NATIONAL/INTERNATIONAL) from MerchantConfiguration for the current merchant
		MerchantConfiguration modeConfiguration =  merchantConfigurationService.getMerchantConfiguration(ShippingConstants.SHIPPING_MODE, store);
		
		if(modeConfiguration!=null) {
			shippingConfiguration.setMode(modeConfiguration.getValue());
		}

		
		
		//get integrations modules from ModuleConfiguration module SHIPPING // TO BE DONE
		
		//get shipping modules configured from MerchantConfiguration //TO BE DONE
		
		model.addAttribute("configuration", shippingConfiguration);
		
		return "shipping-methods";
		
		
	}
	
	@RequestMapping(value="/admin/shipping/saveShippingMode.html", method=RequestMethod.POST)
	public String saveShippingMode(@ModelAttribute("configuration") ShippingConfiguration configuration, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		
		MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
		
		MerchantConfiguration modeConfiguration =  merchantConfigurationService.getMerchantConfiguration(ShippingConstants.SHIPPING_MODE, store);
		
		if(modeConfiguration!=null) {
			//overwrite with submited value
		}
		
		return "shipping-methods";
		
	}
	

}
