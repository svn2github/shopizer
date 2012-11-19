package com.salesmanager.core.business.content.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.salesmanager.core.business.content.dao.ContentDao;
import com.salesmanager.core.business.content.model.content.Content;
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
        InputContentImage contentImage = new InputContentImage( ImageContentType.CONTENT );
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

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy( cmsContentImage.getFile(), output );
            contentImage.setFile( output );
        }
        catch ( IOException e )
        {
            LOG.error( "Error while trying to convert input stream to buffered image", e );
            throw new ServiceException( e );

        }

        LOG.info( "Adding content image for merchant...." );
        contentFileManager.addImage( store, contentImage );

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
}
