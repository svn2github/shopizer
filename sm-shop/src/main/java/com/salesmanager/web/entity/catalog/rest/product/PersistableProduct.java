package com.salesmanager.web.entity.catalog.rest.product;

import java.io.Serializable;
import java.util.List;

import com.salesmanager.web.entity.catalog.rest.category.Category;
import com.salesmanager.web.entity.catalog.rest.manufacturer.Manufacturer;
import com.salesmanager.web.entity.catalog.rest.product.attribute.ProductAttribute;
import com.salesmanager.web.entity.catalog.rest.product.attribute.ProductAttributeEntity;

public class PersistableProduct extends ProductEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ProductDescription> descriptions;
	private List<ProductAttributeEntity> attributes;
	private List<PersistableImage> images;
	private List<Category> categories;
	private List<RelatedProduct> relatedProducts;
	private Manufacturer manufacturer;
	
	public List<ProductDescription> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(List<ProductDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public List<PersistableImage> getImages() {
		return images;
	}
	public void setImages(List<PersistableImage> images) {
		this.images = images;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public List<RelatedProduct> getRelatedProducts() {
		return relatedProducts;
	}
	public void setRelatedProducts(List<RelatedProduct> relatedProducts) {
		this.relatedProducts = relatedProducts;
	}
	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}
	public Manufacturer getManufacturer() {
		return manufacturer;
	}
	public void setAttributes(List<ProductAttributeEntity> attributes) {
		this.attributes = attributes;
	}
	public List<ProductAttributeEntity> getAttributes() {
		return attributes;
	}


}
