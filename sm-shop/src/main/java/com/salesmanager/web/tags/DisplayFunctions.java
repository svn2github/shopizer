package com.salesmanager.web.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.constants.Constants;

public class DisplayFunctions {
	
	
	public com.salesmanager.core.business.catalog.category.model.Category getTopCategories(javax.servlet.http.HttpServletRequest request) {
		
		List<Category> returnCategories = new ArrayList<Category>();
		//get the current language
		Language language = (Language)request.getAttribute(Constants.LANGUAGE);
		MerchantStore store = (MerchantStore) request.getAttribute(Constants.MERCHANT_STORE);
		
		//get top categories
		Map<String,List<Category>> categories = (Map<String, List<Category>>) request.getAttribute(Constants.REQUEST_TOP_CATEGORIES);
		
		if(categories !=null) {
			//build key
			StringBuilder contentKey = new StringBuilder();
			contentKey
			.append(Constants.CATEGORIES_CACHE_KEY)
			.append(store.getId())
			.append(language.getCode());
			
			returnCategories = categories.get(contentKey.toString());
		}
		
		return returnCategories;
		
	}

}
