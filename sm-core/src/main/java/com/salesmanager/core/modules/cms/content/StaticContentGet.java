package com.salesmanager.core.modules.cms.content;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.modules.cms.common.OutputStaticContentData;

public interface StaticContentGet
{
    public OutputStaticContentData getStaticContentData(final String merchantStoreCode, String contentName) throws ServiceException;
}
