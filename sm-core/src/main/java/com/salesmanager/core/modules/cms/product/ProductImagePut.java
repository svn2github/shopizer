package com.salesmanager.core.modules.cms.product;

import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.content.model.image.InputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;


public interface ProductImagePut {
	
	
	public void addProductImage(ProductImage productImage, InputContentImage contentImage) throws ServiceException;


}
