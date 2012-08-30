package com.salesmanager.test.isolated;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;


import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.tree.Fqn;
import org.infinispan.tree.Node;
import org.infinispan.tree.TreeCache;
import org.infinispan.tree.TreeCacheFactory;
import org.jgroups.util.Util;


import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.image.ProductImageDescription;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.catalog.product.model.type.ProductType;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionValueService;
import com.salesmanager.core.business.catalog.product.service.availability.ProductAvailabilityService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.catalog.product.service.price.ProductPriceService;
import com.salesmanager.core.business.catalog.product.service.type.ProductTypeService;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.util.EntityManagerUtils;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.model.CountryDescription;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.currency.service.CurrencyService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.Permission;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.business.user.service.PermissionService;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.modules.cms.OutputContentImage;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.test.core.SalesManagerCoreTestExecutionListener;


@ContextConfiguration(locations = { "classpath:spring/test-spring-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, SalesManagerCoreTestExecutionListener.class })
public class IsolatedTestCase {

  private static final Logger          log  = Logger.getLogger(IsolatedTestCase.class);

  private static final Date            date = new Date(System.currentTimeMillis());

  @Autowired
  private EntityManagerUtils           entityManagerUtils;

  @Autowired
  protected ProductService             productService;

  @Autowired
  protected ProductPriceService        productPriceService;

  @Autowired
  protected ProductAttributeService    productAttributeService;

  @Autowired
  protected ProductOptionService       productOptionService;

  @Autowired
  protected ProductOptionValueService  productOptionValueService;

  @Autowired
  protected ProductAvailabilityService productAvailabilityService;

  @Autowired
  protected ProductImageService        productImageService;

	  @Autowired
  protected CategoryService            categoryService;


  @Autowired
  protected MerchantStoreService       merchantService;

  @Autowired
  protected ProductTypeService         productTypeService;

  @Autowired
  protected LanguageService            languageService;

  @Autowired
  protected CountryService             countryService;

  @Autowired
  protected ZoneService                zoneService;

  @Autowired
  protected CustomerService            customerService;

  @Autowired
  protected ManufacturerService        manufacturerService;

  @Autowired
  protected CurrencyService            currencyService;

  @Autowired
  protected OrderService               orderService;
  
  @Autowired
  protected GroupService               groupService;
  
  @Autowired
  protected PermissionService               permissionService;
  
  @Autowired
  protected UserService               userService;

  //@Autowired
  protected TestSupportFactory         testSupportFactory;
  


  @Test
  public void test1CreateReferences() throws ServiceException {

    Date date = new Date(System.currentTimeMillis());

    Language en = new Language();
    en.setCode("en");
    languageService.create(en);

    Language fr = new Language();
    fr.setCode("fr");
    languageService.create(fr);

    // create country
    Country ca = new Country();
    ca.setIsoCode("CA");

    CountryDescription caden = new CountryDescription();
    caden.setCountry(ca);
    caden.setLanguage(en);
    caden.setName("Canada");
    caden.setDescription("Canada Country");

    CountryDescription cadfr = new CountryDescription();
    cadfr.setCountry(ca);
    cadfr.setLanguage(fr);
    cadfr.setName("Canada");
    cadfr.setDescription("Pays Canada");

    List<CountryDescription> descriptionsca = new ArrayList<CountryDescription>();
    descriptionsca.add(caden);
    descriptionsca.add(cadfr);
    ca.setDescriptions(descriptionsca);

    countryService.create(ca);

    // create a currency
    Currency currency = new Currency();
    currency.setCurrency(java.util.Currency.getInstance(Locale.CANADA));
    currency.setSupported(true);
    currencyService.create(currency);

    // create a merchant
    MerchantStore store = new MerchantStore();
    store.setCountry(ca);
    store.setCurrency(currency);
    store.setDefaultLanguage(en);
    store.setInBusinessSince(date);
	store.setStorename("default store");
	store.setStorephone("888-888-8888");
	store.setCode(MerchantStore.DEFAULT_STORE);
	store.setStorecity("My city");
	store.setStorepostalcode("H2H-2H2");
	store.setStoreEmailAddress("test@test.com");


    merchantService.create(store);

    ProductType generalType = new ProductType();
    generalType.setCode(ProductType.GENERAL_TYPE);
    productTypeService.create(generalType);

  }
  
  @Test
  public void testAjaxResponseObject() throws Exception {
	  
	  

		AjaxResponse resp = new AjaxResponse();
		
		Language en = languageService.getByCode("en");
		MerchantStore store = merchantService.getMerchantStore(MerchantStore.DEFAULT_STORE);
		
		List<Category> categories = categoryService.listByStore(store,en);
		
		for(Category category : categories) {
			
			Map entry = new HashMap();
			entry.put("categoryId", category.getId());
			
			CategoryDescription description = category.getDescriptions().get(0);
			
			entry.put("name", description.getName());
			entry.put("visible", category.isVisible());
			resp.addDataEntry(entry);
			
			
		}
		
		resp.setStatus(0);
		
		System.out.println(resp.toJSONString());
		

	  
	  
  }
  
  @Test
  public void testGetMerchant() throws ServiceException {
	  
	  MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
	  System.out.println("done");
	  
  }
  
  @Test
  public void testCreateProductWithImage() throws ServiceException {
	  
	    Language en = languageService.getByCode("en");
	    Language fr = languageService.getByCode("fr");
	    Country ca = countryService.getByCode("CA");
	    Currency currency = currencyService.getByCode("CAD");
	    MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
	    ProductType generalType = productTypeService.getProductType(ProductType.GENERAL_TYPE);
	    
	    
	    Category book = categoryService.getByCode(store, "book");
	    
	    //TODO in product service
	    Product product = productService.getById(1L);
	    
	    product.setMerchantSore(store);
	    
	    try {
	    	
	    	EmbeddedCacheManager manager = new DefaultCacheManager("cms/infinispan_configuration.xml");
	    	//manager.getDefaultCacheConfiguration().invocationBatching().enabled();
		    Cache defaultCache = manager.getCache("DataRepository");
		    defaultCache.getCacheConfiguration().invocationBatching().enabled();
		    
		    TreeCacheFactory f = new TreeCacheFactory();
		    
		    TreeCache treeCache = f.createTreeCache(defaultCache);
		    
		    Fqn johnFqn = Fqn.fromString("persons/john");
		    
		    Node<String, Object> john = treeCache.getRoot().getChild(johnFqn);
		    if(john==null) {
		    	treeCache.getRoot().addChild(johnFqn);
		    }
		    
		    byte[] bytes2 = (byte[]) john.get("JAP-LETTER.jpg");
		    
		    if(bytes2==null) {
		    
			    
			    //john.put("surname", "Smith");
			    
			    File file1 = new File("/Users/csamson777/Documents/csti/JAP-LETTER.jpg");
		        if (!file1.exists()|| !file1.canRead()) {
		        	throw new ServiceException("Can't read" + file1.getAbsolutePath());
		        }
		        
		        InputStream input = new BufferedInputStream(new FileInputStream(file1));
		        ByteArrayOutputStream out = new ByteArrayOutputStream(); 
	            IOUtils.copy(input, out);
		        //input.
		        
		        byte[] bytes = out.toByteArray();
	
		        
		        john.put("JAP-LETTER.jpg", bytes);
	
		        
		        bytes2 = (byte[]) john.get("JAP-LETTER.jpg");
		        
		        ByteArrayInputStream bis = new ByteArrayInputStream(bytes2);
		        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		        IOUtils.copy(bis, bos);

	        
		    }
		    
			InputStream in = new ByteArrayInputStream(bytes2);
			BufferedImage bImageFromConvert = ImageIO.read(in);
 
			ImageIO.write(bImageFromConvert, "jpg", new File(
					"/Users/csamson777/Documents/csti/JAP-LETTER-2.jpg"));

            
            System.out.println("done");
		    
		    
		    /*Fqn personsFqn = Fqn.fromString("persons");
		    Fqn johnFqn = Fqn.fromRelative(personsFqn, Fqn.fromString("john"));
		    Node<String, Object> john = treeCache.getRoot().addChild(johnFqn);
		    john.put("surname", "Smith");*/
		    
		    /*Node<String, Object> john = ...
		    		Node persons = john.getParent();*/
		    
		   // Set<Node<String, Object>> personsChildren = persons.getChildren();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    
	    
	    
/*        File file1 = new File("C:/doc/carl/Merchant.jpg");
        if (!file1.exists()|| !file1.canRead()) {
        	throw new ServiceException("Can't read" + file1.getAbsolutePath());
        }
        
        File file2 = new File("C:/doc/carl/Recommendations.jpg");
        if (!file2.exists()|| !file2.canRead()) {
        	throw new ServiceException("Can't read" + file1.getAbsolutePath());
        }
	    

	    
        ProductImage productImage1 = new ProductImage();
	    productImage1.setDefaultImage(true);
	    productImage1.setProductImage(file1.getName());

	    
	    ProductImageDescription desc1 = new ProductImageDescription();
        desc1.setLanguage(en);
        desc1.setAltTag("ALT IMAGE 1 en");
        desc1.setName("A beautifill Thing");
        desc1.setProductImage(productImage1);
        
	    ProductImageDescription desc2 = new ProductImageDescription();
        desc2.setLanguage(fr);
        desc2.setAltTag("ALT IMAGE 1 fr");
        desc2.setName("Superbe chose");
        desc2.setProductImage(productImage1);
        
        List image1descriptions = new ArrayList();
        image1descriptions.add(desc1);
        image1descriptions.add(desc2);
        
        productImage1.setDescriptions(image1descriptions);
        
        productService.addProductImage(product, productImage1, file1);
        
        ProductImage productImage2 = new ProductImage();
	    productImage2.setProductImage(file2.getName());
	    productImage2.setDefaultImage(false);
	    
	    ProductImageDescription desc3 = new ProductImageDescription();
	    desc3.setLanguage(en);
	    desc3.setAltTag("ALT IMAGE 2 en");
	    desc3.setName("la la en");
	    desc3.setProductImage(productImage2);
        
	    ProductImageDescription desc4 = new ProductImageDescription();
	    desc4.setLanguage(fr);
	    desc4.setAltTag("ALT IMAGE 2 fr");
	    desc4.setName("la la fr");
	    desc4.setProductImage(productImage2);
        
        List image2descriptions = new ArrayList();
        image2descriptions.add(desc3);
        image2descriptions.add(desc4);
        
        productImage2.setDescriptions(image2descriptions);
        
        productService.addProductImage(product, productImage2, file2);*/
        
  }
  
  @Test
  public void testGetImages() throws ServiceException {
	  
	  Product product = productService.getById(1L);
	  
	  List<OutputContentImage> images = productImageService.getProductImages(product);
	  
	  for(OutputContentImage image : images) {
		  
		  System.out.println(image.getImageName());
		  System.out.println(image.getImageContentType());		  
	  }
	  
	  
	  
  }

  @Test
  public void test2CreateProducts() throws ServiceException {

    Language en = languageService.getByCode("en");
    Language fr = languageService.getByCode("fr");
    Country ca = countryService.getByCode("CA");
    Currency currency = currencyService.getByCode("CAD");
    MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
    ProductType generalType = productTypeService.getProductType(ProductType.GENERAL_TYPE);

    Category book = new Category();
    book.setMerchantSore(store);
    book.setCode("book");

    CategoryDescription bookEnglishDescription = new CategoryDescription();
    bookEnglishDescription.setName("Book");
    bookEnglishDescription.setCategory(book);
    bookEnglishDescription.setLanguage(en);

    CategoryDescription bookFrenchDescription = new CategoryDescription();
    bookFrenchDescription.setName("Livre");
    bookFrenchDescription.setCategory(book);
    bookFrenchDescription.setLanguage(fr);

    List<CategoryDescription> descriptions = new ArrayList<CategoryDescription>();
    descriptions.add(bookEnglishDescription);
    descriptions.add(bookFrenchDescription);

    book.setDescriptions(descriptions);

    categoryService.create(book);

    Category music = new Category();
    music.setMerchantSore(store);
    music.setCode("music");

    CategoryDescription musicEnglishDescription = new CategoryDescription();
    musicEnglishDescription.setName("Music");
    musicEnglishDescription.setCategory(music);
    musicEnglishDescription.setLanguage(en);

    CategoryDescription musicFrenchDescription = new CategoryDescription();
    musicFrenchDescription.setName("Musique");
    musicFrenchDescription.setCategory(music);
    musicFrenchDescription.setLanguage(fr);

    List<CategoryDescription> descriptions2 = new ArrayList<CategoryDescription>();
    descriptions2.add(musicEnglishDescription);
    descriptions2.add(musicFrenchDescription);

    music.setDescriptions(descriptions2);

    categoryService.create(music);

    Category novell = new Category();
    novell.setMerchantSore(store);
    novell.setCode("novell");

    CategoryDescription novellEnglishDescription = new CategoryDescription();
    novellEnglishDescription.setName("Novell");
    novellEnglishDescription.setCategory(novell);
    novellEnglishDescription.setLanguage(en);

    CategoryDescription novellFrenchDescription = new CategoryDescription();
    novellFrenchDescription.setName("Roman");
    novellFrenchDescription.setCategory(novell);
    novellFrenchDescription.setLanguage(fr);

    List<CategoryDescription> descriptions3 = new ArrayList<CategoryDescription>();
    descriptions3.add(novellEnglishDescription);
    descriptions3.add(novellFrenchDescription);

    novell.setDescriptions(descriptions3);
    
    novell.setParent(book);

    categoryService.create(novell);
    categoryService.addChild(book, novell);

    Category tech = new Category();
    tech.setMerchantSore(store);
    tech.setCode("tech");

    CategoryDescription techEnglishDescription = new CategoryDescription();
    techEnglishDescription.setName("Technology");
    techEnglishDescription.setCategory(tech);
    techEnglishDescription.setLanguage(en);

    CategoryDescription techFrenchDescription = new CategoryDescription();
    techFrenchDescription.setName("Technologie");
    techFrenchDescription.setCategory(tech);
    techFrenchDescription.setLanguage(fr);

    List<CategoryDescription> descriptions4 = new ArrayList<CategoryDescription>();
    descriptions4.add(techFrenchDescription);
    descriptions4.add(techFrenchDescription);

    tech.setDescriptions(descriptions4);
    
    tech.setParent(book);

    categoryService.create(tech);
    categoryService.addChild(book, tech);

    Category fiction = new Category();
    fiction.setMerchantSore(store);
    fiction.setCode("fiction");

    CategoryDescription fictionEnglishDescription = new CategoryDescription();
    fictionEnglishDescription.setName("Fiction");
    fictionEnglishDescription.setCategory(fiction);
    fictionEnglishDescription.setLanguage(en);

    CategoryDescription fictionFrenchDescription = new CategoryDescription();
    fictionFrenchDescription.setName("Sc Fiction");
    fictionFrenchDescription.setCategory(fiction);
    fictionFrenchDescription.setLanguage(fr);

    List<CategoryDescription> fictiondescriptions = new ArrayList<CategoryDescription>();
    fictiondescriptions.add(fictionEnglishDescription);
    fictiondescriptions.add(fictionFrenchDescription);

    fiction.setDescriptions(fictiondescriptions);
    
    fiction.setParent(novell);

    categoryService.create(fiction);
    categoryService.addChild(book, fiction);

    // Add products
    // ProductType generalType = productTypeService.

    Manufacturer oreilley = new Manufacturer();
    oreilley.setMerchantSore(store);

    ManufacturerDescription oreilleyd = new ManufacturerDescription();
    oreilleyd.setLanguage(en);
    oreilleyd.setName("O\'reilley");
    oreilleyd.setManufacturer(oreilley);
    oreilley.getDescriptions().add(oreilleyd);

    manufacturerService.create(oreilley);

    Manufacturer packed = new Manufacturer();
    packed.setMerchantSore(store);

    ManufacturerDescription packedd = new ManufacturerDescription();
    packedd.setLanguage(en);
    packedd.setManufacturer(packed);
    packedd.setName("Packed publishing");
    packed.getDescriptions().add(packedd);

    manufacturerService.create(packed);

    Manufacturer novells = new Manufacturer();
    novells.setMerchantSore(store);

    ManufacturerDescription novellsd = new ManufacturerDescription();
    novellsd.setLanguage(en);
    novellsd.setManufacturer(novells);
    novellsd.setName("Novells publishing");
    novells.getDescriptions().add(novellsd);

    manufacturerService.create(novells);

    // PRODUCT 1

    Product product = new Product();
    product.setProductHeight(new BigDecimal(4));
    product.setProductLength(new BigDecimal(3));
    product.setProductWidth(new BigDecimal(1));
    product.setSku("TB12345");
    product.setManufacturer(oreilley);
    product.setType(generalType);
    product.setMerchantSore(store);

    // Product description
    ProductDescription description = new ProductDescription();
    description.setName("Spring in Action");
    description.setLanguage(en);
    description.setProduct(product);

    product.getDescriptions().add(description);

    product.getCategories().add(tech);

    productService.create(product);

    // Availability
    ProductAvailability availability = new ProductAvailability();
    availability.setProductDateAvailable(date);
    availability.setProductQuantity(100);
    availability.setRegion("*");
    availability.setProduct(product);// associate with product

    productAvailabilityService.create(availability);

    ProductPrice dprice = new ProductPrice();
    dprice.setDefaultPrice(true);
    dprice.setProductPriceAmount(new BigDecimal(29.99));
    dprice.setProductPriceAvailability(availability);

    ProductPriceDescription dpd = new ProductPriceDescription();
    dpd.setName("Base price");
    dpd.setProductPrice(dprice);
    dpd.setLanguage(en);

    dprice.getDescriptions().add(dpd);

    productPriceService.create(dprice);

    // PRODUCT 2

    Product product2 = new Product();
    product2.setProductHeight(new BigDecimal(4));
    product2.setProductLength(new BigDecimal(3));
    product2.setProductWidth(new BigDecimal(1));
    product2.setSku("TB2468");
    product2.setManufacturer(packed);
    product2.setType(generalType);
    product2.setMerchantSore(store);

    // Product description
    description = new ProductDescription();
    description.setName("This is Node.js");
    description.setLanguage(en);
    description.setProduct(product2);

    product2.getDescriptions().add(description);

    product2.getCategories().add(tech);
    productService.create(product2);

    // Availability
    ProductAvailability availability2 = new ProductAvailability();
    availability2.setProductDateAvailable(date);
    availability2.setProductQuantity(100);
    availability2.setRegion("*");
    availability2.setProduct(product2);// associate with product

    productAvailabilityService.create(availability2);

    ProductPrice dprice2 = new ProductPrice();
    dprice2.setDefaultPrice(true);
    dprice2.setProductPriceAmount(new BigDecimal(39.99));
    dprice2.setProductPriceAvailability(availability2);

    dpd = new ProductPriceDescription();
    dpd.setName("Base price");
    dpd.setProductPrice(dprice2);
    dpd.setLanguage(en);

    dprice2.getDescriptions().add(dpd);

    productPriceService.create(dprice2);

    // PRODUCT 3

    Product product3 = new Product();
    product3.setProductHeight(new BigDecimal(4));
    product3.setProductLength(new BigDecimal(3));
    product3.setProductWidth(new BigDecimal(1));
    product3.setSku("NB1111");
    product3.setManufacturer(packed);
    product3.setType(generalType);
    product3.setMerchantSore(store);

    // Product description
    description = new ProductDescription();
    description.setName("A nice book for you");
    description.setLanguage(en);
    description.setProduct(product3);

    product3.getDescriptions().add(description);

    product3.getCategories().add(novell);
    productService.create(product3);

    // Availability
    ProductAvailability availability3 = new ProductAvailability();
    availability3.setProductDateAvailable(date);
    availability3.setProductQuantity(100);
    availability3.setRegion("*");
    availability3.setProduct(product3);// associate with product

    productAvailabilityService.create(availability3);

    ProductPrice dprice3 = new ProductPrice();
    dprice3.setDefaultPrice(true);
    dprice3.setProductPriceAmount(new BigDecimal(19.99));
    dprice3.setProductPriceAvailability(availability3);

    dpd = new ProductPriceDescription();
    dpd.setName("Base price");
    dpd.setProductPrice(dprice3);
    dpd.setLanguage(en);

    dprice3.getDescriptions().add(dpd);

    productPriceService.create(dprice3);

    // PRODUCT 4

    Product product4 = new Product();
    product4.setProductHeight(new BigDecimal(4));
    product4.setProductLength(new BigDecimal(3));
    product4.setProductWidth(new BigDecimal(1));
    product4.setSku("SF333345");
    product4.setManufacturer(packed);
    product4.setType(generalType);
    product4.setMerchantSore(store);

    // Product description
    description = new ProductDescription();
    description.setName("Battle of the worlds");
    description.setLanguage(en);
    description.setProduct(product4);

    product4.getDescriptions().add(description);

    product4.getCategories().add(fiction);
    productService.create(product4);

    // Availability
    ProductAvailability availability4 = new ProductAvailability();
    availability4.setProductDateAvailable(date);
    availability4.setProductQuantity(100);
    availability4.setRegion("*");
    availability4.setProduct(product4);// associate with product

    productAvailabilityService.create(availability4);

    ProductPrice dprice4 = new ProductPrice();
    dprice4.setDefaultPrice(true);
    dprice4.setProductPriceAmount(new BigDecimal(18.99));
    dprice4.setProductPriceAvailability(availability4);

    dpd = new ProductPriceDescription();
    dpd.setName("Base price");
    dpd.setProductPrice(dprice4);
    dpd.setLanguage(en);

    dprice4.getDescriptions().add(dpd);

    productPriceService.create(dprice4);

    // PRODUCT 5

    Product product5 = new Product();
    product5.setProductHeight(new BigDecimal(4));
    product5.setProductLength(new BigDecimal(3));
    product5.setProductWidth(new BigDecimal(1));
    product5.setSku("SF333346");
    product5.setManufacturer(packed);
    product5.setType(generalType);
    product5.setMerchantSore(store);

    // Product description
    description = new ProductDescription();
    description.setName("Battle of the worlds 2");
    description.setLanguage(en);
    description.setProduct(product5);

    product5.getDescriptions().add(description);

    product5.getCategories().add(fiction);
    productService.create(product5);

    // Availability
    ProductAvailability availability5 = new ProductAvailability();
    availability5.setProductDateAvailable(date);
    availability5.setProductQuantity(100);
    availability5.setRegion("*");
    availability5.setProduct(product5);// associate with product

    productAvailabilityService.create(availability5);

    ProductPrice dprice5 = new ProductPrice();
    dprice5.setDefaultPrice(true);
    dprice5.setProductPriceAmount(new BigDecimal(18.99));
    dprice5.setProductPriceAvailability(availability5);

    dpd = new ProductPriceDescription();
    dpd.setName("Base price");
    dpd.setProductPrice(dprice5);
    dpd.setLanguage(en);

    dprice5.getDescriptions().add(dpd);

    productPriceService.create(dprice5);

    // PRODUCT 6

    Product product6 = new Product();
    product6.setProductHeight(new BigDecimal(4));
    product6.setProductLength(new BigDecimal(3));
    product6.setProductWidth(new BigDecimal(1));
    product6.setSku("LL333444");
    product6.setManufacturer(packed);
    product6.setType(generalType);
    product6.setMerchantSore(store);

    // Product description
    description = new ProductDescription();
    description.setName("Life book");
    description.setLanguage(en);
    description.setProduct(product6);

    product6.getDescriptions().add(description);

    product6.getCategories().add(novell);
    productService.create(product6);

    // Availability
    ProductAvailability availability6 = new ProductAvailability();
    availability6.setProductDateAvailable(date);
    availability6.setProductQuantity(100);
    availability6.setRegion("*");
    availability6.setProduct(product6);// associate with product

    productAvailabilityService.create(availability6);

    ProductPrice dprice6 = new ProductPrice();
    dprice6.setDefaultPrice(true);
    dprice6.setProductPriceAmount(new BigDecimal(18.99));
    dprice6.setProductPriceAvailability(availability6);

    dpd = new ProductPriceDescription();
    dpd.setName("Base price");
    dpd.setProductPrice(dprice6);
    dpd.setLanguage(en);

    dprice6.getDescriptions().add(dpd);

    productPriceService.create(dprice6);

  }
  
  @Test
  public void test3CreateUser() throws ServiceException {
	  
	  //need to create permission firts
	  
	  Permission userperm = new Permission("GRANT_USER");
	  permissionService.create(userperm);
	  Permission storeperm = new Permission("GRANT_STORE");
	  permissionService.create(storeperm);
	  Permission catalogperm = new Permission("GRANT_CATALOG");
	  permissionService.create(catalogperm);
	  Permission paymentperm = new Permission("GRANT_PAYMENT");
	  permissionService.create(paymentperm);
	  Permission shippingperm = new Permission("GRANT_SHIPPING");
	  permissionService.create(shippingperm);
	  Permission orderperm = new Permission("GRANT_ORDER");
	  permissionService.create(orderperm);
	  Permission configperm = new Permission("GRANT_CONFIG");
	  permissionService.create(configperm);
	  
	  Group admin = new Group("ADMIN");
	  
	  admin.getPermissions().add(userperm);
	  admin.getPermissions().add(storeperm);
	  admin.getPermissions().add(catalogperm);
	  admin.getPermissions().add(paymentperm);
	  admin.getPermissions().add(shippingperm);
	  admin.getPermissions().add(orderperm);
	  admin.getPermissions().add(configperm);
	  
	  groupService.create(admin);
	  
	  User user = new User("admin","password","test@test.com");
	  user.setFirstName("Test");
	  user.setLastName("User");
	  user.getGroups().add(admin);
	  
	  userService.create(user);
	  
	  
	  
	  
	  
  }

  @Test
  public void testGetProduct() throws ServiceException {

    Language language = languageService.getByCode("en");

    Locale locale = new Locale("en", "CA");

    Product p = productService.getProductForLocale(1L, language, locale);

    if (p != null) {

      System.out.println(p.getDescriptions().size());

    }

  }

  @Test
  public void testGetProducts() throws ServiceException {
    Language language = languageService.getByCode("en");
    Locale locale = new Locale("en", "CA");
    
    
    MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);

    Category category = categoryService.getByCode(store, "book");

    int nrOfIterations = 1;

    for (int i = 1; i <= nrOfIterations; i++) {
      List<Product> products = productService.getProductsForLocale(category, language, locale);
      for (Product product : products) {
        log.info(MessageFormat.format("product found:{0}:iteration{1}", product.getId(), i));
      }
    }
  }
  
  @Test
  public void testGetProductsByCategories() throws ServiceException {
    Language language = languageService.getByCode("en");
    Locale locale = new Locale("en", "CA");
    MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
    
    //Category category = categoryService.getByCode(store, "novell");
    
 
    Category category = categoryService.getByCode(store, "book");
    
    categoryService.delete(category);
    
    System.out.println("done");
    
/*    List<Long> ids = new ArrayList<Long>();
    ids.add(1L);
    ids.add(2L);
    ids.add(3L);
    

    List<Product> products = productService.getProducts(ids);
    
    System.out.println(products.size());*/

  }

  @Test
  public void testCategory() throws ServiceException {

    /**
     * Creates a category hierarchy Music Books Novell Science-Fiction
     * Technology Business
     */
	  
	    Language en = languageService.getByCode("en");
	    Language fr = languageService.getByCode("fr");
	    Country ca = countryService.getByCode("CA");
	    Currency currency = currencyService.getByCode("CAD");
	    MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);



	    Category book = new Category();
	    book.setDepth(0);
	    book.setLineage("/");
	    book.setMerchantSore(store);
	    book.setCode("book");

	    CategoryDescription bookEnglishDescription = new CategoryDescription();
	    bookEnglishDescription.setName("Book");
	    bookEnglishDescription.setCategory(book);
	    bookEnglishDescription.setLanguage(en);

	    CategoryDescription bookFrenchDescription = new CategoryDescription();
	    bookFrenchDescription.setName("Livre");
	    bookFrenchDescription.setCategory(book);
	    bookFrenchDescription.setLanguage(fr);

	    List<CategoryDescription> descriptions = new ArrayList<CategoryDescription>();
	    descriptions.add(bookEnglishDescription);
	    descriptions.add(bookFrenchDescription);

	    book.setDescriptions(descriptions);

	    categoryService.create(book);

	    Category music = new Category();
	    music.setDepth(0);
	    music.setLineage("/");
	    music.setMerchantSore(store);
	    music.setCode("music");

	    CategoryDescription musicEnglishDescription = new CategoryDescription();
	    musicEnglishDescription.setName("Music");
	    musicEnglishDescription.setCategory(music);
	    musicEnglishDescription.setLanguage(en);

	    CategoryDescription musicFrenchDescription = new CategoryDescription();
	    musicFrenchDescription.setName("Musique");
	    musicFrenchDescription.setCategory(music);
	    musicFrenchDescription.setLanguage(fr);

	    List<CategoryDescription> descriptions2 = new ArrayList<CategoryDescription>();
	    descriptions2.add(musicEnglishDescription);
	    descriptions2.add(musicFrenchDescription);

	    music.setDescriptions(descriptions2);

	    categoryService.create(music);

	    Category novell = new Category();
	    novell.setDepth(1);
	    novell.setLineage("/" + book.getId() + "/");
	    novell.setMerchantSore(store);
	    novell.setCode("novell");

	    CategoryDescription novellEnglishDescription = new CategoryDescription();
	    novellEnglishDescription.setName("Novell");
	    novellEnglishDescription.setCategory(novell);
	    novellEnglishDescription.setLanguage(en);

	    CategoryDescription novellFrenchDescription = new CategoryDescription();
	    novellFrenchDescription.setName("Roman");
	    novellFrenchDescription.setCategory(novell);
	    novellFrenchDescription.setLanguage(fr);

	    List<CategoryDescription> descriptions3 = new ArrayList<CategoryDescription>();
	    descriptions3.add(novellEnglishDescription);
	    descriptions3.add(novellFrenchDescription);

	    novell.setDescriptions(descriptions3);

	    categoryService.create(novell);
	    categoryService.addChild(book, novell);

	    Category tech = new Category();
	    tech.setDepth(1);
	    tech.setLineage("/" + book.getId() + "/");
	    tech.setMerchantSore(store);
	    tech.setCode("tech");

	    CategoryDescription techEnglishDescription = new CategoryDescription();
	    techEnglishDescription.setName("Technology");
	    techEnglishDescription.setCategory(tech);
	    techEnglishDescription.setLanguage(en);

	    CategoryDescription techFrenchDescription = new CategoryDescription();
	    techFrenchDescription.setName("Technologie");
	    techFrenchDescription.setCategory(tech);
	    techFrenchDescription.setLanguage(fr);

	    List<CategoryDescription> descriptions4 = new ArrayList<CategoryDescription>();
	    descriptions4.add(techFrenchDescription);
	    descriptions4.add(techFrenchDescription);

	    tech.setDescriptions(descriptions4);

	    categoryService.create(tech);
	    categoryService.addChild(book, tech);


  }

  @Test
  public void testGetCategory() throws ServiceException {
	  
    MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);

    Category category = categoryService.getByCode(store, "book");
    System.out.println("Done");

  }

  @Test
  public void testCreateManufacturer() throws ServiceException {

    Language english = new Language();
    english.setCode("en");
    languageService.create(english);

    Language french = new Language();
    french.setCode("fr");
    languageService.create(french);

    Currency euro = new Currency();
    euro.setCurrency(java.util.Currency.getInstance("EUR"));
    currencyService.create(euro);

    Currency cad = new Currency();
    cad.setCurrency(java.util.Currency.getInstance("CAD"));
    currencyService.create(cad);

    Country fr = new Country("FR");
    countryService.create(fr);

    Country ca = new Country("CA");
    countryService.create(ca);

    Language DEFAULT_LANGUAGE = languageService.getByCode("en");
    Language FRENCH = languageService.getByCode("fr");
    Currency currency = currencyService.getByCode("CAD");

    // create a merchant
    MerchantStore store = new MerchantStore();
    store.setCountry(ca);
    store.setCurrency(currency);
    store.setDefaultLanguage(DEFAULT_LANGUAGE);
    store.setInBusinessSince(date);
    store.setStorename("store name");
    store.setStoreEmailAddress("test@test.com");
    merchantService.create(store);

    Manufacturer manufacturer = new Manufacturer();
    // store.getManufacturers().add(manufacturer);

    // merchantService.update(store);

    // Manufacturer manufacturer = new Manufacturer();
    manufacturer.setMerchantSore(store);

    ManufacturerDescription fd = new ManufacturerDescription();
    fd.setLanguage(FRENCH);
    fd.setName("Sony french");
    fd.setManufacturer(manufacturer);

    ManufacturerDescription ed = new ManufacturerDescription();
    ed.setLanguage(DEFAULT_LANGUAGE);
    ed.setName("Sony english");
    ed.setManufacturer(manufacturer);

    Set descriptions = new HashSet();
    descriptions.add(fd);
    descriptions.add(ed);

    manufacturer.setDescriptions(descriptions);

    manufacturerService.create(manufacturer);

    // manufacturerService.delete(manufacturer);
    // merchantService.delete(store);

  }

  @Test
  public void testDeleteManufacturerService() throws ServiceException {

    Manufacturer manufacturer = manufacturerService.getById(1L);
    manufacturerService.delete(manufacturer);

  }

  @Test
  public void testStoreRandomProducts() throws ServiceException {

    Language en = testSupportFactory.createLanguage("en");
    languageService.save(en);

    Language fr = testSupportFactory.createLanguage("fr");
    languageService.save(fr);

    Language[] languages = { en, fr };

    ProductType generalType = testSupportFactory.createProductType();
    productTypeService.save(generalType);

    Country country = testSupportFactory.createCountry(en);
    countryService.save(country);

    Currency currency = testSupportFactory.createCurrency();
    currencyService.save(currency);

    MerchantStore store = testSupportFactory.createMerchantStore(MerchantStore.DEFAULT_STORE, country, currency, en);
    merchantService.save(store);

    Manufacturer manufacturer = testSupportFactory.createRandomManufacturer(store, en);
    manufacturerService.save(manufacturer);

    Category category = testSupportFactory.createCategory(null, store, languages);
    categoryService.save(category);

    int nrOfProducts = 300;

    for (int i = 1; i <= nrOfProducts; i++) {
      log.info(MessageFormat.format("adding product nr:{0}", i));
      testSupportFactory.createAndStoreRandomProduct(manufacturer, generalType, category, store, en);
    }
  }
}
