package com.salesmanager.core.business.system.service;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.system.dao.MerchantConfigurationDao;
import com.salesmanager.core.business.system.model.MerchantConfiguration;
import com.salesmanager.core.business.system.model.MerchantConfigurationType;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.modules.email.EmailConfig;

@Service("merchantConfigurationService")
public class MerchantConfigurationServiceImpl extends
		SalesManagerEntityServiceImpl<Long, MerchantConfiguration> implements
		MerchantConfigurationService {

	private MerchantConfigurationDao merchantConfigurationDao;
	
	@Autowired
	public MerchantConfigurationServiceImpl(
			MerchantConfigurationDao merchantConfigurationDao) {
			super(merchantConfigurationDao);
			this.merchantConfigurationDao = merchantConfigurationDao;
	}
	

	@Override
	public MerchantConfiguration getMerchantConfiguration(String key, MerchantStore store) throws ServiceException {
		return merchantConfigurationDao.getMerchantConfiguration(key, store);
	}
	
	@Override
	public EmailConfig getMerchantEmailConfiguration(MerchantStore store) throws ServiceException {
		
		MerchantConfiguration configuration = getMerchantConfiguration(Constants.EMAIL_CONFIG, store);
		EmailConfig emailConfig = null;
		if(configuration!=null) {
			String value = configuration.getValue();
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				emailConfig = mapper.readValue(value, EmailConfig.class);
			} catch(Exception e) {
				throw new ServiceException("Cannot parse json string " + value);
			}
		}
		return emailConfig;
	}
	
	@Override
	public void saveMerchantEmailConfiguration(EmailConfig emailConfig, MerchantStore store) throws ServiceException {
		MerchantConfiguration configuration = getMerchantConfiguration(Constants.EMAIL_CONFIG, store);
		if(configuration==null) {
			configuration = new MerchantConfiguration();
			configuration.setMerchantStore(store);
			configuration.setKey(Constants.EMAIL_CONFIG);
		}
		
		String value = emailConfig.toJSONString();
		configuration.setValue(value);
		saveOrUpdate(configuration);
	}
	
	@Override
	public List<MerchantConfiguration> listByStore(MerchantStore store) throws ServiceException {
		return merchantConfigurationDao.getMerchantConfigurations(store);
	}
	
	@Override
	public List<MerchantConfiguration> listByType(MerchantConfigurationType type, MerchantStore store) throws ServiceException {
		return merchantConfigurationDao.listByType(type, store);
	}
	
	@Override
	public void saveOrUpdate(MerchantConfiguration entity) throws ServiceException {
		

		
		if(entity.getId()!=null && entity.getId()>0) {
			super.update(entity);
		} else {
			super.create(entity);

		}
	}
	
	
	@Override
	public void delete(MerchantConfiguration merchantConfiguration) throws ServiceException {
		MerchantConfiguration config = merchantConfigurationDao.getById(merchantConfiguration.getId());
		if(config!=null) {
			super.delete(config);
		}
	}

}
