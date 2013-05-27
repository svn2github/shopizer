package com.salesmanager.core.modules.cms.product;

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
import com.salesmanager.core.modules.cms.common.CacheAttribute;
import com.salesmanager.core.modules.cms.content.ContentImageGet;
import com.salesmanager.core.modules.cms.content.ContentImageRemove;
import com.salesmanager.core.modules.cms.content.ImagePut;
import com.salesmanager.core.modules.cms.impl.CacheManager;


public class CmsContentFileManagerInfinispanImpl
    implements ImagePut, ContentImageGet, ContentImageRemove
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CmsContentFileManagerInfinispanImpl.class );

    private static CmsContentFileManagerInfinispanImpl fileManager = null;


    private CacheManager cacheManager;

    /**
     * Requires to stop the engine when image servlet un-deploys
     */

    public void stopFileManager()
    {

        try
        {
        	cacheManager.getManager().stop();
            LOGGER.info( "Stopping CMS" );
        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while stopping CmsContentFileManager", e );
        }
    }

    public static CmsContentFileManagerInfinispanImpl getInstance()
    {

        if ( fileManager == null )
        {
            fileManager = new CmsContentFileManagerInfinispanImpl();
        }

        return fileManager;

    }


    @Override
    public List<String> getImageNames( final String merchantStoreCode, final FileContentType imageContentType )
        throws ServiceException
    {
        if ( cacheManager.getTreeCache() == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }


            final Node<String, Object> merchantNode = getMerchantNode( merchantStoreCode);
            if ( merchantNode == null )
            {
                LOGGER.warn( "merchant node is null" );
                return Collections.<String> emptyList();
            }
            final CacheAttribute contentAttribute = (CacheAttribute) merchantNode.get( imageContentType.name() );

            if ( contentAttribute == null )
            {
                LOGGER.warn( "Unable to find content attribute for given merchant" );
                return Collections.<String> emptyList();
            }
            if ( MapUtils.isEmpty( contentAttribute.getEntities() ) )
            {
                LOGGER.warn( "No Content image for merchant store with code {}", merchantStoreCode);
                return Collections.<String> emptyList();
            }
            
             return new ArrayList<String>(contentAttribute.getEntities().keySet());



    }

    /**
     * Method to return all images for a given merchant store.Content images are being stored Infinispan cache tree.
     * Based on the Merchant store id,it will try to return all content images associated with Merchant store. In case
     * no content image(s) are associated with the given Merchant store, an empty list will be returned.
     * 
     * @param store Merchant store for which associated images will be returned.
     * @param imageContentType
     * @return list of {@link OutputContentImage} or empty list
     * @throws ServiceException
     */
    @Override
    public List<OutputContentFile> getImages( final String merchantStoreCode, final FileContentType imageContentType )
        throws ServiceException
    {
        if ( cacheManager.getTreeCache() == null )
        {
            LOGGER.error( "Unable to find cacheManager.getTreeCache() in Infinispan.." );
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }

        OutputContentFile contentImage = null;
        InputStream input = null;
        List<OutputContentFile> contentImagesList = null;
        try
        {
            final Node<String, Object> merchantNode = getMerchantNode(merchantStoreCode);
            if ( merchantNode == null )
            {
                LOGGER.warn( "merchant node is null" );
                return Collections.<OutputContentFile> emptyList();
            }
            final CacheAttribute contentAttribute = (CacheAttribute) merchantNode.get( imageContentType.name() );

            if ( contentAttribute == null )
            {
                LOGGER.warn( "Unable to find content attribute for given merchant" );
                return Collections.<OutputContentFile> emptyList();
            }

            if ( MapUtils.isEmpty( contentAttribute.getEntities() ) )
            {
                LOGGER.warn( "No Content image for merchant with code {}", merchantStoreCode);
                return Collections.<OutputContentFile> emptyList();
            }

            contentImagesList = new ArrayList<OutputContentFile>();
            for ( final Map.Entry<String, byte[]> entry : contentAttribute.getEntities().entrySet() )
            {
                input = new ByteArrayInputStream( entry.getValue() );
                final ByteArrayOutputStream output = new ByteArrayOutputStream();
                IOUtils.copy( input, output );
                contentImage = new OutputContentFile();
                contentImage.setFile( output );
                contentImage.setMimeType( URLConnection.guessContentTypeFromStream( input ) );
                contentImage.setFileName( entry.getKey() );
                contentImagesList.add( contentImage );
       }
        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while fetching content image for {} merchant ", merchantStoreCode);
            throw new ServiceException( e );
        }
        return contentImagesList != null ? contentImagesList : Collections.<OutputContentFile> emptyList();
    }

    /**
     * Method to remove all images for a given merchant store.
     * This method will take care of removing all images associated for a given merchant store from underlying Infinispan cache tree.
     * @param store merchant store whose associated images will be removed
     * @throws ServiceException
     * 
     */
    @Override
    public void removeImages( final String merchantStoreCode)
        throws ServiceException
    {
        LOGGER.info( "Removing all images for {} merchant ",merchantStoreCode);
        if ( cacheManager.getTreeCache() == null )
        {
            LOGGER.error( "Unable to find cacheManager.getTreeCache() in Infinispan.." );
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }
        
        try
        {
            final Node<String, Object> merchantNode = getMerchantNode(merchantStoreCode );
            if ( merchantNode == null )
            {
                LOGGER.warn( "merchant node is null" );
                throw new ServiceException("unable to get merchant node for CmsImageFileManagerInfinispan");
            } 
            
            merchantNode.clearData();
            LOGGER.info( "All images for merchant {} removed from cache",merchantStoreCode);

        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while deleting content image for {} merchant ", merchantStoreCode);
            throw new ServiceException( e );
        }
   }

    /**
     * It will take care of removing given content image associated with merchant store from the 
     * Infinispan tree cache.
     * In case no such images is associated with the given merchant store {@link ServiceException} will be thrown.
     * 
     * @param store merchant store
     * @param contentImage content images intended for removal
     * @throws ServiceException
     */
    @Override
    public void removeImage(final String merchantStoreCode, final FileContentType imageContentType, final String imageName)
        throws ServiceException
    {
        LOGGER.info( "Removing image {}  for {} merchant ",imageName,merchantStoreCode);
        
        if ( cacheManager.getTreeCache() == null )
        {
            LOGGER.error( "Unable to find cacheManager.getTreeCache() in Infinispan.." );
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }
        try
        {
            final Node<String, Object> merchantNode = getMerchantNode(merchantStoreCode);
            if ( merchantNode == null )
            {
                LOGGER.warn( "merchant node is null" );
                throw new ServiceException("unable to get merchant node for CmsImageFileManagerInfinispan");
            } 
            
            final CacheAttribute contentAttribute = (CacheAttribute) merchantNode.get( imageContentType.name());
            if(contentAttribute ==null){
                LOGGER.warn( "No Content data found for given merchant" );
                return ;
            }
            
            contentAttribute.getEntities().remove( imageName );
            merchantNode.put( imageContentType.name(), contentAttribute );
            LOGGER.info( "Content image added successfully." );
            LOGGER.info( "All images for merchant {} removed from cache",merchantStoreCode);

        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while deleting content image for {} merchant ", merchantStoreCode);
            throw new ServiceException( e );
        }
}

    /**
     * Method to return physical image for given Merchant store based on the imageName. Content image will be searched
     * in underlying Infinispan cache tree and {@link OutputContentImage} will be returned on finding an associated
     * image. In case of no image null be returned.
     * 
     * @param store Merchant store
     * @param imageName name of image being requested
     * @param imageContentType
     * @return {@link OutputContentImage}
     * @throws ServiceException
     */
    @Override
    public OutputContentFile getImage(final String merchantStoreCode, final String imageName,
                                        final FileContentType imageContentType )
        throws ServiceException
    {

        if ( cacheManager.getTreeCache() == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }

        OutputContentFile contentImage = null;
        InputStream input = null;

        try
        {

            final Node<String, Object> merchantNode = getMerchantNode(merchantStoreCode );
            if ( merchantNode == null )
            {
                LOGGER.warn( "merchant node is null" );
                return null;
            }

            final CacheAttribute contentAttribute = (CacheAttribute) merchantNode.get( imageContentType.name());

            if ( contentAttribute == null )
            {
                LOGGER.warn( "Unable to find content attribute for given merchant" );
                return null;
            }

            final byte[] imageBytes = contentAttribute.getEntities().get( imageName );

            if ( imageBytes == null )
            {
                LOGGER.warn( "Image byte is null, no image found" );
                return null;
            }
            input = new ByteArrayInputStream( imageBytes );
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy( input, output );
            contentImage = new OutputContentFile();
            contentImage.setFile( output );
            contentImage.setMimeType( URLConnection.guessContentTypeFromStream( input ) );
            contentImage.setFileName( imageName );
        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while fetching content image for {} merchant ", merchantStoreCode);
            throw new ServiceException( e );
        }

        return contentImage != null ? contentImage : null;

    }

    /**
     * <p>
     * Method to add image for given store.Content Images will be stored in Infinispan and will be retrieved based on
     * the storeID. Following steps will be performed to store content images
     * </p>
     * <li>Merchant Node will be retrieved from the cacheTree if it exists else new node will be created.</li> <li>
     * Images will be stored in CacheAttribute , which eventually will be stored in Infinispan</li>
     * 
     * @param store Merchant store for which images is getting stored in Infinispan.
     * @param image input content image which will get {@link InputContentImage}
     * @throws ServiceException if content image storing process fail.
     * @see InputContentImage
     * @see CacheAttribute
     * @see CmsFileManagerInfinispan
     */

    @Override
    public void addImage( final String merchantStoreCode, final InputContentFile image )
        throws ServiceException
    {

        if ( cacheManager.getTreeCache() == null )
        {
            LOGGER.error( "Unable to find cacheManager.getTreeCache() in Infinispan.." );
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }

        try
        {

            final Node<String, Object> merchantNode = getMerchantNode(merchantStoreCode);
            // object for a given merchant containing all images
            CacheAttribute contentAttribute = (CacheAttribute) merchantNode.get( image.getFileContentType().name() );

            if ( contentAttribute == null )
            {
                contentAttribute = new CacheAttribute();
                // contentAttribute.setEntityId( IMAGE_CONTENT );
            }
            
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy( image.getFile(), output );
            
            System.out.println(output.toByteArray());

            contentAttribute.getEntities().put( image.getFileName(), output.toByteArray() );
            merchantNode.put( image.getFileContentType().name(), contentAttribute );
            LOGGER.info( "Content image added successfully." );

        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while saving content image", e );
            throw new ServiceException( e );

        }

    }
    
    /**
     * <p>
     * Method to add images for given store.Content Images will be stored in Infinispan and will be retrieved based on
     * the storeID. Following steps will be performed to store content images
     * </p>
     * <li>Merchant Node will be retrieved from the cacheTree if it exists else new node will be created.</li> <li>
     * Images will be stored in CacheAttribute , which eventually will be stored in Infinispan</li>
     * 
     * @param store Merchant store for which images is getting stored in Infinispan.
     * @param imagesList input content images list which will get {@link InputContentImage} stored
     * @throws ServiceException if content image storing process fail.
     * @see InputContentImage
     * @see CacheAttribute
     * @see CmsFileManagerInfinispan
     */
    @Override
    public void addImages(final String merchantStoreCode, final List<InputContentFile> imagesList )
        throws ServiceException
    {
        if ( cacheManager.getTreeCache() == null )
        {
            LOGGER.error( "Unable to find cacheManager.getTreeCache() in Infinispan.." );
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }

        try
        {

            final Node<String, Object> merchantNode = getMerchantNode(merchantStoreCode);
            // object for a given merchant containing all images

            for(final InputContentFile image:imagesList){
            	
            	String cmsType = image.getFileContentType().name();
            	
                CacheAttribute contentAttribute = (CacheAttribute) merchantNode.get( cmsType );

                if ( contentAttribute == null )
                {
                    contentAttribute = new CacheAttribute();
                   
                }
                
                final ByteArrayOutputStream output = new ByteArrayOutputStream();
                IOUtils.copy( image.getFile(), output );
            	
                contentAttribute.getEntities().put( image.getFileName(), output.toByteArray() );
                merchantNode.put( cmsType, contentAttribute );
            }
            
            LOGGER.info( "Total {} content images added successfully.",imagesList.size() );

        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while saving content image", e );
            throw new ServiceException( e );

        }
        
    }

    @SuppressWarnings( "unchecked" )
    private Node<String, Object> getMerchantNode(final String storeCode )
    {
        LOGGER.debug( "Fetching merchant node for store {} from Infinispan", storeCode );
        final StringBuilder merchantPath = new StringBuilder();
        merchantPath.append( "content-merchant-" ).append(storeCode );

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
