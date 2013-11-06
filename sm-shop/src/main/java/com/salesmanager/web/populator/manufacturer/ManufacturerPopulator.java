
package com.salesmanager.web.populator.manufacturer;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.populator.AbstractDataPopulator;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.entity.catalog.Manufacturer;


/**
 * @author Carl Samson
 *
 */

@Service(value="manufacturerPopulator")
public class ManufacturerPopulator extends AbstractDataPopulator<com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer,com.salesmanager.web.entity.catalog.Manufacturer>
{

	@Override
	protected Manufacturer createTarget() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public com.salesmanager.web.entity.catalog.Manufacturer populate(com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer source,com.salesmanager.web.entity.catalog.Manufacturer target) {
		target.setId(source.getId());
		Language language = super.getLanguage();
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
		
		target.setLanguage(language.getCode());
		return target;
	}

}
