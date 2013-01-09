/**
 * 
 */
package com.salesmanager.core.modules.cms.content;

import java.util.List;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.modules.cms.common.InputStaticContentData;

/**
 * @author Umesh Awasthi
 *
 */
public interface StaticContentPut
{
    public void addStaticFile(final String merchantStoreCode, InputStaticContentData inputStaticContentData) throws ServiceException;
    public void addStaticFiles(final String merchantStoreCode, List<InputStaticContentData> inputStaticContentDataList) throws ServiceException;
}
