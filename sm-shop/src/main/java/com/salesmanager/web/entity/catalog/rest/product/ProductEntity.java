package com.salesmanager.web.entity.catalog.rest.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.salesmanager.web.entity.catalog.rest.category.Category;
import com.salesmanager.web.entity.catalog.rest.product.attribute.ProductAttributeEntity;


/**
 * A product entity is used by services API
 * to populate or retrieve a Product entity
 * @author Carl Samson
 *
 */
public class ProductEntity extends Product implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal price;
	private int quantity = 0;
	private String sku;
	private boolean productShipeable = false;
	private boolean productVirtual = false;
	private int quantityOrderMaximum =-1;//default unlimited
	private boolean productIsFree;
	private boolean available;
	private BigDecimal productLength;
	private BigDecimal productWidth;
	private BigDecimal productHeight;
	private BigDecimal productWeight;
	private int sortOrder;
	private String dateAvailable;

	
	
	
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public boolean isProductShipeable() {
		return productShipeable;
	}
	public void setProductShipeable(boolean productShipeable) {
		this.productShipeable = productShipeable;
	}
	public boolean isProductIsFree() {
		return productIsFree;
	}
	public void setProductIsFree(boolean productIsFree) {
		this.productIsFree = productIsFree;
	}
	public BigDecimal getProductLength() {
		return productLength;
	}
	public void setProductLength(BigDecimal productLength) {
		this.productLength = productLength;
	}
	public BigDecimal getProductWidth() {
		return productWidth;
	}
	public void setProductWidth(BigDecimal productWidth) {
		this.productWidth = productWidth;
	}
	public BigDecimal getProductHeight() {
		return productHeight;
	}
	public void setProductHeight(BigDecimal productHeight) {
		this.productHeight = productHeight;
	}
	public BigDecimal getProductWeight() {
		return productWeight;
	}
	public void setProductWeight(BigDecimal productWeight) {
		this.productWeight = productWeight;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public void setQuantityOrderMaximum(int quantityOrderMaximum) {
		this.quantityOrderMaximum = quantityOrderMaximum;
	}
	public int getQuantityOrderMaximum() {
		return quantityOrderMaximum;
	}
	public void setProductVirtual(boolean productVirtual) {
		this.productVirtual = productVirtual;
	}
	public boolean isProductVirtual() {
		return productVirtual;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setDateAvailable(String dateAvailable) {
		this.dateAvailable = dateAvailable;
	}
	public String getDateAvailable() {
		return dateAvailable;
	}

}