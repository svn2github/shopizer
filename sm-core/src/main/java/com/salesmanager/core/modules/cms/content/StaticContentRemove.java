/**
 * 
 */
package com.salesmanager.core.modules.cms.content;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.modules.cms.common.ContentData;

/**
 * @author Umesh Awasthi
 *
 */
public interface StaticContentRemove
{
    public void removeStaticContent(final String merchantStoreCode,final ContentData contentData) throws ServiceException;
    public void removeStaticContents(final String merchantStoreCode) throws ServiceException;

}
