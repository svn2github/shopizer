package com.salesmanager.core.modules.cms.content;

import com.salesmanager.core.business.content.model.image.ContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.modules.cms.common.ImageGet;

public interface ContentImageGet extends ImageGet {
	
	public ContentImage getImage(MerchantStore store, String imageName) throws ServiceException;

}
