package com.salesmanager.web.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.constants.Constants;

public class LocaleUtils {
	
	public static Locale getLocale(Language language) {
		
		return new Locale(language.getCode());
		
	}
	
	public static Language getRequestLanguage(HttpServletRequest request) {
		
		Language language = (Language) request.getSession().getAttribute(Constants.LANGUAGE);
		if(language==null) {
			MerchantStore store = (MerchantStore)request.getSession().getAttribute(Constants.MERCHANT_STORE);
			if(store!=null) {
				language = store.getDefaultLanguage();
			}
		}
		return language;
		
	}

}
