package com.salesmanager.core.modules.cms.product;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.modules.cms.common.ImageGet;

public interface ProductImageGet extends ImageGet{
	
	public OutputContentImage getProductImage(final String merchantStoreCode, final Long productId, final String imageName) throws ServiceException;
	public OutputContentImage getProductImage(ProductImage productImage) throws ServiceException;
	public List<OutputContentImage> getImages(Product product) throws ServiceException;


}
