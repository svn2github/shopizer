package com.salesmanager.core.business.reference.country.service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.model.CountryDescription;

public interface CountryService extends SalesManagerEntityService<Integer, Country> {

	public Country getByCode(String code) throws ServiceException;
	
	public void addCountryDescription(Country country, CountryDescription description) throws ServiceException;
}
