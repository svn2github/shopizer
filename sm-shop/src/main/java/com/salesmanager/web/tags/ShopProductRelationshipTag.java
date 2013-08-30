package com.salesmanager.web.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.service.relationship.ProductRelationshipService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.CacheUtils;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.utils.CatalogUtils;

public class ShopProductRelationshipTag extends RequestContextAwareTag  {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ShopProductRelationshipTag.class);

	@Autowired
	private ProductRelationshipService productRelationshipService;
	
	@Autowired
	private CatalogUtils catalogUtils;
	
	
	private String groupName;



	public String getGroupName() {
		return groupName;
	}


	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	@SuppressWarnings("unchecked")
	@Override
	protected int doStartTagInternal() throws Exception {
		if (productRelationshipService == null || catalogUtils==null) {
			LOGGER.debug("Autowiring ProductRelationshipService");
            WebApplicationContext wac = getRequestContext().getWebApplicationContext();
            AutowireCapableBeanFactory factory = wac.getAutowireCapableBeanFactory();
            factory.autowireBean(this);
        }
		
		HttpServletRequest request = (HttpServletRequest) pageContext
		.getRequest();

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		
		Language language = (Language)request.getAttribute(Constants.LANGUAGE);

		StringBuilder groupKey = new StringBuilder();
		groupKey
		.append(store.getId())
		.append("_")
		.append(Constants.PRODUCTS_GROUP_CACHE_KEY)
		.append("-")
		.append(this.getGroupName())
		.append("_")
		.append(language.getCode());
		
		StringBuilder groupKeyMissed = new StringBuilder();
		groupKeyMissed
		.append(groupKey.toString())
		.append(Constants.MISSED_CACHE_KEY);
		
		List<com.salesmanager.web.entity.catalog.Product> objects = null;
		
		if(store.isUseCache()) {
		
			//get from the cache
			CacheUtils cache = CacheUtils.getInstance();
			objects = (List<com.salesmanager.web.entity.catalog.Product>) cache.getFromCache(groupKey.toString());
			Boolean missedContent = null;
			if(objects==null) {
				//get from missed cache
				missedContent = (Boolean)cache.getFromCache(groupKeyMissed.toString());
			}
			
			if(objects==null && missedContent==null) {
				objects = getProducts(request);

				//put in cache
				cache.putInCache(objects, groupKey.toString());
					
			} else {
				//put in missed cache
				cache.putInCache(new Boolean(true), groupKeyMissed.toString());
			}
		
		} else {
			objects = getProducts(request);
		}
		if(objects!=null && objects.size()>0) {
			request.setAttribute(this.getGroupName(), objects);
		}
		
		return SKIP_BODY;

	}


	public int doEndTag() {
		return EVAL_PAGE;
	}
	
	private List<com.salesmanager.web.entity.catalog.Product> getProducts(HttpServletRequest request) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		Language language = (Language)request.getAttribute(Constants.LANGUAGE);
		Locale locale = request.getLocale();
		List<ProductRelationship> relationships = productRelationshipService.getByGroup(store, this.getGroupName(), language);
		
		
		List<com.salesmanager.web.entity.catalog.Product> products = new ArrayList<com.salesmanager.web.entity.catalog.Product>();
		for(ProductRelationship relationship : relationships) {
			
			Product product = relationship.getRelatedProduct();
			com.salesmanager.web.entity.catalog.Product proxyProduct = catalogUtils.buildProxyProduct(product, store, locale);
			products.add(proxyProduct);

		}
		
		return products;
		
	}

	

}
