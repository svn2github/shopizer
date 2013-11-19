package com.salesmanager.web.populator.customer;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.AbstractDataPopulator;
import com.salesmanager.web.entity.customer.Address;
import com.salesmanager.web.entity.customer.PersistableCustomer;

public class PersistableCustomerPopulator extends
		AbstractDataPopulator<Customer, PersistableCustomer> {

	@Override
	public PersistableCustomer populate(Customer source,
			PersistableCustomer target, MerchantStore store, Language language)
			throws ConversionException {

		
		try {
			

			if(source.getBilling()!=null) {
				Address address = new Address();
				address.setCity(source.getBilling().getCity());
				address.setCompany(source.getBilling().getCompany());
				address.setName(source.getBilling().getName());
				address.setPostalCode(source.getBilling().getPostalCode());
				if(source.getBilling().getCountry()!=null) {
					address.setCountry(source.getBilling().getCountry().getIsoCode());
				}
				if(source.getBilling().getZone()!=null) {
					address.setZone(source.getBilling().getZone().getCode());
				}
				
				target.setBilling(address);
			}
			
			if(source.getDelivery()!=null) {
				Address address = new Address();
				address.setCity(source.getDelivery().getCity());
				address.setCompany(source.getDelivery().getCompany());
				address.setName(source.getDelivery().getName());
				address.setPostalCode(source.getDelivery().getPostalCode());
				if(source.getDelivery().getCountry()!=null) {
					address.setCountry(source.getDelivery().getCountry().getIsoCode());
				}
				if(source.getDelivery().getZone()!=null) {
					address.setZone(source.getDelivery().getZone().getCode());
				}
				
				target.setDelivery(address);
			}
			
			target.setId(source.getId());
			target.setEmailAddress(source.getEmailAddress());
			target.setGender(source.getGender().name());
			target.setLanguage(source.getDefaultLanguage().getCode());
			target.setUserName(source.getNick());
			target.setFirstName(source.getFirstname());
			target.setLastName(source.getLastname());
			target.setPhone(source.getTelephone());
			target.setStoreCode(source.getMerchantStore().getCode());
			
			
			
		} catch (Exception e) {
			throw new ConversionException(e);
		}
			
		return target;
		
	}

	@Override
	protected PersistableCustomer createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
