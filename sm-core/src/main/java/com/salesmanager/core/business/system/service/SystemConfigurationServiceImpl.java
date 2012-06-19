package com.salesmanager.core.business.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.system.dao.SystemConfigurationDao;
import com.salesmanager.core.business.system.model.SystemConfiguration;

@Service("systemConfigurationService")
public class SystemConfigurationServiceImpl extends
		SalesManagerEntityServiceImpl<Long, SystemConfiguration> implements
		SystemConfigurationService {

	
	private SystemConfigurationDao systemConfigurationDao;
	
	@Autowired
	public SystemConfigurationServiceImpl(
			SystemConfigurationDao systemConfigurationDao) {
			super(systemConfigurationDao);
			this.systemConfigurationDao = systemConfigurationDao;
	}
	



}
