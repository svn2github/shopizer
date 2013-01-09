/**
 * 
 */
package com.salesmanager.core.modules.cms.content;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.modules.cms.common.ContentData;
import com.salesmanager.core.modules.cms.common.InputStaticContentData;
import com.salesmanager.core.modules.cms.common.OutputStaticContentData;
import com.salesmanager.core.modules.cms.impl.CacheManager;



/**
 * @author Umesh Awasthi
 * @since 1.2
 *
 */
public class CmsStaticContentFileManagerInfinispanImpl implements StaticContentPut,StaticContentGet,StaticContentRemove
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CmsStaticContentFileManagerInfinispanImpl.class );
    private static CmsStaticContentFileManagerInfinispanImpl fileManager = null;
    
    private CacheManager cacheManager;
    
    public void stopFileManager()
    {

        try
        {
            cacheManager.getManager().stop();
            LOGGER.info( "Stopping CMS" );
        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while stopping CmsStaticContentFileManager", e );
        }
    }

    public static CmsStaticContentFileManagerInfinispanImpl getInstance()
    {

        if ( fileManager == null )
        {
            fileManager = new CmsStaticContentFileManagerInfinispanImpl();
        }

        return fileManager;

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
    public OutputStaticContentData getStaticContentData( String merchantStoreCode, String contentName )
        throws ServiceException
    {
        // TODO Auto-generated method stub
        return null;
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
    
    
    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
