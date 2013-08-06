package com.salesmanager.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.merchant.model.MerchantStore;


public class CacheUtils {
	
	
	public final static String REFERENCE_CACHE = "REF";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);
	
	private static CacheUtils cacheUtils = null;

	private final static String repositoryFileName = "cms/infinispan_configuration.xml";
	
	private final static String KEY_DELIMITER = "_";
	
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
	
	public List<String> getCacheKeys(MerchantStore store) throws Exception {
		Set<Object> keys = localCache.keySet();
		List<String> returnKeys = new ArrayList<String>();
		for(Object key : keys) {
			try {
				String sKey = (String)key;
				
				// a key should be <storeId>_<rest of the key>
				int delimiterPosition = sKey.indexOf(KEY_DELIMITER);
				String keyRemaining = sKey.substring(delimiterPosition+1);
				returnKeys.add(keyRemaining);

			} catch (Exception e) {
				LOGGER.equals("key " + key + " cannot be converted to a String or parsed");
			}

		}
		
		return returnKeys;
	}
	
	public void shutDownCache() throws Exception {
		
	}
	
	public void removeFromCache(String keyName) throws Exception {
		localCache.remove(keyName);
	}
	


}
