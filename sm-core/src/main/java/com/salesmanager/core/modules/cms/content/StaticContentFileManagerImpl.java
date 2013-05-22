/**
 * 
 */
package com.salesmanager.core.modules.cms.content;

import java.util.List;

import com.salesmanager.core.business.content.model.content.StaticContentType;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.modules.cms.common.ContentData;
import com.salesmanager.core.modules.cms.common.InputStaticContentData;
import com.salesmanager.core.modules.cms.common.OutputStaticContentData;

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
    public void setUploadStaticContent( StaticContentPut uploadStaticContent )
    {
        this.uploadStaticContent = uploadStaticContent;
    }
    public StaticContentGet getGetStaticContent()
    {
        return getStaticContent;
    }
    public void setGetStaticContent( StaticContentGet getStaticContent )
    {
        this.getStaticContent = getStaticContent;
    }
    public StaticContentRemove getRemoveStaticContent()
    {
        return removeStaticContent;
    }
    public void setRemoveStaticContent( StaticContentRemove removeStaticContent )
    {
        this.removeStaticContent = removeStaticContent;
    }
    
    @Override
    public OutputStaticContentData getStaticContentData( String merchantStoreCode, String contentName )
        throws ServiceException
    {
      return  getStaticContent.getStaticContentData( merchantStoreCode, contentName );
      
    }
    
    @Override
    public void addStaticFile( String merchantStoreCode, InputStaticContentData inputStaticContentData )
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
    public void addStaticFiles( String merchantStoreCode, List<InputStaticContentData> inputStaticContentDataList )
        throws ServiceException
    {
       uploadStaticContent.addStaticFiles( merchantStoreCode, inputStaticContentDataList );
    }
    @Override
    public void removeStaticContent( String merchantStoreCode, ContentData contentData )
        throws ServiceException
    {
     //
        
    }
    @Override
    public void removeStaticContents( String merchantStoreCode )
        throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
	@Override
	public List<OutputStaticContentData> getStaticContentData(
			String merchantStoreCode, StaticContentType staticContentType) throws ServiceException {
		return getStaticContent.getStaticContentData(merchantStoreCode, staticContentType);
	}
	@Override
	public List<String> getStaticContentDataName(String merchantStoreCode, StaticContentType staticContentType)
			throws ServiceException {
		return getStaticContent.getStaticContentDataName(merchantStoreCode, staticContentType);
	}
    
   
    
  
    
}
