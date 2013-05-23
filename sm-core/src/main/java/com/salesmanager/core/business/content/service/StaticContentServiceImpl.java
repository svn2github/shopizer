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

import com.salesmanager.core.business.content.model.content.StaticContentType;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.modules.cms.common.InputStaticContentData;
import com.salesmanager.core.modules.cms.common.OutputStaticContentData;
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
     * It will use merchant store code to store {@link InputStaticContentData} in Infispan tree cache.
     * File name will be used as a key to store file in  cache.
     * 
     * @param merchantStoreCode merchant store code
     * @param inputStaticContentData static content data being stored
     * @throws ServiceException
     */
    @Override
    public void addStaticContentData( String merchantStoreCode, InputStaticContentData inputStaticContentData )
        throws ServiceException
    {
       
        LOG.info( "Adding static content file for merchant with Code {} ", merchantStoreCode);
        Assert.notNull( merchantStoreCode, "Merchant store Id can not be null" );
        Assert.notNull( inputStaticContentData, "Static Content Data can not be null" );
        staticContentFileManager.addStaticFile( merchantStoreCode, inputStaticContentData );
        
    }
    
    /**
     * Implimentation responsible for adding list of static content files for given merchant store in underlying Infinispan tree
     * cache. It will take list of {@link InputStaticContentData} and will store them for given merchant store.
     * 
     * @param merchantStoreCode Merchant store.
     * @param inputStaticContentDataList list of {@link InputStaticContentData} being stored.
     * @throws ServiceException service exception
     */
    @Override
    public void addStaticContentDataFiles( String merchantStoreCode,
                                           List<InputStaticContentData> inputStaticContentDataList )
        throws ServiceException
    {
        Assert.notNull( merchantStoreCode, "Merchant store ID can not be null" );
        Assert.notEmpty( inputStaticContentDataList, "Images list can not be empty" );
        LOG.info( "Adding total {} files for given merchant",inputStaticContentDataList.size() );
        staticContentFileManager.addStaticFiles( merchantStoreCode, inputStaticContentDataList );
    }
    
    /**
     * Implementation for getStaticContentData method defined in {@link StaticContentService} interface. Methods will return
     * static content file with given file name for the Merchant store or will return null if no file with given name found
     * for requested Merchant Store in Infinispan tree cache.
     * 
     * @param store Merchant merchantStoreCode
     * @param fileName name of requested file
     * @param staticContentType
     * @return {@link OutputStaticContentData}
     * @throws ServiceException
     */
    @Override
    public OutputStaticContentData getStaticContentData( String merchantStoreCode, StaticContentType staticContentType, String fileName )
        throws ServiceException
    {
        LOG.info( "Starting to fetch static content file for with name {} for merchant with Code {} ", fileName,merchantStoreCode);
        Assert.notNull( merchantStoreCode, "Merchant store Id can not be null" );
        Assert.notNull( fileName, "file name can not be null" );
        return  staticContentFileManager.getStaticContentData( merchantStoreCode, staticContentType, fileName );
    }


   

}
