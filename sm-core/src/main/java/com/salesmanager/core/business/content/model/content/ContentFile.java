package com.salesmanager.core.business.content.model.content;

public abstract class ContentFile {
	
	
	private String fileName;
	private String mimeType;
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileName() {
		return fileName;
	}


}
