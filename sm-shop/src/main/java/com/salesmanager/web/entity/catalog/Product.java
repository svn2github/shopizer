package com.salesmanager.web.entity.catalog;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;


import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;

public class Product {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -15983661499653383L;
	//provides wrapping to the main product entity
	@Valid
	private com.salesmanager.core.business.catalog.product.model.Product product;
	
	@Valid
	private List<ProductDescription> descriptions = new ArrayList<ProductDescription>();
	
	@Valid
	private ProductAvailability availability = null;
	
	@Valid
	private ProductPrice price = null;

	
	
	public com.salesmanager.core.business.catalog.product.model.Product getProduct() {
		return product;
	}
	public void setProduct(com.salesmanager.core.business.catalog.product.model.Product product) {
		this.product = product;
	}
	
	public List<ProductDescription> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(List<ProductDescription> descriptions) {
		this.descriptions = descriptions;
	}
	public void setAvailability(ProductAvailability availability) {
		this.availability = availability;
	}
	public ProductAvailability getAvailability() {
		return availability;
	}
	public void setPrice(ProductPrice price) {
		this.price = price;
	}
	public ProductPrice getPrice() {
		return price;
	}
	





}
