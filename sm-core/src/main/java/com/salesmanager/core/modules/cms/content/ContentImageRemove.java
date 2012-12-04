package com.salesmanager.core.modules.cms.content;

import com.salesmanager.core.business.content.model.image.ContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.modules.cms.common.ImageRemove;

public interface ContentImageRemove extends ImageRemove {
	
	
	
	public void removeImage(final Integer merchantStoreId,final ContentImage contentImage) throws ServiceException;

}
