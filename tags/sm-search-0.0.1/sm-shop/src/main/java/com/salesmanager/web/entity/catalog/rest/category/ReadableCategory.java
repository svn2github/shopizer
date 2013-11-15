package com.salesmanager.web.entity.catalog.rest.category;

import java.io.Serializable;

public class ReadableCategory extends CategoryEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CategoryDescription description;
	private CategoryEntity parent;
	public void setDescription(CategoryDescription description) {
		this.description = description;
	}
	public CategoryDescription getDescription() {
		return description;
	}
	public void setParent(CategoryEntity parent) {
		this.parent = parent;
	}
	public CategoryEntity getParent() {
		return parent;
	}

}
