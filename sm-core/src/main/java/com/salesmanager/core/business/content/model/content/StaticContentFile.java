package com.salesmanager.core.business.content.model.content;

public abstract class StaticContentFile extends ContentFile {
	
	private FileContentType fileContentType;

	public FileContentType getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(FileContentType fileContentType) {
		this.fileContentType = fileContentType;
	}


	

}
