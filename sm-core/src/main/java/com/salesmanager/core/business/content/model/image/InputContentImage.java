package com.salesmanager.core.business.content.model.image;

import java.io.File;

public class InputContentImage extends ContentImage {

	private ImageContentType contentType;
	
	public InputContentImage(ImageContentType contentType) {
		this.setContentType(contentType);
	}
	
	private File file;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public ImageContentType getContentType() {
		return contentType;
	}
	public void setContentType(ImageContentType contentType) {
		this.contentType = contentType;
	}

}
