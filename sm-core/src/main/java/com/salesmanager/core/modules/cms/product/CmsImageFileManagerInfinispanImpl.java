package com.salesmanager.core.modules.cms.product;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.infinispan.tree.Fqn;
import org.infinispan.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.content.model.image.InputContentImage;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.modules.cms.common.CacheAttribute;
import com.salesmanager.core.modules.cms.common.CmsFileManagerInfinispan;
import com.salesmanager.core.utils.CoreConfiguration;

/**
 * Manager for storing in retrieving image files from the CMS This is a layer on top of Infinispan
 * https://docs.jboss.org/author/display/ISPN/Tree+API+Module
 * 
 * @author csamson
 */
public class CmsImageFileManagerInfinispanImpl
    extends CmsFileManagerInfinispan
    implements ProductImagePut, ProductImageGet, ProductImageRemove
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CmsImageFileManagerInfinispanImpl.class );

    private static CmsImageFileManagerInfinispanImpl fileManager = null;

    private final static String PRODUCT_FILES = "productFiles";

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
            LOGGER.error( "Error while stopping CmsImageFileManager", e );
        }
    }

    public static CmsImageFileManagerInfinispanImpl getInstance()
    {

        if ( fileManager == null )
        {
            fileManager = new CmsImageFileManagerInfinispanImpl();
            try
            {
                fileManager.initFileManager( PRODUCT_FILES );
            }
            catch ( Exception e )
            {
                LOGGER.error( "Error while instantiating CmsImageFileManager", e );
            }
        }

        return fileManager;

    }

    private CmsImageFileManagerInfinispanImpl()
    {

    }

    /**
     * root -productFiles -merchant-id PRODUCT-ID(key) -> CacheAttribute(value) - image 1 - image 2 - image 3
     */

    @SuppressWarnings( "unchecked" )
    @Override
    public void uploadProductImage( CoreConfiguration configuration, ProductImage productImage,
                                    InputContentImage contentImage )
        throws ServiceException
    {

        if ( treeCache == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null treeCache" );
        }

        try
        {

            // retrieve merchant node
            StringBuilder merchantPath = new StringBuilder();
            merchantPath.append( "merchant-" ).append( String.valueOf( productImage.getProduct().getMerchantStore().getId() ) );

            // product key
            String productPath = String.valueOf( productImage.getProduct().getId() );
            Node<String, Object> productFilesNode = treeCache.getRoot().getChild( Fqn.fromString( "productFiles" ) );

            Node<String, Object> merchantNode = productFilesNode.getChild( Fqn.fromString( merchantPath.toString() ) );

            if ( merchantNode == null )
            {
                Fqn merchantFqn = Fqn.fromString( merchantPath.toString() );
                productFilesNode.addChild( merchantFqn );
                merchantNode =
                    treeCache.getRoot().getChild( Fqn.fromElements( "productFiles", merchantPath.toString() ) );
            }

            // object for a given product containing all images
            CacheAttribute productAttribute = (CacheAttribute) merchantNode.get( productPath );

            if ( productAttribute == null )
            {
                productAttribute = new CacheAttribute();
                productAttribute.setEntityId( productPath );
            }

            byte[] imageBytes = contentImage.getFile().toByteArray();
            productAttribute.getEntities().put( contentImage.getImageName(), imageBytes );

            merchantNode.put( productPath, productAttribute );

        }
        catch ( Exception e )
        {

            throw new ServiceException( e );

        }

    }

    @SuppressWarnings( "unchecked" )
    @Override
    public OutputContentImage getProductImage( ProductImage productImage )
        throws ServiceException
    {

        if ( treeCache == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null treeCache" );
        }
        InputStream input = null;
        OutputContentImage contentImage = new OutputContentImage();
        try
        {

            StringBuilder merchantPath = new StringBuilder()
            // filePath.append("/productFiles/")
            .append( "merchant-" ).append( String.valueOf( productImage.getProduct().getMerchantStore().getId() ) );
            // .append("product-").append(String.valueOf(productImage.getProduct().getId()));

            String productPath = String.valueOf( productImage.getProduct().getId() );

            // Fqn productFiles = Fqn.fromString(filePath.toString());

            // Node<String, Object> productFilesTree = treeCache.getRoot().getChild(productFiles);

            Node<String, Object> productFilesNode = treeCache.getRoot().getChild( Fqn.fromString( "productFiles" ) );

            Node<String, Object> merchantNode = productFilesNode.getChild( Fqn.fromString( merchantPath.toString() ) );

            if ( merchantNode == null )
            {
                return null;
            }

            CacheAttribute productAttribute = (CacheAttribute) merchantNode.get( productPath );

            if ( productAttribute == null )
            {
                return null;
            }

            byte[] imageBytes = (byte[]) productAttribute.getEntities().get( productImage.getProductImage() );

            if ( imageBytes == null )
            {
                return null;
            }

            input = new ByteArrayInputStream( imageBytes );
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy( input, output );

            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            String contentType = fileNameMap.getContentTypeFor( productImage.getProductImage() );

            contentImage.setImage( output );
            contentImage.setImageContentType( contentType );
            contentImage.setImageName( productImage.getProductImage() );

        }
        catch ( Exception e )
        {
            throw new ServiceException( e );
        }
        finally
        {
            if ( input != null )
            {
                try
                {
                    input.close();
                }
                catch ( Exception ignore )
                {
                }
            }
        }

        return contentImage;

    }

    @SuppressWarnings( "unchecked" )
    public List<OutputContentImage> getImages( MerchantStore store, ImageContentType imageContentType )
        throws ServiceException
    {

        if ( treeCache == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null treeCache" );
        }
        List<OutputContentImage> images = new ArrayList<OutputContentImage>();
        FileNameMap fileNameMap = URLConnection.getFileNameMap();

        try
        {

            StringBuilder merchantPath =
                new StringBuilder().append( "merchant-" ).append( String.valueOf( store.getId() ) );

            Node<String, Object> productFilesNode = treeCache.getRoot().getChild( Fqn.fromString( "productFiles" ) );

            Node<String, Object> merchantNode = productFilesNode.addChild( Fqn.fromString( merchantPath.toString() ) );

            if ( merchantNode == null )
            {
                return null;
            }

            Set<String> keys = merchantNode.getKeys();
            for ( String key : keys )
            {
                // Product
                CacheAttribute productAttribute = (CacheAttribute) merchantNode.get( key );

                Map<String, byte[]> imgs = productAttribute.getEntities();
                Set<String> imageNames = imgs.keySet();
                for ( String imageName : imageNames )
                {

                    byte[] imageBytes = imgs.get( imageName );

                    OutputContentImage contentImage = new OutputContentImage();

                    InputStream input = new ByteArrayInputStream( imageBytes );
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    IOUtils.copy( input, output );

                    String contentType = fileNameMap.getContentTypeFor( imageName );

                    contentImage.setImage( output );
                    contentImage.setImageContentType( contentType );
                    contentImage.setImageName( imageName );

                    images.add( contentImage );

                }

            }

            /*
             * Set<Node<String, Object>> childNodes = merchantNode.getChildren(); if(childNodes!=null) {
             * for(@SuppressWarnings("rawtypes") Node node : childNodes) {//productId //Set<String> names =
             * node.getChildrenNames();//imageNames //if(names!=null && names.size()>0) { //for(String name : names) {
             * //CacheAttribute productAttribute OutputContentImage contentImage = new OutputContentImage(); byte[]
             * imageBytes = (byte[])node.get(name); InputStream input = new ByteArrayInputStream(imageBytes);
             * ByteArrayOutputStream output = new ByteArrayOutputStream(); IOUtils.copy(input, output); String
             * contentType = fileNameMap.getContentTypeFor(name); contentImage.setImage(output);
             * contentImage.setImageContentType(contentType); contentImage.setImageName(name); images.add(contentImage);
             * //} //} //} }
             */

        }
        catch ( Exception e )
        {
            throw new ServiceException( e );
        }
        finally
        {

        }

        return images;

    }

    @SuppressWarnings( "unchecked" )
    @Override
    public List<OutputContentImage> getImages( Product product )
        throws ServiceException
    {

        if ( treeCache == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null treeCache" );
        }

        List<OutputContentImage> images = new ArrayList<OutputContentImage>();
        FileNameMap fileNameMap = URLConnection.getFileNameMap();

        try
        {

            StringBuilder merchantPath =
                new StringBuilder().append( "merchant-" ).append( String.valueOf( product.getMerchantStore().getId() ) );

            Node<String, Object> productFilesNode = treeCache.getRoot().getChild( Fqn.fromString( "productFiles" ) );

            Node<String, Object> merchantNode = productFilesNode.addChild( Fqn.fromString( merchantPath.toString() ) );

            if ( merchantNode == null )
            {
                return null;
            }

            CacheAttribute productAttribute = (CacheAttribute) merchantNode.get( String.valueOf( product.getId() ) );

            if ( productAttribute == null )
            {
                return null;
            }

            Map<String, byte[]> imgs = productAttribute.getEntities();
            Set<String> imageNames = imgs.keySet();
            for ( String imageName : imageNames )
            {

                byte[] imageBytes = imgs.get( imageName );

                OutputContentImage contentImage = new OutputContentImage();

                InputStream input = new ByteArrayInputStream( imageBytes );
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                IOUtils.copy( input, output );

                String contentType = fileNameMap.getContentTypeFor( imageName );

                contentImage.setImage( output );
                contentImage.setImageContentType( contentType );
                contentImage.setImageName( imageName );

                images.add( contentImage );

            }

        }
        catch ( Exception e )
        {
            throw new ServiceException( e );
        }
        finally
        {

        }

        return images;
    }

    @Override
    public void removeImages( MerchantStore store )
        throws ServiceException
    {
        if ( treeCache == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null treeCache" );
        }

        try
        {

            StringBuilder filePath = new StringBuilder();
            filePath.append( "/productFiles/" ).append( "merchant-" ).append( String.valueOf( store.getId() ) );

            Fqn merchantFiles = Fqn.fromString( filePath.toString() );

            treeCache.removeNode( merchantFiles );

        }
        catch ( Exception e )
        {
            throw new ServiceException( e );
        }
        finally
        {

        }

    }

    @SuppressWarnings( "unchecked" )
    @Override
    public void removeProductImage( ProductImage productImage )
        throws ServiceException
    {

        if ( treeCache == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null treeCache" );
        }

        try
        {

            StringBuilder filePath = new StringBuilder();
            filePath.append( "/productFiles/" ).append( "merchant-" ).append( String.valueOf( productImage.getProduct().getMerchantStore().getId() ) ).append( "/" ).append( "product-" ).append( String.valueOf( productImage.getProduct().getId() ) );

            Fqn pi = Fqn.fromString( filePath.toString() );

            Node<String, Object> productFilesTree = treeCache.getRoot().getChild( pi );

            productFilesTree.remove( productImage.getProductImage() );

        }
        catch ( Exception e )
        {
            throw new ServiceException( e );
        }
        finally
        {

        }

    }

    @Override
    public void removeProductImages( Product product )
        throws ServiceException
    {

        if ( treeCache == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null treeCache" );
        }

        try
        {

            StringBuilder filePath = new StringBuilder();
            filePath.append( "/productFiles/" ).append( "merchant-" ).append( String.valueOf( product.getMerchantStore().getId() ) ).append( "/" ).append( "product-" ).append( String.valueOf( product.getId() ) );

            Fqn productFiles = Fqn.fromString( filePath.toString() );

            treeCache.removeNode( productFiles );

        }
        catch ( Exception e )
        {
            throw new ServiceException( e );
        }
        finally
        {

        }

    }

    public String getRepositoryFileName()
    {
        return repositoryFileName;
    }

    public void setRepositoryFileName( String repositoryFileName )
    {
        this.repositoryFileName = repositoryFileName;
    }

}
