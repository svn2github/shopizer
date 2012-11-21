package com.salesmanager.core.modules.cms.product;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
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

public class CmsContentFileManagerInfinispanImpl
    extends CmsFileManagerInfinispan
    implements ImagePut, ContentImageGet, ContentImageRemove
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CmsContentFileManagerInfinispanImpl.class );

    private static CmsContentFileManagerInfinispanImpl fileManager = null;

    private final static String IMAGE_CONTENT = "IMAGE_CONTENT";

    private final static String CONTENT_FILES = "contentFiles";

    /**
     * Requires to stop the engine when image servlet un-deploys
     */
    @Override
    public void stopFileManager()
    {

        try
        {
            manager.stop();
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
            try
            {
                fileManager.initFileManager( CONTENT_FILES );
            }
            catch ( final Exception e )
            {
                LOGGER.error( "Error while instantiating CmsContentFileManager", e );
            }
        }

        return fileManager;

    }

    @SuppressWarnings( "unchecked" )
    @Override
    public List<String> getImageNames( final MerchantStore store, final ImageContentType imageContentType )
        throws ServiceException
    {
        if ( treeCache == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null treeCache" );
        }

        final List<String> returnNames = new ArrayList<String>();
        try
        {

            final StringBuilder filePath = new StringBuilder();
            filePath.append( "/contentFiles/" ).append( "merchant-" ).append( String.valueOf( store.getId() ) );// .append("/")

            // a logo or content
            if ( imageContentType.equals( ImageContentType.LOGO ) )
            {

                filePath.append( "/logo" );

            }
            else
            {

                // if(image.getContentType().equals(ImageContentType.CONTENT)) {

                filePath.append( "/content" );

            }

            final Fqn imageFile = Fqn.fromString( filePath.toString() );

            if ( imageFile == null )
            {
                return null;
            }

            final Node<String, Object> imageFileFileTree = treeCache.getRoot().getChild( imageFile );

            if ( imageFileFileTree == null )
            {
                return null;
            }

            final Set<String> names = imageFileFileTree.getKeys();

            for ( final String name : names )
            {

                returnNames.add( name );

            }

        }
        catch ( final Exception e )
        {
            throw new ServiceException( e );
        }

        return returnNames;
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
    public List<OutputContentImage> getImages( final MerchantStore store, final ImageContentType imageContentType )
        throws ServiceException
    {
        if ( treeCache == null )
        {
            LOGGER.error( "Unable to find treeCache in Infinispan.." );
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null treeCache" );
        }

        OutputContentImage contentImage = null;
        InputStream input = null;
        List<OutputContentImage> contentImagesList = null;
        try
        {
            final Node<String, Object> merchantNode = getMerchantNode( store.getId() );
            if ( merchantNode == null )
            {
                LOGGER.warn( "merchant node is null" );
                return Collections.<OutputContentImage> emptyList();
            }
            final CacheAttribute contentAttribute = (CacheAttribute) merchantNode.get( IMAGE_CONTENT );

            if ( contentAttribute == null )
            {
                LOGGER.warn( "Unable to find content attribute for given merchant" );
                return Collections.<OutputContentImage> emptyList();
            }

            if ( MapUtils.isEmpty( contentAttribute.getEntities() ) )
            {
                LOGGER.warn( "No Content image for merchant with {}", store.getId() );
                return Collections.<OutputContentImage> emptyList();
            }

            contentImagesList = new ArrayList<OutputContentImage>();
            for ( final Map.Entry<String, byte[]> entry : contentAttribute.getEntities().entrySet() )
            {
                input = new ByteArrayInputStream( entry.getValue() );
                final ByteArrayOutputStream output = new ByteArrayOutputStream();
                IOUtils.copy( input, output );
                contentImage = new OutputContentImage();
                contentImage.setImage( output );
                contentImage.setImageContentType( URLConnection.guessContentTypeFromStream( input ) );
                contentImage.setImageName( entry.getKey() );
                contentImagesList.add( contentImage );
       }
        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while fetching content image for {} merchant ", store.getId() );
            throw new ServiceException( e );
        }
        return contentImagesList != null ? contentImagesList : Collections.<OutputContentImage> emptyList();
    }

    @Override
    public void removeImages( final MerchantStore store )
        throws ServiceException
    {
        LOGGER.info( "Removing all images for {} merchant ",store.getId() );
        if ( treeCache == null )
        {
            LOGGER.error( "Unable to find treeCache in Infinispan.." );
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null treeCache" );
        }
        
        try
        {
            final Node<String, Object> merchantNode = getMerchantNode( store.getId() );
            if ( merchantNode == null )
            {
                LOGGER.warn( "merchant node is null" );
                throw new ServiceException("unable to get merchant node for CmsImageFileManagerInfinispan");
            } 
            
            merchantNode.clearData();
            LOGGER.info( "All images for merchant {} removed from cache",store.getId() );

        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while deleting content image for {} merchant ", store.getId() );
            throw new ServiceException( e );
        }
   }

    @Override
    public void removeImage( final ContentImage contentImage )
        throws ServiceException
    {
        // TODO Auto-generated method stub

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
    public OutputContentImage getImage( final MerchantStore store, final String imageName,
                                        final ImageContentType imageContentType )
        throws ServiceException
    {

        if ( treeCache == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null treeCache" );
        }

        OutputContentImage contentImage = null;
        InputStream input = null;

        try
        {

            final Node<String, Object> merchantNode = getMerchantNode( store.getId() );
            if ( merchantNode == null )
            {
                LOGGER.warn( "merchant node is null" );
                return null;
            }

            final CacheAttribute contentAttribute = (CacheAttribute) merchantNode.get( IMAGE_CONTENT );

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
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy( input, output );
            contentImage = new OutputContentImage();
            contentImage.setImage( output );
            contentImage.setImageContentType( URLConnection.guessContentTypeFromStream( input ) );
            contentImage.setImageName( imageName );
        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while fetching content image for {} merchant ", store.getId() );
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
    public void addImage( final MerchantStore store, final InputContentImage image )
        throws ServiceException
    {

        if ( treeCache == null )
        {
            LOGGER.error( "Unable to find treeCache in Infinispan.." );
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null treeCache" );
        }

        try
        {

            final Node<String, Object> merchantNode = getMerchantNode( store.getId() );
            // object for a given merchant containing all images
            CacheAttribute contentAttribute = (CacheAttribute) merchantNode.get( IMAGE_CONTENT );

            if ( contentAttribute == null )
            {
                contentAttribute = new CacheAttribute();
                // contentAttribute.setEntityId( IMAGE_CONTENT );
            }

            contentAttribute.getEntities().put( image.getImageName(), image.getFile().toByteArray() );
            merchantNode.put( IMAGE_CONTENT, contentAttribute );
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
    public void addImagees( final MerchantStore store, final List<InputContentImage> imagesList )
        throws ServiceException
    {
        if ( treeCache == null )
        {
            LOGGER.error( "Unable to find treeCache in Infinispan.." );
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null treeCache" );
        }

        try
        {

            final Node<String, Object> merchantNode = getMerchantNode( store.getId() );
            // object for a given merchant containing all images
            CacheAttribute contentAttribute = (CacheAttribute) merchantNode.get( IMAGE_CONTENT );

            if ( contentAttribute == null )
            {
                contentAttribute = new CacheAttribute();
               
            }

            for(final InputContentImage image:imagesList){
                contentAttribute.getEntities().put( image.getImageName(), image.getFile().toByteArray() );
            }
            merchantNode.put( IMAGE_CONTENT, contentAttribute );
            LOGGER.info( "Total {} content images added successfully.",imagesList.size() );

        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while saving content image", e );
            throw new ServiceException( e );

        }
        
    }

    @SuppressWarnings( "unchecked" )
    private Node<String, Object> getMerchantNode( final Integer storeId )
    {
        LOGGER.debug( "Fetching merchant node for store {} from Infinispan", storeId );
        final StringBuilder merchantPath = new StringBuilder();
        merchantPath.append( "merchant-" ).append( String.valueOf( storeId ) );

        final Fqn contentFiles = Fqn.fromString( "contentFiles" );
        Node<String, Object> contentFilesNode = treeCache.getRoot().getChild( contentFiles );

        if ( contentFilesNode == null )
        {
            treeCache.getRoot().addChild( contentFiles );
            contentFilesNode = treeCache.getRoot().getChild( contentFiles );

        }

        Node<String, Object> merchantNode = contentFilesNode.getChild( Fqn.fromString( merchantPath.toString() ) );

        if ( merchantNode == null )
        {
            final Fqn merchantFqn = Fqn.fromString( merchantPath.toString() );
            contentFilesNode.addChild( merchantFqn );
            merchantNode = treeCache.getRoot().getChild( Fqn.fromElements( "contentFiles", merchantPath.toString() ) );
        }

        return merchantNode;
    }

   

}
