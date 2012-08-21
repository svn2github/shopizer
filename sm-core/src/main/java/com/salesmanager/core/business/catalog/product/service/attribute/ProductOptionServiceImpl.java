package com.salesmanager.core.business.catalog.product.service.attribute;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.attribute.ProductOptionDao;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

@Service("productOptionService")
public class ProductOptionServiceImpl extends
		SalesManagerEntityServiceImpl<Long, ProductOption> implements ProductOptionService {

	
	private ProductOptionDao productOptionDao;
	
	@Autowired
	public ProductOptionServiceImpl(
			ProductOptionDao productOptionDao) {
			super(productOptionDao);
			this.productOptionDao = productOptionDao;
	}
	
	@Override
	public List<ProductOption> listByStore(MerchantStore store, Language language) throws ServiceException {
		
		
		return productOptionDao.listByStore(store, language);
		
		
	}



}
