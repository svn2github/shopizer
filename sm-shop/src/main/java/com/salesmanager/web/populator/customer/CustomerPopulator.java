package com.salesmanager.web.populator.customer;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.business.common.model.Billing;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.utils.AbstractDataPopulator;
import com.salesmanager.web.entity.customer.Address;
import com.salesmanager.web.entity.customer.PersistableCustomer;

public class CustomerPopulator extends
		AbstractDataPopulator<PersistableCustomer, Customer> {
	
	private CountryService countryService;
	private ZoneService zoneService;

	/**
	 * Creates a Customer entity ready to be saved
	 */
	@Override
	public Customer populate(PersistableCustomer source, Customer target,
			MerchantStore store, Language language) throws ConversionException {
		
		
		try {
			
			//target.setAnonymous(anonymous)
			//target.setAttributes(attributes)

			Map<String,Country> countries = countryService.getCountriesMap(language);
			
			
			Address sourceBilling = source.getBilling();
			if(sourceBilling!=null) {
				Billing billing = new Billing();
				billing.setAddress(sourceBilling.getAddress());
				billing.setCity(sourceBilling.getCity());
				billing.setCompany(sourceBilling.getCompany());
				//billing.setCountry(country);
				billing.setName(sourceBilling.getName());
				billing.setPostalCode(sourceBilling.getPostalCode());
				billing.setState(sourceBilling.getStateProvince());
				Country billingCountry = null;
				if(!StringUtils.isBlank(sourceBilling.getCountry())) {
					billingCountry = countries.get(sourceBilling.getCountry());
					if(billingCountry==null) {
						throw new ConversionException("Unsuported country code " + sourceBilling.getCountry());
					}
					billing.setCountry(billingCountry);
				}
				
				if(billingCountry!=null && !StringUtils.isBlank(sourceBilling.getZone())) {
					Zone zone = zoneService.getByCode(sourceBilling.getZone());
					if(zone==null) {
						throw new ConversionException("Unsuported zone code " + sourceBilling.getZone());
					}
					billing.setZone(zone);
				}
				
				target.setBilling(billing);
			}
			
			//the same for delivery
		
		} catch (Exception e) {
			throw new ConversionException(e);
		}
		
		
		
		
		return null;
	}

	@Override
	protected Customer createTarget() {
		return null;
	}

}
