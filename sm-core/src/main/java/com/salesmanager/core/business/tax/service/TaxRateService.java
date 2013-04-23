package com.salesmanager.core.business.tax.service;

import java.util.List;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.tax.model.taxrate.TaxRate;

public interface TaxRateService extends SalesManagerEntityService<Long, TaxRate> {

	public List<TaxRate> listByStore(MerchantStore store) throws ServiceException;

}
