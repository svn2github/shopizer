package com.salesmanager.core.modules.cms.product;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.FileNameMap;
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
    
    private final static String STORE_MERCHANT = "store-merchant";


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


        
        
        
    		StringBuilder nodePath = new StringBuilder();
    		nodePath.append(merchantStoreCode).append("/").append(imageContentType.name());
    	
    		Node<String, Object> objectNode = this.getNode(nodePath.toString());
    		
    		if(objectNode.getKeys().isEmpty()) {
    			LOGGER.warn( "Unable to find content attribute for given merchant" );
                return Collections.<String> emptyList();
    		}
    		return new ArrayList<String>(objectNode.getKeys());
        
        


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


        List<OutputContentFile> images = new ArrayList<OutputContentFile>();
        try
        {
            
        	FileNameMap fileNameMap = URLConnection.getFileNameMap();
    		StringBuilder nodePath = new StringBuilder();
    		nodePath.append(merchantStoreCode).append("/").append(imageContentType.name());

            Node<String, Object> merchantNode = this.getNode(nodePath.toString());

            if ( merchantNode == null )
            {
                return null;
            }
            
            
            for(String key : merchantNode.getKeys()) {
            	
                byte[] imageBytes = (byte[])merchantNode.get( key );

                OutputContentFile contentImage = new OutputContentFile();

                InputStream input = new ByteArrayInputStream( imageBytes );
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                IOUtils.copy( input, output );

                String contentType = fileNameMap.getContentTypeFor( key );

                contentImage.setFile( output );
                contentImage.setMimeType( contentType );
                contentImage.setFileName( key );

                images.add( contentImage );
            	
            }

        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while fetching content image for {} merchant ", merchantStoreCode);
            throw new ServiceException( e );
        }
        return images;
    }

    /**
     * Method to remove all images for a given merchant store.
     * This method will take care of removing all images associated for a given merchant store from underlying Infinispan cache tree.
     * @param store merchant store whose associated images will be removed
     * @throws ServiceException
     * 
     */
    @SuppressWarnings("unchecked")
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
            
        	
			final StringBuilder merchantPath = new StringBuilder();
	        merchantPath.append( STORE_MERCHANT).append(merchantStoreCode );
	        cacheManager.getTreeCache().getRoot().remove(merchantPath.toString());
        	
        	


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
            
        	
        	StringBuilder nodePath = new StringBuilder();
        	nodePath.append(merchantStoreCode).append("/").append(imageContentType.name());
        	
        	
        	Node<String, Object> imagesNode = this.getNode(nodePath.toString());
        	
        	imagesNode.remove(imageName);
        	

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

        OutputContentFile contentImage = new OutputContentFile();
        try
        {

        	
        	FileNameMap fileNameMap = URLConnection.getFileNameMap();
        	
        	StringBuilder nodePath = new StringBuilder();
        	nodePath.append(merchantStoreCode).append("/").append(imageContentType.name());
        	
        	Node<String,Object> imageNode = this.getNode(nodePath.toString());
        	
        	
            byte[] imageBytes = (byte[])imageNode.get( imageName );



            InputStream input = new ByteArrayInputStream( imageBytes );
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy( input, output );

            String contentType = fileNameMap.getContentTypeFor( imageName );

            contentImage.setFile( output );
            contentImage.setMimeType( contentType );
            contentImage.setFileName( imageName );
        	

        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while fetching content image for {} merchant ", merchantStoreCode);
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

            
        	StringBuilder nodePath = new StringBuilder();
        	nodePath.append(merchantStoreCode).append("/").append(image.getFileContentType().name());
        	
        	Node<String,Object> imageNode = this.getNode(nodePath.toString());


            
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy( image.getFile(), output );
            
            imageNode.put(image.getFileName(), output.toByteArray());
            
            System.out.println(output.toByteArray());

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

            //final Node<String, Object> merchantNode = getMerchantNode(merchantStoreCode);
            // object for a given merchant containing all images

            for(final InputContentFile image:imagesList){
            	
            	String cmsType = image.getFileContentType().name();
            	
            	StringBuilder nodePath = new StringBuilder();
            	nodePath.append(merchantStoreCode).append("/").append(cmsType);
            	
            	Node<String,Object> imageNode = this.getNode(nodePath.toString());
            	

                
                final ByteArrayOutputStream output = new ByteArrayOutputStream();
                IOUtils.copy( image.getFile(), output );
 
                imageNode.put( image.getFileName(), output.toByteArray() );
            }
            
            LOGGER.info( "Total {} content images added successfully.",imagesList.size() );

        }
        catch ( final Exception e )
        {
            LOGGER.error( "Error while saving content image", e );
            throw new ServiceException( e );

        }
        
    }
    
	@SuppressWarnings("unchecked")
	private Node<String, Object> getNode( final String node )
    {
        LOGGER.debug( "Fetching node for store {} from Infinispan", node );
        final StringBuilder merchantPath = new StringBuilder();
        merchantPath.append( STORE_MERCHANT ).append(node);

        Fqn contentFilesFqn = Fqn.fromString(merchantPath.toString()); 

		Node<String,Object> nd = cacheManager.getTreeCache().getRoot().getChild(contentFilesFqn); 
        
        if(nd==null) {

            cacheManager.getTreeCache().getRoot().addChild(contentFilesFqn);
            nd = cacheManager.getTreeCache().getRoot().getChild(contentFilesFqn); 

        }
        
        return nd;

    }

/*    @SuppressWarnings( "unchecked" )
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

    }*/

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

   

}
