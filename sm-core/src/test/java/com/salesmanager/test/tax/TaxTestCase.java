package com.salesmanager.test.tax;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.common.model.Delivery;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderSummary;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.shipping.model.ShippingConfiguration;
import com.salesmanager.core.business.shipping.service.ShippingService;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;
import com.salesmanager.core.business.tax.model.TaxBasisCalculation;
import com.salesmanager.core.business.tax.model.TaxConfiguration;
import com.salesmanager.core.business.tax.model.TaxItem;
import com.salesmanager.core.business.tax.model.taxclass.TaxClass;
import com.salesmanager.core.business.tax.model.taxrate.TaxRate;
import com.salesmanager.core.business.tax.model.taxrate.TaxRateDescription;
import com.salesmanager.core.business.tax.service.TaxClassService;
import com.salesmanager.core.business.tax.service.TaxRateService;
import com.salesmanager.core.business.tax.service.TaxService;
import com.salesmanager.test.core.AbstractSalesManagerCoreTestCase;

public class TaxTestCase extends AbstractSalesManagerCoreTestCase {
	
	private static final Date date = new Date(System.currentTimeMillis());
	
	@Autowired
	private TaxService taxService;
	
	@Autowired
	private TaxRateService taxRateService;
	
	@Autowired
	private ShippingService shippingService;
	
	@Autowired
	private TaxClassService taxClassService;
	
	/**
	 * Test tax calculation
	 * @throws ServiceException
	 */
	@Test
	public void testCanadianSalesTax() throws ServiceException {

	    Language en = languageService.getByCode("en");
	    Country country = countryService.getByCode("CA");
	    Zone zone = zoneService.getByCode("QC");
	    TaxClass defaultTaxClass = taxClassService.getByCode(TaxClass.DEFAULT_TAX_CLASS);

	    MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
	    
	    //create tax configuration based on store location in the admin
	    
	    TaxConfiguration taxConfiguration = new TaxConfiguration();
	    taxConfiguration.setTaxBasisCalculation(TaxBasisCalculation.STOREADDRESS);
	    
	    taxService.saveTaxConfiguration(taxConfiguration, store);
	    
	    //tax on shipping
	    ShippingConfiguration shippingConfiguration = new ShippingConfiguration();
	    shippingConfiguration.setTaxOnShipping(true);
	    shippingService.saveShippingConfiguration(shippingConfiguration, store);
	    
	    OrderSummary orderSummary = new OrderSummary();
	    orderSummary.setShipping(new BigDecimal(10));
	    
	    ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
	    
	    Product product = new Product();
	    product.setProductHeight(new BigDecimal(4));
	    product.setProductLength(new BigDecimal(3));
	    product.setProductWidth(new BigDecimal(5));
	    product.setProductWeight(new BigDecimal(8));
	    product.setSku("TB12345");
	    product.setMerchantStore(store);
	    product.setTaxClass(defaultTaxClass);
	    
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
	    
	    shoppingCartItem.setItemPrice(new BigDecimal(29.99));
	    shoppingCartItem.setProduct(product);
	    
	    orderSummary.getProducts().add(shoppingCartItem);
	    
	    //create tax rates in the admin for QC - CA
	    TaxRate tps = new TaxRate();
	    tps.setCode("TPS");
	    tps.setCountry(country);
	    tps.setZone(zone);
	    tps.setMerchantStore(store);
	    tps.setTaxClass(defaultTaxClass);
	    tps.setTaxPriority(0);
	    tps.setTaxRate(new BigDecimal(5));
	    
	    TaxRateDescription tpsDescription = new TaxRateDescription();
	    tpsDescription.setName("TPS");
	    tpsDescription.setDescription("TPS Sales Tax");
	    tpsDescription.setTaxRate(tps);
	    
	    taxRateService.create(tps);
	    
	    
	    TaxRate tvq = new TaxRate();
	    tvq.setCode("TVQ");
	    tvq.setCountry(country);
	    tvq.setZone(zone);
	    tvq.setMerchantStore(store);
	    tvq.setTaxClass(defaultTaxClass);
	    tvq.setTaxPriority(1);
	    tvq.setTaxRate(new BigDecimal(7));
	    tvq.setPiggyback(true);
	    tvq.setParent(tps);
	    
	    TaxRateDescription tvqDescription = new TaxRateDescription();
	    tvqDescription.setName("TVQ");
	    tvqDescription.setDescription("TVQ Sales Tax");
	    tvqDescription.setTaxRate(tvq);
	    
	    taxRateService.create(tvq);
	    
	    //create a Customer with origin QC - CA
		Customer customer = new Customer();
		customer.setFirstname("Test");
		customer.setMerchantStore(store);
		customer.setLastname("User");
		customer.setCity("city");
		customer.setEmailAddress("test@test.com");
		customer.setGender("M");
		customer.setTelephone("00000");
		customer.setAnonymous(true);
		customer.setCompany("ifactory");
		customer.setDateOfBirth(new Date());
		customer.setFax("fax");
		customer.setNewsletter('c');
		customer.setNick("My nick");
		customer.setPassword("123456");
		customer.setPostalCode("000");
		customer.setState("state");
		customer.setStreetAddress("Street 1");
		customer.setTelephone("123123");
		customer.setCountry(country);
		customer.setZone(zone);
		
	    Delivery delivery = new Delivery();
	    delivery.setAddress("Shipping address");
	    delivery.setCity("Boucherville");
	    delivery.setCountry(country);
	    delivery.setZone(zone);
	    delivery.setPostalCode("J4B-8J9");
	    
	    List<TaxItem> taxLines = taxService.calculateTax(orderSummary, customer, store, Locale.ENGLISH);
	    
	    Assert.assertNotNull(taxLines);
	    
	    for(TaxItem taxItem : taxLines) {
	    	
	    	System.out.println(taxItem.getLabel() + " " + taxItem.getItemPrice().toPlainString());
	    	
	    }
	    
	}



}