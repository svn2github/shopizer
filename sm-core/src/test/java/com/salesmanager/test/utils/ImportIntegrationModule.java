package com.salesmanager.test.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.business.system.service.ModuleConfigurationService;
import com.salesmanager.core.utils.reference.IntegrationModulesLoader;




@ContextConfiguration( locations = { "classpath:spring/test-spring-context.xml" } )
@RunWith( SpringJUnit4ClassRunner.class )
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class } )
public class ImportIntegrationModule  {

	@Autowired
	private IntegrationModulesLoader integrationModulesLoader;
	
	
	@Autowired
	private ModuleConfigurationService moduleCongigurationService;
	
	/**
	 * Import an integration module 
	 * @throws Exception
	 */
	@Ignore
	public void importIntegrationModule() throws Exception {
		

			ObjectMapper mapper = new ObjectMapper();
			File file = new File("/Users/csamson777/Documents/workspace2/sm-core/src/main/resources/reference/integrationmodules.json");
			
			InputStream in = null;
			
			
			try {
				

				
				
	            in = new FileInputStream(file);

	            if(in==null) {
	            	throw new Exception("File not found");
	            }
	            @SuppressWarnings("rawtypes")
	    		Map[] objects = mapper.readValue(in, Map[].class);
	            
	            IntegrationModule module = null;
	            //get the module to be loaded
	            for(int i = 0; i < objects.length; i++) {
	            	@SuppressWarnings("rawtypes")
					Map o = objects[i];
	            	//load that specific module
	            	if(o.get("code").equals("beanstream")) {
	            		//get module object
	            		module = integrationModulesLoader.loadModule(o);
	            		break;
	            	}
	            }
	            
	            if(module!=null) {
	            	IntegrationModule m = moduleCongigurationService.getByCode(module.getCode());
	            	if(m!=null) {
	            		moduleCongigurationService.delete(m);
	            	}
	            	
	            	moduleCongigurationService.create(module);
	            }

	  		} catch (Exception e) {
	  			throw new ServiceException(e);
	  		} finally {
	  			if(in !=null) {
	  				try {
	  					in.close();
	  				} catch(Exception ignore) {}
	  			}
	  		}


		
	}

}
