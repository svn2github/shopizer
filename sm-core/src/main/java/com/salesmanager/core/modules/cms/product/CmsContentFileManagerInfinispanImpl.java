package com.salesmanager.core.modules.cms.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.infinispan.tree.Fqn;
import org.infinispan.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.content.model.image.ContentImage;
import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.content.model.image.InputContentImage;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.modules.cms.common.CacheAttribute;
import com.salesmanager.core.modules.cms.common.CmsFileManagerInfinispan;
import com.salesmanager.core.modules.cms.content.ContentImageGet;
import com.salesmanager.core.modules.cms.content.ContentImageRemove;
import com.salesmanager.core.modules.cms.content.ImagePut;

public class CmsContentFileManagerInfinispanImpl extends
		CmsFileManagerInfinispan implements ImagePut, ContentImageGet, ContentImageRemove {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CmsContentFileManagerInfinispanImpl.class);
	
	private static CmsContentFileManagerInfinispanImpl fileManager = null; 
	
	private final static String IMAGE_CONTENT = "IMAGE_CONTENT";
	
	private final static String CONTENT_FILES = "contentFiles";
	

	
	/**
	 *Requires to stop the engine
	 *when image servlet un-deploys
	 */
	public void stopFileManager() {
		
        try {
        	manager.stop();
            LOGGER.info("Stopping CMS");
        } catch (Exception e) {
        	LOGGER.error("Error while stopping CmsContentFileManager",e);
        }
	}
	
	
	public static CmsContentFileManagerInfinispanImpl getInstance() {
		
		if(fileManager==null) {
			fileManager = new CmsContentFileManagerInfinispanImpl();
			try {
				fileManager.initFileManager(CONTENT_FILES);
			} catch (Exception e) {
				LOGGER.error("Error while instantiating CmsContentFileManager",e);
			}
		}
		
		return fileManager;
		
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getImageNames(MerchantStore store,
			ImageContentType imageContentType) throws ServiceException {
        if(treeCache==null) {
        	throw new ServiceException("CmsImageFileManagerInfinispan has a null treeCache");
        }

		List<String> returnNames = new ArrayList<String>();
		try {
        	
			StringBuilder filePath = new StringBuilder();
			filePath.append("/contentFiles/")
			.append("merchant-").append(String.valueOf(store.getId()));//.append("/")
			
			
			//a logo or content
			if(imageContentType.equals(ImageContentType.LOGO)) {
				
				filePath.append("/logo");

			} else {
			
			//if(image.getContentType().equals(ImageContentType.CONTENT)) {
				
				filePath.append("/content");

			}
			
			
			Fqn imageFile = Fqn.fromString(filePath.toString());
			
			if(imageFile==null) {
				return null;
			}
			
			Node<String, Object> imageFileFileTree = treeCache.getRoot().getChild(imageFile);
			
			
			if(imageFileFileTree==null) {
				return null;
			}
			
			Set<String> names = imageFileFileTree.getKeys();
			
			
			
			for(String name : names) {
				
				returnNames.add(name);
				
			}
			
			

			

        } catch(Exception e) {
        	throw new ServiceException(e);
		} 
		
		return returnNames;
	}

	
	/**
	 * Returns the physical files
	 */
	@Override
	public List<OutputContentImage> getImages(MerchantStore store, ImageContentType imageContentType)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeImages(MerchantStore store) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeImage(ContentImage contentImage) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * Returns the physical file
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public OutputContentImage getImage(MerchantStore store, String imageName, ImageContentType imageContentType) throws ServiceException {
		
		
        if(treeCache==null) {
        	throw new ServiceException("CmsImageFileManagerInfinispan has a null treeCache");
        }

		OutputContentImage contentImage = new OutputContentImage();
		try {
        	
		//TODO to be implemented

        } catch(Exception e) {
        	throw new ServiceException(e);
		} 
		
		return contentImage;
		
		
	}
	
	
	
	/**
	 * root
	 * 		-contentFiles
	 * 				-merchant-id
	 * 					     IMAGES(key) -> CacheAttribute(value)
	 * 												- image 1
	 * 												- image 2
	 * 												- image 3
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void addImage(MerchantStore store, InputContentImage image)
			throws ServiceException {
		

        if(treeCache==null) {
        	throw new ServiceException("CmsImageFileManagerInfinispan has a null treeCache");
        }

		try {
			
			
			//retrieve merchant node
			StringBuilder merchantPath = new StringBuilder();
			merchantPath.append("merchant-").append(String.valueOf(store.getId()));

			Node<String,Object> productFilesNode = treeCache.getRoot().getChild(Fqn.fromString("contentFiles"));
			
			
			Node<String, Object> merchantNode = productFilesNode.getChild(Fqn.fromString(merchantPath.toString()));
			
			
			if(merchantNode==null) {
				Fqn merchantFqn = Fqn.fromString(merchantPath.toString());
				productFilesNode.addChild(merchantFqn);
				merchantNode = treeCache.getRoot().getChild(Fqn.fromElements("contentFiles", merchantPath.toString()));
			}
			
			//object for a given product containing all images
			CacheAttribute contentAttribute = (CacheAttribute) merchantNode.get(IMAGE_CONTENT);
			
			
			if(contentAttribute==null) {
				contentAttribute = new CacheAttribute();
			}
			
			//bytearrayoutputstream is in inputcontentimage
			
			//All images for  a given merchant will be stored in CacheAttribute
			//getEntities.add(image.name,baos.getBytes());
	
	
        } catch(Exception e) {
        	
        	throw new ServiceException(e);
	        
		} 
		
		
		
	}




}
