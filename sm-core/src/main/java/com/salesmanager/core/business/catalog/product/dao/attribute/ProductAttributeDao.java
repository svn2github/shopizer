package com.salesmanager.core.business.catalog.product.dao.attribute;

import java.util.List;

import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValue;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDao;
import com.salesmanager.core.business.merchant.model.MerchantStore;

public interface ProductAttributeDao extends SalesManagerEntityDao<Long, ProductAttribute> {

	List<ProductAttribute> getByOptionId(MerchantStore store,
			ProductOption option);

	List<ProductAttribute> getByOptionValueId(MerchantStore store,
			ProductOptionValue optionValue);

}
