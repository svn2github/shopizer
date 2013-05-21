package com.salesmanager.core.utils;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jboss.cache.CacheFactory;
import org.jboss.cache.DefaultCacheFactory;
import org.jboss.cache.Fqn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class CacheUtils {
	
	
	public final static String REFERENCE_CACHE = "REF";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);
	
	private static CacheUtils cacheUtils = null;

	private org.jboss.cache.Node refCache;
	
	private String repositoryFileName = "cms/infinispan_configuration.xml";
	
	private Cache<Object, Object> localCache = null;
	

	
	
	private CacheUtils() {
		
		try {
			
			   
			   CacheFactory factory = new DefaultCacheFactory();
			   org.jboss.cache.Cache cache = factory.createCache();
			   org.jboss.cache.Node rootNode = cache.getRoot();
			   Fqn refFqn = Fqn.fromString("/ref/");
			   refCache = rootNode.addChild(refFqn);
			   
			   //localCache =  new DefaultCacheManager(repositoryFileName).getCache("Cache"); 

			
			
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
	

	
	@SuppressWarnings("unchecked")
	public void putInCache(Object object, String keyName) throws Exception {
		
		refCache.put(keyName, object);
		//localCache.put(keyName, object);
		
	}
	
	@SuppressWarnings("unchecked")
	public Object getFromCache(String keyName) throws Exception {
		
		 return refCache.get(keyName);
		 //return localCache.get(keyName);
		
	}
	
	public void shutDownCache() throws Exception {
		
	}

}
