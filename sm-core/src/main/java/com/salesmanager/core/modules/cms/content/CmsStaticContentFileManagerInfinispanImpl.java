/**
 * 
 */
package com.salesmanager.core.modules.cms.content;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.infinispan.tree.Fqn;
import org.infinispan.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.content.model.content.FileContentType;
import com.salesmanager.core.business.content.model.content.InputContentFile;
import com.salesmanager.core.business.content.model.content.OutputContentFile;
import com.salesmanager.core.business.generic.exception.ServiceException;
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
     * @see FileContentType
     * </p>
     *  
     * @param merchantStoreCode merchant store for whom data is being stored
     * @param inputStaticContentData data object being stored
     * @throws ServiceException
     * 
     */
    @Override
    public void addStaticFile( final String merchantStoreCode, final InputContentFile inputStaticContentData )
        throws ServiceException
    {
        if ( cacheManager.getTreeCache() == null )
        {
            LOGGER.error( "Unable to find cacheManager.getTreeCache() in Infinispan.." );
            throw new ServiceException( "CmsStaticContentFileManagerInfinispanImpl has a null cacheManager.getTreeCache()" );
        }
        try
        {
            final Node<String, Object> merchantNode = getMerchantNodeForStaticContent(merchantStoreCode, inputStaticContentData.getFileContentType());
            
            StaticContentCacheAttribute contentAttribute = (StaticContentCacheAttribute) merchantNode.get( inputStaticContentData.getFileContentType().name() );
            if ( contentAttribute == null )
            {
            	contentAttribute = new StaticContentCacheAttribute();
            }
            contentAttribute.getEntities().put( inputStaticContentData.getFileName(), IOUtils.toByteArray( inputStaticContentData.getFile() ));
            //contentAttribute.getStaticDataEntities().put( inputStaticContentData.getFileName(), inputStaticContentData.getFile() );
            
            //contentAttribute.setDataType( inputStaticContentData.getContentType().name() );
           
            //merchantNode.put( inputStaticContentData.getFileName(), IOUtils.toByteArray( inputStaticContentData.getFile() ) );
            
            merchantNode.put( inputStaticContentData.getFileContentType().name(),  contentAttribute );
            
            LOGGER.info( "Content data added successfully." );
        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while saving static content data", e );
            throw new ServiceException( e );

        }
        
    }

    /**
     * <p>
     * Method to add files for given store.Files will be stored in Infinispan and will be retrieved based on
     * the storeID. Following steps will be performed to store static content files
     * </p>
     * <li>Merchant Node will be retrieved from the cacheTree if it exists else new node will be created.</li> <li>
     * Files will be stored in StaticContentCacheAttribute , which eventually will be stored in Infinispan</li>
     * 
     * @param merchantStoreCode Merchant store for which files are getting stored in Infinispan.
     * @param inputStaticContentDataList input static content file list which will get {@link InputContentImage} stored
     * @throws ServiceException if content file storing process fail.
     * @see InputStaticContentData
     * @see StaticContentCacheAttribute
     */
    @Override
    //TODO BE REVISED
    public void addStaticFiles( final String merchantStoreCode, final List<InputContentFile> inputStaticContentDataList )
        throws ServiceException
    {
        if ( cacheManager.getTreeCache() == null )
        {
            LOGGER.error( "Unable to find cacheManager.getTreeCache() in Infinispan.." );
            throw new ServiceException( "CmsStaticContentFileManagerInfinispanImpl has a null cacheManager.getTreeCache()" );
        }
        try
        {
          
          for(final InputContentFile inputStaticContentData:inputStaticContentDataList){
 
                //String cmsType = inputStaticContentData.getContentType().name();
                //StaticContentCacheAttribute contentAttribute = (StaticContentCacheAttribute) merchantNode.get( cmsType );
                //if ( contentAttribute == null )
                //{
        	   Node<String, Object> merchantNode = getMerchantNodeForStaticContent(merchantStoreCode,inputStaticContentData.getFileContentType());
               StaticContentCacheAttribute contentAttribute = new StaticContentCacheAttribute();
                   
                //}
                
               contentAttribute.getEntities().put( inputStaticContentData.getFileName(), IOUtils.toByteArray( inputStaticContentData.getFile() ));
               contentAttribute.setDataType( inputStaticContentData.getFileContentType().name() );
               merchantNode.put( inputStaticContentData.getFileName(), contentAttribute );
               
            }
          
          
            
            LOGGER.info( "Total {} files added successfully.",inputStaticContentDataList.size() );

        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while saving content image", e );
            throw new ServiceException( e );

        }
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
    public OutputContentFile getStaticContentData( final String merchantStoreCode, final FileContentType fileContentType, final String contentFileName )
        throws ServiceException
    {
       
        if ( cacheManager.getTreeCache() == null )
        {
            throw new ServiceException( "CmsStaticContentFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }
        OutputContentFile outputStaticContentData=null;
        InputStream input = null;
        try
        {
            final Node<String, Object> merchantNode = getMerchantNodeForStaticContent(merchantStoreCode,fileContentType);
            if ( merchantNode == null )
            {
                LOGGER.warn( "merchant node is null" );
                return null;
            }
            
            
            final byte[] fileBytes= (byte[]) merchantNode.get( contentFileName );
            
            if ( fileBytes == null )
            {
                LOGGER.warn( "file byte is null, no file found" );
                return null;
            }
            
/*            final StaticContentCacheAttribute contentAttribute = (StaticContentCacheAttribute) merchantNode.get( contentFileName );

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
            }*/
            input=new ByteArrayInputStream( fileBytes );
           
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy( input, output );
            
            outputStaticContentData=new OutputContentFile();
            outputStaticContentData.setFile( output );
            outputStaticContentData.setMimeType( URLConnection.getFileNameMap().getContentTypeFor(contentFileName) );
            outputStaticContentData.setFileName( contentFileName );
            outputStaticContentData.setFileContentType( fileContentType );
            
        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while fetching file for {} merchant ", merchantStoreCode);
            throw new ServiceException( e );
        }
       return outputStaticContentData != null ? outputStaticContentData : null;
    }
    
    
	@Override
	//TODO TO BE REVISED
	public List<OutputContentFile> getStaticContentData(
			final String merchantStoreCode, final FileContentType staticContentType) throws ServiceException {

		
		
        if ( cacheManager.getTreeCache() == null )
        {
            throw new ServiceException( "CmsStaticContentFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }
        OutputContentFile outputStaticContentData=null;
        InputStream input = null;
        try
        {
            final Node<String, Object> merchantNode = getMerchantNodeForStaticContent(merchantStoreCode,staticContentType);
            if ( merchantNode == null )
            {
                LOGGER.warn( "merchant node is null" );
                return null;
            }
            

            final StaticContentCacheAttribute contentAttribute = (StaticContentCacheAttribute) merchantNode.get( staticContentType.name() );

            if ( contentAttribute == null )
            {
                LOGGER.warn( "Unable to find content attribute for given merchant" );
                return null;
            }
            
            
            Map<String, byte[]> entries = contentAttribute.getEntities();
            
            if(entries != null) {
            	
            	for(String key : entries.keySet()) {
            		
            		
            		byte[] fileBytes = entries.get(key);
            		
                    if ( fileBytes == null )
                    {
                        LOGGER.warn( "file byte is null, no file found" );
                        continue;
                    }
                    input=new ByteArrayInputStream( fileBytes );
                   
                    final ByteArrayOutputStream output = new ByteArrayOutputStream();
                    IOUtils.copy( input, output );
                    
                    outputStaticContentData=new OutputContentFile();
                    outputStaticContentData.setFile( output );
                    outputStaticContentData.setMimeType( URLConnection.getFileNameMap().getContentTypeFor(key) );
                    outputStaticContentData.setFileName( key );
                    outputStaticContentData.setFileContentType( FileContentType.valueOf( contentAttribute.getDataType() ) );
	
            	}

            }

            
        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while fetching file for {} merchant ", merchantStoreCode);
            throw new ServiceException( e );
        }

		
		return null;
		
		
	}
    
    

    @Override
    public void removeStaticContent( final String merchantStoreCode, final FileContentType staticContentType, final String fileName )
        throws ServiceException
    {

    	
        if ( cacheManager.getTreeCache() == null )
        {
            throw new ServiceException( "CmsStaticContentFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }

        try
        {
            final Node<String, Object> merchantNode = getMerchantNodeForStaticContent(merchantStoreCode,staticContentType);
            if ( merchantNode == null )
            {
                LOGGER.warn( "merchant node is null" );
                return;
  
            }

            merchantNode.remove(fileName);

        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while fetching file for {} merchant ", merchantStoreCode);
            throw new ServiceException( e );
        }

        
    }

    /**
     * Removes the data in a given merchant node
     */
    @Override
    public void removeStaticContents( final String merchantStoreCode )
        throws ServiceException
    {
        
    	//TODO remove all nodes
    	
        if ( cacheManager.getTreeCache() == null )
        {
            throw new ServiceException( "CmsStaticContentFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }

        try
        {
            //final Node<String, Object> merchantNode = getMerchantNodeForStaticContent(merchantStoreCode );
            //if ( merchantNode == null )
            //{
            //    return;
            //}

            

            //merchantNode.clearData();
        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while fetching file for {} merchant ", merchantStoreCode);
            throw new ServiceException( e );
        }

    }
    
    @SuppressWarnings( "unchecked" )
    private Node<String, Object> getMerchantNodeForStaticContent(final String storeCode,final FileContentType contentType)
    {
        LOGGER.debug( "Fetching merchant node for store {} from Infinispan", storeCode );
        final StringBuilder merchantPath = new StringBuilder();
        //merchantPath.append(contentType.name()).append(storeCode );
        
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


    /**
     * Queries the CMS to retrieve all static content files. Only the name of the file will be returned to the client
     * @param merchantStoreCode
     * @return
     * @throws ServiceException
     */
	@Override
	//TODO BE REVISED
	public List<String> getStaticContentDataName(final String merchantStoreCode, final FileContentType staticContentType)
			throws ServiceException {
		
		
		
	       if ( cacheManager.getTreeCache() == null )
	        {
	            throw new ServiceException( "CmsStaticContentFileManagerInfinispan has a null cacheManager.getTreeCache()" );
	        }

	        try
	        {
	            final Node<String, Object> merchantNode = getMerchantNodeForStaticContent(merchantStoreCode,staticContentType);

	            
	            if ( merchantNode == null )
	            {
	                LOGGER.warn( "Unable to find content attribute for given merchant" );
	                return Collections.<String> emptyList();
	            }
	            

	            if ( MapUtils.isEmpty( merchantNode.getData()) )
	            {
	                LOGGER.warn( "No Content for merchant store with code {}", merchantStoreCode);
	                return Collections.<String> emptyList();
	            }
	            
	             return new ArrayList<String>(merchantNode.getData().keySet());

	        }
	        catch ( final Exception e )
	        {
	            LOGGER.error( "Error while fetching file for {} merchant ", merchantStoreCode);
	            throw new ServiceException( e );
	        }

	}


}
