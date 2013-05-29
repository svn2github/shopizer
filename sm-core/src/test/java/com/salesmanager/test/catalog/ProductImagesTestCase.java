package com.salesmanager.test.catalog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.content.model.content.FileContentType;
import com.salesmanager.core.business.content.model.content.ImageContentFile;
import com.salesmanager.core.business.content.model.content.OutputContentFile;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.test.core.AbstractSalesManagerCoreTestCase;

public class ProductImagesTestCase extends AbstractSalesManagerCoreTestCase {
	
	private static final Date date = new Date(System.currentTimeMillis());
	
	@Autowired
	private ContentService contentService;
	
	
/*	@Test
	public void testCache() throws ServiceException, FileNotFoundException, IOException {
		
		
        //Cache cache = new DefaultCacheManager("cms/infinispan_configuration.xml").getCache();
        
		 Cache cache = new DefaultCacheManager("cms/infinispan_configuration.xml").getCache("StoreRepository");
		 cache.getCacheConfiguration().invocationBatching().enabled();
    
		 TreeCacheFactory f = new TreeCacheFactory();
    
		 TreeCache treeCache = f.createTreeCache(cache);
		 
		 treeCache.start();
		 

		 
		 
	        final File file1 = new File( "c:/doc/carl/Merchant.jpg" );

	        if ( !file1.exists() || !file1.canRead() )
	        {
	            throw new ServiceException( "Can't read" + file1.getAbsolutePath() );
	        }

	        final byte[] is = IOUtils.toByteArray( new FileInputStream( file1 ) );
	        final ByteArrayInputStream inputStream = new ByteArrayInputStream( is );
	        final ImageContentFile cmsContentImage = new ImageContentFile();
	        cmsContentImage.setFileName( file1.getName() );
	        cmsContentImage.setFile( inputStream );
	        cmsContentImage.setFileContentType(FileContentType.PRODUCT);
	        

            //Node<String, Object> productNode =  merchantNode.getChild(productFqn);
	        Fqn contentFilesFqn = Fqn.fromString("/product-merchant-DEFAULT/TB12345"); 
            Node<String, Object> productNode = treeCache.getRoot().getChild(contentFilesFqn);
            
            
            
            if(productNode==null) {
            	//merchantNode.addChild(productFqn);
            	
            	treeCache.getRoot().addChild(contentFilesFqn);
            	productNode = treeCache.getRoot().getChild(contentFilesFqn);
            }
		 
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy( inputStream, output );
		 
            productNode.put(file1.getName(), output.toByteArray());
		 
		 
		 
		 
		 
		 
		 
        

	}
	*/
	
