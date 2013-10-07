package com.salesmanager.web.admin.controller.products;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.web.Menu;

@Controller
public class ManufacturerController {
	
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/catalogue/manufacturer/list.html", method=RequestMethod.GET)
	public String getManufacturers(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		this.setMenu(model, request);
		//get the list of manufacturers
		
		return ControllerConstants.Tiles.Product.manufacturerList;
	}
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/catalogue/manufacturer/create.html", method=RequestMethod.GET)
	public String createManufacturer(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setMenu(model, request);

		
		return ControllerConstants.Tiles.Product.manufacturerDetails;
	}
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/catalogue/manufacturer/edit.html", method=RequestMethod.GET)
	public String editManufacturer(@RequestParam("id") long manufacturerId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setMenu(model, request);

		
		return ControllerConstants.Tiles.Product.manufacturerDetails;
	}
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/catalogue/manufacturer/save.html", method=RequestMethod.GET)
	public String saveManufacturer(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setMenu(model, request);
		//save or edit a manufacturer

		
		return ControllerConstants.Tiles.Product.manufacturerDetails;
	}
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/catalogue/manufacturer/paging.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageManufacturers(HttpServletRequest request, HttpServletResponse response) {

		
		AjaxResponse resp = new AjaxResponse();
		
		resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
		String returnString = resp.toJSONString();
		return returnString;
		
	}
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/catalogue/manufacturer/remove.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String deleteManufacturer(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("id");
	
		//get the manufacturer
		
		//IF already attached to products it can't be deleted
		
		AjaxResponse resp = new AjaxResponse();
		
		resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
		String returnString = resp.toJSONString();
		return returnString;
		
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("manufacturer-list", "manufacturer-list");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
}
