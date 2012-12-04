package com.salesmanager.core.modules.cms.content;

import java.util.List;

import com.salesmanager.core.business.content.model.image.InputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;

public interface ImagePut {
	
	
	public void addImage(final Integer merchantStoreId, InputContentImage image) throws ServiceException;
	public void addImages(final Integer merchantStoreId, List<InputContentImage> imagesList) throws ServiceException;

}
