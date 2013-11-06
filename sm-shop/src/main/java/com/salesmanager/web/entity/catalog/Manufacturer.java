package com.salesmanager.web.entity.catalog;

import java.io.Serializable;

import com.salesmanager.web.entity.ShopEntity;


public class Manufacturer extends ShopEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}



}
