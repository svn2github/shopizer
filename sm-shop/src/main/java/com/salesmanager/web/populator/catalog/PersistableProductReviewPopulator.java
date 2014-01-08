package com.salesmanager.web.populator.catalog;

import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.AbstractDataPopulator;
import com.salesmanager.web.entity.catalog.product.PersistableProductReview;

public class PersistableProductReviewPopulator extends
		AbstractDataPopulator<PersistableProductReview, ProductReview> {

	@Override
	public ProductReview populate(PersistableProductReview source,
			ProductReview target, MerchantStore store, Language language)
			throws ConversionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ProductReview createTarget() {
		// TODO Auto-generated method stub
		return null;
	}


}
