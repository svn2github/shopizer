package com.salesmanager.web.utils;

import java.util.Locale;

import com.salesmanager.core.business.reference.language.model.Language;

public class LocaleUtils {
	
	public static Locale getLocale(Language language) {
		
		return new Locale(language.getCode());
		
	}

}
