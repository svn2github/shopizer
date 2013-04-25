package com.salesmanager.test.payment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;


@ContextConfiguration(locations = {
		"classpath:spring/test-spring-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class
})
public class AuthorizeNetPaymentTestCase {
	
	@Test
	public void testAuthorize() {
		System.out.println("Hello world");
	}
	
	public void testAuthorizeAndCapture() {
		
	}
	
	public void testRefund() {
		
	}

}
