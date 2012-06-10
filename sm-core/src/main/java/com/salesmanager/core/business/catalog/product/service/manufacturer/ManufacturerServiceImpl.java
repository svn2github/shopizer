package com.salesmanager.core.business.catalog.product.service.manufacturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.manufacturer.ManufacturerDao;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("manufacturerService")
public class ManufacturerServiceImpl extends
		SalesManagerEntityServiceImpl<Long, Manufacturer> implements ManufacturerService {

	
	private ManufacturerDao manufacturerDao;
	
	@Autowired
	public ManufacturerServiceImpl(
		ManufacturerDao manufacturerDao) {
		super(manufacturerDao);
		this.manufacturerDao = manufacturerDao;
		
	}


}
