package com.salesmanager.core.business.customer.service.attribute;

import com.salesmanager.core.business.customer.model.attribute.CustomerAttribute;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;

public interface CustomerAttributeService extends
		SalesManagerEntityService<Long, CustomerAttribute> {

	void saveOrUpdate(CustomerAttribute customerAttribute)
			throws ServiceException;

	CustomerAttribute getByCustomerOptionId(MerchantStore store,
			Long customerId, Long id);

	//CustomerAttribute getByCustomerOptionValueId(MerchantStore store,
	//		Long id);
	

}
