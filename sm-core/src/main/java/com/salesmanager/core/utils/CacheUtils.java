package com.salesmanager.core.utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.merchant.model.MerchantStore;

@Component("cache")
public class CacheUtils {
	
	
    @Inject
    @Qualifier("serviceCache")
    private Cache cache;
	
	
	public final static String REFERENCE_CACHE = "REF";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);
	
	//private static CacheUtils cacheUtils = null;

	//private final static String repositoryFileName = "cms/infinispan_configuration.xml";
	
	private final static String KEY_DELIMITER = "_";
	
	//private Cache<Object, Object> localCache = null;
	

	
	
/*	private CacheUtils() {
		
		try {

			   
			   localCache =  new DefaultCacheManager(repositoryFileName).getCache("Cache"); 

			
			
		} catch (Exception e) {
			LOGGER.error("Error loading cache singletons", e);
		}
		
	}*/
	
/*	public static CacheUtils getInstance() {
		
		if(cacheUtils==null) {
			cacheUtils = new CacheUtils();
	
		}
		
		return cacheUtils;
		
	}*/
	

	

	public void putInCache(Object object, String keyName) throws Exception {

		//localCache.put(keyName, object);
		cache.put(keyName, object);
		
	}
	

	public Object getFromCache(String keyName) throws Exception {

		 //return localCache.get(keyName);
		ValueWrapper vw = cache.get(keyName);
		if(vw!=null) {
			return vw.get();
		}
		
		return null;
		
	}
	
	public List<String> getCacheKeys(MerchantStore store) throws Exception {
		
		  net.sf.ehcache.Cache cacheImpl = (net.sf.ehcache.Cache) cache.getNativeCache();
		  List<String> returnKeys = new ArrayList<String>();
		  for (Object key: cacheImpl.getKeys()) {
		    
			  
				try {
					String sKey = (String)key;
					
					// a key should be <storeId>_<rest of the key>
					int delimiterPosition = sKey.indexOf(KEY_DELIMITER);
					
					if(delimiterPosition>0 && Character.isDigit(sKey.charAt(0))) {
					
						String keyRemaining = sKey.substring(delimiterPosition+1);
						returnKeys.add(keyRemaining);
					
					}

				} catch (Exception e) {
					LOGGER.equals("key " + key + " cannot be converted to a String or parsed");
				}  

		  }
		
/*		Set<Object> keys = localCache.keySet();
		List<String> returnKeys = new ArrayList<String>();
		for(Object key : keys) {
			try {
				String sKey = (String)key;
				
				// a key should be <storeId>_<rest of the key>
				int delimiterPosition = sKey.indexOf(KEY_DELIMITER);
				
				if(delimiterPosition>0 && Character.isDigit(sKey.charAt(0))) {
				
					String keyRemaining = sKey.substring(delimiterPosition+1);
					returnKeys.add(keyRemaining);
				
				}

			} catch (Exception e) {
				LOGGER.equals("key " + key + " cannot be converted to a String or parsed");
			}

		}*/
		
		return returnKeys;
	}
	
	public void shutDownCache() throws Exception {
		
	}
	
	public void removeFromCache(String keyName) throws Exception {
		//localCache.remove(keyName);
		cache.evict(keyName);
	}
	


}
