package com.salesmanager.web.shop.controller.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValue;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationshipType;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.relationship.ProductRelationshipService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.CacheUtils;
import com.salesmanager.web.constants.Constants;
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
	
	@Autowired
	private CategoryService categoryService;
	
	

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
		
		//temporary working objects
		//Map<Long,>
		
		//Attributes
/*		if (attributes != null && attributes.size() > 0) {
			// extract read only
			long lastOptionId = -1;
			long lastSpecificationOptionId = -1;
			for (ProductAttribute attribute : attributes) {
				ProductOption productOption = attribute.getProductOption();
				ProductOptionValue productOptionValue = attribute.getProductOptionValue();
				if (productOption != null) {
					if (attribute.getAttributeDisplayOnly()) {
						
						

						if (lastSpecificationOptionId == -1) {
							lastSpecificationOptionId = po.getProductOptionId();
							pod = new ProductOptionDescriptor();
							pod.setOptionType(po.getProductOptionType());
							pod.setName(po.getName());
							specifications.add(pod);
						} else {
							if (pa.getOptionId() != lastOptionId) {
								lastSpecificationOptionId = po
										.getProductOptionId();
								pod = new ProductOptionDescriptor();
								pod.setOptionType(po.getProductOptionType());
								pod.setName(po.getName());
								specifications.add(pod);
							}
						}
					} else {// option
						if (lastOptionId == -1) {
							lastOptionId = po.getProductOptionId();
							pod = new ProductOptionDescriptor();
							pod.setOptionType(po.getProductOptionType());
							pod.setName(po.getName());
							options.add(pod);
							if (pa.isAttributeDefault()) {
								defaultOptions.add(pa);
							}
						} else {
							if (pa.getOptionId() != lastOptionId) {
								lastOptionId = po.getProductOptionId();
								pod = new ProductOptionDescriptor();
								pod.setOptionType(po.getProductOptionType());
								pod.setName(po.getName());
								options.add(pod);
								if (pa.isAttributeDefault()) {
									defaultOptions.add(pa);
								}
							}
						}
					}
					pod.addValue(pa);
					pod.setOptionId(pa.getOptionId());
					if (pa.isAttributeDefault()) {
						pod.setDefaultOption(pa.getProductAttributeId());
					}
				}
			}
		}
		model.addObject("specifications", specifications);
		model.addObject("options", options);*/
		
		
		
		
		//TODO reviews
		
		
			
		model.addAttribute("product", product);

		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Product.product).append(".").append(store.getStoreTemplate());

		return template.toString();
	}
	


}
