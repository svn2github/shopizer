package com.salesmanager.test.utils;



import org.jboss.cache.CacheFactory;
import org.jboss.cache.DefaultCacheFactory;
import org.jboss.cache.Fqn;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.utils.CacheUtils;
import com.salesmanager.test.core.SalesManagerCoreTestExecutionListener;

import java.util.List;

@ContextConfiguration(locations = { "classpath:spring/test-spring-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, SalesManagerCoreTestExecutionListener.class })
public class UtilsTestCase {
	
	
	@Autowired
	private CountryService countryService;
	
	@Test
	public void testCache() throws Exception {
		
		@SuppressWarnings("rawtypes")
		List countries = countryService.list();
		


		
		CacheUtils cache = CacheUtils.getInstance();
		cache.putInCache(countries, "COUNTRIES");
		
		@SuppressWarnings("rawtypes")
		List objects = (List) cache.getFromCache("COUNTRIES");
		
		Assert.assertNotNull(objects);
		
	}

}
