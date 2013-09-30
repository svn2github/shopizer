package com.salesmanager.core.business.customer.dao.attribute;

import com.salesmanager.core.business.customer.model.attribute.CustomerAttribute;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantStore;

public interface CustomerAttributeDao extends SalesManagerEntityDao<Long, CustomerAttribute> {

	CustomerAttribute getByOptionId(MerchantStore store, Long id);

	CustomerAttribute getByOptionValueId(MerchantStore store, Long id);




}
