package com.salesmanager.web.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.web.entity.catalog.product.attribute.ProductAttribute;

public class PersistableOrderProduct extends OrderProductEntity implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Product product;//readable product
	private BigDecimal finalPrice;//specify final price
	private List<ProductAttribute> attributes;//may have attributes

	public void setFinalPrice(BigDecimal finalPrice) {
		this.finalPrice = finalPrice;
	}

	public BigDecimal getFinalPrice() {
		return finalPrice;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}

	public void setAttributes(List<ProductAttribute> attributes) {
		this.attributes = attributes;
	}

	public List<ProductAttribute> getAttributes() {
		return attributes;
	}

}
