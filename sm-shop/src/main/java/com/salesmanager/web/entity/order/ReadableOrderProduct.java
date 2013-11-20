package com.salesmanager.web.entity.order;

import java.io.Serializable;

import com.salesmanager.web.entity.catalog.product.ReadableProduct;

public class ReadableOrderProduct extends OrderProductEntity implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ReadableProduct product;
	public void setProduct(ReadableProduct product) {
		this.product = product;
	}
	public ReadableProduct getProduct() {
		return product;
	}

}
