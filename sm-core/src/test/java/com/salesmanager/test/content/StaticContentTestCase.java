package com.salesmanager.test.content;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.salesmanager.core.business.content.model.content.FileContentType;
import com.salesmanager.core.business.content.model.content.InputContentFile;
import com.salesmanager.core.business.content.service.StaticContentService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.test.core.AbstractSalesManagerCoreTestCase;

/**
 * Test 
 * 
 * - static content files (.js, .pdf etc)
 * - static content images (jpg, gig ...)
 * @author Carl Samson
 *
 */
public class StaticContentTestCase extends AbstractSalesManagerCoreTestCase {
	

	@Autowired
	private StaticContentService staticContentService;
	
	@Test
	public void testCreateStaticContent() throws Exception {

	    MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
	    
        final File file = new File( "c:/doc/carl/cdbaby.zip" );

        if ( !file.exists() || !file.canRead() )
        {
            throw new ServiceException( "Can't read" + file.getAbsolutePath() );
        }

        byte[] is;
        ByteArrayInputStream inputStream = null;

		is = IOUtils.toByteArray( new FileInputStream( file ) );
		inputStream = new ByteArrayInputStream( is );

	     InputContentFile staticContent = new InputContentFile();
	     staticContent.setFile(inputStream);
	     staticContent.setFileName(file.getName());
	     staticContent.setFileContentType(FileContentType.STATIC_FILE);//default to static data
        
	     staticContentService.addFile(store, staticContent);
	     
	     //staticContentService.getFile(store, FileContentType.STATIC_FILE, file.getName());

        //now get the file
	     
/*	     OutputStaticContentData getData = staticContentService.getStaticContentData(store, StaticContentType.STATIC_DATA ,file.getName());
	     Assert.assertNotNull(getData);
	     if(getData != null) {
	    	 
	    	 System.out.println(getData.getFileName());
	    	 System.out.println(getData.getFileContentType());
	    	 
	    	 OutputStream outputStream = new FileOutputStream ("c:/tmp/" + getData.getFileName()); 

	    	 ByteArrayOutputStream baos =  getData.getFile();
	    	 baos.writeTo(outputStream);
	    	 
	     }*/
	     
	     //remove the file
	     
	     //staticContentService.removeFile(store, FileContentType.STATIC_FILE, file.getName());

	}
	
	@Test
	public void testCreateMultipleStaticContent() throws Exception {
		
		
	    MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
	    
	    
	    // FILE 1
        final File file = new File( "c:/doc/carl/cdbaby.zip" );

        if ( !file.exists() || !file.canRead() )
        {
            throw new ServiceException( "Can't read" + file.getAbsolutePath() );
        }

        byte[] is;
        ByteArrayInputStream inputStream = null;

		is = IOUtils.toByteArray( new FileInputStream( file ) );
		inputStream = new ByteArrayInputStream( is );

	    InputContentFile staticContent = new InputContentFile();
	    staticContent.setFile(inputStream);
	    staticContent.setFileName(file.getName());
	    staticContent.setFileContentType(FileContentType.STATIC_FILE);//default to static data
	    
	    
	    // FILE 2
        final File file2 = new File( "c:/doc/carl/Cocoa - Requirements.doc" );

        if ( !file2.exists() || !file2.canRead() )
        {
            throw new ServiceException( "Can't read" + file2.getAbsolutePath() );
        }

        byte[] is2;
        ByteArrayInputStream inputStream2 = null;

		is2 = IOUtils.toByteArray( new FileInputStream( file2 ) );
		inputStream2 = new ByteArrayInputStream( is2 );

		InputContentFile staticContent2 = new InputContentFile();
	    staticContent2.setFile(inputStream2);
	    staticContent2.setFileName(file2.getName());
	    staticContent2.setFileContentType(FileContentType.STATIC_FILE);//default to static data
	    
	    List<InputContentFile> staticFiles = new ArrayList<InputContentFile>();
	    staticFiles.add(staticContent);
	    staticFiles.add(staticContent2);
		
	    staticContentService.addFiles(store, staticFiles);
	    
	    //get file names
	    //staticContentService.get
		
		
	}
	

}