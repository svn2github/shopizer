package com.salesmanager.web.entity.catalog.rest.category;

import java.io.Serializable;
import java.util.List;

public class CategoryEntity extends Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<CategoryDescription> descriptions;
	
	private Long parent;
	private int sortOrder;
	private boolean visible;
	private String code;
	
	
	public void setDescriptions(List<CategoryDescription> descriptions) {
		this.descriptions = descriptions;
	}
	public List<CategoryDescription> getDescriptions() {
		return descriptions;
	}
	public Long getParent() {
		return parent;
	}
	public void setParent(Long parent) {
		this.parent = parent;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
