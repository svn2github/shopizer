package com.salesmanager.test.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValue;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.catalog.product.model.type.ProductType;
import com.salesmanager.core.business.customer.model.Billing;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.Delivery;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderTotal;
import com.salesmanager.core.business.order.model.orderaccount.OrderAccount;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.model.orderproduct.OrderProductAttribute;
import com.salesmanager.core.business.order.model.orderproduct.OrderProductDownload;
import com.salesmanager.core.business.order.model.orderproduct.OrderProductPrice;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.test.core.AbstractSalesManagerCoreTestCase;


public class OrderSalesManagerTestCase extends AbstractSalesManagerCoreTestCase {

//	@Ignore
	@Test
	public void createOrder() throws ServiceException {
		

	    MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
	    
		//create a product
	    ProductType generalType = productTypeService.getProductType(ProductType.GENERAL_TYPE);
	    
	    Language en = languageService.getByCode("en");

	    Product product = new Product();
	    product.setProductHeight(new BigDecimal(4));
	    product.setProductLength(new BigDecimal(3));
	    product.setProductWidth(new BigDecimal(5));
	    product.setProductWeight(new BigDecimal(8));
	    product.setSku("TESTSKU");
	    product.setType(generalType);
	    product.setMerchantStore(store);

	    // Product description
	    ProductDescription description = new ProductDescription();
	    description.setName("Product 1");
	    description.setLanguage(en);
	    description.setProduct(product);

	    product.getDescriptions().add(description);
	    

	    // Availability
	    ProductAvailability availability = new ProductAvailability();
	    availability.setProductDateAvailable(new Date());
	    availability.setProductQuantity(100);
	    availability.setRegion("*");
	    availability.setProduct(product);// associate with product

	    ProductPrice dprice = new ProductPrice();
	    dprice.setDefaultPrice(true);
	    dprice.setProductPriceAmount(new BigDecimal(29.99));
	    dprice.setProductAvailability(availability);

	    ProductPriceDescription dpd = new ProductPriceDescription();
	    dpd.setName("Base price");
	    dpd.setProductPrice(dprice);
	    dpd.setLanguage(en);

	    dprice.getDescriptions().add(dpd);
	    
	    productService.saveOrUpdate(product);
	    
	    //create a Customer
		Country country = countryService.getByCode("CA");
		Zone zone = zoneService.getByCode("VT");
		
		Customer customer = new Customer();
		customer.setFirstname("Leonardo");
		customer.setMerchantStore(store);
		customer.setLastname("Ribeiro");
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
	    delivery.setAddress("Delivery address");
	    delivery.setCity( "Delivery city " );
	    delivery.setCompany( "Delivery company ");
	    delivery.setCountry(country);
	    delivery.setCountryCode("1" );
	    delivery.setName("Delivery Name" );
	    delivery.setPostalCode("Delivery PostalCode" );
	    delivery.setState("Delivery State" );
	    delivery.setZone(zone);	    
	    
	    Billing billing = new Billing();
	    billing.setAddress("Billing address");
	    billing.setCity("Billing city");
	    billing.setCompany("Billing company");
	    billing.setCountry(country);
	    billing.setCountryCode(CA_COUNTRY_CODE);
	    billing.setName("Billing name");
	    billing.setPostalCode("Billing postal code");
	    billing.setState("Billing state");
	    billing.setZone(zone);
	    
	    customer.setBilling(billing);
	    customer.setDelivery(delivery);		
		customerService.create(customer);
		
		Currency currency = currencyService.getByCode(EURO_CURRENCY_CODE);

		OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
		
		
		Order order = new Order();
		order.setDatePurchased(new Date());
		order.setCurrency(currency);
		order.setLastModified(new Date());
		order.setBilling(billing);

		order.setCardType("Visa");
		order.setCcCvv("123");
		order.setCcExpires("12/30/2020" );
		order.setCcNumber( "123456789");
		order.setCcOwner("ccOwner" );
		order.setChannel(1);
		order.setCouponCode("1");
		order.setCurrencyValue(new BigDecimal(19.99));
		order.setCustomerId(new Long(1) );
		order.setDelivery(delivery);
		order.setDisplayInvoicePayments(true);
		order.setIpAddress("ipAddress" );
		order.setMerchant(store);
		order.getOrderAccounts().add( new OrderAccount() );
		order.setOrderDateFinished(new Date());
		orderStatusHistory.setComments(" Status History comment");
		orderStatusHistory.setCustomerNotified(1);
		orderStatusHistory.setStatus( OrderStatus.ORDERED);
		orderStatusHistory.setDateAdded(new Date() );
		orderStatusHistory.setOrder(order);
		order.getOrderHistory().add( orderStatusHistory );		
		order.setOrderTax(new BigDecimal(4.00));
		order.setPaymentMethod("Cash");
		order.setPaymentModuleCode("payment Module Code");
		order.setShippingMethod("UPS");
		order.setShippingModuleCode("Shipping Module Code" );
		order.setStatus( OrderStatus.ORDERED);
		order.setTotal(new BigDecimal(23.99));
		
		
		//OrderProductDownload
		OrderProductDownload orderProductDownload = new OrderProductDownload();
		orderProductDownload.setDownloadCount(1);
		orderProductDownload.setFileId( new Long(1) );
		orderProductDownload.setMaxdays(31);		
		orderProductDownload.setOrderProductFilename("order Product Download");
		
		//OrderProductPrice
		OrderProductPrice oproductprice = new OrderProductPrice();
		oproductprice.setDefaultPrice(true);	
		oproductprice.setProductPriceAmount(new BigDecimal(19.99) );
		oproductprice.setProductPriceCode("product Price code" );
		oproductprice.setProductPriceName("product Price Name" );
		oproductprice.setProductPriceSpecialAmount(new BigDecimal(13.99) );	

		
		//OrderProduct
		OrderProduct oproduct = new OrderProduct();
		oproduct.getDownloads().add(  orderProductDownload);
		oproduct.setFinalPrice(new BigDecimal(19.99) );
		oproduct.setOnetimeCharge( new BigDecimal(6.99) );
		oproduct.setOrder(order);		
		oproduct.setProductName( "Order Product Name" );
		oproduct.setProductQuantity(5);
		oproduct.setProductSpecialDateAvailable( new Date() );
		oproduct.setProductSpecialDateExpire( new Date() );
		oproduct.setProductSpecialPrice( new BigDecimal(14.99 ) );
		oproduct.setSku("Order Product sku" );		
		oproduct.getPrices().add(oproductprice ) ;
		
		oproductprice.setOrderProduct(oproduct);		
		orderProductDownload.setOrderProduct(oproduct);
		order.getOrderProducts().add(oproduct);

		//requires 
		//OrderProduct
		//OrderProductPrice
		//OrderTotal
		

		
		//OrderTotal
		OrderTotal ordertotal = new OrderTotal();	
		ordertotal.setModule("OrderTotal Module" );		
		ordertotal.setSortOrder(1);
		ordertotal.setText("OrderTotal Text" );
		ordertotal.setTitle("OrderTotal Title" );
		ordertotal.setValue(new BigDecimal(19.99 ) );
		ordertotal.setOrder(order);
		
		order.getOrderTotal().add(ordertotal);
		
		orderService.create(order);
		Assert.assertTrue(orderService.count() == 1);
	}
	
