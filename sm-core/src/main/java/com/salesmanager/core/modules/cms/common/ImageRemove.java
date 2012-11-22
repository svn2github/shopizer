package com.salesmanager.core.modules.cms.common;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;


public interface ImageRemove {
	
	
	public void removeImages(MerchantStore store) throws ServiceException;
	
}
