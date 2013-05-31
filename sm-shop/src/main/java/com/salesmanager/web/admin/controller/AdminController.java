package com.salesmanager.web.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.common.model.CriteriaOrderBy;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderCriteria;
import com.salesmanager.web.constants.Constants;

@Controller
public class AdminController {
	
	@RequestMapping(value={"/admin/home.html","/admin/","/admin"}, method=RequestMethod.GET)
	public String displayDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("home", "home");
		
		model.addAttribute("activeMenus",activeMenus);
		
		//get store information
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		model.addAttribute("store", store);
		//get last 10 orders
		OrderCriteria orderCriteria = new OrderCriteria();
		orderCriteria.setMaxCount(10);
		orderCriteria.setOrderBy(CriteriaOrderBy.DESC);
		
		return ControllerConstants.Tiles.adminDashboard;
	}

}
