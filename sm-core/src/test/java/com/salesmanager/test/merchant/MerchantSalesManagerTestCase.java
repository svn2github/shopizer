package com.salesmanager.test.merchant;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.model.StoreBranding;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.model.CountryDescription;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.test.core.AbstractSalesManagerCoreTestCase;

public class MerchantSalesManagerTestCase extends AbstractSalesManagerCoreTestCase {
	
	private static final String CURRENCY_CODE = "EUR";
	
	@Autowired
	private MerchantStoreService merchantStoreService;
	
	private Date date = new Date(System.currentTimeMillis());

	@Before
	public void initialize() throws ServiceException {

		//create a language
		Language en = new Language();
		en.setCode("en");
		languageService.create(en);
		
		Language fr = new Language();
		fr.setCode("fr");
		languageService.create(fr);
		
		// Create currency
		Currency currency = new Currency();
		currency.setCurrency(java.util.Currency.getInstance(CURRENCY_CODE));
		currencyService.create(currency);
		
		//create country
		Country ca = new Country();
		ca.setIsoCode("CA");
		
		CountryDescription caden = new CountryDescription();
		caden.setCountry(ca);
		caden.setLanguage(en);
		caden.setName("Canada");
		caden.setDescription("Canada Country");
		
		CountryDescription cadfr = new CountryDescription();
		cadfr.setCountry(ca);
		cadfr.setLanguage(fr);
		cadfr.setName("Canada");
		cadfr.setDescription("Pays Canada");
		
		List<CountryDescription> descriptionsca = new ArrayList<CountryDescription>();
		descriptionsca.add(caden);
		descriptionsca.add(cadfr);
		ca.setDescriptions(descriptionsca);
		
		countryService.create(ca);
		
		Country us = new Country();
		us.setIsoCode("US");
		
		CountryDescription usen = new CountryDescription();
		usen.setCountry(us);
		usen.setLanguage(en);
		usen.setName("United States");
		usen.setDescription("USA Country");
		
		CountryDescription usfr = new CountryDescription();
		usfr.setCountry(us);
		usfr.setLanguage(fr);
		usfr.setName("États-Unis");
		usfr.setDescription("Pays États-Unis");
		
		List<CountryDescription> descriptionsus = new ArrayList<CountryDescription>();
		descriptionsus.add(usen);
		descriptionsus.add(usfr);
		us.setDescriptions(descriptionsus);
		
		countryService.create(us);

	}
	
	@Test
	public void testMerchant() throws Exception {
		System.out.println("Ended the test");
		
		//create a merchant
		Country country = super.countryService.getByCode("CA");
		Language lang = super.languageService.getByCode("en");
		List<Language> langs = super.languageService.list();
		
		MerchantStore store = new MerchantStore();
		store.setCountry(country);
		store.setCurrency(currencyService.getByCode(CURRENCY_CODE));
		store.setDefaultLanguage(lang);
		store.setInBusinessSince(date);
		store.setStorename("store name");
		store.setLanguages(langs);
		
		merchantStoreService.create(store);

		StoreBranding branding = new StoreBranding();
		branding.setDomainName("www.branding1.com");
		branding.setStoreEmailAddress("store@email.com");
		branding.setMerchantSore(store);
		
		storeBrandingService.create(branding);
		
		store.setBranding(branding);
		store.setStorecity("test 2");
		merchantStoreService.update(store);

		store = merchantStoreService.getById(store.getId());
		System.out.println(store.getId());
		// TODO : replace by Assert 
		System.out.println(store.getBranding().getDomainName());
		System.out.println(store.getDefaultLanguage().getId());
		System.out.println(store.getLanguages().size());

	}

}
