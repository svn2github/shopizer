package com.salesmanager.test.catalog;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.catalog.product.model.review.ProductReviewDescription;
import com.salesmanager.core.business.catalog.product.model.type.ProductType;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.test.core.AbstractSalesManagerCoreTestCase;

public class CatalogSalesManagerTestCase extends AbstractSalesManagerCoreTestCase {
	
	private static final Date date = new Date(System.currentTimeMillis());

	@Test
	public void testCatalog() throws ServiceException {

	    Language en = languageService.getByCode("en");
	    Language fr = languageService.getByCode("fr");

	    MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
	    ProductType generalType = productTypeService.getProductType(ProductType.GENERAL_TYPE);

	    Category book = new Category();
	    book.setMerchantStore(store);
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
	    music.setMerchantStore(store);
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
	    novell.setMerchantStore(store);
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
	    tech.setMerchantStore(store);
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
	    fiction.setMerchantStore(store);
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
	    oreilley.setMerchantStore(store);

	    ManufacturerDescription oreilleyd = new ManufacturerDescription();
	    oreilleyd.setLanguage(en);
	    oreilleyd.setName("O\'reilley");
	    oreilleyd.setManufacturer(oreilley);
	    oreilley.getDescriptions().add(oreilleyd);

	    manufacturerService.create(oreilley);

	    Manufacturer packed = new Manufacturer();
	    packed.setMerchantStore(store);

	    ManufacturerDescription packedd = new ManufacturerDescription();
	    packedd.setLanguage(en);
	    packedd.setManufacturer(packed);
	    packedd.setName("Packed publishing");
	    packed.getDescriptions().add(packedd);

	    manufacturerService.create(packed);

	    Manufacturer novells = new Manufacturer();
	    novells.setMerchantStore(store);

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
	    product.setMerchantStore(store);

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
	    
	    ProductReview review = new ProductReview();
	    review.setProduct(product);
	    review.setReviewRating(4);
	    
	    ProductReviewDescription reviewDescription = new ProductReviewDescription();
	    reviewDescription.setLanguage(en);
	    reviewDescription.setDescription("This is a product review");
	    reviewDescription.setProductReview(review);
	    review.getDescriptions().add(reviewDescription);
	    
	    productReviewService.create(review);
	    
	    review = new ProductReview();
	    review.setProduct(product);
	    review.setReviewRating(5);
	    
	    reviewDescription = new ProductReviewDescription();
	    reviewDescription.setLanguage(en);
	    reviewDescription.setDescription("This is a second product review");
	    reviewDescription.setProductReview(review);
	    review.getDescriptions().add(reviewDescription);
	    
	    productReviewService.create(review);
	    

	    // PRODUCT 2

	    Product product2 = new Product();
	    product2.setProductHeight(new BigDecimal(4));
	    product2.setProductLength(new BigDecimal(3));
	    product2.setProductWidth(new BigDecimal(1));
	    product2.setSku("TB2468");
	    product2.setManufacturer(packed);
	    product2.setType(generalType);
	    product2.setMerchantStore(store);

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
	    product3.setMerchantStore(store);

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
	    product4.setMerchantStore(store);

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
	    product5.setMerchantStore(store);

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
	    product6.setMerchantStore(store);

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
	    
	    
	    //Test get products
	    ProductList productList = productService.listByStore(store, en, null);
	    
	    List<Product> products = productList.getProducts();
	    Assert.assertNotNull(products);
	    
	}


}