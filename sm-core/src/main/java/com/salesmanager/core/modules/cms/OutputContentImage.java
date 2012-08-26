package com.salesmanager.core.modules.cms;

import java.io.ByteArrayOutputStream;

public class OutputContentImage extends ContentImage {
	
	private ByteArrayOutputStream image;
	
	public ByteArrayOutputStream getImage() {
		return image;
	}
	public void setImage(ByteArrayOutputStream image) {
		this.image = image;
	}

}
