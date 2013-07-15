package com.salesmanager.web.shop.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationshipType;
import com.salesmanager.core.business.catalog.product.service.relationship.ProductRelationshipService;
import com.salesmanager.core.business.content.model.content.Content;
import com.salesmanager.core.business.content.model.content.ContentDescription;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shop.PageInformation;
import com.salesmanager.web.utils.CatalogUtils;

@Controller
public class LandingController {
	
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private ProductRelationshipService productRelationshipService;
	
	@Autowired
	private CatalogUtils catalogUtils;
	
	@Autowired
	private CategoryService categoryService;
	
	//@Autowired
	//CategoryService categoryService;
	
	@RequestMapping(value={"/shop/home.html","/shop/","/shop"}, method=RequestMethod.GET)
	public String displayLanding(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		
		Content content = contentService.getByCode("LANDING_PAGE", store, language);



		if(content!=null) {
			
			ContentDescription description = content.getDescription();
			
			
			model.addAttribute("page",description);
			
			
			PageInformation pageInformation = new PageInformation();
			pageInformation.setPageTitle(description.getMetatagTitle());
			pageInformation.setPageDescription(description.getMetatagDescription());
			pageInformation.setPageKeywords(description.getMetatagKeywords());
			
			request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);
			
		}

		
		//featured items
		List<ProductRelationship> relationships = productRelationshipService.getByType(store, ProductRelationshipType.FEATURED_ITEM, language);
		List<com.salesmanager.web.entity.catalog.Product> featuredItems = new ArrayList<com.salesmanager.web.entity.catalog.Product>();
		for(ProductRelationship relationship : relationships) {
			
			Product product = relationship.getRelatedProduct();
			com.salesmanager.web.entity.catalog.Product proxyProduct = catalogUtils.buildProxyProduct(product,store,locale);
			

			featuredItems.add(proxyProduct);
		}

		
		model.addAttribute("featuredItems", featuredItems);
		
		/** template **/
		StringBuilder template = new StringBuilder().append("landing.").append(store.getStoreTemplate());

		return template.toString();
	}

}
