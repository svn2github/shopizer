package com.salesmanager.core.business.content.model.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public class InputContentImage extends ContentImage {

	private ImageContentType contentType;
	private ByteArrayOutputStream file;
	
	public InputContentImage(ImageContentType contentType) {
		this.setContentType(contentType);
	}
	
	//private InputStream file;
/*	public InputStream getFile() {
		return file;
	}
	public void setFile(InputStream file) {
		this.file = file;
	}*/
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

	public ByteArrayOutputStream getFile() {
		return file;
	}

	public void setFile(ByteArrayOutputStream file) {
		this.file = file;
	}

	private BufferedImage bufferedImage;

}