	@Test
	public void testCreateContentImage() throws ServiceException, FileNotFoundException, IOException {
		
	    Language en = languageService.getByCode("en");
	    Country country = countryService.getByCode("CA");
	    Zone zone = zoneService.getByCode("QC");


    	
        final MerchantStore store = merchantService.getByCode( MerchantStore.DEFAULT_STORE );
        
        
        
        
	    /**
	     * Create the category
	     */
	    Category book = new Category();
	    book.setMerchantStore(store);
	    book.setCode("book");

	    CategoryDescription bookEnglishDescription = new CategoryDescription();
	    bookEnglishDescription.setName("Book");
	    bookEnglishDescription.setCategory(book);
	    bookEnglishDescription.setLanguage(en);



	    List<CategoryDescription> descriptions = new ArrayList<CategoryDescription>();
	    descriptions.add(bookEnglishDescription);


	    book.setDescriptions(descriptions);

	    categoryService.create(book);
	    
	    
	    /**
	     * Create a manufacturer
	     */
	    Manufacturer packed = new Manufacturer();
	    packed.setMerchantStore(store);

	    ManufacturerDescription packedd = new ManufacturerDescription();
	    packedd.setLanguage(en);
	    packedd.setManufacturer(packed);
	    packedd.setName("Packed publishing");
	    packed.getDescriptions().add(packedd);

	    manufacturerService.create(packed);
	    

	    
	    
	    /**
	     * Create the product
	     */
	    Product product = new Product();
	    product.setProductHeight(new BigDecimal(4));
	    product.setProductLength(new BigDecimal(3));
	    product.setProductWidth(new BigDecimal(1));
	    product.setSku("TB12345");
	    product.setManufacturer(packed);
	    product.setMerchantStore(store);

	    // Product description
	    ProductDescription description = new ProductDescription();
	    description.setName("Spring in Action");
	    description.setLanguage(en);
	    description.setProduct(product);

	    product.getDescriptions().add(description);
	    product.getCategories().add(book);
	    
	    
	    //availability
	    ProductAvailability availability = new ProductAvailability();
	    availability.setProductDateAvailable(date);
	    availability.setProductQuantity(100);
	    availability.setRegion("*");
	    availability.setProduct(product);// associate with product
	    
	    //price
	    ProductPrice dprice = new ProductPrice();
	    dprice.setDefaultPrice(true);
	    dprice.setProductPriceAmount(new BigDecimal(29.99));
	    dprice.setProductAvailability(availability);
	    
	    

	    ProductPriceDescription dpd = new ProductPriceDescription();
	    dpd.setName("Base price");
	    dpd.setProductPrice(dprice);
	    dpd.setLanguage(en);

	    dprice.getDescriptions().add(dpd);
	    availability.getPrices().add(dprice);

	  
	    productService.create(product);
        
        
   
        
        
        final File file1 = new File( "c:/doc/carl/Merchant.jpg" );

        if ( !file1.exists() || !file1.canRead() )
        {
            throw new ServiceException( "Can't read" + file1.getAbsolutePath() );
        }

        final byte[] is = IOUtils.toByteArray( new FileInputStream( file1 ) );
        final ByteArrayInputStream inputStream = new ByteArrayInputStream( is );
        final ImageContentFile cmsContentImage = new ImageContentFile();
        cmsContentImage.setFileName( file1.getName() );
        cmsContentImage.setFile( inputStream );
        cmsContentImage.setFileContentType(FileContentType.PRODUCT);

	    //final List<ProductImage> contentImagesList=new ArrayList<ProductImage>();

        ProductImage productImage = new ProductImage();
        productImage.setProductImage(file1.getName());
        productImage.setProduct(product);

        
        productImageService.addProductImage(product, productImage, cmsContentImage);

        //get productImage
        productImage = productImageService.getById(productImage.getId());
        
        //get physical image
        OutputContentFile contentFile = productImageService.getProductImage(store.getCode(), product.getSku(), productImage.getProductImage());
        
        Assert.assertNotNull(contentFile);
        
        //print image
   	 	OutputStream outputStream = new FileOutputStream ("c:/TEMP/" + contentFile.getFileName()); 

   	 	ByteArrayOutputStream baos =  contentFile.getFile();
   	 	baos.writeTo(outputStream);
   	 	
   	 	//remove productImage
   	 	productImageService.removeProductImage(productImage);
   	 	
   	 	
        final File file2 = new File( "c:/doc/carl/IA.jpg" );

        if ( !file2.exists() || !file2.canRead() )
        {
            throw new ServiceException( "Can't read" + file2.getAbsolutePath() );
        }

        final byte[] is2 = IOUtils.toByteArray( new FileInputStream( file2 ) );
        final ByteArrayInputStream inputStream2 = new ByteArrayInputStream( is2 );
        final ImageContentFile cmsContentImage2 = new ImageContentFile();
        cmsContentImage2.setFileName( file2.getName() );
        cmsContentImage2.setFile( inputStream2 );
        cmsContentImage2.setFileContentType(FileContentType.PRODUCT);

	    //final List<ProductImage> contentImagesList=new ArrayList<ProductImage>();

        ProductImage productImage2 = new ProductImage();
        productImage2.setProductImage(file2.getName());
        productImage2.setProduct(product);

        
        productImageService.addProductImage(product, productImage2, cmsContentImage2);
        
   	 	//remove productImage
   	 	productImageService.removeProductImage(productImage2);
	    
	    

	}
	

}