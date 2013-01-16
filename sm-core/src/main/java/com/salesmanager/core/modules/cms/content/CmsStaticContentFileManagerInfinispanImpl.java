/**
 * 
 */
package com.salesmanager.core.modules.cms.content;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.infinispan.tree.Fqn;
import org.infinispan.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.content.model.content.StaticContentType;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.modules.cms.common.ContentData;
import com.salesmanager.core.modules.cms.common.InputStaticContentData;
import com.salesmanager.core.modules.cms.common.OutputStaticContentData;
import com.salesmanager.core.modules.cms.common.StaticContentCacheAttribute;
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
    private static final String STATIC_CONTENT_NODE="static-merchant-";
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

    
    /**
     * <p>Method to add static content data for given merchant.Static content data can be of following type
     * <pre>
     * 1. CSS and JS files
     * 2. Digital Data like audio or video
     * </pre>
     * </p>
     * <p>
     * Merchant store code will be used to create cache node where merchant data will be stored,input data will
     * contain name, file as well type of data being stored.
     * @see StaticContentType
     * </p>
     *  
     * @param merchantStoreCode merchant store for whom data is being stored
     * @param inputStaticContentData data object being stored
     * @throws ServiceException
     * 
     */
    @Override
    public void addStaticFile( String merchantStoreCode, InputStaticContentData inputStaticContentData )
        throws ServiceException
    {
        if ( cacheManager.getTreeCache() == null )
        {
            LOGGER.error( "Unable to find cacheManager.getTreeCache() in Infinispan.." );
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }
        try
        {
            final Node<String, Object> merchantNode = getMerchantNodeForStaticContent(merchantStoreCode );
            StaticContentCacheAttribute contentAttribute = (StaticContentCacheAttribute) merchantNode.get( inputStaticContentData.getContentType().name() );
            if ( contentAttribute == null )
            {
                contentAttribute = new StaticContentCacheAttribute();
            }
            contentAttribute.getEntities().put( inputStaticContentData.getFileName(), IOUtils.toByteArray( inputStaticContentData.getFile() ));
            contentAttribute.setDataType( inputStaticContentData.getContentType().name() );
            merchantNode.put( inputStaticContentData.getFileName(), contentAttribute );
            LOGGER.info( "Content data added successfully." );
        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while saving static content data", e );
            throw new ServiceException( e );

        }
        
    }

    @Override
    public void addStaticFiles( String merchantStoreCode, List<InputStaticContentData> inputStaticContentDataList )
        throws ServiceException
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * Method to return static data for given Merchant store based on the file name. Content data will be searched
     * in underlying Infinispan cache tree and {@link OutputStaticContentData} will be returned on finding an associated
     * file. In case of no file, null be returned.
     * 
     * @param store Merchant store
     * @param contentFileName name of file being requested
     * @return {@link OutputStaticContentData}
     * @throws ServiceException
     */
    @Override
    public OutputStaticContentData getStaticContentData( String merchantStoreCode, String contentFileName )
        throws ServiceException
    {
       
        if ( cacheManager.getTreeCache() == null )
        {
            throw new ServiceException( "CmsStaticContentFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }
        OutputStaticContentData outputStaticContentData=null;
        InputStream input = null;
        try
        {
            final Node<String, Object> merchantNode = getMerchantNodeForStaticContent(merchantStoreCode );
            if ( merchantNode == null )
            {
                LOGGER.warn( "merchant node is null" );
                return null;
            }
            final StaticContentCacheAttribute contentAttribute = (StaticContentCacheAttribute) merchantNode.get( contentFileName);

            if ( contentAttribute == null )
            {
                LOGGER.warn( "Unable to find content attribute for given merchant" );
                return null;
            }
            final byte[] fileBytes=contentAttribute.getEntities().get( contentFileName );
            if ( fileBytes == null )
            {
                LOGGER.warn( "file byte is null, no file found" );
                return null;
            }
            input=new ByteArrayInputStream( fileBytes );
           
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy( input, output );
            
            outputStaticContentData=new OutputStaticContentData();
            outputStaticContentData.setFile( output );
            
            outputStaticContentData.setFileName( URLConnection.guessContentTypeFromStream( input ) );
            outputStaticContentData.setContentType( StaticContentType.valueOf( contentAttribute.getDataType() ) );
            
        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while fetching file for {} merchant ", merchantStoreCode);
            throw new ServiceException( e );
        }
       return outputStaticContentData != null ? outputStaticContentData : null;
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
    
    @SuppressWarnings( "unchecked" )
    private Node<String, Object> getMerchantNodeForStaticContent(final String storeCode )
    {
        LOGGER.debug( "Fetching merchant node for store {} from Infinispan", storeCode );
        final StringBuilder merchantPath = new StringBuilder();
        merchantPath.append(STATIC_CONTENT_NODE).append(storeCode );

        Fqn contentFilesFqn = Fqn.fromString(merchantPath.toString()); 
        Node<String,Object> merchant = cacheManager.getTreeCache().getRoot().getChild(contentFilesFqn); 
        
        if(merchant==null) {

            cacheManager.getTreeCache().getRoot().addChild(contentFilesFqn);
            merchant = cacheManager.getTreeCache().getRoot().getChild(contentFilesFqn); 

        }
        
        return merchant;

    }
    
    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
