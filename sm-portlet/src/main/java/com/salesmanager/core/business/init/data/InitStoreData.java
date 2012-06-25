package com.salesmanager.core.business.init.data;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
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
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.currency.service.CurrencyService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.service.ZoneService;

@Component
public class InitStoreData implements InitData {
	
	
	
	@Autowired
	protected ProductService productService;

	
	@Autowired
	protected ProductPriceService productPriceService;
	
	@Autowired
	protected ProductAttributeService productAttributeService;
	
	@Autowired
	protected ProductOptionService productOptionService;
	
	@Autowired
	protected ProductOptionValueService productOptionValueService;
	
	@Autowired
	protected ProductAvailabilityService productAvailabilityService;
	
	@Autowired
	protected ProductImageService productImageService;
	
	@Autowired
	protected CategoryService categoryService;
	
	@Autowired
	protected MerchantStoreService merchantService;
	
	@Autowired
	protected ProductTypeService productTypeService;
	
	@Autowired
	protected LanguageService languageService;
	
	@Autowired
	protected CountryService countryService;
	
	@Autowired
	protected ZoneService zoneService;
	
	@Autowired
	protected CustomerService customerService;
	
	@Autowired
	protected ManufacturerService manufacturerService;

	@Autowired
	protected CurrencyService currencyService;
	
	@Autowired
	protected OrderService orderService;

