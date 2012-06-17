package com.salesmanager.core.business.init.service;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.model.CountryDescription;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.currency.service.CurrencyService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.system.model.SystemConfiguration;
import com.salesmanager.core.business.system.service.SystemConfigurationService;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.constants.SystemConstants;

@Service("initializationDatabase")
public class InitializationDatabaseImpl implements InitializationDatabase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationDatabaseImpl.class);
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private SystemConfigurationService systemConfigurationService;
	
	private String name;
	
	public boolean isEmpty() {
		return languageService.count() == 0;
	}
	
	@Transactional
	public void populate(String contextName) throws ServiceException {
		this.name =  contextName;
		
		createLanguages();
		createCountries();
		createCurrencies();
		//TODO need to create a default admin merchant store
		createSystemConfiguration();
	}

	private void createCurrencies() throws ServiceException {
		LOGGER.info(String.format("%s : Populating Currencies ", name));
		for (String code : SchemaConstant.CURRENCY_MAP.keySet()) {
			Currency currency = new Currency();
			try {
				currency.setCurrency(java.util.Currency.getInstance(code));
				currency.setName(SchemaConstant.CURRENCY_MAP.get(code));
				currencyService.create(currency);
			} catch (IllegalArgumentException e) {
				LOGGER.info(String.format("%s : Populating Currencies : bad code : %s", name, code));
			}
			
		}
	}

	private void createCountries() throws ServiceException {
		LOGGER.info(String.format("%s : Populating Countries ", name));
		for(String code : SchemaConstant.COUNTRY_ISO_CODE) {
			Locale locale = SchemaConstant.LOCALES.get(code);
			if (locale != null) {
				Country country = new Country(code);
				countryService.create(country);
				
				for (Language language : languageService.list()) {
					String name = locale.getDisplayCountry(new Locale(language.getCode()));
					CountryDescription description = new CountryDescription(language, name);
					countryService.addCountryDescription(country, description);
				}
				
				// TODO : add GEOZONE
				// TODO : add ZONES
			}
		}
	}

	private void createLanguages() throws ServiceException {
		LOGGER.info(String.format("%s : Populating Languages ", name));
		for(String code : SchemaConstant.LANGUAGE_ISO_CODE) {
			Language language = new Language(code);
			languageService.create(language);
		}
	}
	
	private void createSystemConfiguration() throws ServiceException {
		LOGGER.info(String.format("%s : Checking configuration ", name));
		
		List<SystemConfiguration> configurations = systemConfigurationService.list();
		
		if(configurations.size()==0) {
			//create flag populate data
			SystemConfiguration configuration = new SystemConfiguration();
			configuration.getAuditSection().setModifiedBy("SYSTEM");
			configuration.setKey(SystemConstants.POPULATE_TEST_DATA_KEY);
			configuration.setValue("false");
			systemConfigurationService.create(configuration);
		} else {
			for(SystemConfiguration config : configurations) {
				if(config.getKey().equals(SystemConstants.POPULATE_TEST_DATA_KEY)) {
					if(config.getValue().equals(SystemConstants.CONFIG_VALUE_TRUE)) {
						//populate
						createTestData();
						//set value to false
						config.setValue(SystemConstants.CONFIG_VALUE_FALSE);
						systemConfigurationService.save(config);		
					}
					break;
				}	
			}	
		}
	}
	
	private void createTestData() {
		
		
		try {
			
			//TODO Store, Categories, Products
			
		} catch (Exception e) {//don't raise the exception
			LOGGER.error("An exception occured while populating test data, you may want to erase the data before trying to pupulate", e);
		}
		
		
	}

}
