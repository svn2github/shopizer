package com.salesmanager.web.admin.controller.products;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceType;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.price.ProductPriceService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.core.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.utils.LabelUtils;

@Controller
public class ProductPriceController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductPriceController.class);
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductPriceService productPriceService;
	
	@Autowired
	private ProductPriceUtils priceUtil;
	
	@Autowired
	LabelUtils messages;
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/products/prices.html", method=RequestMethod.GET)
	public String displayProductPrices(@RequestParam("id") long productId,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);

		Product product = productService.getById(productId);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		if(product==null || product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			return "forward:/admin/products/products.html";
		}

		model.addAttribute("product",product);
		

		return ControllerConstants.Tiles.Product.productPrices;
		
	}
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/products/prices/paging.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pagePrices(HttpServletRequest request, HttpServletResponse response) {

		String sProductId = request.getParameter("productId");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		
		AjaxResponse resp = new AjaxResponse();
		
		Long productId;
		Product product = null;
		
		try {
			productId = Long.parseLong(sProductId);
		} catch (Exception e) {
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("Product id is not valid");
			String returnString = resp.toJSONString();
			return returnString;
		}

		
		try {

			product = productService.getById(productId);

			
			if(product==null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Product id is not valid");
				String returnString = resp.toJSONString();
				return returnString;
			}
			
			if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Product id is not valid");
				String returnString = resp.toJSONString();
				return returnString;
			}
			
			ProductAvailability defaultAvailability = null;
			
			Set<ProductAvailability> availabilities = product.getAvailabilities();

			//get default availability
			for(ProductAvailability availability : availabilities) {
				if(availability.getRegion().equals(com.salesmanager.core.constants.Constants.ALL_REGIONS)) {
					defaultAvailability = availability;
					break;
				}
			}
			
			if(defaultAvailability==null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Product id is not valid");
				String returnString = resp.toJSONString();
				return returnString;
			}
			
			Set<ProductPrice> prices = defaultAvailability.getPrices();
			
			
			for(ProductPrice price : prices) {
				Map entry = new HashMap();
				entry.put("priceId", price.getId());
				
				
				String priceName = "";
				Set<ProductPriceDescription> descriptions = price.getDescriptions();
				if(descriptions!=null) {
					for(ProductPriceDescription description : descriptions) {
						if(description.getLanguage().getCode().equals(language.getCode())) {
							priceName = description.getName(); 
						}
					}
				}
				

				entry.put("name", priceName);
				entry.put("price", priceUtil.getAdminFormatedAmountWithCurrency(store,price.getProductPriceAmount()));
				
				String discount = "";
				if(priceUtil.hasDiscount(price)) {
					discount = priceUtil.getAdminFormatedAmountWithCurrency(store,price.getProductPriceAmount());
				}
				entry.put("special", discount);
				
				resp.addDataEntry(entry);
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
	@RequestMapping(value="/admin/products/price/edit.html", method=RequestMethod.GET)
	public String editProductPrice(@RequestParam("productId") long productId, @RequestParam("id") long productPriceId,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		return displayProductPrice(productId, productPriceId, model, request, response);
		
	}
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/products/price/create.html", method=RequestMethod.GET)
	public String createProductPrice(@RequestParam("productId") long productId,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		return displayProductPrice(productId, null, model, request, response);


		
	}
	
	private String displayProductPrice(Long productId, Long productPriceId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Product product = productService.getById(productId);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		if(product.getId().intValue()!=store.getId().intValue()) {
			return "redirect:/admin/products/products.html";
		}
		
		
		ProductPrice productPrice = null;
		ProductAvailability productAvailability = null;
		
		if(productPriceId!=null) {
		
			Set<ProductAvailability> availabilities = product.getAvailabilities();
	
			//get default availability
			for(ProductAvailability availability : availabilities) {
				productAvailability = availability;
				if(availability.getRegion().equals(com.salesmanager.core.constants.Constants.ALL_REGIONS)) {
					Set<ProductPrice> prices = availability.getPrices();
					for(ProductPrice price : prices) {
						if(price.getId().longValue()==productPriceId.longValue()) {
							productPrice = price;
							price.setProductPrice(priceUtil.getAdminFormatedAmount(store, price.getProductPriceAmount()));
							if(price.getProductPriceSpecialAmount()!=null) {
								price.setProductSpecialPrice(priceUtil.getAdminFormatedAmount(store, price.getProductPriceSpecialAmount()));
							}
							break;
						}
					}
				}
			}
		
		}	
		
		if(productPrice==null) {
			productPrice = new ProductPrice();
			productPrice.setProductPriceType(ProductPriceType.ONE_TIME.name());
		}
		
		//descriptions
		List<Language> languages = store.getLanguages();
		
		Set<ProductPriceDescription> productPriceDescriptions = productPrice.getDescriptions();
		
		for(Language l : languages) {
			ProductPriceDescription productPriceDesc = null;
			for(ProductPriceDescription desc : productPriceDescriptions) {
				Language lang = desc.getLanguage();
				if(lang.getCode().equals(l.getCode())) {
					productPriceDesc = desc;
				}
			}
			
			if(productPriceDesc==null) {
				productPriceDesc = new ProductPriceDescription();
				productPriceDesc.setLanguage(l);
				productPriceDescriptions.add(productPriceDesc);
			}	
		}


		model.addAttribute("product",product);
		model.addAttribute("price",productPrice);
		model.addAttribute("availability",productAvailability);
		
		return ControllerConstants.Tiles.Product.productPrice;
	}
	
	
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/products/price/save.html", method=RequestMethod.POST)
	public String saveProduct(@ModelAttribute("availability") ProductAvailability availability, @Valid @ModelAttribute("price") ProductPrice price, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		
		
		//validate price
		BigDecimal submitedPrice = null;
		try {
			submitedPrice = priceUtil.getAmount(price.getProductPrice());
		} catch (Exception e) {
			ObjectError error = new ObjectError("productPrice",messages.getMessage("NotEmpty.product.productPrice", locale));
			result.addError(error);
		}
		
		//validate discount price
		BigDecimal submitedDiscountPrice = null;
		
		if(!StringUtils.isBlank(price.getProductSpecialPrice())) {
			try {
				submitedDiscountPrice = priceUtil.getAmount(price.getProductSpecialPrice());
			} catch (Exception e) {
				ObjectError error = new ObjectError("productSpecialPrice",messages.getMessage("NotEmpty.product.productPrice", locale));
				result.addError(error);
			}
		}
		
		if (result.hasErrors()) {
			return ControllerConstants.Tiles.Product.productPrice;
		}
		
		//if(price.getId()!=null && price.getId().longValue()>0) {
			
			
			
			
		//}
		
		price.setProductPriceAmount(submitedPrice);
		if(!StringUtils.isBlank(price.getProductSpecialPrice())) {
			price.setProductPriceSpecialAmount(submitedDiscountPrice);
		}
		
		Set<ProductPriceDescription> descriptions = new HashSet<ProductPriceDescription>();
		if(price.getDescriptions()!=null && price.getDescriptions().size()>0) {
			
			for(ProductPriceDescription description : price.getDescriptions()) {
				description.setProductPrice(price);
				descriptions.add(description);
				
			}
		}
		
		price.setDescriptions(descriptions);
		price.setProductPriceAvailability(availability);
		
		productPriceService.saveOrUpdate(price);
		
		return ControllerConstants.Tiles.Product.productPrice;
		
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
