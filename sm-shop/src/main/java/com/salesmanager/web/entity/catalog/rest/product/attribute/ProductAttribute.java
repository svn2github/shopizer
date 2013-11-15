package com.salesmanager.web.entity.catalog.rest.product.attribute;

import java.io.Serializable;

import com.salesmanager.web.entity.Entity;

public class ProductAttribute extends Entity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProductOptionEntity option;
	private ProductOptionValueEntity optionValue;
	
	
	public void setOptionValue(ProductOptionValueEntity optionValue) {
		this.optionValue = optionValue;
	}
	public ProductOptionValueEntity getOptionValue() {
		return optionValue;
	}
	public void setOption(ProductOptionEntity option) {
		this.option = option;
	}
	public ProductOptionEntity getOption() {
		return option;
	}

}
