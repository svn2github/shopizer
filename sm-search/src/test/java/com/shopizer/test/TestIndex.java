package com.shopizer.test;

import javax.inject.Inject;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.shopizer.search.services.SearchService;


@ContextConfiguration(locations = {
		"classpath:spring/spring-context-test.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
})


public class TestIndex {
	
	@Inject
	private SearchService searchService;
	
	@Test
	public void testIndex() throws Exception {
		
		String jsonData = "{\"id\":\"3\",\"name\":\"Spring in action\",\"price\":\"23.99\",\"categories\":[\"book\",\"technology\"],\"store\":\"default\",\"availability\":\"*\",\"available\":\"true\",\"lang\":\"en\",\"description\":\"Best spring book, covers Spring MVC and Spring security\", \"tags\":[\"Spring\",\"Security\",\"Spring MVC\",\"Web\"]}";
		
		searchService.index(jsonData, "product_en_default", "product_en");
		
		System.out.println("Done !");
		
	}

}
