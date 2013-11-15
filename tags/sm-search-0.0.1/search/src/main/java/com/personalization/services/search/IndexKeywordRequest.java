package com.personalization.services.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import com.personalization.services.search.field.Field;

public class IndexKeywordRequest implements Serializable {
	
	private String id;
	//private Locale locale;
	private String key;
	private Collection<Field> filters = new ArrayList();
	
	
	public Collection<Field> getFilters() {
		
		return filters;
	}
	public void setFilters(Collection<Field> filters) {
		this.filters = filters;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
/*	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}*/
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	

}
