package com.salesmanager.core.modules.cms.common;

import java.util.List;

import com.salesmanager.core.business.content.model.image.ImageContentType;
import com.salesmanager.core.business.content.model.image.OutputContentImage;
import com.salesmanager.core.business.generic.exception.ServiceException;

public interface ImageGet
{

    public List<OutputContentImage> getImages( final Integer merchantStoreId, ImageContentType imageContentType )
        throws ServiceException;

}
