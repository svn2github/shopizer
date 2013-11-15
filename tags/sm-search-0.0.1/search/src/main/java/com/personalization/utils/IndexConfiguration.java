package com.shopizer.utils;

/**
 * Configured from Spring
 * @author Carl Samson
 *
 */
public class IndexConfiguration {
	
	private String collectionName;
	private String mappingFileName;
	public String getMappingFileName() {
		return mappingFileName;
	}
	public void setMappingFileName(String mappingFileName) {
		this.mappingFileName = mappingFileName;
	}
	public String getCollectionName() {
		return collectionName;
	}
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	private String indexName;


}
