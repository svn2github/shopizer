package com.salesmanager.core.business.content.model.image;


public abstract class ContentImage {
	
	
	
	private boolean defaultImage;
	private ImageContentType imageContentType;
	
	
	private String imageName;
	private String fileContentType;

    public String getImageContentType() {
		return fileContentType;
	}
	public void setImageContentType(String imageContentType) {
		this.fileContentType = imageContentType;
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
		t.append("imageName : ").append(imageName).append(" contentType : ").append(fileContentType)
		.append(" defaultImage : ").append(defaultImage);
		
		return t.toString();
		
	}
	public void setContentType(ImageContentType contentType) {
		this.imageContentType = contentType;
	}
	public ImageContentType getContentType() {
		return imageContentType;
	}

}
