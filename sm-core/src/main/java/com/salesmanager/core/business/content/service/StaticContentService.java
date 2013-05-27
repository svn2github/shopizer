/**
 * 
 */
package com.salesmanager.core.business.content.service;

import java.util.List;

import com.salesmanager.core.business.content.model.content.FileContentType;
import com.salesmanager.core.business.content.model.content.InputContentFile;
import com.salesmanager.core.business.content.model.content.OutputContentFile;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;


/**
 * Interface defining methods responsible for StaticContentService.
 * ContentServive will be be entry point for handling static content in CMS and take care of following functionalities.
 * 
 * Shopizer store static following type of static data for given merchant
 * <pre>
 * 1. Digital content data for products
 * 2. Any other static data like JS, CSS,PDF etc. for static content pages
 * </pre>
 * 
 * <li>Adding,removing static contents  for given merchant store</li>
 * <li>Get,Save,Update static content data for given merchant store</li>
 *  
 * @author Umesh Awasthhi
 * @version 1.2
 *
 */

public interface StaticContentService
{

    /**
     * Method responsible for storing static content data for given Store.data for given merchant store will be stored in
     * Infinispan.
     * static content can either be digital data or any other static file.
     * 
     * @param merchantStore merchant store whose content images are being saved.
     * @param inputStaticContentData content data being stored
     * @throws ServiceException
     */
    void addFile( final MerchantStore store, final InputContentFile inputStaticContentFile )
        throws ServiceException;
    
    
    /**
     * Method responsible for storing list of static files for given Store.files for given merchant store will be stored in
     * Infinispan.
     * 
     * @param merchantStore  merchant store whose content images are being saved.
     * @param inputStaticContentDataList list of content files being stored.
     * @throws ServiceException
     */
    void addFiles(final MerchantStore store,final List<InputContentFile> inputStaticContentFileList) throws ServiceException;
    
    /**
     * Method responsible for fetching specific static content file for a given merchant store. Requested file will be
     * search in Infinispan tree cache and OutputStaticContentFile will be sent, in case no filee is found null will
     * returned.
     * 
     * @param merchantStoreCode
     * @param file
     * @return {@link OutputContentFile}
     * @throws ServiceException
     */
    public OutputContentFile getFile( final MerchantStore store, final FileContentType staticContentType, final String fileName )
        throws ServiceException;


	void removeFiles(final MerchantStore store) throws ServiceException;


	void removeFile(final MerchantStore store,
			final FileContentType staticContentType, final String fileName)
			throws ServiceException;
    
    
    
}
