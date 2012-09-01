package com.salesmanager.core.modules.cms.common;


public abstract class ContentImage {
	
	
	
	private boolean defaultImage;
	
	
	
	private String imageName;
	private String imageContentType;

    public String getImageContentType() {
		return imageContentType;
	}
	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
	}

	public void setDefaultImage(boolean defaultImage) {
		this.defaultImage = defaultImage;
	}
	public boolean isDefaultImage() {
		return defaultImage;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getImageName() {
		return imageName;
	}
	
	public String toString() {
		
		StringBuilder t = new StringBuilder();
		t.append("imageName : ").append(imageName).append(" contentType : ").append(imageContentType)
		.append(" defaultImage : ").append(defaultImage);
		
		return t.toString();
		
	}

}
