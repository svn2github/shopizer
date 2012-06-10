package com.salesmanager.core.business.merchant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.dao.StoreBrandingDao;
import com.salesmanager.core.business.merchant.model.StoreBranding;

@Service("storeBrandingService")
public class StoreBrandingServiceImpl extends
		SalesManagerEntityServiceImpl<Long, StoreBranding> implements
		StoreBrandingService {

	@Autowired
	public StoreBrandingServiceImpl(StoreBrandingDao storeBrandingDao) {
		super(storeBrandingDao);
	}

}
