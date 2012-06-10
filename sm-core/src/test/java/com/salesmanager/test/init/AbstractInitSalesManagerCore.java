package com.salesmanager.test.init;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.EntityType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.util.EntityManagerUtils;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.model.CountryDescription;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.currency.service.CurrencyService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.test.core.SalesManagerCoreTestExecutionListener;

@ContextConfiguration(locations = {
		"classpath:spring/test-spring-context.xml"
})
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
	SalesManagerCoreTestExecutionListener.class
})
@RunWith(SpringJUnit4ClassRunner.class)
public class AbstractInitSalesManagerCore {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractInitSalesManagerCore.class);
	
	@Autowired
	private EntityManagerUtils entityManagerUtils;
	
	@Autowired
	protected LanguageService languageService;
	
	@Autowired
	protected CountryService countryService;
	
	@Autowired
	protected ZoneService zoneService;
	
	@Autowired
	protected CurrencyService currencyService;
	
	@Before
	public void clean() throws ServiceException {
		cleanAll();
		checkEmptyDatabase();
	}
	
	@Test
	public void init() throws ServiceException {
		initLanguages();
		initCountries();
		initCurrencies();
	}
	
	public void initCountries() throws ServiceException {
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
	
	public void initLanguages() throws ServiceException {
		for(String code : SchemaConstant.LANGUAGE_ISO_CODE) {
			Language language = new Language(code);
			languageService.create(language);
		}
	}
	
	private void initCurrencies() throws ServiceException {
		for (String code : SchemaConstant.CURRENCY_MAP.keySet()) {
			Currency currency = new Currency();
			try {
				currency.setCurrency(java.util.Currency.getInstance(code));
				currency.setName(SchemaConstant.CURRENCY_MAP.get(code));
				currencyService.create(currency);
			} catch (IllegalArgumentException e) {
				LOGGER.info("----------------- bad currency code" + code);
			}
			
		}
	}

	private void cleanAll() throws ServiceException {
		cleanCurrency();
		cleanCountry();
		cleanLanguage();
		
	}
	
	private void cleanCurrency() throws ServiceException {
		for(Currency currency : currencyService.list()) {
			currencyService.delete(currency);
		}
	}

	private void cleanCountry() throws ServiceException {
		for(Country country : countryService.list()) {
			countryService.delete(country);
		}
	}

	private void cleanLanguage() throws ServiceException {
		List<Language> languages = languageService.list();
		for (Language language : languages) {
			languageService.delete(language);
		}
	}

	private void checkEmptyDatabase() {
		Set<EntityType<?>> entityTypes = getEntityManager().getEntityManagerFactory().getMetamodel().getEntities();
		for (EntityType<?> entityType : entityTypes) {
			List<?> entities = listEntities(entityType.getBindableJavaType());
			
			if (entities.size() > 0) {
				Assert.fail(String.format("Remaining objects of type %1$s ", entities.get(0).getClass().getSimpleName()));
			}
		}
	}
	
	protected <E> List<E> listEntities(Class<E> clazz) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		cq.from(clazz);
		
		return entityManagerUtils.getEntityManager().createQuery(cq).getResultList();
	}
	
	private EntityManager getEntityManager() {
		return entityManagerUtils.getEntityManager();
	}
}
