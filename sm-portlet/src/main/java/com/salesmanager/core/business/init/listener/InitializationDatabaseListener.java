package com.salesmanager.core.business.init.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.init.service.InitializationDatabase;

@Component
public class InitializationDatabaseListener implements ApplicationListener<ContextRefreshedEvent> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationDatabaseListener.class);
	
	@Autowired
	private InitializationDatabase database;
	
	public void onApplicationEvent(ContextRefreshedEvent event) {
		String name = event.getApplicationContext().getDisplayName();
		if (database.isEmpty()) {
			LOGGER.info(String.format("%s : Shopizer database is empty, populate it....", name));
			try {
				database.populate(name);
			} catch (ServiceException e) {
				LOGGER.error("Initialization Database failed!!!!");
			}
		}
	}
}
