package com.salesmanager.test.shipping;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.catalog.product.model.type.ProductType;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shipping.model.PackageDetails;
import com.salesmanager.core.business.shipping.model.ShippingBasisType;
import com.salesmanager.core.business.shipping.model.ShippingConfiguration;
import com.salesmanager.core.business.shipping.model.ShippingPackageType;
import com.salesmanager.core.business.shipping.model.ShippingProduct;
import com.salesmanager.core.business.shipping.model.ShippingType;
import com.salesmanager.core.business.shipping.service.ShippingService;
import com.salesmanager.core.business.system.model.Environment;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.test.core.AbstractSalesManagerCoreTestCase;

public class ShippingTestCase extends AbstractSalesManagerCoreTestCase {
	
	private static final Date date = new Date(System.currentTimeMillis());
	
	@Autowired
	private ShippingService shippingService;
	
	/**
	 * This test will invoke a shipping module to get real time shipping quotes
	 * @throws ServiceException
	 */
	@Test
	public void testGetShippingQuote() throws ServiceException {

	    Language en = languageService.getByCode("en");

	    MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
	    ProductType generalType = productTypeService.getProductType(ProductType.GENERAL_TYPE);
	    
	    //generate 2 products
	    
	    // PRODUCT 1 (height 4 inches x 3 inches length + 5 inches width) 1 pound
	    Product product = new Product();
	    product.setProductHeight(new BigDecimal(4));
	    product.setProductLength(new BigDecimal(3));
	    product.setProductWidth(new BigDecimal(5));
	    product.setProductWeight(new BigDecimal(8));
	    product.setSku("TB12345");
	    product.setType(generalType);
	    product.setMerchantStore(store);

	    // Product description
	    ProductDescription description = new ProductDescription();
	    description.setName("Product 1");
	    description.setLanguage(en);
	    description.setProduct(product);

	    product.getDescriptions().add(description);
	    //productService.create(product);

	    // Availability
	    ProductAvailability availability = new ProductAvailability();
	    availability.setProductDateAvailable(date);
	    availability.setProductQuantity(100);
	    availability.setRegion("*");
	    availability.setProduct(product);// associate with product

	    //productAvailabilityService.create(availability);

	    ProductPrice dprice = new ProductPrice();
	    dprice.setDefaultPrice(true);
	    dprice.setProductPriceAmount(new BigDecimal(29.99));
	    dprice.setProductAvailability(availability);

	    ProductPriceDescription dpd = new ProductPriceDescription();
	    dpd.setName("Base price");
	    dpd.setProductPrice(dprice);
	    dpd.setLanguage(en);

	    dprice.getDescriptions().add(dpd);

	    //productPriceService.create(dprice);
	    

	    

	    // PRODUCT 2  (height 3 inches x 4 inches length x 5 inches width) 2 pounds

	    Product product2 = new Product();
	    product2.setProductHeight(new BigDecimal(3));
	    product2.setProductLength(new BigDecimal(4));
	    product2.setProductWidth(new BigDecimal(5));
	    product2.setProductWeight(new BigDecimal(2));
	    product2.setSku("TB2468");
	    product2.setType(generalType);
	    product2.setMerchantStore(store);

	    // Product description
	    description = new ProductDescription();
	    description.setName("Product 2");
	    description.setLanguage(en);
	    description.setProduct(product2);

	    product2.getDescriptions().add(description);

	    //productService.create(product2);

	    // Availability
	    ProductAvailability availability2 = new ProductAvailability();
	    availability2.setProductDateAvailable(date);
	    availability2.setProductQuantity(100);
	    availability2.setRegion("*");
	    availability2.setProduct(product2);// associate with product

	    //productAvailabilityService.create(availability2);

	    ProductPrice dprice2 = new ProductPrice();
	    dprice2.setDefaultPrice(true);
	    dprice2.setProductPriceAmount(new BigDecimal(39.99));
	    dprice2.setProductAvailability(availability2);

	    dpd = new ProductPriceDescription();
	    dpd.setName("Base price");
	    dpd.setProductPrice(dprice2);
	    dpd.setLanguage(en);

	    dprice2.getDescriptions().add(dpd);

	    //productPriceService.create(dprice2);
	    
	    //add an attribute to product 2 that will augment weight of 1 pound
	    ProductAttribute attribute = new ProductAttribute();
	    attribute.setProduct(product2);
	    attribute.setAttributeDefault(true);
	    attribute.setOptionValuePrice(new BigDecimal(0));//no price variation
	    attribute.setProductAttributeWeight(new BigDecimal(1));//weight variation

	    
	    product2.getAttributes().add(attribute);
	    
	    //create an integration configuration
	    IntegrationConfiguration configuration = new IntegrationConfiguration();
	    configuration.setActive(true);
	    configuration.setEnvironment(Environment.TEST.name());
	    configuration.setModuleCode("canadapost");
	    
	    //configure shipping
	    ShippingConfiguration shippingConfiguration = new ShippingConfiguration();
	    shippingConfiguration.setShippingBasisType(ShippingBasisType.SHIPPING);
	    shippingConfiguration.setShippingType(ShippingType.INTERNATIONAL);
	    shippingConfiguration.setShippingPackageType(ShippingPackageType.BOX);
	    shippingConfiguration.setBoxHeight(5);
	    shippingConfiguration.setBoxLength(5);
	    shippingConfiguration.setBoxWidth(5);
	    shippingConfiguration.setBoxWeight(1);
	    shippingConfiguration.setMaxWeight(10);
	    
	    //configure module
	    List<String> options = new ArrayList<String>();
	    options.add("PACKAGE");
	    configuration.getIntegrationKeys().put("account", "CPC_CS_TI_INC");
	    configuration.getIntegrationOptions().put("packages",options);
	    
	    shippingService.saveShippingConfiguration(shippingConfiguration, store);
	    shippingService.saveShippingQuoteModuleConfiguration(configuration, store);
	    
	    //now create ShippingProduct
	    ShippingProduct shippingProduct1 = new ShippingProduct(product);
	    ShippingProduct shippingProduct2 = new ShippingProduct(product2);
	    List<ShippingProduct> shippingProducts = new ArrayList<ShippingProduct>();
	    shippingProducts.add(shippingProduct1);
	    shippingProducts.add(shippingProduct2);
	    
	    List<PackageDetails> details = shippingService.getPackagesDetails(shippingProducts, store);
	    
	    Assert.notNull(details);
	    
	    for(PackageDetails pack : details) {
	    	System.out.println("Height " + pack.getShippingHeight());
	    	System.out.println("Length " + pack.getShippingLength());
	    	System.out.println("Width " + pack.getShippingWidth());
	    	System.out.println("Weight " + pack.getShippingWeight());
	    }

	    
	}


}