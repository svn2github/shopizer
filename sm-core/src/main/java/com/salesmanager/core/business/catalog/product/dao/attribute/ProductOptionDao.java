package com.salesmanager.core.business.catalog.product.dao.attribute;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;

public interface ProductOptionDao extends SalesManagerEntityDao<Long, ProductOption> {

	List<ProductOption> listByStore(MerchantStore store, Language language);

}
