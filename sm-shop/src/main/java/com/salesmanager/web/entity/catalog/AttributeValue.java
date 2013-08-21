package com.salesmanager.web.entity.catalog;

import java.io.Serializable;

public class AttributeValue implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name = null;
	private boolean defaultAttribute;
	private String image;
	private String price;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isDefaultAttribute() {
		return defaultAttribute;
	}
	public void setDefaultAttribute(boolean defaultAttribute) {
		this.defaultAttribute = defaultAttribute;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

}
