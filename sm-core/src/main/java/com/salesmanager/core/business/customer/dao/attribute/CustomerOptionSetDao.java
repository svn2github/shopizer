package com.salesmanager.core.business.customer.dao.attribute;

import java.util.List;

import com.salesmanager.core.business.customer.model.attribute.CustomerOptionSet;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantStore;

public interface CustomerOptionSetDao extends SalesManagerEntityDao<Long, CustomerOptionSet> {

	List<CustomerOptionSet> getByOptionId(MerchantStore store, Long id);





}
