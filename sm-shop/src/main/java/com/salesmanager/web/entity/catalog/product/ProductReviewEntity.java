package com.salesmanager.web.entity.catalog.product;

import java.io.Serializable;

import com.salesmanager.web.entity.ShopEntity;
import com.salesmanager.web.entity.customer.ReadableCustomer;

public class ProductReviewEntity extends ShopEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String description;
	private Long productId;
	private ReadableCustomer customer;
	private Double rating;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public ReadableCustomer getCustomer() {
		return customer;
	}
	public void setCustomer(ReadableCustomer customer) {
		this.customer = customer;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}

}
