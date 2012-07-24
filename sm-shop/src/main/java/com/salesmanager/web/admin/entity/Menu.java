package com.salesmanager.web.admin.entity;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Menu {
	
	private String code;
	private String url;
	private String icon;
	private List<String> roles = new ArrayList();
	private int order;
	private List<Menu> menus = new ArrayList();
	public String getCode() {
		return code;
	}
	@JsonProperty("code")  
	public void setCode(String code) {
		this.code = code;
	}
	public String getUrl() {
		return url;
	}
	@JsonProperty("url")  
	public void setUrl(String url) {
		this.url = url;
	}
	public List<String> getRoles() {
		return roles;
	}
	@JsonProperty("roles")  
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public int getOrder() {
		return order;
	}
	@JsonProperty("order")  
	public void setOrder(int order) {
		this.order = order;
	}
	public List<Menu> getMenus() {
		return menus;
	}
	@JsonProperty("menus")  
	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIcon() {
		return icon;
	}

}
