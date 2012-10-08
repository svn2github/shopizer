package com.salesmanager.core.modules.cms.content;

import java.util.List;

import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.modules.cms.common.ImageGet;

public interface ContentImageGet extends ImageGet {
	
	public OutputContentImage getImage(MerchantStore store, String imageName,ImageContentType imageContentType) throws ServiceException;
	public List<String> getImageNames(MerchantStore store, ImageContentType imageContentType) throws ServiceException;

}
