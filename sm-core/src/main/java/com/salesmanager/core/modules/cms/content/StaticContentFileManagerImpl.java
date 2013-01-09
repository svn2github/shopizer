/**
 * 
 */
package com.salesmanager.core.modules.cms.content;

import java.util.List;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.modules.cms.common.ContentData;
import com.salesmanager.core.modules.cms.common.InputStaticContentData;
import com.salesmanager.core.modules.cms.common.OutputStaticContentData;
import com.salesmanager.core.modules.cms.impl.CacheManager;

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
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void addStaticFile( String merchantStoreCode, InputStaticContentData inputStaticContentData )
        throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void addStaticFiles( String merchantStoreCode, List<InputStaticContentData> inputStaticContentDataList )
        throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void removeStaticContent( String merchantStoreCode, ContentData contentData )
        throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void removeStaticContents( String merchantStoreCode )
        throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }
    
   
    
  
    
}
