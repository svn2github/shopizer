package com.salesmanager.core.business.content.model.image;

import java.awt.image.BufferedImage;
import java.io.InputStream;

public class InputContentImage extends ContentImage {

	private ImageContentType contentType;
	
	public InputContentImage(ImageContentType contentType) {
		this.setContentType(contentType);
	}
	
	private InputStream file;
	public InputStream getFile() {
		return file;
	}
	public void setFile(InputStream file) {
		this.file = file;
	}
	public ImageContentType getContentType() {
		return contentType;
	}
	public void setContentType(ImageContentType contentType) {
		this.contentType = contentType;
	}
	
	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}
	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}

	private BufferedImage bufferedImage;

}
