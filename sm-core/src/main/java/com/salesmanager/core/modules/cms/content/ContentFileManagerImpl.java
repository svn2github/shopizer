package com.salesmanager.core.modules.cms.content;

import java.util.List;

import com.salesmanager.core.business.content.model.image.ContentImage;
import com.salesmanager.core.business.content.model.image.InputContentImage;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;

public class ContentFileManagerImpl extends ContentFileManager {

	@Override
	public void addImage(MerchantStore store, InputContentImage image)
			throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<OutputContentImage> getImages(MerchantStore store)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeImage(ContentImage contentImage) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeImages(MerchantStore store) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public OutputContentImage getImage(MerchantStore store, String imageName) {
		// TODO Auto-generated method stub
		return null;
	}

}