	@Override
	public void initInitialData() throws ServiceException {
		

		
		Date date = new Date(System.currentTimeMillis());
		
		Language en = languageService.getByCode("en");
		Language fr = languageService.getByCode("fr");
		
		//Country ca = countryService.getByCode("CA");
		//Currency currency = currencyService.getByCode("CAD");
		
		//create a merchant
		MerchantStore store = merchantService.getMerchantStore(MerchantStore.DEFAULT_STORE);
		ProductType generalType = productTypeService.getProductType(ProductType.GENERAL_TYPE);
		
		
		Category book = new Category();
		book.setDepth(0);
		book.setLineage("/");
		book.setMerchantSore(store);
		
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
		
		
		Category fiction = new Category();
		fiction.setDepth(2);
		fiction.setLineage("/" + book.getId() + "/" + novell.getId() + "/");
		fiction.setMerchantSore(store);
		
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
		
		categoryService.create(fiction);
		categoryService.addChild(book, fiction);
		
		//Add products
		//ProductType generalType = productTypeService.
		
		Manufacturer oreilley = new Manufacturer();
		oreilley.setMerchantSore(store);
		
		ManufacturerDescription oreilleyd = new ManufacturerDescription();
		oreilleyd.setLanguage(en);
		oreilleyd.setName("O\'reilley");
		oreilley.getDescriptions().add(oreilleyd);
		
		manufacturerService.create(oreilley);
		
		Manufacturer packed = new Manufacturer();
		packed.setMerchantSore(store);
		
		ManufacturerDescription packedd = new ManufacturerDescription();
		packedd.setLanguage(en);
		packedd.setName("Packed publishing");
		packed.getDescriptions().add(packedd);
		
		manufacturerService.create(packed);
		
		Manufacturer novells = new Manufacturer();
		novells.setMerchantSore(store);
		
		ManufacturerDescription novellsd = new ManufacturerDescription();
		novellsd.setLanguage(en);
		novellsd.setName("Novells publishing");
		novells.getDescriptions().add(novellsd);
		
		manufacturerService.create(novells);
		
		//PRODUCT 1
		
		Product product = new Product();
		product.setProductHeight(new BigDecimal(4));
		product.setProductLength(new BigDecimal(3));
		product.setProductWidth(new BigDecimal(1));
		product.setSku("TB12345");
		product.setManufacturer(oreilley);
		product.setType(generalType);
		
		
		//Product description
		ProductDescription description = new ProductDescription();
		description.setName("Spring in Action");
		description.setLanguage(en);
		description.setProduct(product);
		
		product.getCategories().add(tech);
		
		productService.create(product);
		
		//Availability
		ProductAvailability availability = new ProductAvailability();
		availability.setProductDateAvailable(date);
		availability.setProductQuantity(100);
		availability.setRegion("*");
		availability.setProduct(product);//associate with product
		
		productAvailabilityService.create(availability);
		
		ProductPrice dprice = new ProductPrice();
		dprice.setDefaultPrice(true);
		dprice.setProductPriceAmount(new BigDecimal(29.99));
		dprice.setProductPriceAvailability(availability);

		ProductPriceDescription dpd = new ProductPriceDescription();
		dpd.setName("Base price");
		dpd.setProductPrice(dprice);
		dpd.setLanguage(en);
		
		productPriceService.create(dprice);
		

		
		//PRODUCT 2
		
		Product product2 = new Product();
		product2.setProductHeight(new BigDecimal(4));
		product2.setProductLength(new BigDecimal(3));
		product2.setProductWidth(new BigDecimal(1));
		product2.setSku("TB2468");
		product2.setManufacturer(packed);
		product2.setType(generalType);
		
		
		//Product description
		description = new ProductDescription();
		description.setName("This is Node.js");
		description.setLanguage(en);
		description.setProduct(product2);
		
		product2.getCategories().add(tech);
		productService.create(product2);
		
		//Availability
		ProductAvailability availability2 = new ProductAvailability();
		availability2.setProductDateAvailable(date);
		availability2.setProductQuantity(100);
		availability2.setRegion("*");
		availability2.setProduct(product2);//associate with product
		
		productAvailabilityService.create(availability2);
		
		ProductPrice dprice2 = new ProductPrice();
		dprice2.setDefaultPrice(true);
		dprice2.setProductPriceAmount(new BigDecimal(39.99));
		dprice2.setProductPriceAvailability(availability2);

		dpd = new ProductPriceDescription();
		dpd.setName("Base price");
		dpd.setProductPrice(dprice2);
		dpd.setLanguage(en);
		
		productPriceService.create(dprice2);
		
		//PRODUCT 3
		
		Product product3 = new Product();
		product3.setProductHeight(new BigDecimal(4));
		product3.setProductLength(new BigDecimal(3));
		product3.setProductWidth(new BigDecimal(1));
		product3.setSku("NB1111");
		product3.setManufacturer(packed);
		product3.setType(generalType);
		
		
		//Product description
		description = new ProductDescription();
		description.setName("A nice book for you");
		description.setLanguage(en);
		description.setProduct(product3);
		
		product3.getCategories().add(novell);
		productService.create(product3);
		
		//Availability
		ProductAvailability availability3 = new ProductAvailability();
		availability3.setProductDateAvailable(date);
		availability3.setProductQuantity(100);
		availability3.setRegion("*");
		availability3.setProduct(product3);//associate with product
		
		productAvailabilityService.create(availability3);
		
		ProductPrice dprice3 = new ProductPrice();
		dprice3.setDefaultPrice(true);
		dprice3.setProductPriceAmount(new BigDecimal(19.99));
		dprice3.setProductPriceAvailability(availability3);

		dpd = new ProductPriceDescription();
		dpd.setName("Base price");
		dpd.setProductPrice(dprice3);
		dpd.setLanguage(en);
		
		productPriceService.create(dprice3);
		
		
		
		//PRODUCT 4
		
		Product product4 = new Product();
		product4.setProductHeight(new BigDecimal(4));
		product4.setProductLength(new BigDecimal(3));
		product4.setProductWidth(new BigDecimal(1));
		product4.setSku("SF333345");
		product4.setManufacturer(packed);
		product4.setType(generalType);
				
				
		//Product description
		description = new ProductDescription();
		description.setName("Battle of the worlds");
		description.setLanguage(en);
		description.setProduct(product4);
				
		product4.getCategories().add(fiction);
		productService.create(product3);
				
		//Availability
		ProductAvailability availability4 = new ProductAvailability();
		availability4.setProductDateAvailable(date);
		availability4.setProductQuantity(100);
		availability4.setRegion("*");
		availability4.setProduct(product4);//associate with product
				
		productAvailabilityService.create(availability4);
				
		ProductPrice dprice4 = new ProductPrice();
		dprice4.setDefaultPrice(true);
		dprice4.setProductPriceAmount(new BigDecimal(18.99));
		dprice4.setProductPriceAvailability(availability4);

		dpd = new ProductPriceDescription();
		dpd.setName("Base price");
		dpd.setProductPrice(dprice4);
		dpd.setLanguage(en);
				
		productPriceService.create(dprice4);
				
		
		
		
		
		
	}

	

}
