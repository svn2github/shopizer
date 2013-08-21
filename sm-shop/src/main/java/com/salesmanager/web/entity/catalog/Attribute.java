package com.salesmanager.web.entity.catalog;

import java.io.Serializable;
import java.util.List;

public class Attribute implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = null;
	private String type = null;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	private List<AttributeValue> values = null;

}
