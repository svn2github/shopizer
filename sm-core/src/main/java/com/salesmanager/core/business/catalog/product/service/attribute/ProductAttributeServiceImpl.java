package com.salesmanager.core.business.catalog.product.service.attribute;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.attribute.ProductAttributeDao;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValue;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;

@Service("productAttributeService")
public class ProductAttributeServiceImpl extends
		SalesManagerEntityServiceImpl<Long, ProductAttribute> implements ProductAttributeService {
	
	private ProductAttributeDao productAttributeDao;

	@Autowired
	public ProductAttributeServiceImpl(ProductAttributeDao productAttributeDao) {
		super(productAttributeDao);
		this.productAttributeDao = productAttributeDao;
	}
	
	
	@Override
	public List<ProductAttribute> getById(MerchantStore store,
			ProductOption option) throws ServiceException {
		
		return productAttributeDao.getByOptionId(store, option);
		
	}
	
	@Override
	public List<ProductAttribute> getByOptionValueId(MerchantStore store,
			ProductOptionValue optionValue) throws ServiceException {
		
		return productAttributeDao.getByOptionValueId(store, optionValue);
		
	}


	@Override
	public void saveOrUpdate(ProductAttribute productAttribute)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}

}
