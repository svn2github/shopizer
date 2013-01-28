package com.salesmanager.core.business.catalog.product.service.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.file.DigitalProductDao;
import com.salesmanager.core.business.catalog.product.model.file.DigitalProduct;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("digitalProductService")
public class DigitalProductServiceImpl extends SalesManagerEntityServiceImpl<Long, DigitalProduct> 
	implements DigitalProductService {
	
	private DigitalProductDao digitalProductDao;

	@Autowired
	public DigitalProductServiceImpl(DigitalProductDao digitalProductDao) {
		super(digitalProductDao);
		this.digitalProductDao = digitalProductDao;
	}
	

}
