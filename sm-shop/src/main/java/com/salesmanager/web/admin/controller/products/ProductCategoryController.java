package com.salesmanager.web.admin.controller.products;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;

@Controller
public class ProductCategoryController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductCategoryController.class);
	
	
	
	@RequestMapping(value="/admin/products/setCategories.html", method=RequestMethod.GET)
	public String displayProductToCategories(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);


		//TODO get the list of categories
		
		
		
		return "catalogue-products-categories";
		
		
		
	}
	
	
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(value="/admin/products-categories/paging.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageProductsCategories(HttpServletRequest request, HttpServletResponse response) {
		String categoryName = request.getParameter("name");//category name

		//TODO get the associations
		
		
		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Language language = (Language)request.getAttribute("LANGUAGE");
				
		
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			
			List<Category> categories = null;
					
			if(!StringUtils.isBlank(categoryName)) {
				
				
				//categories = categoryService.getByName(store, categoryName, language);
				
			} else {
				
				//categories = categoryService.listByStore(store, language);
				
			}
					
					
			
			for(Category category : categories) {
				
				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("categoryId", category.getId());
				
				CategoryDescription description = category.getDescriptions().get(0);
				
				entry.put("name", description.getName());
				entry.put("code", category.getCode());
				entry.put("visible", category.isVisible());
				resp.addDataEntry(entry);
				
				
			}
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			

		
		} catch (Exception e) {
			LOGGER.error("Error while paging categories", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-products", "catalogue-products");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
