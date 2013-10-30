package com.salesmanager.web.entity.catalog;

import java.io.Serializable;


/**
 * Used when invoking /service type urls
 * Contains simplified and minimal structure
 * @author casams1
 *
 */
public class Category extends CatalogEntity implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private int order;
	private int depth;
	private long totalCount;
	

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public long getTotalCount() {
		return totalCount;
	}

	

}
