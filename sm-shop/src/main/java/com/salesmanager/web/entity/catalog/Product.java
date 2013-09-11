package com.salesmanager.web.entity.catalog;

import java.io.Serializable;
import java.util.List;

/**
 * A Product is used in the web application shopping section
 * This object is a simplification of com.salesmanager.core.business.catalog.product.model.Product
 * @author Carl Samson
 *
 */
public class Product extends CatalogEntity implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * this is the final price
	 */
	private String productPrice = "0";
	private String dateAvailable;
	private String sku;
	private boolean productVirtual;
	private String type;
	private String manufacturer;
	private int quantity = 0;
	private int quantityOrderMaximum =-1;//default unlimited

	
	private boolean discounted = false;
	/**
	 * when discounted this is the original price
	 */
	private String originalProductPrice = null;
	
	private String image;
	private String imageUrl;
	
	private List<Image> images;


	
	public String getDateAvailable() {
		return dateAvailable;
	}
	public void setDateAvailable(String dateAvailable) {
		this.dateAvailable = dateAvailable;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public boolean isProductVirtual() {
		return productVirtual;
	}
	public void setProductVirtual(boolean productVirtual) {
		this.productVirtual = productVirtual;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setImage(String image) {
		this.image = image;
	}
	public String getImage() {
		return image;
	}

	public void setOriginalProductPrice(String originalProductPrice) {
		this.originalProductPrice = originalProductPrice;
	}
	public String getOriginalProductPrice() {
		return originalProductPrice;
	}
	public void setDiscounted(boolean discounted) {
		this.discounted = discounted;
	}
	public boolean isDiscounted() {
		return discounted;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
	public List<Image> getImages() {
		return images;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantityOrderMaximum(int quantityOrderMaximum) {
		this.quantityOrderMaximum = quantityOrderMaximum;
	}
	public int getQuantityOrderMaximum() {
		return quantityOrderMaximum;
	}







}
