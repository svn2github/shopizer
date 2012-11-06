package com.salesmanager.core.business.content.service;

import java.util.List;

import com.salesmanager.core.business.content.model.content.Content;
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

}
