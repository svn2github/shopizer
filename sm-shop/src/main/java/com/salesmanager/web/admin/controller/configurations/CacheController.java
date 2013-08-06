package com.salesmanager.web.admin.controller.configurations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.system.service.MerchantConfigurationService;
import com.salesmanager.core.utils.CacheUtils;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;


@Controller
public class CacheController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheController.class);



	@Secured("AUTH")
	@RequestMapping(value="/admin/cache/cacheManagement.html", method=RequestMethod.GET)
	public String displayAccounts(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		//get cache keys
		CacheUtils cacheUtils = CacheUtils.getInstance();
		List<String> cacheKeysList = cacheUtils.getCacheKeys(store);

		model.addAttribute("keys", cacheKeysList);

		return ControllerConstants.Tiles.Configuration.cache;
		
	}
	
	
	@Secured("AUTH")
	@RequestMapping(value="/admin/cache/clear.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String clearCache(HttpServletRequest request, HttpServletResponse response) {
		String cacheKey = request.getParameter("cacheKey");

		AjaxResponse resp = new AjaxResponse();

		try {

			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			
			CacheUtils cacheUtils = CacheUtils.getInstance();
			StringBuilder key = new StringBuilder();
			key.append(store.getId()).append("_").append(cacheKey);
			
			cacheUtils.removeFromCache(key.toString());

			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while updateing groups", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
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
