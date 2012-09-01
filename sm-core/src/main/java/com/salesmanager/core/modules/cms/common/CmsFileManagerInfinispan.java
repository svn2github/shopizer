package com.salesmanager.core.modules.cms.common;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.tree.Fqn;
import org.infinispan.tree.Node;
import org.infinispan.tree.TreeCache;
import org.infinispan.tree.TreeCacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CmsFileManagerInfinispan {
	
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CmsFileManagerInfinispan.class);
	
	
	protected String repositoryFileName = "cms/infinispan_configuration.xml";
	
	protected EmbeddedCacheManager manager = null;
	@SuppressWarnings("rawtypes")
	protected TreeCache treeCache = null;
	

	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initFileManager() throws Exception {
		
    	
		try {
		
			 manager = new DefaultCacheManager(repositoryFileName);
			 Cache defaultCache = manager.getCache("DataRepository");
			 defaultCache.getCacheConfiguration().invocationBatching().enabled();
	    
			 TreeCacheFactory f = new TreeCacheFactory();
	    
			 treeCache = f.createTreeCache(defaultCache);
			 
			 manager.start();
			 
			 Fqn productFiles = Fqn.fromString("productFiles");
			 Node<String, Object> productFilesTree = treeCache.getRoot().getChild(productFiles);
			 
			 if(productFilesTree==null) {
				 treeCache.getRoot().addChild(productFiles);
			 }
		
			 
	         
	         LOGGER.debug("CMS started");

 
        } catch (Exception e) {
        	LOGGER.error("Error while instantiating CmsImageFileManager",e);
        } finally {
            
        }
		
		
		
	}
	
	/**
	 *Requires to stop the engine
	 *when image servlet un-deploys
	 */
	public void stopFileManager() {
		
        try {
        	manager.stop();
            LOGGER.info("Stopping CMS");
        } catch (Exception e) {
        	LOGGER.error("Error while stopping CmsImageFileManager",e);
        }
	}

}
