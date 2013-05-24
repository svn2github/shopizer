package com.salesmanager.core.business.content.model.image;

import java.io.ByteArrayOutputStream;

public class OutputContentImage extends ImageContentFile {
	
	private ByteArrayOutputStream image;
	
	public ByteArrayOutputStream getImage() {
		return image;
	}
	public void setImage(ByteArrayOutputStream image) {
		this.image = image;
	}

}
