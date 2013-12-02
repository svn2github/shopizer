package com.salesmanager.web.populator.catalog;

import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.AbstractDataPopulator;
import com.salesmanager.web.entity.catalog.product.attribute.PersistableProductOption;

public class PersistableProductOptionPopulator extends
		AbstractDataPopulator<PersistableProductOption, ProductOption> {

	@Override
	public ProductOption populate(PersistableProductOption source,
			ProductOption target, MerchantStore store, Language language)
			throws ConversionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ProductOption createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
