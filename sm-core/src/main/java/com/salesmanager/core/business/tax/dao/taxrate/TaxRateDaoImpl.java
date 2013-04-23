package com.salesmanager.core.business.tax.dao.taxrate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.tax.model.taxrate.TaxRate;

@Repository("taxRateDao")
public class TaxRateDaoImpl extends SalesManagerEntityDaoImpl<Long, TaxRate> implements TaxRateDao{
	
	public TaxRateDaoImpl() {
		super();
	}
	
	@Override
	public List<TaxRate> listByStore(MerchantStore store) {
		return null;
	}
}