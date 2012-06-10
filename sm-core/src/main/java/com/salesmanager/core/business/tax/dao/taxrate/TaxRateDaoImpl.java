package com.salesmanager.core.business.tax.dao.taxrate;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.tax.model.taxrate.TaxRate;

@Repository("taxRateDao")
public class TaxRateDaoImpl extends SalesManagerEntityDaoImpl<Long, TaxRate> implements TaxRateDao{
	
	public TaxRateDaoImpl() {
		super();
	}
}