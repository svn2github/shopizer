package com.salesmanager.core.modules.cms.product;

import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.content.model.image.InputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.utils.CoreConfiguration;


public interface ProductImagePut {
	
	
	public void uploadProductImage(CoreConfiguration coreConfiguration, ProductImage productImage, InputContentImage contentImage) throws ServiceException;


}
