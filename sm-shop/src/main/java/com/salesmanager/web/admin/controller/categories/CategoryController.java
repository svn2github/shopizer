package com.salesmanager.web.admin.controller.categories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;

@Controller
public class CategoryController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
	
	@Autowired
	LanguageService languageService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	CountryService countryService;

	
	@RequestMapping(value="/admin/categories/editCategory.html", method=RequestMethod.GET)
	public String displayCategoryEdit(@RequestParam("id") long categoryId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayCategory(categoryId,model,request,response);

	}
	
	@RequestMapping(value="/admin/categories/createCategory.html", method=RequestMethod.GET)
	public String displayCategoryCreate(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayCategory(null,model,request,response);

	}
	
	
	
	public String displayCategory(Long categoryId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		


		
		//display menu
		setMenu(model,request);
		
		
		MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		//get parent categories
		List<Category> categories = categoryService.listByStore(store,language);
		
		//String categoryId = request.getParameter("id");


		Category category = new Category();
	

		
		
		if(categoryId!=null && categoryId!=0) {//edit mode
			
			//get from DB
			category = categoryService.getById(store,categoryId);
			
			if(category==null) {
				return "catalogue-categories";
			}

			
			categories.remove(category); //remove current category from categories
		
			
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
		setMenu(model,request);
		
		MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
				
		
		List<Category> subCategories = null;
		
		if(category.getId() != null && category.getId() >0) { //edit entry
			
			//get from DB
			Category currentCategory = categoryService.getById(store,category.getId());
			
			if(currentCategory==null) {
				return "catalogue-categories";
			}
			
			subCategories = categoryService.listByLineage(store, currentCategory.getLineage());
			
		}

			
			Map<String,Language> langs = languageService.getLanguagesMap();
			

			List<CategoryDescription> descriptions = category.getDescriptions();
			if(descriptions!=null) {
				
				for(CategoryDescription description : descriptions) {
					
					String code = description.getLanguage().getCode();
					Language l = langs.get(code);
					description.setLanguage(l);
					description.setCategory(category);
					
					
				}
				
			}
			
			//save to DB
			category.setMerchantSore(store);
		//}
		
		if (result.hasErrors()) {
			return "catalogue-categories-category";
		}
		
		//check parent
		if(category.getParent()!=null) {
			if(category.getParent().getId()==-1) {//this is a root category
				category.setParent(null);
				category.setLineage("/");
				category.setDepth(0);
			}
		}
		
		
		categoryService.saveOrUpdate(category);

			
		//ajust lineage and depth
		if(category.getParent()!=null && category.getParent().getId()!=-1) { 
		
			Category parent = new Category();
			parent.setId(category.getParent().getId());
			parent.setMerchantSore(store);
			
			categoryService.addChild(parent, category);
		
		}
		
		//ajust all sub categories lineages
		if(subCategories!=null && subCategories.size()>0) {
			for(Category subCategory : subCategories) {
				if(category.getId()!=subCategory.getId()) {
					categoryService.addChild(category, subCategory);
				}
			}
			
		}
		
		//get parent categories
		List<Category> categories = categoryService.listByStore(store,language);
		model.addAttribute("categories", categories);
		

		model.addAttribute("success","success");
		return "catalogue-categories-category";
	}
	
	
	//category list
	@RequestMapping(value="/admin/categories/categories.html", method=RequestMethod.GET)
	public String displayCategories(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		
		setMenu(model,request);
		
		//does nothing, ajax subsequent request
		
		return "catalogue-categories";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/admin/categories/paging.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageCategories(HttpServletRequest request, HttpServletResponse response) {
		String categoryName = request.getParameter("name");
		
		
		//String startRow = request.getParameter("_startRow");
		//String endRow = request.getParameter("_endRow");
		
		//May search using name pattern %name%
		

		
		System.out.println(categoryName);
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			
				
		
			MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
			
			List<Category> categories = categoryService.listByStore(store, language);
			
			for(Category category : categories) {
				
				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("categoryId", category.getId());
				
				CategoryDescription description = category.getDescriptions().get(0);
				
				entry.put("name", description.getName());
				entry.put("visible", category.isVisible());
				resp.addDataEntry(entry);
				
				
			}
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			
			//PojoMapper mapper
			//ObjectMapper mapper = new ObjectMapper();
			//mapper.writeValueAsString(value)
			
			
			//get sub category & sub categories for input categoryId
			
			//get products using startRow and endRow
			
			//populate response object which has to be converted to JSON 
			
			//will receive name and sku as filter elements
			
			//List<Map<String,String> //List<Map<key,value>
/*	
			res.append("{ response:{     status:0,     startRow:" + startRow + ",     endRow:" + endRow + ",     totalRows:" + totalRows + ",     data:" +
					"[           ");
			
					for(int i = 0; i < 4; i++) {
						
						
						res.append("{categoryId : 1, name:\"category_" + i + "\",active:\"true\"}");
						if(i < Integer.parseInt(totalRows)-1) {
							res.append(",");
						}
					}
	
					res.append("]   } }");
					
				System.out.println(res.toString());*/
	
				//return res.toString();
		
		} catch (Exception e) {
			LOGGER.error("Error while paging categories", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	@RequestMapping(value="/admin/categories/hierarchy.html", method=RequestMethod.GET)
	public String displayCategoryHierarchy(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		
		setMenu(model,request);
		
		//get the list of categories
		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
		
		List<Category> categories = categoryService.listByStore(store, language);
		
		model.addAttribute("categories", categories);
		
		return "catalogue-categories-hierarchy";
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
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
		
	}

}
