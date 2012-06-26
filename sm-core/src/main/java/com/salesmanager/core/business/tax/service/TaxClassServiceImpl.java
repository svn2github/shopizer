package com.salesmanager.core.business.tax.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
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
}
