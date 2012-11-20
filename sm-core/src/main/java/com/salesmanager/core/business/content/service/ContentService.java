package com.salesmanager.core.business.content.service;

import java.util.List;

import com.salesmanager.core.business.content.model.content.Content;
import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.modules.cms.common.CMSContentImage;

public interface ContentService
    extends SalesManagerEntityService<Long, Content>
{

    public List<Content> listByType( String contentType, MerchantStore store, Language language )
        throws ServiceException;

    public List<Content> listByType( List<String> contentType, MerchantStore store, Language language )
        throws ServiceException;

    Content getByCode( String code, MerchantStore store )
        throws ServiceException;

    void saveOrUpdate( Content content )
        throws ServiceException;

    Content getByCode( String code, MerchantStore store, Language language )
        throws ServiceException;

    /**
     * Method responsible for storing content image for given Store.Image for given merchant store will be stored in
     * Infinispan.
     * 
     * @param store merchant store whose content images are being saved.
     * @param contentImage content image being stored
     * @throws ServiceException
     */
    void addContentImage( MerchantStore store, CMSContentImage contentImage )
        throws ServiceException;

    /**
     * Method responsible for fetching particular content image for a given merchant store. Requested image will be
     * search in Infinispan tree cache and OutputContentImage will be sent, in case no image is found null will
     * returned.
     * 
     * @param store
     * @param imageName
     * @return {@link OutputContentImage}
     * @throws ServiceException
     */
    public OutputContentImage getContentImage( final MerchantStore store, final String imageName )
        throws ServiceException;
    
    
    /**
     * Method to get list of all images associated with a given merchant store.In case of no image method will return an empty list.
     * @param store
     * @param imageContentType
     * @return list of {@link OutputContentImage}
     * @throws ServiceException
     */
    public List<OutputContentImage> getContentImages( MerchantStore store, ImageContentType imageContentType )
                    throws ServiceException;

	List<String> getContentImagesNames(MerchantStore store,
			ImageContentType imageContentType) throws ServiceException;

}
