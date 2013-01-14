package com.salesmanager.web.init.data;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.init.service.InitializationDatabase;
import com.salesmanager.core.business.system.model.SystemConfiguration;
import com.salesmanager.core.business.system.service.SystemConfigurationService;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.Permission;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.business.user.service.PermissionService;
import com.salesmanager.core.constants.SystemConstants;
import com.salesmanager.web.admin.security.UserServicesImpl;
import com.salesmanager.web.admin.security.WebUserServices;
import com.salesmanager.web.constants.ApplicationConstants;
import com.salesmanager.web.utils.AppConfiguration;



@Component
public class InitializationLoader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationLoader.class);
	
	@Autowired
	private AppConfiguration appConfiguration;

	
	@Autowired
	private InitializationDatabase initializationDatabase;
	
	@Autowired
	private com.salesmanager.web.init.data.InitStoreData initStoreData;
	
	@Autowired
	private SystemConfigurationService systemConfigurationService;
	
	@Autowired
	private WebUserServices userDetailsService;
	
	@Autowired
	protected PermissionService  permissionService;
	
	@Autowired
	protected GroupService   groupService;
	
	@PostConstruct
	//@Transactional
	public void init() {
		
		try {
			
			if (initializationDatabase.isEmpty()) {
				LOGGER.info(String.format("%s : Shopizer database is empty, populate it....", "sm-shop"));
		
				initializationDatabase.populate("sm-shop");
				
				
				
				//security groups and permissions

				  Group gsuperadmin = new Group("SUPERADMIN");
				  Group gadmin = new Group("ADMIN");
				  Group gcatalogue = new Group("ADMIN_CATALOGUE");
				  Group gstore = new Group("ADMIN_STORE");
				  Group gorder = new Group("ADMIN_ORDER");
				  Group gcontent = new Group("ADMIN_CONTENT");

				  groupService.create(gsuperadmin);
				  groupService.create(gadmin);
				  groupService.create(gcatalogue);
				  groupService.create(gstore);
				  groupService.create(gorder);
				  groupService.create(gcontent);
				  
				  Permission storeadmin = new Permission("STORE_ADMIN");//Administrator of the store
				  storeadmin.getGroups().add(gsuperadmin);
				  storeadmin.getGroups().add(gadmin);
				  permissionService.create(storeadmin);
				  
				  Permission superadmin = new Permission("SUPERADMIN");
				  superadmin.getGroups().add(gsuperadmin);
				  permissionService.create(superadmin);
				  
				  Permission auth = new Permission("AUTH");//Authenticated
				  auth.getGroups().add(gsuperadmin);
				  auth.getGroups().add(gadmin);
				  auth.getGroups().add(gcatalogue);
				  auth.getGroups().add(gstore);
				  auth.getGroups().add(gorder);
				  permissionService.create(auth);

				  
				  Permission products = new Permission("PRODUCTS");
				  products.getGroups().add(gsuperadmin);
				  products.getGroups().add(gadmin);
				  products.getGroups().add(gcatalogue);
				  permissionService.create(products);

				  
				  Permission order = new Permission("ORDER");
				  order.getGroups().add(gsuperadmin);
				  order.getGroups().add(gorder);
				  order.getGroups().add(gadmin);
				  permissionService.create(order);
				  
				  Permission content = new Permission("CONTENT");
				  content.getGroups().add(gsuperadmin);
				  content.getGroups().add(gadmin);
				  content.getGroups().add(gcontent);
				  permissionService.create(content);
				  
				  
				  
				  Permission pstore = new Permission("STORE");
				  pstore.getGroups().add(gsuperadmin);
				  pstore.getGroups().add(gstore);
				  pstore.getGroups().add(gadmin);
				  permissionService.create(pstore);
				  
				  Permission tax = new Permission("TAX");
				  tax.getGroups().add(gsuperadmin);
				  tax.getGroups().add(gstore);
				  tax.getGroups().add(gadmin);
				  permissionService.create(tax);
				  
				  
				  Permission payment = new Permission("PAYMENT");
				  payment.getGroups().add(gsuperadmin);
				  payment.getGroups().add(gstore);
				  payment.getGroups().add(gadmin);
				  permissionService.create(payment);
				  
				  
				  Permission shipping = new Permission("SHIPPING");
				  shipping.getGroups().add(gsuperadmin);
				  shipping.getGroups().add(gadmin);
				  shipping.getGroups().add(gstore);
				  
				  permissionService.create(shipping);
				
				
				
				  userDetailsService.createDefaultAdmin();
				  loadData();

			}
			
		} catch (Exception e) {
			LOGGER.error("Error in the init method",e);
		}
		

		
	}
	
	private void loadData() throws ServiceException {
		

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
