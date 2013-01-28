package com.salesmanager.core.business.catalog.product.dao.file;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.catalog.product.model.file.DigitalProduct;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;

@Repository("digitalProductDao")
public class DigitalProductDaoImpl extends SalesManagerEntityDaoImpl<Long, DigitalProduct> 
	implements DigitalProductDao {
	


}
