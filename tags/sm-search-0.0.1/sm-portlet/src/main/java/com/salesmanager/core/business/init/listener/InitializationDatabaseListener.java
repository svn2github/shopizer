package com.salesmanager.core.business.init.listener;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.init.data.InitStoreData;
import com.salesmanager.core.business.reference.init.service.InitializationDatabase;
import com.salesmanager.core.business.system.model.SystemConfiguration;
import com.salesmanager.core.business.system.service.SystemConfigurationService;
import com.salesmanager.core.business.utils.AppConfiguration;
import com.salesmanager.core.constants.SystemConstants;
import com.salesmanager.portlet.constants.ApplicationConstants;

@Component
public class InitializationDatabaseListener implements ApplicationListener<ContextRefreshedEvent> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationDatabaseListener.class);
	
	@Autowired
	private InitializationDatabase database;
	
	@Autowired
	private InitStoreData initStoreData;
	
	@Autowired
	private SystemConfigurationService systemConfigurationService;
	
	@Autowired
	private AppConfiguration appConfiguration;
	
	public void onApplicationEvent(ContextRefreshedEvent event) {
		String name = event.getApplicationContext().getDisplayName();
		if (database.isEmpty()) {
			LOGGER.info(String.format("%s : Shopizer database is empty, populate it....", name));
			try {
				database.populate(name);
				loadData();
			} catch (ServiceException e) {
				LOGGER.error("Initialization Database failed!!!!");
			}
			
			
		}
	}
	
	private void loadData() throws ServiceException {
		LOGGER.info(String.format("%s : Checking configuration "));
		
		List<SystemConfiguration> configurations = systemConfigurationService.list();
		
		
		String loadTestData = appConfiguration.getProperty(ApplicationConstants.POPULATE_TEST_DATA);
		boolean loadData =  !StringUtils.isBlank(loadTestData) && loadTestData.equals(SystemConstants.CONFIG_VALUE_TRUE);
		
		
		if(loadData) {
			
			SystemConfiguration configuration = systemConfigurationService.getByKey(ApplicationConstants.TEST_DATA_LOADED);
		
			if(configuration!=null) {
					if(configuration.getKey().equals(ApplicationConstants.TEST_DATA_LOADED)) {
						if(configuration.getValue().equals(SystemConstants.CONFIG_VALUE_TRUE)) {
							return;		
						}
					}		
			}
			
			initStoreData.initInitialData();
			
			configuration = new SystemConfiguration();
			configuration.getAuditSection().setModifiedBy(SystemConstants.SYSTEM_USER);
			configuration.setKey(ApplicationConstants.TEST_DATA_LOADED);
			configuration.setValue(SystemConstants.CONFIG_VALUE_TRUE);
			systemConfigurationService.create(configuration);
			
			
		}
	}
}
