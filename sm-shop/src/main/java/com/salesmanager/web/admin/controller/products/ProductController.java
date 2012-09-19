package com.salesmanager.web.admin.controller.products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.utils.LabelUtils;

@Controller
public class ProductController {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	LanguageService languageService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	LabelUtils messages;

	
	@RequestMapping(value="/admin/products/editProduct.html", method=RequestMethod.GET)
	public String displayProductEdit(@RequestParam("id") long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayProduct(productId,model,request,response);

	}
	
	@RequestMapping(value="/admin/products/createProduct.html", method=RequestMethod.GET)
	public String displayProductCreate(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayProduct(null,model,request,response);

	}
	
	
	
	private String displayProduct(Long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		


		
		//display menu
		setMenu(model,request);
		
		
		MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		//get parent categories
		List<Category> categories = categoryService.listByStore(store,language);
		
		//TODO need a local Product with an array list of descriptions etc...
		Product product = new Product();
		
		if(productId!=null && productId!=0) {//edit mode
			
			//get from DB
			//TODO getById
			//product = categoryService.getById(store,productId);
			
			if(product==null) {
				return "admin-products";
			}


		
			
		} else {
			
			List<Language> languages = languageService.list();//TODO get supported languages from MerchantStore
			

			List<ProductDescription> descriptions = new ArrayList<ProductDescription>();

			for(Language l : languages) {
				
				ProductDescription desc = new ProductDescription();
				desc.setLanguage(l);
				descriptions.add(desc);
				
			}
			
			
			
			product.setAvailable(true);
			//product.setDescriptions(descriptions);
		
			
			
		}


/*		
	

		
		
		if(productId!=null && productId!=0) {//edit mode
			
			//get from DB
			category = categoryService.getById(store,categoryId);
			
			if(category==null) {
				return "catalogue-categories";
			}

			
			categories.remove(category); //remove current category from categories
		
			
		} else {
		
			//create a category
			
			List<Language> languages = languageService.list();//TODO get supported languages from MerchantStore
			
			//List<Language> languages = store.getLanguages();
			
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
		model.addAttribute("categories", categories);*/
		

		
		return "admin-products-edit";
	}
	
/*	
	@RequestMapping(value="/admin/categories/save.html", method=RequestMethod.POST)
	public String saveCategory(@Valid @ModelAttribute("category") Category category, BindingResult result, Model model, HttpServletRequest request) throws Exception {
		
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		//display menu
		setMenu(model,request);
		
		MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
				

		if(category.getId() != null && category.getId() >0) { //edit entry
			
			//get from DB
			Category currentCategory = categoryService.getById(store,category.getId());
			
			if(currentCategory==null) {
				return "catalogue-categories";
			}
			

			
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
		
		
		//get parent categories
		List<Category> categories = categoryService.listByStore(store,language);
		model.addAttribute("categories", categories);
		

		model.addAttribute("success","success");
		return "catalogue-categories-category";
	}
	*/
	
/*	//category list
	@RequestMapping(value="/admin/categories/categories.html", method=RequestMethod.GET)
	public String displayCategories(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		
		setMenu(model,request);
		
		//does nothing, ajax subsequent request
		
		return "catalogue-categories";
	}*/
	
	/*
	
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(value="/admin/products/paging.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageProducts(HttpServletRequest request, HttpServletResponse response) {
		String categoryName = request.getParameter("name");


		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Language language = (Language)request.getAttribute("LANGUAGE");
				
		
			MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
			
			List<Category> categories = null;
					
			if(!StringUtils.isBlank(categoryName)) {
				
				
				categories = categoryService.getByName(store, categoryName, language);
				
			} else {
				
				categories = categoryService.listByStore(store, language);
				
			}
					
					
			
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
			

		
		} catch (Exception e) {
			LOGGER.error("Error while paging categories", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	*/
	
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
