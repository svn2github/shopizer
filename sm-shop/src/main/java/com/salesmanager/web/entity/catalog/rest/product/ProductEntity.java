package com.salesmanager.web.entity.catalog.rest.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.salesmanager.web.entity.catalog.rest.category.Category;
import com.salesmanager.web.entity.catalog.rest.product.attribute.ProductAttribute;


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
	private Long manufacturerId;
	private int quantity = 0;
	private String sku;
	private boolean productShipeable = false;
	private boolean productIsFree;
	private BigDecimal productLength;
	private BigDecimal productWidth;
	private BigDecimal productHeight;
	private BigDecimal productWeight;
	private int sortOrder;
	private List<ProductDescription> descriptions;
	private List<ProductAttribute> attributes;
	private List<Image> images;
	private List<Category> categories;
	
	
	
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Long getManufacturerId() {
		return manufacturerId;
	}
	public void setManufacturerId(Long manufacturerId) {
		this.manufacturerId = manufacturerId;
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
	public List<ProductDescription> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(List<ProductDescription> descriptions) {
		this.descriptions = descriptions;
	}
	public List<ProductAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<ProductAttribute> attributes) {
		this.attributes = attributes;
	}
	public List<Image> getImages() {
		return images;
	}
	public void setImages(List<Image> images) {
		this.images = images;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public List<Category> getCategories() {
		return categories;
	}

}
