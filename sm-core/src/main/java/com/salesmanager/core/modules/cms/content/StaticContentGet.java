package com.salesmanager.core.modules.cms.content;

import java.util.List;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.modules.cms.common.OutputStaticContentData;

/**
 * Methods to retrieve the static content from the CMS
 * @author Carl Samson
 *
 */
public interface StaticContentGet
{

	public OutputStaticContentData getStaticContentData(final String merchantStoreCode, String contentName) throws ServiceException;
    public List<String> getStaticContentDataName(final String merchantStoreCode) throws ServiceException;
    public List<OutputStaticContentData> getStaticContentData(final String merchantStoreCode) throws ServiceException;
}
