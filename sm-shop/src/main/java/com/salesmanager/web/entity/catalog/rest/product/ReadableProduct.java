package com.salesmanager.web.entity.catalog.rest.product;

import java.io.Serializable;
import java.util.List;

import com.salesmanager.web.entity.catalog.ReadableImage;
import com.salesmanager.web.entity.catalog.rest.manufacturer.ManufacturerEntity;
import com.salesmanager.web.entity.catalog.rest.product.attribute.ProductAttributeEntity;

public class ReadableProduct extends ProductEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProductDescription description;
	private String finalPrice = "0";
	private String originalPrice = null;
	private boolean discounted = false;
	private ReadableImage image;
	private List<ReadableImage> images;
	private ManufacturerEntity manufacturer;
	private List<ProductAttributeEntity> attributes;
	
	
	
	public ProductDescription getDescription() {
		return description;
	}
	public void setDescription(ProductDescription description) {
		this.description = description;
	}
	public String getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(String finalPrice) {
		this.finalPrice = finalPrice;
	}
	public String getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}
	public boolean isDiscounted() {
		return discounted;
	}
	public void setDiscounted(boolean discounted) {
		this.discounted = discounted;
	}

	public void setManufacturer(ManufacturerEntity manufacturer) {
		this.manufacturer = manufacturer;
	}
	public ManufacturerEntity getManufacturer() {
		return manufacturer;
	}

	public void setImages(List<ReadableImage> images) {
		this.images = images;
	}
	public List<ReadableImage> getImages() {
		return images;
	}
	public void setImage(ReadableImage image) {
		this.image = image;
	}
	public ReadableImage getImage() {
		return image;
	}
	public void setAttributes(List<ProductAttributeEntity> attributes) {
		this.attributes = attributes;
	}
	public List<ProductAttributeEntity> getAttributes() {
		return attributes;
	}

}
