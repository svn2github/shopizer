package com.salesmanager.web.utils;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.catalog.product.model.file.DigitalProduct;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.web.constants.Constants;

public class FilePathUtils {
	
	
	
	/**
	 * Builds a static content content file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param imageName
	 * @return
	 */
	public static String buildStaticFilePath(MerchantStore store, String imageName) {
		return new StringBuilder().append(Constants.FILES_URI).append("/").append(store.getCode()).append("/").append(imageName).toString();
	}
	
	public static String buildAdminDownloadProductFilePath(MerchantStore store, DigitalProduct digitalProduct) {
		return new StringBuilder().append(Constants.FILES_URI).append("/").append(store.getCode()).append("/").append(digitalProduct.getProductFileName()).toString();
	}
	

}
