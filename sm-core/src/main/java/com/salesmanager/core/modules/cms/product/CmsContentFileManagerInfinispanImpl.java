package com.salesmanager.core.modules.cms.product;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public void stopFileManager()
    {

        try
        {
            manager.stop();
            LOGGER.info( "Stopping CMS" );
        }
        catch ( Exception e )
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
            catch ( Exception e )
            {
                LOGGER.error( "Error while instantiating CmsContentFileManager", e );
            }
        }

        return fileManager;

    }

    @SuppressWarnings( "unchecked" )
    @Override
    public List<String> getImageNames( MerchantStore store, ImageContentType imageContentType )
        throws ServiceException
    {
        if ( treeCache == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null treeCache" );
        }

        List<String> returnNames = new ArrayList<String>();
        try
        {

            StringBuilder filePath = new StringBuilder();
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

            Fqn imageFile = Fqn.fromString( filePath.toString() );

            if ( imageFile == null )
            {
                return null;
            }

            Node<String, Object> imageFileFileTree = treeCache.getRoot().getChild( imageFile );

            if ( imageFileFileTree == null )
            {
                return null;
            }

            Set<String> names = imageFileFileTree.getKeys();

            for ( String name : names )
            {

                returnNames.add( name );

            }

        }
        catch ( Exception e )
        {
            throw new ServiceException( e );
        }

        return returnNames;
    }

    /**
     * Returns the physical files
     */
    @Override
    public List<OutputContentImage> getImages( MerchantStore store, ImageContentType imageContentType )
        throws ServiceException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeImages( MerchantStore store )
        throws ServiceException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeImage( ContentImage contentImage )
        throws ServiceException
    {
        // TODO Auto-generated method stub

    }

    /**
     * Returns the physical file
     */

    @Override
    public OutputContentImage getImage( MerchantStore store, String imageName, ImageContentType imageContentType )
        throws ServiceException
    {

        if ( treeCache == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null treeCache" );
        }

        OutputContentImage contentImage = new OutputContentImage();
        InputStream input = null;

        try
        {

            Node<String, Object> merchantNode = getMerchantNode( store.getId() );
            if ( merchantNode == null )
            {
                LOGGER.warn( "merchant node is null" );
                return null;
            }

            CacheAttribute contentAttribute = (CacheAttribute) merchantNode.get( IMAGE_CONTENT );

            if ( contentAttribute == null )
            {
                LOGGER.warn( "Unable to find content attribute for given merchant" );
                return null;
            }

            byte[] imageBytes = (byte[]) contentAttribute.getEntities().get( imageName );

            if ( imageBytes == null )
            {
                LOGGER.warn( "Image byte is null, no image found" );
                return null;
            }
            input = new ByteArrayInputStream( imageBytes );
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy( input, output );
            contentImage.setImage( output );
            contentImage.setImageContentType( URLConnection.guessContentTypeFromStream( input ) );
            contentImage.setImageName( imageName );

        }
        catch ( Exception e )
        {
            LOGGER.error( "Error while fetching content image for {} merchant ", store.getId() );
            throw new ServiceException( e );
        }

        return contentImage;

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
    public void addImage( MerchantStore store, InputContentImage image )
        throws ServiceException
    {

        if ( treeCache == null )
        {
            LOGGER.error( "Unable to find treeCache in Infinispan.." );
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null treeCache" );
        }

        try
        {

            Node<String, Object> merchantNode = getMerchantNode( store.getId() );
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
        catch ( Exception e )
        {
            LOGGER.error( "Error while saving content image", e );
            throw new ServiceException( e );

        }

    }

    @SuppressWarnings( "unchecked" )
    private Node<String, Object> getMerchantNode( final Integer storeId )
    {
        LOGGER.debug( "Fetching merchant node for store {} from Infinispan", storeId );
        StringBuilder merchantPath = new StringBuilder();
        merchantPath.append( "merchant-" ).append( String.valueOf( storeId ) );

        Fqn contentFiles = Fqn.fromString( "contentFiles" );
        Node<String, Object> contentFilesNode = treeCache.getRoot().getChild( contentFiles );

        if ( contentFilesNode == null )
        {
            treeCache.getRoot().addChild( contentFiles );
            contentFilesNode = treeCache.getRoot().getChild( contentFiles );

        }

        Node<String, Object> merchantNode = contentFilesNode.getChild( Fqn.fromString( merchantPath.toString() ) );

        if ( merchantNode == null )
        {
            Fqn merchantFqn = Fqn.fromString( merchantPath.toString() );
            contentFilesNode.addChild( merchantFqn );
            merchantNode = treeCache.getRoot().getChild( Fqn.fromElements( "contentFiles", merchantPath.toString() ) );
        }

        return merchantNode;
    }

}
