package com.salesmanager.core.modules.cms.impl;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.tree.TreeCache;
import org.infinispan.tree.TreeCacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CacheManagerImpl implements CacheManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheManagerImpl.class);
	private String repositoryFileName = "cms/infinispan_configuration.xml";
	
	private EmbeddedCacheManager manager = null;
	@SuppressWarnings("rawtypes")
	private TreeCache treeCache = null;

	@SuppressWarnings("unchecked")
	protected void init(String namedCache) {
		
		
		try {
			
			 manager = new DefaultCacheManager(repositoryFileName);
			 @SuppressWarnings("rawtypes")
			 Cache defaultCache = manager.getCache(namedCache);
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
	
	public EmbeddedCacheManager getManager() {
		return manager;
	}

	@SuppressWarnings("rawtypes")
	public TreeCache getTreeCache() {
		return treeCache;
	}
	
	

}
