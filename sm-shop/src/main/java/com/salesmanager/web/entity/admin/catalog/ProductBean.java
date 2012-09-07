package com.salesmanager.web.entity.admin.catalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;

import com.salesmanager.core.business.catalog.product.model.Product;

public class ProductBean implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -15983661499653383L;
	//provides wrapping to the main product entity
	@Valid
	private Product product;
	
	@Valid
	private List<ProductDescription> descriptions = new ArrayList<ProductDescription>();
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public List<ProductDescription> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(List<ProductDescription> descriptions) {
		this.descriptions = descriptions;
	}



}
