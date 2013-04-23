package com.salesmanager.core.business.tax.dao.taxrate;

import java.util.List;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.tax.model.taxrate.TaxRate;

public interface TaxRateDao  extends SalesManagerEntityDao<Long, TaxRate> {

	List<TaxRate> listByStore(MerchantStore store);

}
