package com.salesmanager.web.shop.model.catalog;

import java.io.Serializable;

import com.salesmanager.web.entity.ShopEntity;

public class AttributeValue extends ShopEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = null;
	private boolean defaultAttribute;
	private String image;
	private String price;

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