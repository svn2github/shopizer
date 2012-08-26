package com.salesmanager.core.modules.cms;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



import org.apache.commons.io.IOUtils;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.tree.Fqn;
import org.infinispan.tree.Node;
import org.infinispan.tree.TreeCache;
import org.infinispan.tree.TreeCacheFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;


/**
 * Manager for storing in retrieving image files from the CMS
 * This is a layer on top of Infinispan
 * https://docs.jboss.org/author/display/ISPN/Tree+API+Module
 * @author csamson
 *
 */
public class CmsImageFileManagerInfinispanImpl implements ProductImagePut, ProductImageGet, ProductImageRemove {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CmsImageFileManagerInfinispanImpl.class);
	
	private static CmsImageFileManagerInfinispanImpl fileManager = null; 
	
	//get repository config
	private String repositoryFileName = "cms/infinispan_configuration.xml";
	
	private EmbeddedCacheManager manager = null;
	private TreeCache treeCache = null;
	

	
	private final static String PROPS = "props";
	private final static String MIME_TYPE = "mimeType";
	private final static String NAME = "name";
	private final static String EXTENSION = "extension";
	private final static String DEFAULT = "default";
	private final static String ALT = "alt";

	@SuppressWarnings("unchecked")
	private void initFileManager() throws Exception {
		
    	
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
	
	
	public static CmsImageFileManagerInfinispanImpl getInstance() {
		
		if(fileManager==null) {
			fileManager = new CmsImageFileManagerInfinispanImpl();
			try {
				fileManager.initFileManager();
			} catch (Exception e) {
				LOGGER.error("Error while instantiating CmsImageFileManager",e);
			}
		}
		
		return fileManager;
		
		
		
	}
	
	private CmsImageFileManagerInfinispanImpl(){
		
	}

	/**
	 * root
	 * 		-productFiles
	 * 				-merchantId
	 * 					     -productId
	 */

	@Override
	public void uploadProductImage(ProductImage productImage, InputContentImage contentImage)
			throws ServiceException {
		
        //TODO validate null objects
		
		try {
        	
        	
        	
			StringBuilder merchantPath = new StringBuilder();
			merchantPath.append("/productFiles/")
			.append(String.valueOf(productImage.getProduct().getMerchantSore().getId()));
			//.append(String.valueOf(productImage.getProduct().getId())).append("/")
			//.append(contentImage.getFile().getName());
			
			Fqn merchantFiles = Fqn.fromString(merchantPath.toString());
			
			Node<String, Object> merchantFilesTree = treeCache.getRoot().getChild(merchantFiles);
			
			if(merchantFilesTree==null) {
				treeCache.getRoot().addChild(merchantFiles);
			}
				
			
			//LOGGER.info("Loading content node " + filePath.toString());
			LOGGER.info("Content image " + contentImage.toString());
	
        } catch(Exception e) {
        	
        	throw new ServiceException(e);
	        
		} finally {
			
		}

		
	}

	@Override
	public OutputContentImage getProductImage(ProductImage productImage) throws ServiceException {


		try {
        	
			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append(String.valueOf(productImage.getProduct().getMerchantSore().getId())).append("/")
			.append(String.valueOf(productImage.getProduct().getId())).append("/")
			.append(productImage.getProductImage());


        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			
		}
		
		return null;

         //return image;
	}
	
	public List<OutputContentImage> getImages(MerchantStore store) throws ServiceException {
		
		

        
		List<OutputContentImage> images = new ArrayList<OutputContentImage>();

        try {


			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append(String.valueOf(store.getId())).append("/");

	        
			//images = this.getContentImages(session, filePath.toString());
			
        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			
		}
		
		return images;

	}
	
	
	@Override
	public List<OutputContentImage> getImages(Product product)
			throws ServiceException {



		List<OutputContentImage> images = new ArrayList<OutputContentImage>();

        try {

			
        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			//session.logout();
		}
		
		return images;
	}
	
	
	private List<OutputContentImage> getContentImages(String path) throws Exception {


		List<OutputContentImage> images = new ArrayList<OutputContentImage>();
        
		Node files = null;
		
		try {
			//files = session.getNode(path);
		} catch (Exception e) {
		}
		

/*         if(files!=null) {

         	while(niter.hasNext()) {
         		OutputContentImage c = new OutputContentImage();

                InputStream in = content.getStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream(); 
                IOUtils.copy(in, out);
                c.setImage(out);
                c.setImageContentType(contentType);
                c.setDefaultImage(defaultImage);
                c.setImageName(name);
                images.add(c);
                
                content.dispose();
         	}
         }*/
		
		return images;
		
	}

	public String getRepositoryFileName() {
		return repositoryFileName;
	}

	public void setRepositoryFileName(String repositoryFileName) {
		this.repositoryFileName = repositoryFileName;
	}

	@Override
	public void removeImages(MerchantStore store) throws ServiceException {
        /*Repository repository = null;
        String repositoryName = null;
        Session session = null;
        
        if(engine==null) {
        	throw new ServiceException("updateProductImage has null CMS engine");
        }

        try {
        	repositoryName = config.getName();
        	repository = engine.getRepository(repositoryName);

        	// Create a session ...
        	session = repository.login("default");
        	
			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append(String.valueOf(store.getId())).append("/");

			Node imgs = null;
			
			try {
				imgs = session.getNode(filePath.toString());
			} catch (Exception e) {
				//do not log
			}
			
			
			
			if(imgs!=null) {

				imgs.remove();
            
			}

        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			session.logout();
		}
*/
		
		
		
	}

	@Override
	public void removeProductImage(ProductImage productImage)
			throws ServiceException {
		
		
        /*Repository repository = null;
        String repositoryName = null;
        Session session = null;
        
        if(engine==null) {
        	throw new ServiceException("updateProductImage has null CMS engine");
        }

        try {
        	repositoryName = config.getName();
        	repository = engine.getRepository(repositoryName);

        	// Create a session ...
        	session = repository.login("default");
        	
			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append(String.valueOf(productImage.getProduct().getMerchantSore().getId())).append("/")
			.append(String.valueOf(productImage.getProduct().getId())).append("/")
			.append(productImage.getProductImage());


			
			Node img = null;
			
			try {
				img = session.getNode(filePath.toString());
			} catch (Exception e) {
				//do not log
			}
			
			
			
			if(img!=null) {

				img.remove();
            
			}

        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			session.logout();
		}
*/
		
		
	}

	@Override
	public void removeProductImages(Product product) throws ServiceException {
        /*Repository repository = null;
        String repositoryName = null;
        Session session = null;
        
        if(engine==null) {
        	throw new ServiceException("updateProductImage has null CMS engine");
        }

        try {
        	repositoryName = config.getName();
        	repository = engine.getRepository(repositoryName);

        	// Create a session ...
        	session = repository.login("default");
        	

			StringBuilder filePath = new StringBuilder();
			filePath.append("/productFiles/")
			.append(String.valueOf(product.getMerchantSore().getId())).append("/")
			.append(String.valueOf(product.getId())).append("/");


			
			Node imgs = null;
			
			try {
				imgs = session.getNode(filePath.toString());
			} catch (Exception e) {
				// do not log
			}
			
			
			
			if(imgs!=null) {

				imgs.remove();
            
			}

        } catch(Exception e) {
        	throw new ServiceException(e);
		} finally {
			session.logout();
		}*/

		
	}


	
	

}
