package com.salesmanager.core.modules.cms;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.generic.exception.ServiceException;

public interface ProductImageGet extends ImageGet{
	
	public OutputContentImage getProductImage(ProductImage productImage) throws ServiceException;
	public List<OutputContentImage> getImages(Product product) throws ServiceException;


}
