package com.salesmanager.core.business.tax.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.tax.dao.taxclass.TaxClassDao;
import com.salesmanager.core.business.tax.model.taxclass.TaxClass;

@Service("taxClassService")
public class TaxClassServiceImpl extends SalesManagerEntityServiceImpl<Long, TaxClass>
		implements TaxClassService {

	private TaxClassDao taxClassDao;
	
	@Autowired
	public TaxClassServiceImpl(TaxClassDao taxClassDao) {
		super(taxClassDao);
		
		this.taxClassDao = taxClassDao;
	}
	
	@Override
	public List<TaxClass> listByStore(MerchantStore store) throws ServiceException {	
		return taxClassDao.listByStore(store);
	}
}
