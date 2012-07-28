package com.salesmanager.core.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheUtils {
	
	
	public final static String REFERENCE_CACHE = "REF";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);
	
	private static CacheUtils cacheUtils = null;

	private CacheManager singletonManager;
	
	private CacheUtils() {
		
		try {
			
			singletonManager = CacheManager.getInstance();
			Cache memoryOnlyCache = new Cache(REFERENCE_CACHE, 0, false, false, 36000, 2);
			singletonManager.addCache(memoryOnlyCache);
			
			
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
	

	
	public void putInCache(Object object, String keyName, String cacheName) throws Exception {
		
		Cache cache = singletonManager.getCache(cacheName);
		Element element = new Element(keyName, object);
		cache.put(element);
		
	}
	
	public Object getFromCache(String keyName, String cacheName) throws Exception {
		
		Cache cache = singletonManager.getCache(cacheName);
		Element element = cache.get(keyName);
		if(element!=null) {
			return element.getValue();
		}
		
		return null;
		
	}

}
