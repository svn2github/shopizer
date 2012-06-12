package com.salesmanager.test.merchant;

import java.sql.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.test.core.AbstractSalesManagerCoreTestCase;

public class MerchantSalesManagerTestCase extends AbstractSalesManagerCoreTestCase {
	

	
	@Autowired
	private MerchantStoreService merchantStoreService;
	
	private Date date = new Date(System.currentTimeMillis());

	@Before
	public void initialize() throws ServiceException {


	}
	
	@Test
	public void createMerchant() throws Exception {

	
		//create a merchant
		Country country = super.countryService.getByCode("CA");
		Language lang = super.languageService.getByCode("en");
		List<Language> langs = super.languageService.list();
		

		
		MerchantStore store = new MerchantStore();
		store.setCountry(country);
		store.setCurrency(currencyService.getByCode("CAD"));
		store.setDefaultLanguage(lang);
		store.setInBusinessSince(date);
		store.setStorename("store name");
		store.setLanguages(langs);
		store.setStoreEmailAddress("test@test.com");

		merchantStoreService.create(store);



		store = merchantStoreService.getById(store.getId());
		
		Assert.assertTrue(store!=null);
		
		System.out.println(store.getId());
		System.out.println(store.getDomainName());
		System.out.println(store.getDefaultLanguage().getId());
		System.out.println(store.getLanguages().size());

	}

}
