/**
 * 
 */
package com.salesmanager.core.business.content.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import com.salesmanager.core.business.content.model.content.FileContentType;
import com.salesmanager.core.business.content.model.content.InputContentFile;
import com.salesmanager.core.business.content.model.content.OutputContentFile;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;

import com.salesmanager.core.modules.cms.content.StaticContentFileManager;

/**
 * Static content service will be responsible for handling static content data which includes
 * <pre>
 * 1. Digital data
 * 2. Any other static resources like css, js etc.
 * </pre>
 * Static content data will be handle by underlying Infinispan cache API 
 * 
 * @author Umesh Awasthi
 * @version 1.2
 *
 */
@Service("staticContentService")
public class StaticContentServiceImpl implements StaticContentService
{

    
    private static final Logger LOG = LoggerFactory.getLogger( StaticContentServiceImpl.class );
    
    @Autowired
    StaticContentFileManager staticContentFileManager;
    /**
     * Implementation to handle and store static content data in underlying Infispan cache.
     * It will use merchant store code to store {@link InputContentFile} in Infispan tree cache.
     * File name will be used as a key to store file in  cache.
     * 
     * @param merchantStoreCode merchant store code
     * @param inputStaticContentData static content data being stored
     * @throws ServiceException
     */
    @Override
    public void addFile( final MerchantStore store, final InputContentFile inputStaticContentData )
        throws ServiceException
    {
       
        
        Assert.notNull( store, "Merchant store can not be null" );
        Assert.notNull( inputStaticContentData, "Static Content Data can not be null" );
        LOG.info( "Adding static content file for merchant with Code {} ", store.getCode());
        staticContentFileManager.addStaticFile( store.getCode(), inputStaticContentData );
        
    }
    
    /**
     * Implimentation responsible for adding list of static content files for given merchant store in underlying Infinispan tree
     * cache. It will take list of {@link InputContentFile} and will store them for given merchant store.
     * 
     * @param merchantStoreCode Merchant store.
     * @param inputStaticContentDataList list of {@link InputContentFile} being stored.
     * @throws ServiceException service exception
     */
    @Override
    public void addFiles( final MerchantStore store,
                                           final List<InputContentFile> inputStaticContentDataList )
        throws ServiceException
    {
        Assert.notNull( store, "Merchant store can not be null" );
        Assert.notEmpty( inputStaticContentDataList, "Images list can not be empty" );
        LOG.info( "Adding total {} files for given merchant",inputStaticContentDataList.size() );
        staticContentFileManager.addStaticFiles( store.getCode(), inputStaticContentDataList );
    }
    
    /**
     * Implementation for getStaticContentData method defined in {@link StaticContentService} interface. Methods will return
     * static content file with given file name for the Merchant store or will return null if no file with given name found
     * for requested Merchant Store in Infinispan tree cache.
     * 
     * @param store Merchant merchantStoreCode
     * @param fileName name of requested file
     * @param staticContentType
     * @return {@link OutputContentFile}
     * @throws ServiceException
     */
    @Override
    public OutputContentFile getFile( final MerchantStore store, final FileContentType fileContentType, String fileName )
        throws ServiceException
    {
        
        Assert.notNull( store, "Merchant store can not be null" );
        Assert.notNull( fileName, "file name can not be null" );
        LOG.info( "Starting to fetch static content file for with name {} for merchant with Code {} ", fileName,store.getCode());
        return  staticContentFileManager.getStaticContentData( store.getCode(), fileContentType, fileName );
    }
    
    /**
     * Removes all static content for a given merchant store
     * @param merchantStoreCode
     * @throws ServiceException
     */
    @Override
    public void removeFiles( final MerchantStore store)
    throws ServiceException
	{
	    Assert.notNull( store, "Merchant Store can not be null" );
	    staticContentFileManager.removeStaticContents(store.getCode());
	    
	}
    
    /**
     * Removes a specific file as a specific content type
     * @param merchantStoreCode
     * @param staticContentType
     * @param fileName
     * @throws ServiceException
     */
    @Override
    public void removeFile( final MerchantStore store, final FileContentType fileContentType, final String fileName )
    throws ServiceException {
    	Assert.notNull( store, "Merchant Store can not be null" );
    	Assert.notNull( fileContentType, "FileContentType can not be null" );
    	Assert.notNull( fileName, "fileName can not be null" );
    	staticContentFileManager.removeStaticContent(store.getCode(), fileContentType, fileName);
    }



   

}
