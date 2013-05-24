package com.salesmanager.core.modules.cms.content;

import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.modules.cms.common.ImageRemove;

public interface ContentImageRemove extends ImageRemove {
	
	
	
	public void removeImage(final String merchantStoreCode,final ImageContentType imageContentType, final String imageName) throws ServiceException;

}
