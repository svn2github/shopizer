package com.salesmanager.web.admin.controller.customers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.web.admin.entity.Menu;

@Controller
public class CustomerController {
	
	
	@RequestMapping(value="/admin/customers/display.html", method=RequestMethod.GET)
	public String displayCustomer(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	

		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("customer", "customer");
		model.addAttribute("activeMenus",activeMenus);
		//
		
		return "admin-customer";
		
		
		
	}
	
	
	@RequestMapping(value="/admin/customers/save.html", method=RequestMethod.POST)
	public String saveCustomer(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, Model model, HttpServletRequest request) {
	

		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		
		Map menus = (Map)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
		return "admin/products/products";
		
		
		
	}
	
	
		
	@RequestMapping(value="/admin/customers/list.html", method=RequestMethod.GET)
	public String displayCustomers(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		

		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("customer", "customer");
		model.addAttribute("activeMenus",activeMenus);
		//
		
		return "admin-customers";
		
	}
	
	
	@RequestMapping(value="/admin/customers/page.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageCustomers(HttpServletRequest request, HttpServletResponse response) {
		
		
		String startRow = request.getParameter("_startRow");
		String endRow = request.getParameter("_endRow");
		
		//get customers
		
		String r = "{ response:{" +
		"	status:0," +
		"	startRow:5," +
		"	endRow:75," +
		"	totalRows:200" +
		"	data: [...]";
		
		return r;
		
	}
}
