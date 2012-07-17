package com.salesmanager.core.business.reference.country.service;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.reference.country.dao.CountryDao;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.model.CountryDescription;
import com.salesmanager.core.business.reference.country.model.Country_;
import com.salesmanager.core.business.reference.language.model.Language;

@Service("countryService")
public class CountryServiceImpl extends SalesManagerEntityServiceImpl<Integer, Country>
		implements CountryService {
	
	private CountryDao countryDao;
	
	@Autowired
	public CountryServiceImpl(CountryDao countryDao) {
		super(countryDao);
		this.countryDao = countryDao;
	}
	
	public Country getByCode(String code) throws ServiceException {
		return countryDao.getByField(Country_.isoCode, code);
	}

	@Override
	public void addCountryDescription(Country country, CountryDescription description) throws ServiceException {
		country.getDescriptions().add(description);
		description.setCountry(country);
		update(country);
	}
	
	@Override
	@Cacheable(value = { "countries" })
	public List<Country> getCountries(Language language) throws ServiceException {
		
		List<Country> countries = countryDao.listByLanguage(language);
		
		//set names
		for(Country country : countries) {
			
			CountryDescription description = country.getDescriptions().get(0);
			country.setName(description.getName());
			
		}
		
		return countries;
		
		
	}

}
