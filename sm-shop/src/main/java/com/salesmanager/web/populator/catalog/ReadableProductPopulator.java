package com.salesmanager.web.populator.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.utils.AbstractDataPopulator;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.web.entity.catalog.ReadableImage;
import com.salesmanager.web.entity.catalog.rest.manufacturer.ManufacturerEntity;
import com.salesmanager.web.entity.catalog.rest.product.ReadableProduct;
import com.salesmanager.web.utils.ImageFilePathUtils;

public class ReadableProductPopulator extends
		AbstractDataPopulator<Product, ReadableProduct> {
	
	private ProductPriceUtils productPriceUtils;

	@Override
	public ReadableProduct populateFromEntity(Product source,
			ReadableProduct target, MerchantStore store, Language language)
			throws ServiceException {
		Validate.notNull(productPriceUtils, "Requires to set CategoryService");
		
		try {
			

			ProductDescription description = source.getProductDescription();
	
			target.setId(source.getId());
			target.setAvailable(source.isAvailable());
			if(description!=null) {
				com.salesmanager.web.entity.catalog.rest.product.ProductDescription tragetDescription = new com.salesmanager.web.entity.catalog.rest.product.ProductDescription();
				tragetDescription.setFriendlyUrl(description.getSeUrl());
				tragetDescription.setName(description.getName());
				tragetDescription.setDescription(description.getDescription());
			}
			
			if(source.getManufacturer()!=null) {
				ManufacturerDescription manufacturer = source.getManufacturer().getDescriptions().iterator().next(); 
				ManufacturerEntity manufacturerEntity = new ManufacturerEntity();
				manufacturerEntity.setName(manufacturer.getName());
				manufacturerEntity.setId(manufacturer.getId());
				target.setManufacturer(manufacturerEntity);
			}
			
			ProductImage image = source.getProductImage();
			if(image!=null) {
				ReadableImage rimg = new ReadableImage();
				rimg.setImageName(image.getProductImage());
				String imagePath = ImageFilePathUtils.buildProductImageFilePath(store, source.getSku(), image.getProductImage());
				rimg.setImageUrl(imagePath);
				target.setImage(rimg);
				
				//other images
				Set<ProductImage> images = source.getImages();
				if(images!=null && images.size()>0) {
					
					List<ReadableImage> imageList = new ArrayList<ReadableImage>();
					for(ProductImage img : images) {
						
						ReadableImage prdImage = new ReadableImage();
						prdImage.setImageName(img.getProductImage());
						String imgPath = ImageFilePathUtils.buildProductImageFilePath(store, source.getSku(), img.getProductImage());
						prdImage.setImageUrl(imgPath);
						imageList.add(prdImage);
					}
					target
					.setImages(imageList);
				}
			}
	
			target.setSku(source.getSku());
			//target.setLanguage(language.getCode());
	
			FinalPrice price = productPriceUtils.getFinalPrice(source);
			target.setFinalPrice(productPriceUtils.getStoreFormatedAmountWithCurrency(store,price.getFinalPrice()));
	
			if(price.isDiscounted()) {
				target.setDiscounted(true);
				target.setOriginalPrice(productPriceUtils.getStoreFormatedAmountWithCurrency(store,price.getOriginalPrice()));
			}
			
			//availability
			for(ProductAvailability availability : source.getAvailabilities()) {
				if(availability.getRegion().equals(Constants.ALL_REGIONS)) {//TODO REL 2.1 accept a region
					target.setQuantity(availability.getProductQuantity());
					target.setQuantityOrderMaximum(availability.getProductQuantityOrderMax());
				}
			}
			
			
			return target;
		
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Product populateToEntity(Product target, ReadableProduct source,
			MerchantStore store) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setProductPriceUtils(ProductPriceUtils productPriceUtils) {
		this.productPriceUtils = productPriceUtils;
	}

	public ProductPriceUtils getProductPriceUtils() {
		return productPriceUtils;
	}

}