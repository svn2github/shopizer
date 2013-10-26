package com.salesmanager.core.business.catalog.product.service.manufacturer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.manufacturer.ManufacturerDao;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerServiceImpl;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.search.service.SearchService;



@Service("manufacturerService")
public class ManufacturerServiceImpl extends
		SalesManagerEntityServiceImpl<Long, Manufacturer> implements ManufacturerService {

	@Autowired
	SearchService searchService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
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
	
	@Override
	public List<Manufacturer> listByStore(MerchantStore store) throws ServiceException {
		return manufacturerDao.listByStore(store);
	}

	@Override
	public void addManufacturerDescription(Manufacturer manufacturer, ManufacturerDescription description)
			throws ServiceException {
		
		
		if(manufacturer.getDescriptions()==null) {
			manufacturer.setDescriptions(new HashSet<ManufacturerDescription>());
		}
		
		manufacturer.getDescriptions().add(description);
		description.setManufacturer(manufacturer);
		update(manufacturer);
//		searchService.index(manufacturer.getMerchantStore(), manufacturer);
	}
	
	@Override	
	public void saveOrUpdate(Manufacturer manufacturer) throws ServiceException {

		LOGGER.debug("Creating Manufacturer");
		
		if(manufacturer.getId()!=null && manufacturer.getId()>0) {
			super.update(manufacturer);   // commented for now, get constraint error( update manuf_description code missing)  
		} else {			
		
			Set<ManufacturerDescription> manufacturerDescriptions = manufacturer.getDescriptions();
			manufacturer.setDescriptions(null);
			
			super.create(manufacturer);

			for(ManufacturerDescription manufacturerDescription : manufacturerDescriptions) {
				addManufacturerDescription(manufacturer,manufacturerDescription);
			}
		}
	}
}
