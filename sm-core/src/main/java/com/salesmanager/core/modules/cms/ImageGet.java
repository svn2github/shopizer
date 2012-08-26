package com.salesmanager.core.modules.cms;

import java.util.List;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;

public interface ImageGet {
	
	
	

	public List<OutputContentImage> getImages(MerchantStore store) throws ServiceException;

}
