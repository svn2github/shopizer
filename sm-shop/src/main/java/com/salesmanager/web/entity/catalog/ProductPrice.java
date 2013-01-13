package com.salesmanager.web.entity.catalog;

import java.util.ArrayList;
import java.util.List;

import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;

public class ProductPrice {
	
	private com.salesmanager.core.business.catalog.product.model.price.ProductPrice price = null;
	private List <ProductPriceDescription> descriptions = new ArrayList<ProductPriceDescription>();
	private String priceText;
	private String specialPriceText;
	private ProductAvailability productAvailability;
	
	
	
	
	
	public List <ProductPriceDescription> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(List <ProductPriceDescription> descriptions) {
		this.descriptions = descriptions;
	}
	public ProductAvailability getProductAvailability() {
		return productAvailability;
	}
	public void setProductAvailability(ProductAvailability productAvailability) {
		this.productAvailability = productAvailability;
	}
	public String getPriceText() {
		return priceText;
	}
	public void setPriceText(String priceText) {
		this.priceText = priceText;
	}
	public com.salesmanager.core.business.catalog.product.model.price.ProductPrice getPrice() {
		return price;
	}
	public void setPrice(com.salesmanager.core.business.catalog.product.model.price.ProductPrice price) {
		this.price = price;
	}
	public String getSpecialPriceText() {
		return specialPriceText;
	}
	public void setSpecialPriceText(String specialPriceText) {
		this.specialPriceText = specialPriceText;
	}

}
