package com.salesmanager.core.utils;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CacheUtils {
	
	
	public final static String REFERENCE_CACHE = "REF";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);
	
	private static CacheUtils cacheUtils = null;

	private String repositoryFileName = "cms/infinispan_configuration.xml";
	
	private Cache<Object, Object> localCache = null;
	

	
	
	private CacheUtils() {
		
		try {

			   
			   localCache =  new DefaultCacheManager(repositoryFileName).getCache("Cache"); 

			
			
		} catch (Exception e) {
			LOGGER.error("Error loading cache singletons", e);
		}
		
	}
	
	public static CacheUtils getInstance() {
		
		if(cacheUtils==null) {
			cacheUtils = new CacheUtils();
	
		}
		
		return cacheUtils;
		
	}
	

	

	public void putInCache(Object object, String keyName) throws Exception {

		localCache.put(keyName, object);
		
	}
	

	public Object getFromCache(String keyName) throws Exception {

		 return localCache.get(keyName);
		
	}
	
	public void shutDownCache() throws Exception {
		
	}
	
	public void removeFromCache(String keyName) throws Exception {
		localCache.remove(keyName);
	}

}
