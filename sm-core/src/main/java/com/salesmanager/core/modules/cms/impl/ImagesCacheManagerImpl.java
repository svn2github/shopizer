package com.salesmanager.core.modules.cms.impl;



/**
 * Used for managing images
 * @author casams1
 *
 */
public class ImagesCacheManagerImpl extends CacheManagerImpl {
	
	
	private static  ImagesCacheManagerImpl cacheManager = null;
	private final static String NAMED_CACHE = "StoreRepository";
	

	

	private ImagesCacheManagerImpl() {
		
		super.init(NAMED_CACHE);
		
		
	}

	
	public static ImagesCacheManagerImpl getInstance() {
		
		if(cacheManager==null) {
			cacheManager = new ImagesCacheManagerImpl();

		}
		
		return cacheManager;
		
		
	}



}

