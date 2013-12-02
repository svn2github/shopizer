package com.salesmanager.web.populator.catalog;

import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValue;
import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.AbstractDataPopulator;
import com.salesmanager.web.entity.catalog.product.attribute.PersistableProductOptionValue;

public class PersistableProductOptionValuePopulator extends
		AbstractDataPopulator<PersistableProductOptionValue, ProductOptionValue> {

	@Override
	public ProductOptionValue populate(PersistableProductOptionValue source,
			ProductOptionValue target, MerchantStore store, Language language)
			throws ConversionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ProductOptionValue createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
