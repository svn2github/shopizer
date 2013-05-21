package com.salesmanager.web.utils;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.merchant.model.MerchantStore;

public class ImageFilePathUtils {
	
	/**
	 * Builds a static content image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param imageName
	 * @return
	 */
	public static String buildStaticImageFilePath(MerchantStore store, String imageName) {
		return new StringBuilder().append(store.getCode()).append("/").append(ImageContentType.CONTENT.name()).append("/").append(imageName).toString();
	}
	
	/**
	 * Builds a product image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param product
	 * @param imageName
	 * @return
	 */
	public static String buildProductImageFilePath(MerchantStore store, Product product, String imageName) {
		return new StringBuilder().append(store.getCode()).append("/").append(ImageContentType.PRODUCT.name()).append("/")
				.append(product.getId()).append("/").append(imageName).toString();
	}

}
