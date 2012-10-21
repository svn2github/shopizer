package com.salesmanager.core.business.catalog.product.service.attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.attribute.ProductAttributeDao;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("productAttributeService")
public class ProductAttributeServiceImpl extends
		SalesManagerEntityServiceImpl<Long, ProductAttribute> implements ProductAttributeService {

	@Autowired
	public ProductAttributeServiceImpl(ProductAttributeDao productAttributeDao) {
		super(productAttributeDao);
	}
	
	
	@Override
	public void saveOrUpdate(ProductAttribute productAttribute) throws ServiceException {
		
		
		if(productAttribute.getId()!=null && productAttribute.getId()>0) {
			this.update(productAttribute);
		} else {
			this.create(productAttribute);
		}
		
	}

}
