package com.salesmanager.core.business.catalog.product.service.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.image.ProductImageDao;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("productImage")
public class ProductImageServiceImpl extends SalesManagerEntityServiceImpl<Long, ProductImage> 
	implements ProductImageService {

	@Autowired
	public ProductImageServiceImpl(ProductImageDao productImageDao) {
		super(productImageDao);
	}
}
