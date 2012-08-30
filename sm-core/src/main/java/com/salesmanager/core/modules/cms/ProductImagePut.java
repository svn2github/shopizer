package com.salesmanager.core.modules.cms;

import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.utils.CoreConfiguration;


public interface ProductImagePut {
	
	
	public void uploadProductImage(CoreConfiguration coreConfiguration, ProductImage productImage, InputContentImage contentImage) throws ServiceException;


}
