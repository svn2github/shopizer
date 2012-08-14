package com.salesmanager.web.admin.controller.categories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.web.admin.entity.web.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;

@Controller
public class CategoryController {
	
	@Autowired
	LanguageService languageService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	CountryService countryService;

	
	
	
	
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
	
	
	@RequestMapping(value="/admin/categories/save.html", method=RequestMethod.POST)
	public String saveCategory(@Valid @ModelAttribute("category") Category category, BindingResult result, Model model, HttpServletRequest request) throws Exception {
		
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-categories", "catalogue-categories");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
		MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
		
		
		if(category.getId() != null && category.getId() >0) { //edit entry
			
			//get the category from the db and copy objects
			
			//check if it belongs to category
			
		} else { //add entry
			
			Map<String,Language> langs = languageService.getLanguagesMap();
			
			Map<String,Country> countriesMap = countryService.getCountriesMap(language);
			
			List<CategoryDescription> descriptions = category.getDescriptions();
			if(descriptions!=null) {
				
				for(CategoryDescription description : descriptions) {
					
					String code = description.getLanguage().getCode();
					Language l = langs.get(code);
					description.setLanguage(l);
					description.setCategory(category);
					
					
				}
				
			}
		}
		
		
		
		//ObjectError e = new ObjectError("descriptions[0].name","Hey en must be present");
		//result.addError(e);
		
		//ObjectError e1 = new ObjectError("descriptions0.name","Hey -id- en must be present");
		//result.addError(e1);
		
		if (result.hasErrors()) {
			return "catalogue-categories-category";
		}
		
		//save to DB
		category.setMerchantSore(store);
		
		categoryService.save(category);
		
		Category parent = new Category();
		parent.setId(category.getParent().getId());
		
		categoryService.addChild(parent, category);
		
		//get parent categories
		List<Category> categories = categoryService.listByStore(store,language);
		model.addAttribute("categories", categories);
		

		model.addAttribute("success","success");
		return "catalogue-categories-category";
	}
	
	
	//category list
	@RequestMapping(value="/admin/categories/list.html", method=RequestMethod.GET)
	public String displayCategories(Model model) {

		//does nothing, ajax subsequent request
		
		return "catalogue-categories";
	}
	
	@RequestMapping(value="/admin/categories/paging.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageCategories(HttpServletRequest request, HttpServletResponse response) {
		String categoryId = request.getParameter("categoryId");
		String searchTerm = request.getParameter("searchTerm");
		
		String startRow = request.getParameter("_startRow");
		String endRow = request.getParameter("_endRow");
		
		String totalRows = "10";
		

		
		if(searchTerm!=null) {
			totalRows="2";
		}
		
		
		try {
			
			AjaxResponse resp = new AjaxResponse();
				
		
			MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
			
			List<Category> categories = categoryService.listByStore(store);
			
			for(Category category : categories) {
				
				Map entry = new HashMap();
				entry.put("categoryId", category.getId());
				
				CategoryDescription description = category.getDescriptions().get(0);
				
				entry.put("name", description.getName());
				entry.put("visible", category.isVisible());
				resp.addDataEntry(entry);
				
				
			}
			
			resp.setStatus(0);
			
			//PojoMapper mapper
			//ObjectMapper mapper = new ObjectMapper();
			//mapper.writeValueAsString(value)
			
			
			//get sub category & sub categories for input categoryId
			
			//get products using startRow and endRow
			
			//populate response object which has to be converted to JSON 
			
			//will receive name and sku as filter elements
			
			//List<Map<String,String> //List<Map<key,value>
	
			/*StringBuilder res = new StringBuilder().append("{ response:{     status:0,     startRow:" + startRow + ",     endRow:" + endRow + ",     totalRows:" + totalRows + ",     data:" +
					"[           ");
			
					for(int i = Integer.parseInt(startRow); i < Integer.parseInt(totalRows); i++) {
						
						
						res.append("{name:\"category_" + i + "\",active:\"true\"}");
						if(i < Integer.parseInt(totalRows)-1) {
							res.append(",");
						}
					}
	
					res.append("]   } }");
					
	
				return res.toString();*/
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}

}
