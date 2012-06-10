package com.salesmanager.core.business.catalog.product.dao.price;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("productPriceDao")
public class ProductPriceDaoImpl extends SalesManagerEntityDaoImpl<Long, ProductPrice> 
	implements ProductPriceDao {

}
