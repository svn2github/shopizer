package com.salesmanager.core.business.merchant.dao;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.StoreBranding;

@Repository("storeBrandingDao")
public class StoreBrandingDaoImpl extends SalesManagerEntityDaoImpl<Long, StoreBranding> implements StoreBrandingDao{

	
	public StoreBrandingDaoImpl() {
		super();
	}
	
}
