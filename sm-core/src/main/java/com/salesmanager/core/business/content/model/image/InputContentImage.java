package com.salesmanager.core.business.content.model.image;

import java.io.FileInputStream;

public class InputContentImage extends ContentImage {

	private ImageContentType contentType;
	
	public InputContentImage(ImageContentType contentType) {
		this.setContentType(contentType);
	}
	
	private FileInputStream file;
	
	public FileInputStream getFile() {
		return file;
	}
	public void setFile(FileInputStream file) {
		this.file = file;
	}
	public ImageContentType getContentType() {
		return contentType;
	}
	public void setContentType(ImageContentType contentType) {
		this.contentType = contentType;
	}

}
