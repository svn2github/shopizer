/**
 * 
 */
package com.salesmanager.core.modules.cms.content;

import java.util.List;

import com.salesmanager.core.business.content.model.content.InputContentFile;
import com.salesmanager.core.business.generic.exception.ServiceException;


/**
 * @author Umesh Awasthi
 *
 */
public interface StaticContentPut
{
    public void addStaticFile(final String merchantStoreCode, InputContentFile inputStaticContentData) throws ServiceException;
    public void addStaticFiles(final String merchantStoreCode, List<InputContentFile> inputStaticContentDataList) throws ServiceException;
}
