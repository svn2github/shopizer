package com.salesmanager.web.populator.customer;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.business.common.model.Billing;
import com.salesmanager.core.business.common.model.Delivery;
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
		
		//TODO complete
		try {
			
			//target.setAnonymous(anonymous)
			//target.setAttributes(attributes)
			
			target.setPassword(source.getPassword());
			target.setEmailAddress(source.getEmailAddress());
			target.setNick(source.getUserName());
			target.setFirstname(source.getFirstName());
			target.setLastname(source.getLastName());


			Map<String,Country> countries = countryService.getCountriesMap(language);
			
			
			Address sourceBilling = source.getBilling();
			if(sourceBilling!=null) {
				Billing billing = new Billing();
				billing.setAddress(sourceBilling.getAddress());
				billing.setCity(sourceBilling.getCity());
				billing.setCompany(sourceBilling.getCompany());
				//billing.setCountry(country);
				billing.setName(sourceBilling.getName());
				billing.setTelephone(source.getPhone());
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
			
			Address sourceShipping = source.getDelivery();
			if(sourceShipping!=null) {
				Delivery delivery = new Delivery();
				delivery.setAddress(sourceShipping.getAddress());
				delivery.setCity(sourceShipping.getCity());
				delivery.setCompany(sourceShipping.getCompany());
				delivery.setName(sourceShipping.getName());
				delivery.setTelephone(sourceShipping.getPhone());
				delivery.setPostalCode(sourceShipping.getPostalCode());
				delivery.setState(sourceShipping.getStateProvince());
				Country deliveryCountry = null;
				if(!StringUtils.isBlank(sourceShipping.getCountry())) {
					deliveryCountry = countries.get(sourceShipping.getCountry());
					if(deliveryCountry==null) {
						throw new ConversionException("Unsuported country code " + sourceShipping.getCountry());
					}
					delivery.setCountry(deliveryCountry);
				}
				
				if(deliveryCountry!=null && !StringUtils.isBlank(sourceShipping.getZone())) {
					Zone zone = zoneService.getByCode(sourceShipping.getZone());
					if(zone==null) {
						throw new ConversionException("Unsuported zone code " + sourceShipping.getZone());
					}
					delivery.setZone(zone);
				}
				target.setDelivery(delivery);
			}

		
		} catch (Exception e) {
			throw new ConversionException(e);
		}
		
		
		
		
		return target;
	}

	@Override
	protected Customer createTarget() {
		return null;
	}

}
