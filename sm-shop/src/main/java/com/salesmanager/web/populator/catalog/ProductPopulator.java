package com.salesmanager.web.populator.catalog;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.AbstractDataPopulator;


public class ProductPopulator extends AbstractDataPopulator<Product, com.salesmanager.web.entity.catalog.Product> {

    @Override
    public com.salesmanager.web.entity.catalog.Product populateFromEntity( Product source,
                                                                           com.salesmanager.web.entity.catalog.Product target,
                                                                           MerchantStore store, Language language )
        throws ConversionException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected com.salesmanager.web.entity.catalog.Product createTarget()
    {
        // TODO Auto-generated method stub
        return null;
    }

	

	


}
