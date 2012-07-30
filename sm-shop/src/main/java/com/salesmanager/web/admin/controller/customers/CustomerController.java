package com.salesmanager.web.admin.controller.customers;

import java.util.HashMap;
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

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;

@Controller
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	
	/**
	 * Customer details
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/customers/customer.html", method=RequestMethod.GET)
	public String displayCustomer(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	

		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("customer", "customer");
		model.addAttribute("activeMenus",activeMenus);
		//
		
		//if request.attribute contains id then get this customer from customerService
		
		//get list of countries (see merchant controller)
		
		//get list of zones
		
		return "admin-customer";
		
		
		
	}
	
	
	@RequestMapping(value="/admin/customers/save.html", method=RequestMethod.POST)
	public String saveCustomer(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, Model model, HttpServletRequest request) {
	

		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("customer", "customer");
		

		model.addAttribute("activeMenus",activeMenus);
		//
		
		return "admin/products/products";
		
		
		
	}

	
	/**
	 * List of customers
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/customers/list.html", method=RequestMethod.GET)
	public String displayCustomers(Model model) throws Exception {
		
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("customer", "customer");
		

		model.addAttribute("activeMenus",activeMenus);
		//
		
		return "admin-customers";
		
		
		
	}
	
	
	
	@RequestMapping(value="/admin/customers/page.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageCustomers(HttpServletRequest request, HttpServletResponse response) {

		String searchTerm = request.getParameter("searchTerm");// will be the name of the customer
		
		String startRow = request.getParameter("_startRow");
		String endRow = request.getParameter("_endRow");
		
		String totalRows = "10";
		

		
		if(searchTerm!=null) {
			totalRows="2";
		}
		//get sub category & sub categories for input categoryId
		
		//get products using startRow and endRow
		
		//populate response object which has to be converted to JSON 
		
		//will receive name and sku as filter elements
		
		//JSONListResponse r = new JSONListResponse()
		//r.setStatus(0);
		//JSONObject obj = new JSONObject();
		//obj.put("response",r)
		
		
		StringBuilder res = new StringBuilder().append("{ response:{     status:0,     startRow:0,     endRow:9,     totalRows:10,     data:" +
				"[           ");
		
				
		
		for(int i = 0; i < 10; i++) {
					
					
					res.append("{id:" + i + ",name:\"customer_" + i + "\",country:\"CA\",active:\"true\"}");
					if(i < Integer.parseInt(totalRows)-1) {
						res.append(",");
					}
				}

				res.append("]   } }");
				

			return res.toString();
	}
	
		

}
