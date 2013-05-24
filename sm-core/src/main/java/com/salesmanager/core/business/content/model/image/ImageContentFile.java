package com.salesmanager.core.business.content.model.image;

import com.salesmanager.core.business.content.model.content.ContentFile;

public abstract class ImageContentFile extends ContentFile {
	
	private ImageContentType imageContentType;

	public void setImageContentType(ImageContentType imageContentType) {
		this.imageContentType = imageContentType;
	}

	public ImageContentType getImageContentType() {
		return imageContentType;
	}

}
