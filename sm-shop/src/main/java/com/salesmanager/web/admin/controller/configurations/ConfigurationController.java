package com.salesmanager.web.admin.controller.configurations;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.system.service.MerchantConfigurationService;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.web.Menu;


@Controller
public class ConfigurationController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationController.class);

	
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;

	

	@Secured("AUTH")
	@RequestMapping(value="/admin/configuration/accounts.html", method=RequestMethod.GET)
	public String displayAccounts(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);
		
		//Get from MerchantConfiguration KEY - VALUE
		
		return ControllerConstants.Tiles.Configuration.accounts;
		
	}
	
	
	@Secured("AUTH")
	@RequestMapping(value="/admin/configuration/emailConfig.html", method=RequestMethod.GET)
	public String displayEmailSettings(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);
		
		//must be able to store smtp server settings
		/*
		protocol=smtp
		host=smtpout.secureserver.net
		port=3535
		username=support@shopizer.com
		password=buzz

		auth=true
		starttls.enable=false
		 */
		//see sm-core / system.model.email.EmailConfig
		
		//save in MerchantConfiguration using key EMAIL_CONFIG (see similar shippingService.saveShippingConfiguration and shippingService.getShippingConfiguration)
		
		//may use the default configuration from spring if nothing is configured see shopizer-core-modules.xml)
		
		
		
		//Get from MerchantConfiguration KEY - JSON VALUE
		
		return ControllerConstants.Tiles.Configuration.email;
		
	}
	

	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("configuration", "configuration");
		activeMenus.put("accounts-conf", "accounts-conf");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("shipping");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	

}
