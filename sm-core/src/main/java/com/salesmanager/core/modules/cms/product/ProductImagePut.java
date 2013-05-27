package com.salesmanager.core.modules.cms.product;

import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.content.model.content.ImageContentFile;
import com.salesmanager.core.business.content.model.content.InputContentFile;
import com.salesmanager.core.business.generic.exception.ServiceException;


public interface ProductImagePut {
	
	
	public void addProductImage(ProductImage productImage, InputContentFile contentImage) throws ServiceException;


}
