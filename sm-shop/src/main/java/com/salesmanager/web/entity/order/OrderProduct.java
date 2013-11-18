package com.salesmanager.web.entity.order;

import java.io.Serializable;

import com.salesmanager.web.entity.catalog.rest.product.ReadableProduct;

public class OrderProduct extends ReadableProduct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int orderedQuantity;
	
	public void setOrderedQuantity(int orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}
	public int getOrderedQuantity() {
		return orderedQuantity;
	}

}
