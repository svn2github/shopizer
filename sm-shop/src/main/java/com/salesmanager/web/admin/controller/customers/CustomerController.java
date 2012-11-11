package com.salesmanager.web.admin.controller.customers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.web.admin.entity.web.Menu;

@Controller
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	ZoneService zoneService;
	
	
	/**
	 * Customer details
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/customers/customer.html", method=RequestMethod.GET)
	public String displayCustomer(Long id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
			
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("customer", "customer");
		activeMenus.put("customer-create", "customer-create");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("customer");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);		//
		
		Customer customer = null;
		
		//if request.attribute contains id then get this customer from customerService
		if(id!=null && id!=0) {//edit mode
			
			//get from DB
			customer = customerService.getById(id);
					
			
		} else {
			 customer = new Customer();
		}
		//get list of countries (see merchant controller)
		Language language = (Language)request.getAttribute("LANGUAGE");				
		//get countries
		List<Country> countries = countryService.getCountries(language);
		
		//get list of zones
		List<Zone> zones = zoneService.list();
		
		model.addAttribute("zones", zones);
		model.addAttribute("countries", countries);
		model.addAttribute("customer", customer);
		return "admin-customer";
		
		
		
	}
	
	
	@RequestMapping(value="/admin/customers/save.html", method=RequestMethod.POST)
	public String saveCustomer(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, Model model, HttpServletRequest request) throws Exception{
	
		customerService.save(customer);
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("customer", "customer");
		activeMenus.put("customer-create", "customer-create");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
				
		Menu currentMenu = (Menu)menus.get("customer");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		model.addAttribute("success","success");
		
		return "admin-customer";
				
		
		
		
	}

	
	/**
	 * List of customers
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/customers/list.html", method=RequestMethod.GET)
	public String displayCustomers(Model model,HttpServletRequest request) throws Exception {
		
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("customer", "customer");
		activeMenus.put("customer-list", "customer-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("customer");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
		return "admin-customers";
		
		
		
	}
	
	
	
	@RequestMapping(value="/admin/customers/page.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody
	String pageCustomers(HttpServletRequest request,HttpServletResponse response) {

		String searchTerm = request.getParameter("name");// will be the name of the customer

		List<Customer> customers = new ArrayList<Customer>();
		String totalRows = "0";
		String endRow = "0";

		StringBuilder res = new StringBuilder().append("{ response:{     status:0,     startRow:0,     endRow:0,     totalRows:0 ,     data: [           ");		

		if (!StringUtils.isBlank(searchTerm)) {

			customers = customerService.getByName(searchTerm);
			
		} else {
			customers = customerService.list();
		}

		if (customers != null && customers.size() > 0) {
			totalRows = new Integer(customers.size()).toString();
			endRow = new Integer(customers.size() - 1).toString();
			res = new StringBuilder().append("{ response:{     status:0,     startRow:0,     endRow:");
			res.append(endRow);
			res.append(" , totalRows:");
			res.append(totalRows);
			res.append(",     data: [           ");
			int i = 0;
			
			for (Customer customer : customers) {
				
				res.append("{id:" + ++i + ",name:\'" + customer.getFirstname()
						+ " " + customer.getLastname() + "\',country:\' "
						+ customer.getCountry().getIsoCode()
						+ " \' ,active:\'true\'}");
				
				if (i < Integer.parseInt(totalRows)) {
					res.append(",");
				}
			}

		}

		res.append("]   } }");

		return res.toString();
	}
	
		

}
