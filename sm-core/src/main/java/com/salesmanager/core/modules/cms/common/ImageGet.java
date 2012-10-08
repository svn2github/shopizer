package com.salesmanager.core.modules.cms.common;

import java.util.List;

import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;

public interface ImageGet {
	
	
	

	public List<OutputContentImage> getImages(MerchantStore store, ImageContentType imageContentType)
			throws ServiceException;

}
