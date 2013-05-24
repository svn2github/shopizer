package com.salesmanager.core.business.content.model.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public class InputContentImage extends ImageContentFile {

	
	private ByteArrayOutputStream file;
	private boolean defaultImage;
	
	public boolean isDefaultImage() {
		return defaultImage;
	}


	public void setDefaultImage(boolean defaultImage) {
		this.defaultImage = defaultImage;
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
