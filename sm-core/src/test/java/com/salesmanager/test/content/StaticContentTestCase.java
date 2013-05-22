package com.salesmanager.test.content;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import java.io.ByteArrayOutputStream;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.salesmanager.core.business.content.model.content.StaticContentType;
import com.salesmanager.core.business.content.service.StaticContentService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.modules.cms.common.InputStaticContentData;
import com.salesmanager.core.modules.cms.common.OutputStaticContentData;
import com.salesmanager.test.core.AbstractSalesManagerCoreTestCase;

public class StaticContentTestCase extends AbstractSalesManagerCoreTestCase {
	
	private static final Date date = new Date(System.currentTimeMillis());
	
	@Autowired
	private StaticContentService staticContentService;
	
	@Test
	public void testCreateStaticContent() throws Exception {
		
	    Language en = languageService.getByCode("en");
	    Country country = countryService.getByCode("CA");
	    Zone zone = zoneService.getByCode("QC");

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

	     InputStaticContentData staticContent = new InputStaticContentData();
	     staticContent.setFile(inputStream);
	     staticContent.setFileName(file.getName());
	     staticContent.setContentType(StaticContentType.STATIC_DATA);
        
	     staticContentService.addStaticContentData(store.getCode(), staticContent);

        //now get the file
	     
	     OutputStaticContentData getData = staticContentService.getStaticContentData(store.getCode(), file.getName());
	     if(getData != null) {
	    	 
	    	 System.out.println(getData.getFileName());
	    	 System.out.println(getData.getFileContentType());
	    	 
	    	 OutputStream outputStream = new FileOutputStream ("c:/tmp/" + getData.getFileName()); 

	    	 ByteArrayOutputStream baos =  getData.getFile();
	    	 baos.writeTo(outputStream);
	    	 
	     }

	}
	

}