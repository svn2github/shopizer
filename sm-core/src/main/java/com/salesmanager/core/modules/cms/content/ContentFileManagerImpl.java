package com.salesmanager.core.modules.cms.content;

import java.util.List;

import com.salesmanager.core.business.content.model.image.ContentImage;
import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.content.model.image.InputContentImage;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.modules.cms.product.CmsContentFileManagerInfinispanImpl;

public class ContentFileManagerImpl
    extends ContentFileManager
{

    private ImagePut uploadImage;

    private ContentImageGet getImage;

    private ContentImageRemove removeImage;

    /**
     * Implementation for add image method. This method will called respected add image method of underlying
     * CMSContentManager. For CMS Content images {@link CmsContentFileManagerInfinispanImpl} will take care of adding
     * given content image with Infinispan cache.
     * 
     * @param merchantStoreId merchant store.
     * @param image Input content image
     * @throws ServiceException
     */
    @Override
    public void addImage( final Integer merchantStoreId, final InputContentImage image )
        throws ServiceException
    {

        uploadImage.addImage( merchantStoreId, image );


    }
    /**
     * Implementation for add images. This method will called respected add image method of underlying
     * CMSContentManager. For CMS Content images {@link CmsContentFileManagerInfinispanImpl} will take care of adding
     * given content images with Infinispan cache.
     * 
     * @param merchantStoreId merchant store.
     * @param imagesList Input content images
     * @throws ServiceException
     */
    @Override
    public void addImages( final Integer merchantStoreId, final List<InputContentImage> imagesList )
        throws ServiceException
    {
        uploadImage.addImages( merchantStoreId, imagesList );
        
    }

    /**
     * Implementation for get images method. This method will called respected get images method of underlying
     * CMSContentManager. For CMS Content images {@link CmsContentFileManagerInfinispanImpl} will take care of fetching all images
     * for given merchant from Infinispan cache.
     * 
     * @param merchantStoreId merchant store.
     * @param imageContentType Input content image
     * @return list {@link OutputContentImage}
     * @throws ServiceException
     */
    @Override
    public List<OutputContentImage> getImages( final Integer merchantStoreId, final ImageContentType imageContentType )
        throws ServiceException
    {
        return getImage.getImages( merchantStoreId, imageContentType );
        
    }

    /**
     * Method to remove a specific content image from merchant store.
     * Images will be searched in Infinispan cache based on there name and 
     * will be removed if any image with given name is found for associated merchant cache.
     * 
     * @param merchantStoreId Merchant store
     * @param contentImage content image which will be removed
     * @throws ServiceException
     */
    @Override
    public void removeImage(final Integer merchantStoreId, final ContentImage contentImage )
        throws ServiceException
    {
      removeImage.removeImage( merchantStoreId, contentImage );
   }

    /**
     * Method responsible for removing all associated images for given merchant store.
     * Content images for a merchant store are being stored in Infinispan cache tree and they will be removed from
     * cache tree.
     * 
     *  @param merchantStoreId Merchant store whose associated images will be removed
     *  @throws ServiceException
     *  
     */
    @Override
    public void removeImages( final Integer merchantStoreId )
        throws ServiceException
    {
        removeImage.removeImages( merchantStoreId );

    }

    /**
     * Method to get images for a given merchant store.For fetching image all we need to provide MerchantStore for whole
     * image will be fetched and image name. It will return {@link OutputContentImage}
     * 
     * @param merchantStoreId merchantStore for whom image will be fetched
     * @param imageName name of the image
     * @param imageContentType {@link ImageContentType}
     * @throws ServiceException
     */
    @Override
    public OutputContentImage getImage( final Integer merchantStoreId, final String imageName, final ImageContentType imageContentType )
        throws ServiceException
    {
        return getImage.getImage( merchantStoreId, imageName, imageContentType );
    }

    public ContentImageRemove getRemoveImage()
    {
        return removeImage;
    }

    public void setRemoveImage( final ContentImageRemove removeImage )
    {
        this.removeImage = removeImage;
    }

    public ContentImageGet getGetImage()
    {
        return getImage;
    }

    public void setGetImage( final ContentImageGet getImage )
    {
        this.getImage = getImage;
    }

    public ImagePut getUploadImage()
    {
        return uploadImage;
    }

    public void setUploadImage( final ImagePut uploadImage )
    {
        this.uploadImage = uploadImage;
    }

    /**
     * Method to get names of all images being associated for a given merchant store.
     * Image name will be used to build UI content to manage content images.
     * 
     * Names of the images will be retrieved from Infinispan cache.
     * 
     * @param merchantStoreId merchant store
     * @param imageContentType image content type
     * @throws ServiceException
     */
    @Override
    public List<String> getImageNames(final Integer merchantStoreId, final ImageContentType imageContentType )
        throws ServiceException
    {
       
        return getImage.getImageNames( merchantStoreId, imageContentType );
    }

    

}
