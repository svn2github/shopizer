package com.salesmanager.web.shop.controller.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionDescription;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValue;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationshipType;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.relationship.ProductRelationshipService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.catalog.Attribute;
import com.salesmanager.web.entity.catalog.AttributeValue;
import com.salesmanager.web.entity.shop.PageInformation;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.utils.CatalogUtils;
import com.salesmanager.web.utils.PageBuilderUtils;

@Controller
public class ShopProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CatalogUtils catalogUtils;
	
	@Autowired
	private ProductRelationshipService productRelationshipService;
	


	@RequestMapping("/shop/product/{friendlyUrl}.html")
	public String displayProduct(@PathVariable final String friendlyUrl, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		Language language = (Language)request.getAttribute("LANGUAGE");
		
		Product product = productService.getBySeUrl(store, friendlyUrl, locale);
		
		if(product==null) {
			return PageBuilderUtils.build404(store);
		}
		
		com.salesmanager.web.entity.catalog.Product productProxy = catalogUtils.buildProxyProduct(product, store, locale);
		

		//meta information
		PageInformation pageInformation = new PageInformation();
		pageInformation.setPageDescription(productProxy.getMetaDescription());
		pageInformation.setPageKeywords(productProxy.getKeyWords());
		pageInformation.setPageTitle(productProxy.getTitle());
		pageInformation.setPageUrl(productProxy.getFriendlyUrl());
		
		request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);
		

		//related items
		List<ProductRelationship> relatedItems = productRelationshipService.getByType(store, product, ProductRelationshipType.RELATED_ITEM);
		if(relatedItems!=null && relatedItems.size()>0) {
			List<com.salesmanager.web.entity.catalog.Product> items = new ArrayList<com.salesmanager.web.entity.catalog.Product>();
			for(ProductRelationship relationship : relatedItems) {
				Product relatedProduct = relationship.getRelatedProduct();
				com.salesmanager.web.entity.catalog.Product proxyProduct = catalogUtils.buildProxyProduct(relatedProduct, store, locale);
				items.add(proxyProduct);
			}
			model.addAttribute("relatedProducts",items);		
		}
		
		Set<ProductAttribute> attributes = product.getAttributes();
		
		//split read only and options
		Map<Long,Attribute> readOnlyAttributes = null;
		Map<Long,Attribute> selectableOptions = null;
		
		if(!CollectionUtils.isEmpty(attributes)) {
			for(ProductAttribute attribute : attributes) {
				Attribute attr = null;
				if(attribute.getProductOption().isReadOnly()) {
					if(readOnlyAttributes==null) {
						readOnlyAttributes = new HashMap<Long,Attribute>();
					}
					attr = readOnlyAttributes.get(attribute.getProductOption().getId());
					if(attr==null) {
						attr = createAttribute(attribute, language);
						readOnlyAttributes.put(attribute.getProductOption().getId(), attr);
					}
					readOnlyAttributes.put(attribute.getProductOption().getId(), attr);
				} else {
					if(selectableOptions==null) {
						selectableOptions = new HashMap<Long,Attribute>();
					}
					attr = selectableOptions.get(attribute.getProductOption().getId());
					if(attr==null) {
						attr = createAttribute(attribute, language);
						readOnlyAttributes.put(attribute.getProductOption().getId(), attr);
					}
					readOnlyAttributes.put(attribute.getProductOption().getId(), attr);
				}
				ProductOptionValue optionValue = attribute.getProductOptionValue();
				AttributeValue attrValue = new AttributeValue();
				attrValue.setDefaultAttribute(attribute.getAttributeDefault());
				attrValue.setId(optionValue.getId());
				attrValue.setLanguage(language.getCode());
				attrValue.setName(optionValue.getDescriptionsList().get(0).getDescription());
				List<AttributeValue> attrs = attr.getValues();
				if(attrs==null) {
					attrs = new ArrayList<AttributeValue>();
					attr.setValues(attrs);
				}
				attrs.add(attrValue);
				
			}
		}

		
		//TODO reviews
		
		model.addAttribute("attributes", readOnlyAttributes);
		model.addAttribute("options", selectableOptions);
			
		model.addAttribute("product", product);

		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Product.product).append(".").append(store.getStoreTemplate());

		return template.toString();
	}
	
	private Attribute createAttribute(ProductAttribute productAttribute, Language language) {
		
		Attribute attribute = new Attribute();
		attribute.setId(productAttribute.getId());
		attribute.setType(productAttribute.getProductOption().getProductOptionType());
		List<ProductOptionDescription> descriptions = productAttribute.getProductOption().getDescriptionsList();
		ProductOptionDescription description = descriptions.get(0);
		if(descriptions.size()>1) {
			
			for(ProductOptionDescription optionDescription : descriptions) {
				if(optionDescription.getLanguage().getId().intValue()==language.getId().intValue()) {
					description = optionDescription;
					break;
				}
			}

		}
		
		attribute.setLanguage(language.getCode());
		attribute.setName(description.getName());
		attribute.setCode(productAttribute.getProductOption().getCode());
		
		return attribute;
		
	}
	


}
