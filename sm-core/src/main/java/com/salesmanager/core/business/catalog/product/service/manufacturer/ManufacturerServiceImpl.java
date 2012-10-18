package com.salesmanager.core.business.catalog.product.service.manufacturer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.manufacturer.ManufacturerDao;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

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
	
	@Override
	public List<Manufacturer> listByStore(MerchantStore store, Language language) throws ServiceException {
		return manufacturerDao.listByStore(store, language);
	}


}
