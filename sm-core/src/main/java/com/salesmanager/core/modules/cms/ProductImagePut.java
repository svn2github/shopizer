package com.salesmanager.core.modules.cms;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.generic.exception.ServiceException;


public interface ProductImagePut {
	
	
	public void uploadProductImage(ProductImage productImage, InputContentImage contentImage) throws ServiceException;


}
