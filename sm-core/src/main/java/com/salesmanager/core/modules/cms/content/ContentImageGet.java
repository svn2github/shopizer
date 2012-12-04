package com.salesmanager.core.modules.cms.content;

import java.util.List;

import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.modules.cms.common.ImageGet;

public interface ContentImageGet extends ImageGet {
	
	public OutputContentImage getImage(final Integer merchantStoreId, String imageName,ImageContentType imageContentType) throws ServiceException;
	public List<String> getImageNames(final Integer merchantStoreId, ImageContentType imageContentType) throws ServiceException;

}
