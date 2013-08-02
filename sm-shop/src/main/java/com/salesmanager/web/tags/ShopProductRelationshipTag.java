package com.salesmanager.web.tags;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;


import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationshipType;
import com.salesmanager.core.business.catalog.product.service.relationship.ProductRelationshipService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.constants.Constants;

public class ShopProductRelationshipTag extends RequestContextAwareTag  {
	
	

	private static final Logger LOGGER = LoggerFactory.getLogger(ShopProductRelationshipTag.class);

	@Autowired
	private ProductRelationshipService productRelationshipService;
	
	
	private String groupName;



	public String getGroupName() {
		return groupName;
	}


	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	@Override
	protected int doStartTagInternal() throws Exception {
		// TODO Auto-generated method stub
		if (productRelationshipService == null) {
			LOGGER.debug("Autowiring ProductRelationshipService");
            WebApplicationContext wac = getRequestContext().getWebApplicationContext();
            AutowireCapableBeanFactory factory = wac.getAutowireCapableBeanFactory();
            factory.autowireBean(this);
        }
		
		HttpServletRequest request = (HttpServletRequest) pageContext
		.getRequest();

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		
		Language language = (Language)request.getAttribute(Constants.LANGUAGE);
		
		HttpSession session = request.getSession();
		
		List<ProductRelationship> products = productRelationshipService.getByType(store, ProductRelationshipType.FEATURED_ITEM, language);
		
		//TODO use caching
		
		//TODO transform to proxyProduct
		
		//pageContext.getOut().print(imagePath.toString());
		String[] arr = {"1","3","3"};
		request.setAttribute(this.getGroupName(), arr);
		
		return SKIP_BODY;

	}


	public int doEndTag() {
		return EVAL_PAGE;
	}

	

}
