/**
 * 
 */
package com.salesmanager.core.modules.cms.content;

import java.util.List;

import com.salesmanager.core.business.content.model.content.FileContentType;
import com.salesmanager.core.business.content.model.content.InputContentFile;
import com.salesmanager.core.business.content.model.content.OutputContentFile;
import com.salesmanager.core.business.generic.exception.ServiceException;


/**
 * @author Umesh Awasthi
 *
 */
public class StaticContentFileManagerImpl extends StaticContentFileManager
{

    private StaticContentPut uploadStaticContent;
    private StaticContentGet getStaticContent;
    private StaticContentRemove removeStaticContent;
    public StaticContentPut getUploadStaticContent()
    {
        return uploadStaticContent;
    }
    public void setUploadStaticContent( final StaticContentPut uploadStaticContent )
    {
        this.uploadStaticContent = uploadStaticContent;
    }
    public StaticContentGet getGetStaticContent()
    {
        return getStaticContent;
    }
    public void setGetStaticContent( final StaticContentGet getStaticContent )
    {
        this.getStaticContent = getStaticContent;
    }
    public StaticContentRemove getRemoveStaticContent()
    {
        return removeStaticContent;
    }
    public void setRemoveStaticContent( final StaticContentRemove removeStaticContent )
    {
        this.removeStaticContent = removeStaticContent;
    }
    
    @Override
    public OutputContentFile getStaticContentData( final String merchantStoreCode, final FileContentType staticContentType, final String contentName )
        throws ServiceException
    {
      return  getStaticContent.getStaticContentData( merchantStoreCode, staticContentType, contentName );
      
    }
    
    @Override
    public void addStaticFile( final String merchantStoreCode, final InputContentFile inputStaticContentData )
        throws ServiceException
    {
        uploadStaticContent.addStaticFile( merchantStoreCode, inputStaticContentData );
        
    }
    
    /**
     * Implementation for add static data files. This method will called respected add files method of underlying
     * CMSStaticContentManager. For CMS Content files {@link CmsStaticContentFileManagerInfinispanImpl} will take care of adding
     * given content images with Infinispan cache.
     * 
     * @param merchantStoreCode merchant store.
     * @param inputStaticContentDataList Input content images
     * @throws ServiceException
     */
    @Override
    public void addStaticFiles( final String merchantStoreCode, final List<InputContentFile> inputStaticContentDataList )
        throws ServiceException
    {
       uploadStaticContent.addStaticFiles( merchantStoreCode, inputStaticContentDataList );
    }
    @Override
    public void removeStaticContent( final String merchantStoreCode, final FileContentType staticContentType, final String fileName)
        throws ServiceException
    {
    	removeStaticContent.removeStaticContent(merchantStoreCode, staticContentType, fileName);
        
    }
    @Override
    public void removeStaticContents( final String merchantStoreCode )
        throws ServiceException
    {
    	removeStaticContent.removeStaticContents(merchantStoreCode);
        
    }
	@Override
	public List<OutputContentFile> getStaticContentData(
			final String merchantStoreCode, final FileContentType staticContentType) throws ServiceException {
		return getStaticContent.getStaticContentData(merchantStoreCode, staticContentType);
	}
	@Override
	public List<String> getStaticContentDataName(final String merchantStoreCode, final FileContentType staticContentType)
			throws ServiceException {
		return getStaticContent.getStaticContentDataName(merchantStoreCode, staticContentType);
	}
    
   
    
  
    
}
