package com.salesmanager.core.utils;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.salesmanager.core.modules.integration.IntegrationException;

public class ModuleUtils {
	
	/**
	 * Retrieves modules configured in shopizer-core-modules.xml
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static Object getModule(String name) throws IntegrationException {
		
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
		        new String[] {"shopizer-core-modules.xml"});
		BeanFactory factory = (BeanFactory) appContext;

		return factory.getBean(name);
		
	}

}
