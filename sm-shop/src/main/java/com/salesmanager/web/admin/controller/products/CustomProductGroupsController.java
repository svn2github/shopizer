package com.salesmanager.web.admin.controller.products;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationshipType;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.relationship.ProductRelationshipService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;


@Controller
public class CustomProductGroupsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomProductGroupsController.class);
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductRelationshipService productRelationshipService;
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/products/groups/list.html", method=RequestMethod.GET)
	public String displayProductGroups(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		setMenu(model,request);
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		ProductRelationship group = new ProductRelationship();
		
		
		model.addAttribute("group", group);

		return ControllerConstants.Tiles.Product.customGroups;
		
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/products/groups/paging.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageCustomGroups(HttpServletRequest request, HttpServletResponse response) {
		
		
		AjaxResponse resp = new AjaxResponse();
		
		try {

			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			

			List<ProductRelationship> relationships = productRelationshipService.getGroups(store);
			
			for(ProductRelationship relationship : relationships) {
				
				if(!"FEATURED_ITEM".equals(relationship.getCode())) {//do not add featured items

					Map entry = new HashMap();
					entry.put("id", relationship.getId());
					entry.put("code", relationship.getCode());
	
					resp.addDataEntry(entry);
				
				}
				
			}
			

			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;


	}
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/products/groups/save.html", method=RequestMethod.POST)
	public String saveCustomProductGroup(@ModelAttribute("group") ProductRelationship group, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		
		//check if group already exist
		
		
		if(StringUtils.isBlank(group.getCode())) {
			FieldError fieldError = new FieldError("group","code",group.getCode(),false,null,null,"message.group.required");
			result.addError(fieldError);
			return ControllerConstants.Tiles.Product.customGroups;
		}
		
		String[] messages = {"message.group.alerady.exists"};
		
		List<ProductRelationship> groups = productRelationshipService.getGroups(store);
		for(ProductRelationship grp : groups) {
			if(grp.getCode().equals(group.getCode())) {
				String[] args = {group.getCode()};
				FieldError fieldError = new FieldError("group","code",group.getCode(),false,messages,args,null);
				result.addError(fieldError);
			}
		}
		
		if(result.hasErrors()) {
			return ControllerConstants.Tiles.Product.customGroups;
		}

		group.setActive(true);
		group.setStore(store);

		
		productRelationshipService.saveOrUpdate(group);
		
		model.addAttribute("success","success");
		
		return ControllerConstants.Tiles.Product.customGroups;
		
	}
	
	public @ResponseBody String removeCustomProductGroup(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	
	public String displayCustomProductGroup(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		setMenu(model,request);
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		List<Category> categories = categoryService.listByStore(store,language);//for categories
		
		model.addAttribute("categories", categories);
		return "admin-catalogue-featured";
		
	}
	
	public @ResponseBody String addItemToCustomGroup(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	
	public @ResponseBody String removeItemFromCustomGroup(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	

		
		
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-products-group", "catalogue-products-group");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
