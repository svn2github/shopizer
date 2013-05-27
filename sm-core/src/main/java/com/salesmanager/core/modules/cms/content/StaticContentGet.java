package com.salesmanager.core.modules.cms.content;

import java.util.List;

import com.salesmanager.core.business.content.model.content.FileContentType;
import com.salesmanager.core.business.content.model.content.OutputContentFile;
import com.salesmanager.core.business.generic.exception.ServiceException;


/**
 * Methods to retrieve the static content from the CMS
 * @author Carl Samson
 *
 */
public interface StaticContentGet
{

	public OutputContentFile getStaticContentData(final String merchantStoreCode, FileContentType fileContentType, String contentName) throws ServiceException;
    public List<String> getStaticContentDataName(final String merchantStoreCode,FileContentType fileContentType) throws ServiceException;
    public List<OutputContentFile> getStaticContentData(final String merchantStoreCode, FileContentType fileContentType) throws ServiceException;
}
