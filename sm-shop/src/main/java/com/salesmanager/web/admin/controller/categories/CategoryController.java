package com.salesmanager.web.admin.controller.categories;

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

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.web.admin.entity.Menu;

@Controller
public class CategoryController {
	
	@Autowired
	LanguageService languageService;
	
	@Autowired
	CategoryService categoryService;
	
	
	@RequestMapping(value="/admin/categories/category.html", method=RequestMethod.GET)
	public String displayCategory(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-categories", "catalogue-categories");

		
		@SuppressWarnings("unchecked")
		Map<String,Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
		
		MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		//get parent categories
		List<Category> categories = categoryService.listByStore(store,language);
		
		String categoryId = request.getParameter("id");
		
		Category category = null;
		
		if(categoryId!=null) {
			
			//get from DB
			
			//remove current category from categories
			
			
		} else {
		
			//create a category
			
			List<Language> languages = languageService.list();//TODO get supported languages from MerchantStore
			
			List<CategoryDescription> descriptions = new ArrayList<CategoryDescription>();
			
			category = new Category();
			
			for(Language l : languages) {
				
				CategoryDescription desc = new CategoryDescription();
				desc.setLanguage(l);
				descriptions.add(desc);
				
			}
			
			
			
			category.setVisible(true);
			category.setDescriptions(descriptions);
		
		}
		


		
		model.addAttribute("category", category);
		model.addAttribute("categories", categories);
		

		
		return "catalogue-categories-category";
	}
	
	
	@RequestMapping(value="/admin/category/save.html", method=RequestMethod.POST)
	public String saveCategory(@Valid @ModelAttribute("category") Category category, BindingResult result, Model model, HttpServletRequest request) throws Exception {
		
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-categories-create", "catalogue-categories-create");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
		
		//check category description
		//get language list
		//iterate, create map id, code
		
		
		//List descriptions = category.getDescriptions();
		//for(int i = 0;i< 2; i++) {
			
			//from the map get the id -> code
			
			
		//}
		
		//ObjectError e = new ObjectError("descriptions[0].name","Hey en must be present");
		//result.addError(e);
		
		//ObjectError e1 = new ObjectError("descriptions0.name","Hey -id- en must be present");
		//result.addError(e1);
		
		if (result.hasErrors()) {
			return "catalogue-categories-category";
		}
		
		//save to DB
		

		model.addAttribute("success","success");
		return "catalogue-categories-category";
	}

}
