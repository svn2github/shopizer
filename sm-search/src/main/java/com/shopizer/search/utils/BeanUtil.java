package com.shopizer.search.utils;

import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.context.ApplicationContext;

public class BeanUtil {
	

	private BeanFactoryReference bf = null;

	private static BeanUtil instance = null;

/*	public static Object getBean(String name) throws RuntimeException {

		if (instance == null) {
			instance = new BeanUtil();
		}

		try {
			Object o = instance.getApplicationContext().getBean(name);
			return o;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}*/

	private ApplicationContext getApplicationContext() {
		if (bf == null) {
			BeanFactoryLocator bfl = SingletonBeanFactoryLocator.getInstance();
			bf = bfl.useBeanFactory("personalization");
		}
		return (ApplicationContext) bf.getFactory();
	}

}
