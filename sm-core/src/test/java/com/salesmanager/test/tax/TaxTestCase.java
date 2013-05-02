package com.salesmanager.test.tax;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.tax.model.TaxBasisCalculation;
import com.salesmanager.core.business.tax.model.TaxConfiguration;
import com.salesmanager.core.business.tax.service.TaxRateService;
import com.salesmanager.core.business.tax.service.TaxService;
import com.salesmanager.test.core.AbstractSalesManagerCoreTestCase;

public class TaxTestCase extends AbstractSalesManagerCoreTestCase {
	
	private static final Date date = new Date(System.currentTimeMillis());
	
	@Autowired
	private TaxService taxService;
	
	@Autowired
	private TaxRateService taxRateService;
	
	/**
	 * Test tax calculation
	 * @throws ServiceException
	 */
	@Test
	public void testGetShippingQuotes() throws ServiceException {

	    Language en = languageService.getByCode("en");
	    Country country = countryService.getByCode("CA");
	    Zone zone = zoneService.getByCode("QC");

	    MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
	    
	    //create tax configuration based on store location
	    
	    TaxConfiguration taxConfiguration = new TaxConfiguration();
	    taxConfiguration.setTaxBasisCalculation(TaxBasisCalculation.STOREADDRESS);
	    
	    taxService.saveTaxConfiguration(taxConfiguration, store);
	    
	    //tax on shipping
	    
	}



}