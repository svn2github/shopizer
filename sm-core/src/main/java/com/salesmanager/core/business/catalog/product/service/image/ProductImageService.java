package com.salesmanager.core.business.catalog.product.service.image;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.service.ProductImageEnum;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;


public interface ProductImageService extends SalesManagerEntityService<Long, ProductImage> {
	
	
	
	/**
	 * Add a ProductImage to the persistence and an entry to the CMS
	 * @param product
	 * @param productImage
	 * @param file
	 * @throws ServiceException
	 */
	void addProductImage(Product product, ProductImage productImage)
			throws ServiceException;

	/**
	 * Get the image ByteArrayOutputStream and content description from CMS
	 * @param productImage
	 * @return
	 * @throws ServiceException
	 */
	OutputContentImage getProductImage(ProductImage productImage, ProductImageEnum size)
			throws ServiceException;

	/**
	 * Returns all Images for a given product
	 * @param product
	 * @return
	 * @throws ServiceException
	 */
	List<OutputContentImage> getProductImages(Product product)
			throws ServiceException;

	void removeProductImage(ProductImage productImage) throws ServiceException;
	
}
