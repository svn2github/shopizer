package com.salesmanager.web.shop.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class LandingController {
	
	@RequestMapping(value={"/shop/home.html","/shop/"}, method=RequestMethod.GET)
	public String displayLanding(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		

		
		//model.addAttribute("activeMenus",activeMenus);
		
		//get store information
		
		
		//get last 10 orders
		
		return "landing";
	}

}
