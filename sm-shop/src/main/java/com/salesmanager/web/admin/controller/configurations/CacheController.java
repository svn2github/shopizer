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
import com.salesmanager.core.utils.CacheUtils;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.web.Menu;


@Controller
public class CacheController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheController.class);

	
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;

	

	@Secured("CATALOG")
	@RequestMapping(value="/admin/cache/cacheManagement.html", method=RequestMethod.GET)
	public String displayAccounts(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);
		
		//get caches
		CacheUtils cacheUtils = CacheUtils.getInstance();
		


		return ControllerConstants.Tiles.Configuration.cache;
		
	}
	

	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("cache", "cache");

		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("cache");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	

}
