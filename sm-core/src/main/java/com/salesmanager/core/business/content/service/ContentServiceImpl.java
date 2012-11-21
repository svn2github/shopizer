package com.salesmanager.core.business.content.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.salesmanager.core.business.content.dao.ContentDao;
import com.salesmanager.core.business.content.model.content.Content;
import com.salesmanager.core.business.content.model.image.ContentImage;
import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.content.model.image.InputContentImage;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.modules.cms.common.CMSContentImage;
import com.salesmanager.core.modules.cms.content.ContentFileManager;

@Service( "contentService" )
public class ContentServiceImpl
    extends SalesManagerEntityServiceImpl<Long, Content>
    implements ContentService
{

    private static final Logger LOG = LoggerFactory.getLogger( ContentServiceImpl.class );

    private final ContentDao contentDao;

    @Autowired
    ContentFileManager contentFileManager;

    @Autowired
    public ContentServiceImpl( final ContentDao contentDao )
    {
        super( contentDao );

        this.contentDao = contentDao;
    }

    @Override
    public List<Content> listByType( final String contentType, final MerchantStore store, final Language language )
        throws ServiceException
    {

        return contentDao.listByType( contentType, store, language );
    }

    @Override
    public List<Content> listByType( final List<String> contentType, final MerchantStore store, final Language language )
        throws ServiceException
    {

        return contentDao.listByType( contentType, store, language );
    }

    @Override
    public Content getByCode( final String code, final MerchantStore store )
        throws ServiceException
    {

        return contentDao.getByCode( code, store );

    }

    @Override
    public void saveOrUpdate( final Content content )
        throws ServiceException
    {

        // save or update (persist and attach entities
        if ( content.getId() != null && content.getId() > 0 )
        {
            super.update( content );
        }
        else
        {
            super.save( content );
        }

    }

    @Override
    public Content getByCode( final String code, final MerchantStore store, final Language language )
        throws ServiceException
    {
        return contentDao.getByCode( code, store, language );
    }

    @Override
    public void addContentImage( final MerchantStore store, final CMSContentImage cmsContentImage )
        throws ServiceException
    {
        Assert.notNull( store, "Merchant store can not be null" );
        Assert.notNull( cmsContentImage, "CMSContent image can not be null" );
        final InputContentImage contentImage = new InputContentImage( ImageContentType.CONTENT );
        contentImage.setImageName( cmsContentImage.getImageName() );

        try
        {
            LOG.info( "Adding content image for merchant id {}", store.getId() );
            if ( cmsContentImage.getContentType() == null )
            {
                contentImage.setImageContentType( URLConnection.guessContentTypeFromStream( cmsContentImage.getFile() ) );
            }
            else
            {
                contentImage.setImageContentType( cmsContentImage.getContentType() );
            }

            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy( cmsContentImage.getFile(), output );
            contentImage.setFile( output );
        }
        catch ( final IOException e )
        {
            LOG.error( "Error while trying to convert input stream to buffered image", e );
            throw new ServiceException( e );

        }

        LOG.info( "Adding content image for merchant...." );
        contentFileManager.addImage( store, contentImage );
       

    }

    
    /**
     * Method responsible for adding list of content images for given merchant store in underlying Infinispan tree
     * cache. It will take list of {@link CMSContentImage} and will store them for given merchant store.
     * 
     * @param store Merchant store
     * @param contentImagesList list of {@link CMSContentImage} being stored
     * @throws ServiceException service exception
     */
    @Override
    public void addContentImages( final MerchantStore store, final List<CMSContentImage> contentImagesList )
        throws ServiceException
    {
        Assert.notNull( store, "Merchant store can not be null" );
        Assert.notEmpty( contentImagesList, "Images list can not be empty" );
        LOG.info( "Adding total {} images for given merchant",contentImagesList.size() );
        final List<InputContentImage> inputContentImagesList=new ArrayList<InputContentImage>();
        for(final CMSContentImage cmsContentImage:contentImagesList){
            final InputContentImage contentImage = new InputContentImage( ImageContentType.CONTENT );
            contentImage.setImageName( cmsContentImage.getImageName() );
            try
            {
                if ( cmsContentImage.getContentType() == null )
                {
                    contentImage.setImageContentType( URLConnection.guessContentTypeFromStream( cmsContentImage.getFile() ) );
                }
                else
                {
                    contentImage.setImageContentType( cmsContentImage.getContentType() );
                }

                final ByteArrayOutputStream output = new ByteArrayOutputStream();
                IOUtils.copy( cmsContentImage.getFile(), output );
                contentImage.setFile( output );
                inputContentImagesList.add(contentImage);
            }
            catch ( final IOException e )
            {
                LOG.error( "Error while trying to convert input stream to buffered image", e );
                throw new ServiceException( e );

            }
        }
        LOG.info( "Adding content images for merchant...." );
        contentFileManager.addImagees( store, inputContentImagesList );
   }
    
    

    /**
     * Method to remove given content image.Images are stored in underlying system based on there name.
     * Name will be used to search given image for removal
     * @param contentImage
     * @throws ServiceException
     */
    @Override
    public void removeImage( final ContentImage contentImage )
        throws ServiceException
    {
        Assert.notNull( contentImage, "Content Image can not be null" );
        
    }
    
    /**
     * Method to remove all images for a given merchant.It will take merchant store as an input and will
     * remove all images associated with given merchant store.
     * 
     * @param store
     * @throws ServiceException
     */
    @Override
    public void removeImages( final MerchantStore store )
        throws ServiceException
    {
        Assert.notNull( store, "Merchant Store can not be null" );
        contentFileManager.removeImages( store );
        
    }

    
    /**
     * Implementation for getContentImage method defined in {@link ContentService} interface. Methods will return
     * Content image with given image name for the Merchant store or will return null if no image with given name found
     * for requested Merchant Store in Infinispan tree cache.
     * 
     * @param store Merchant store
     * @param imageName name of requested image
     * @return {@link OutputContentImage}
     * @throws ServiceException
     */
    @Override
    public OutputContentImage getContentImage( final MerchantStore store, final String imageName )
        throws ServiceException
    {
        Assert.notNull( store, "Merchant store can not be null" );
        Assert.notNull( imageName, "CMSContent image can not be null" );
        return contentFileManager.getImage( store, imageName, ImageContentType.CONTENT );
    }

    
    /**
     * Implementation for getContentImages method defined in {@link ContentService} interface. Methods will return list of all
     * Content image associated with given  Merchant store or will return empty list if no image is associated with
     * given Merchant Store in Infinispan tree cache.
     * 
     * @param store Merchant store
     * @return list of {@link OutputContentImage}
     * @throws ServiceException
     */
    @Override
    public List<OutputContentImage> getContentImages( final MerchantStore store, final ImageContentType imageContentType )
        throws ServiceException
    {
        Assert.notNull( store, "Merchant store can not be null" );
        return contentFileManager.getImages( store, imageContentType );
    }
    
    /**
     * Returns the image names for a given merchant and store
     * @param store
     * @param imageContentType
     * @return
     * @throws ServiceException
     */
    @Override
    public List<String> getContentImagesNames( final MerchantStore store, final ImageContentType imageContentType )
            throws ServiceException
        {
            Assert.notNull( store, "Merchant store can not be null" );
            return contentFileManager.getImageNames(store, imageContentType);
        }

   
   

}
