/**
 * 
 */
package com.salesmanager.core.modules.cms.content;

import com.salesmanager.core.business.content.model.content.StaticContentType;
import com.salesmanager.core.business.generic.exception.ServiceException;


/**
 * @author Umesh Awasthi
 *
 */
public interface StaticContentRemove
{
    public void removeStaticContent(final String merchantStoreCode, StaticContentType staticContentType, final String fileName) throws ServiceException;
    public void removeStaticContents(final String merchantStoreCode) throws ServiceException;

}
