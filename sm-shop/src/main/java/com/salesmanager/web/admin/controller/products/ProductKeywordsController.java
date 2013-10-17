package com.salesmanager.web.admin.controller.products;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.catalog.Keyword;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.utils.LabelUtils;

@Controller
public class ProductKeywordsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductKeywordsController.class);
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	LabelUtils messages;
	

	@Secured("PRODUCTS")
	@RequestMapping(value={"/admin/products/product/keywords.html"}, method=RequestMethod.GET)
	public String getDigitalProduct(@RequestParam("id") long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

		Product product = productService.getById(productId);
		
		if(product==null || product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			return "redirect:/admin/products/products.html";
		}
		
		model.addAttribute("store", store);

		return ControllerConstants.Tiles.Product.productKeywords;
		
	}
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/products/product/addKeyword.html", method=RequestMethod.POST)
	public String addKeyword(@Valid @RequestParam("productKeyword") Keyword keyword, final BindingResult bindingResult,final Model model, final HttpServletRequest request, Locale locale) throws Exception{
		this.setMenu(model, request);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

		
		Product product = productService.getById(keyword.getProductId());
		
		if(product==null || product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			return "redirect:/admin/products/products.html";
		}
		
		Set<ProductDescription> descriptions = product.getDescriptions();
		ProductDescription productDescription = null;
		for(ProductDescription description : descriptions) {
			
			if(description.getLanguage().getCode().equals(keyword.getLanguageCode())) {
				productDescription = description;
			}
			
		}
		
		if(productDescription==null) {
			FieldError error = new FieldError("keyword","keyword",messages.getMessage("message.product.language", locale));
			bindingResult.addError(error);
			return ControllerConstants.Tiles.Product.productKeywords;
		}
		
		
		String keywords = productDescription.getMetatagKeywords();
		List<String> keyWordsList = null;
		if(!StringUtils.isBlank(keywords)) {
			String[] splits = keywords.split(",");
			keyWordsList = Arrays.asList(splits);
		}
		
		if(keyWordsList==null) {
			keyWordsList = new ArrayList<String>();
		}
		keyWordsList.add(keyword.getKeyword());
		
		StringBuilder kwString = new StringBuilder();
		for(String s : keyWordsList) {
			kwString.append(s).append(",");
		}
		
		productDescription.setMetatagKeywords(kwString.toString());
		Set<ProductDescription> updatedDescriptions = new HashSet<ProductDescription>();
		for(ProductDescription description : descriptions) {
			
			if(!description.getLanguage().getCode().equals(keyword.getLanguageCode())) {
				updatedDescriptions.add(description);
			}
		}
		
		updatedDescriptions.add(productDescription);
		product.setDescriptions(updatedDescriptions);
		
		productService.saveOrUpdate(product);
		
        return ControllerConstants.Tiles.Product.productKeywords;
	}
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/products/product/removeKeyword.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String removeKeyword(@RequestParam("fileId") long fileId, HttpServletRequest request, HttpServletResponse response, Locale locale) {

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		AjaxResponse resp = new AjaxResponse();

		
		try {
			

		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
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
