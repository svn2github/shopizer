
package com.salesmanager.web.populator.manufacturer;

import java.util.Set;

import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.AbstractDataPopulator;
import com.salesmanager.web.entity.catalog.manufacturer.ManufacturerEntity;


/**
 * @author Carl Samson
 *
 */


public class ManufacturerPopulator extends AbstractDataPopulator<com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer,ManufacturerEntity>
{



	
	@Override
	public ManufacturerEntity populate(
			com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer source,
			ManufacturerEntity target, MerchantStore store, Language language) throws ConversionException {
		target.setId(source.getId());
		if(source.getDescriptions()!=null && source.getDescriptions().size()>0) {
			
				Set<ManufacturerDescription> descriptions = source.getDescriptions();
				ManufacturerDescription description = null;
				for(ManufacturerDescription desc : descriptions) {
					if(desc.getLanguage().getCode().equals(language.getCode())) {
						description = desc;
						break;
					}
				}
				
				if (description != null) {
					target.setName(description.getName());
				}

		}

		return target;
	}

    @Override
    protected ManufacturerEntity createTarget()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