	@Ignore
	@Test
	public void getMerchantOrders() throws ServiceException {
		
		List<Order> merchantOrders= new ArrayList<Order>();
		
		Language language = languageService.getByCode(ENGLISH_LANGUAGE_CODE);
		Currency currency = currencyService.getByCode(EURO_CURRENCY_CODE);
		Country country = countryService.getByCode(FR_COUNTRY_CODE);
		Zone zone = zoneService.getByCode("VT");
		
		MerchantStore merchant = new MerchantStore();		
		merchant.setCurrency(currency);
		merchant.setStorename("Test Store");
		merchant.setCountry(country);
		merchant.setDefaultLanguage(language);		
		merchant.setStorecity("Test Store City");
		merchant.setCode( merchantService.count()+"");
		Language en = languageService.getByCode("en");
		Language fr = languageService.getByCode("fr");
		List<Language> supportedLanguages = new ArrayList<Language>();
		supportedLanguages.add(en);
		supportedLanguages.add(fr);
		merchant.setLanguages( supportedLanguages );
		merchant.setStoreEmailAddress("store_email@email.com");
		merchant.setStorephone("Merchant Store Phone");
		merchant.setStorepostalcode("12061");		
		merchantService.create(merchant);	
		
		
		Customer customer = new Customer();	
		customer.setMerchantStore(merchant);
		customer.setCountry(country);
		customer.setFirstname("Ahmed");
		customer.setLastname("Faraz");
		customer.setCity("Dubai");
		customer.setEmailAddress("email@email.com");
		customer.setPostalCode("63839");		
		customer.setStreetAddress("Customer Address");		
		customer.setTelephone("Customer Phone");
		customer.setZone(zone);
		customer.setNewsletter('W');
		customer.setState("Unknown state");
		customer.setPassword("-1999");
		customer.setNick("My New nick");
		customer.setCompany(" Apple");	
		customer.setGender("F");
		customer.setFax("fax-123-4443");
		customer.setDateOfBirth(new Date());		
		
		Billing billing = new Billing();
	    billing.setAddress("Billing address");
	    billing.setCity("Billing city");
	    billing.setCompany("Billing company");
	    billing.setCountry(country);
	    billing.setCountryCode(CA_COUNTRY_CODE);
	    billing.setName("Billing name");
	    billing.setPostalCode("Billing postal code");
	    billing.setState("Billing state");
	    billing.setZone(zone);
	    
	    Delivery delivery = new Delivery();
	    delivery.setAddress("Shipping address");
	    delivery.setCountry(country);
	    delivery.setZone(zone);	    
	    
	    customer.setBilling(billing);
	    customer.setDelivery(delivery);
	    
		customerService.create(customer);		
				
		OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
		Order order = new Order();
		order.setDatePurchased(new Date());
		order.setCurrency(currency);
		order.setMerchant(merchant);
		order.setLastModified(new Date());
		
		order.setCardType("Visa");
		order.setCcCvv("123");
		order.setCcExpires("12/30/2020" );
		order.setCcNumber( "123456789");
		order.setCcOwner("ccOwner" );
		order.setChannel(1);
		order.setCouponCode("1");
		order.setCurrencyValue(new BigDecimal(19.99));
		order.setCustomerId(new Long(1) );
		order.setDelivery(delivery);
		order.setDisplayInvoicePayments(true);
		order.setIpAddress("ipAddress" );
		order.setMerchant(merchant);
		order.getOrderAccounts().add( new OrderAccount() );
		order.setOrderDateFinished(new Date());		
		orderStatusHistory.setDateAdded(new Date() );
		orderStatusHistory.setOrder(order);
		order.setOrderTax(new BigDecimal(4.00));
		order.setPaymentMethod("Cash");
		order.setPaymentModuleCode("payment Module Code");
		order.setShippingMethod("UPS");
		order.setShippingModuleCode("Shipping Module Code" );
		order.setStatus( OrderStatus.ORDERED);
		order.setTotal(new BigDecimal(23.99));
		
		//OrderProduct
		OrderProduct oproduct = new OrderProduct();
		oproduct.setDownloads(null);
		oproduct.setFinalPrice( new BigDecimal(19.99) );
		oproduct.setOnetimeCharge( new BigDecimal(6.99) );
		oproduct.setOrder(order);		
		oproduct.setProductName( "Order Product Name" );
		oproduct.setProductQuantity(5);
		oproduct.setProductSpecialDateAvailable( new Date() );
		oproduct.setProductSpecialDateExpire( new Date() );
		oproduct.setProductSpecialPrice( new BigDecimal(14.99 ) );
		oproduct.setSku("Order Product sku" );		

//		order.getOrderProducts().add(oproduct);
		orderService.create(order);

		try {
			merchantOrders = orderService.listByStore(merchant);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Assert.assertTrue("Merchant Orders are null." , merchantOrders != null);
		Assert.assertTrue("Merchant Orders count is not one." , (merchantOrders != null && merchantOrders.size() == 1) );
	}

}