package com.salesmanager.core.business.catalog.product.dao.image;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("productImageDao")
public class ProductImageDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductImage> 
	implements ProductImageDao {

}
