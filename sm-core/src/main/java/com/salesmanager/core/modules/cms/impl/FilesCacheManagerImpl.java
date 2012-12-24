package com.salesmanager.core.modules.cms.impl;


/**
 * Used for managing files (downloads, documents, css ...)
 * @author casams1
 *
 */
public class FilesCacheManagerImpl extends CacheManagerImpl {
	
	
	private static  FilesCacheManagerImpl cacheManager = null;
	private final static String NAMED_CACHE = "FilesRepository";
	

	

	private FilesCacheManagerImpl() {
		
		super.init(NAMED_CACHE);
		
	}
	
	public static FilesCacheManagerImpl getInstance() {
		
		if(cacheManager==null) {
			cacheManager = new FilesCacheManagerImpl();

		}
		
		return cacheManager;
		
		
	}



}
