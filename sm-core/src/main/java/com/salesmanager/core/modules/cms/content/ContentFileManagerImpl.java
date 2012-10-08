package com.salesmanager.core.modules.cms.content;

import java.util.List;

import com.salesmanager.core.business.content.model.image.ContentImage;
import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.content.model.image.InputContentImage;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.modules.cms.common.ImageGet;
import com.salesmanager.core.modules.cms.common.ImageRemove;
import com.salesmanager.core.modules.cms.product.ProductImageGet;
import com.salesmanager.core.modules.cms.product.ProductImagePut;
import com.salesmanager.core.modules.cms.product.ProductImageRemove;

public class ContentFileManagerImpl extends ContentFileManager {
	
	
	private ImagePut uploadImage;
	private ImageGet getImage;
	private ImageRemove removeImage;

	@Override
	public void addImage(MerchantStore store, InputContentImage image)
			throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<OutputContentImage> getImages(MerchantStore store, ImageContentType imageContentType)
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
	public OutputContentImage getImage(MerchantStore store, String imageName, ImageContentType imageContentType) {
		// TODO Auto-generated method stub
		return null;
	}

	public ImageRemove getRemoveImage() {
		return removeImage;
	}

	public void setRemoveImage(ImageRemove removeImage) {
		this.removeImage = removeImage;
	}

	public ImageGet getGetImage() {
		return getImage;
	}

	public void setGetImage(ImageGet getImage) {
		this.getImage = getImage;
	}

	public ImagePut getUploadImage() {
		return uploadImage;
	}

	public void setUploadImage(ImagePut uploadImage) {
		this.uploadImage = uploadImage;
	}

	@Override
	public List<String> getImageNames(MerchantStore store,
			ImageContentType imageContentType) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
