package com.salesmanager.core.business.common.model;

public class Criteria {
	
	private int startIndex;
	private int maxCount;
	private String code;
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}



}