package com.salesmanager.web.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController {
	
	@RequestMapping(value={"/admin/home.html","/admin/","/admin"}, method=RequestMethod.GET)
	public String displayDashboard(Model model) throws Exception {
		
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("home", "home");
		
		model.addAttribute("activeMenus",activeMenus);
		
		//get store information
		
		
		//get last 10 orders
		
		return "admin-dashboard";
	}

}
