package com.salesmanager.core.modules.cms.impl;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.tree.TreeCache;
import org.infinispan.tree.TreeCacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
public class CacheManagerImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheManagerImpl.class);
	private static  CacheManagerImpl cacheManager = null;
	
	
	private String repositoryFileName = "cms/infinispan_configuration.xml";
	
	private EmbeddedCacheManager manager = null;
	@SuppressWarnings("rawtypes")
	private TreeCache treeCache = null;
	

	
	@SuppressWarnings("unchecked")
	private CacheManagerImpl() {
		
		try {
			
			 manager = new DefaultCacheManager(repositoryFileName);
			 @SuppressWarnings("rawtypes")
			 Cache defaultCache = manager.getCache("DataRepository");
			 defaultCache.getCacheConfiguration().invocationBatching().enabled();
	    
			 TreeCacheFactory f = new TreeCacheFactory();
	    
			 treeCache = f.createTreeCache(defaultCache);
			 
			 manager.start();

	         LOGGER.debug("CMS started");


       } catch (Exception e) {
       	LOGGER.error("Error while instantiating CmsImageFileManager",e);
       } finally {
           
       }
		
	}
	
	public static CacheManagerImpl getInstance() {
		
		if(cacheManager==null) {
			cacheManager = new CacheManagerImpl();

		}
		
		return cacheManager;
		
		
	}

	public EmbeddedCacheManager getManager() {
		return manager;
	}

	@SuppressWarnings("rawtypes")
	public TreeCache getTreeCache() {
		return treeCache;
	}

}
