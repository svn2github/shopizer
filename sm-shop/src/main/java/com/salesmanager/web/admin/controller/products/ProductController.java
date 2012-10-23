package com.salesmanager.web.admin.controller.products;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.type.ProductType;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.catalog.product.service.type.ProductTypeService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.tax.model.taxclass.TaxClass;
import com.salesmanager.core.business.tax.service.TaxClassService;
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
	ManufacturerService manufacturerService;
	
	@Autowired
	ProductTypeService productTypeService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	TaxClassService taxClassService;
	

	
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
		

		List<Manufacturer> manufacturers = manufacturerService.listByStore(store, language);
		
		List<ProductType> productTypes = productTypeService.list();
		
		List<TaxClass> taxClasses = taxClassService.listByStore(store);
		
		List<Language> languages = store.getLanguages();
		

		
		com.salesmanager.web.entity.catalog.Product product = new com.salesmanager.web.entity.catalog.Product();
		List<ProductDescription> descriptions = new ArrayList<ProductDescription>();

		if(productId!=null && productId!=0) {//edit mode
			

			Product dbProduct = productService.getById(productId);
			
			if(dbProduct==null) {
				return "admin-products";
			}
			
			product.setProduct(dbProduct);
			Set<ProductDescription> productDescriptions = dbProduct.getDescriptions();
			
			for(Language l : languages) {
				
				ProductDescription productDesc = null;
				for(ProductDescription desc : productDescriptions) {
					
					Language lang = desc.getLanguage();
					if(lang.getCode().equals(l.getCode())) {
						productDesc = desc;
					}

				}
				
				if(productDesc==null) {
					productDesc = new ProductDescription();
					productDesc.setLanguage(l);
				}

				descriptions.add(productDesc);
				
			}
			
			
			ProductAvailability productAvailability = null;
			ProductPrice productPrice = null;
			
			Set<ProductAvailability> availabilities = dbProduct.getAvailabilities();
			if(availabilities!=null && availabilities.size()>0) {
				
				for(ProductAvailability availability : availabilities) {
					if(availability.getRegion().equals(ProductAvailability.DEFAULT_AVAILABILITY)) {
						productAvailability = availability;
						Set<ProductPrice> prices = availability.getPrices();
						for(ProductPrice price : prices) {
							if(price.isDefaultPrice()) {
								productPrice = price;
							}
						}
					}
				}
			}
			
			if(productAvailability==null) {
				productAvailability = new ProductAvailability();
			}
			
			if(productPrice==null) {
				productPrice = new ProductPrice();
			}
			
			product.setAvailability(productAvailability);
			product.setPrice(productPrice);
			product.setDescriptions(descriptions);


		} else {


			for(Language l : languages) {
				
				ProductDescription desc = new ProductDescription();
				desc.setLanguage(l);
				descriptions.add(desc);
				
			}
			
			Product prod = new Product();
			
			prod.setAvailable(true);
			
			ProductAvailability productAvailability = new ProductAvailability();
			ProductPrice price = new ProductPrice();
			product.setPrice(price);
			product.setAvailability(productAvailability);
			product.setProduct(prod);
			product.setDescriptions(descriptions);


		}
		


		
		model.addAttribute("product",product);
		model.addAttribute("manufacturers", manufacturers);
		model.addAttribute("productTypes", productTypes);
		model.addAttribute("taxClasses", taxClasses);
		return "admin-products-edit";
	}
	

	@RequestMapping(value="/admin/products/save.html", method=RequestMethod.POST)
	public String saveProduct(@Valid @ModelAttribute("product") com.salesmanager.web.entity.catalog.Product  product, BindingResult result, Model model, HttpServletRequest request) throws Exception {
		
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		//display menu
		setMenu(model,request);
		
		MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
		
		List<Manufacturer> manufacturers = manufacturerService.listByStore(store, language);
		
		List<ProductType> productTypes = productTypeService.list();
		
		List<TaxClass> taxClasses = taxClassService.listByStore(store);
				

		
		model.addAttribute("manufacturers", manufacturers);
		model.addAttribute("productTypes", productTypes);
		model.addAttribute("taxClasses", taxClasses);
		
		if (result.hasErrors()) {
			return "admin-products-edit";
		}
		
		Product newProduct = product.getProduct();
		
		ProductAvailability newProductAvailability = null;
		
		ProductPrice newProductPrice = null;
		
		
		if(product.getProduct().getId()!=null && product.getProduct().getId()>0) {
		
		
			//get actual product
			newProduct = productService.getById(product.getProduct().getId());
			if(newProduct!=null && newProduct.getMerchantStore().getId()!=store.getId()) {
				return "redirect:/admin/products/product-categories.html";
			}
			
			Set<ProductAvailability> availabilities = newProduct.getAvailabilities();
			if(availabilities !=null && availabilities.size()>0) {
				
				for(ProductAvailability availability : availabilities) {
					if(availability.getRegion().equals(ProductAvailability.DEFAULT_AVAILABILITY)) {
						
						newProductAvailability = availability;

						ProductPrice price = newProductAvailability.defaultPrice();
						if(price!=null) {
							newProductPrice = price;
						}
					}
				}
			}
		}
		
		if(newProductPrice==null) {
			newProductPrice = new ProductPrice();
		}
		
		if(newProductAvailability==null) {
			newProductAvailability = new ProductAvailability();
		}
		
		newProductAvailability.setProductQuantity(product.getAvailability().getProductQuantity());
		newProductAvailability.setProductQuantityOrderMin(product.getAvailability().getProductQuantityOrderMin());
		newProductAvailability.setProductQuantityOrderMax(product.getAvailability().getProductQuantityOrderMax());
		
		
		if(product.getImage()!=null) {
			
			productService.saveOrUpdate(newProduct);
			
		} else {
			
			productService.saveOrUpdate(newProduct,(FileInputStream) product.getImage().getInputStream());
			
		}
		
		
		
		
		return "admin-products-edit";
	}
	
	
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
