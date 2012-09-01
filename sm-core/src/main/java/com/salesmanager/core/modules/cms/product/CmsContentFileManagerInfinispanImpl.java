package com.salesmanager.core.modules.cms.product;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.infinispan.tree.Fqn;
import org.infinispan.tree.Node;

import com.salesmanager.core.business.content.model.image.ContentImage;
import com.salesmanager.core.business.content.model.image.InputContentImage;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.modules.cms.common.CmsFileManagerInfinispan;
import com.salesmanager.core.modules.cms.content.ContentImageGet;
import com.salesmanager.core.modules.cms.content.ContentImageRemove;
import com.salesmanager.core.modules.cms.content.ImagePut;

public class CmsContentFileManagerInfinispanImpl extends
		CmsFileManagerInfinispan implements ImagePut, ContentImageGet, ContentImageRemove {

	@Override
	public List<OutputContentImage> getImages(MerchantStore store)
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

	@Override
	public ContentImage getImage(MerchantStore store, String imageName) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addImage(MerchantStore store, InputContentImage image)
			throws ServiceException {
		

        if(treeCache==null) {
        	throw new ServiceException("CmsImageFileManagerInfinispan has a null treeCache");
        }
        InputStream input = null;
        ByteArrayOutputStream output = null;
		try {
        	

			StringBuilder merchantPath = new StringBuilder();
			merchantPath.append("/contentFiles/")
			.append("merchant-").append(String.valueOf(store.getId()));

			Fqn merchantFiles = Fqn.fromString(merchantPath.toString());
			
			Node<String, Object> merchantFilesTree = treeCache.getRoot().getChild(merchantFiles);
			
			if(merchantFilesTree==null) {
				treeCache.getRoot().addChild(merchantFiles);
				merchantFilesTree = treeCache.getRoot().getChild(merchantFiles);
			}


            input = new BufferedInputStream(new FileInputStream(image.getFile()));
            output = new ByteArrayOutputStream(); 
            IOUtils.copy(input, output);

            byte[] imageBytes = output.toByteArray();
            
            merchantFilesTree.put(image.getImageName(), imageBytes);

	
        } catch(Exception e) {
        	
        	throw new ServiceException(e);
	        
		} finally {
			
			if(input!=null) {
				try {
					input.close();
				} catch (Exception ignore) {}
			}
			
			if(output!=null) {
				try {
					output.close();
				} catch (Exception ignore) {}
			}
			
		}
		
		
		
	}

}
