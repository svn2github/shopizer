package com.salesmanager.web.entity.catalog.rest.product.attribute;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductAttribute implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProductOption option;
	private ProductOptionValue optionValue;
	private BigDecimal productAttributePrice;
	private int sortOrder;
	private BigDecimal productAttributeWeight;
	private boolean attributeDefault=false;
	public void setOptionValue(ProductOptionValue optionValue) {
		this.optionValue = optionValue;
	}
	public ProductOptionValue getOptionValue() {
		return optionValue;
	}
	public void setOption(ProductOption option) {
		this.option = option;
	}
	public ProductOption getOption() {
		return option;
	}
	public void setProductAttributePrice(BigDecimal productAttributePrice) {
		this.productAttributePrice = productAttributePrice;
	}
	public BigDecimal getProductAttributePrice() {
		return productAttributePrice;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setProductAttributeWeight(BigDecimal productAttributeWeight) {
		this.productAttributeWeight = productAttributeWeight;
	}
	public BigDecimal getProductAttributeWeight() {
		return productAttributeWeight;
	}
	public void setAttributeDefault(boolean attributeDefault) {
		this.attributeDefault = attributeDefault;
	}
	public boolean isAttributeDefault() {
		return attributeDefault;
	}
}
