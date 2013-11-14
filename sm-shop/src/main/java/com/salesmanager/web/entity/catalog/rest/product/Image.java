package com.salesmanager.web.entity.catalog.rest.product;

public class Image {
	
	   private byte[] bytes = null;


	   private String contentType = null;


	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}


	public byte[] getBytes() {
		return bytes;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	public String getContentType() {
		return contentType;
	}

}
