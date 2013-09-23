package com.salesmanager.core.business.search.model;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.shipping.model.ShippingConfiguration;

public class IndexProduct implements JSONAware {
	
	private String name;
	private Double price;
	private List<String> categories;//category code
	private boolean available;
	private String description;
	private List<String> tags;//keywords ?
	private String highlight;
	private String store;
	private String lang;

	@Override
	public String toJSONString() {
		
		JSONArray categories = new JSONArray();
		
		JSONObject obj = new JSONObject();
		obj.put("name", this.getName());
		
		return obj.toJSONString();

		
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getHighlight() {
		return highlight;
	}

	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getPrice() {
		return price;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getStore() {
		return store;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLang() {
		return lang;
	}

}
