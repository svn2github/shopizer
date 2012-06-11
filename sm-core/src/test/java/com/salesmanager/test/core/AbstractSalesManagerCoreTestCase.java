package com.salesmanager.test.core;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValue;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionValueService;
import com.salesmanager.core.business.catalog.product.service.availability.ProductAvailabilityService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.catalog.product.service.price.ProductPriceService;
import com.salesmanager.core.business.catalog.product.service.type.ProductTypeService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.generic.util.EntityManagerUtils;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.model.StoreBranding;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.merchant.service.StoreBrandingService;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.currency.service.CurrencyService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.service.ZoneService;

@ContextConfiguration(locations = {
		"classpath:spring/test-spring-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
	SalesManagerCoreTestExecutionListener.class
})
public abstract class AbstractSalesManagerCoreTestCase {
	
	protected static final String ENGLISH_LANGUAGE_CODE = "en";
	
	protected static final String EURO_CURRENCY_CODE = "EUR";
	
	protected static final String FRA_COUNTRY_CODE = "FRA";
	
	@Autowired
	private EntityManagerUtils entityManagerUtils;
	
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
	protected StoreBrandingService storeBrandingService;
	
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


	@Before
	public void init() throws ServiceException {
		cleanAll();
		checkEmptyDatabase();	
		populate();

	}
	
	@After
	public void close() throws ServiceException {
		//cleanAll();
		//checkEmptyDatabase();
	}
	
	private void cleanAll() throws ServiceException {
		cleanCategories();
		cleanOrders();
		cleanCustomers();
		cleanProducts();
		cleanCurrencies();
		cleanCountries();
		cleanMerchants();
		cleanLanguages();
	}
	
	

	private void cleanCountries() throws ServiceException {
		for (Country country : countryService.list()) {
			countryService.delete(country);
		}
	}

	private void cleanCustomers() throws ServiceException {
		List<Customer> customers = customerService.list();
		for(Customer customer : customers) {
			customerService.delete(customer);
		}
	}

	private void cleanCurrencies() throws ServiceException {
		List<Currency> list = currencyService.list();
		for (Currency currency : list) {
			currencyService.delete(currency);
		}
		
	}

	private void cleanCategories() throws ServiceException {
		List<Category> list = categoryService.list();
		for (Category category : list) {
			categoryService.delete(category);
		}
	}
	
	private void cleanMerchants() throws ServiceException {
/*		List<MerchantStore> list = merchantService.list();
		for (MerchantStore merchant : list) {
			merchantService.delete(merchant);
		}
		List<StoreBranding> blist = storeBrandingService.list();
		for (StoreBranding storeBranding : blist) {
			storeBrandingService.delete(storeBranding);
		}*/
	}
	

	
	private void cleanProducts() throws ServiceException {
		List<Product> list = productService.list();
		for (Product product : list) {
			productService.delete(product);
		}
		for(ProductAvailability availability : productAvailabilityService.list()) {
			productAvailabilityService.delete(availability);
		}
		for(ProductPrice price : productPriceService.list()) {
			productPriceService.delete(price);
		}
		for(ProductImage image : productImageService.list()) {
			productImageService.delete(image);
		}
		for(ProductOption option : productOptionService.list()){
			productOptionService.delete(option);
		}
		for(ProductOptionValue value : productOptionValueService.list()) {
			productOptionValueService.delete(value);
		}
		for(ProductAttribute attribute : productAttributeService.list()) {
			productAttributeService.delete(attribute);
		}
		for(Manufacturer manufacturer : manufacturerService.list()) {
			manufacturerService.delete(manufacturer);
		}
	}
	
	private void cleanLanguages() throws ServiceException {
		List<Language> list = languageService.list();
		for (Language language : list) {
			languageService.delete(language);
		}
	}
	
	private void cleanOrders() throws ServiceException {
		List<Order> list = orderService.list();
		for (Order order : list) {
			orderService.delete(order);
		}
	}
	
	private void checkEmptyDatabase() {
		Set<EntityType<?>> entityTypes = getEntityManager().getEntityManagerFactory().getMetamodel().getEntities();
		for (EntityType<?> entityType : entityTypes) {
			List<?> entities = listEntities(entityType.getBindableJavaType());
			
			if (entities.size() > 0) {
				Assert.fail(String.format("Il reste des objets de type %1$s", entities.get(0).getClass().getSimpleName()));
			}
		}
	}
	
	protected <E> List<E> listEntities(Class<E> clazz) {
		CriteriaBuilder cb = entityManagerUtils.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		cq.from(clazz);
		
		return entityManagerUtils.getEntityManager().createQuery(cq).getResultList();
	}
	
	protected <E extends SalesManagerEntity<?, ?>> Long countEntities(Class<E> clazz) {
		CriteriaBuilder cb = entityManagerUtils.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<E> root = cq.from(clazz);
		cq.select(cb.count(root));
		
		return (Long) entityManagerUtils.getEntityManager().createQuery(cq).getSingleResult();
	}
	
	protected void assertDatesWithinXSeconds(Date date1, Date date2, Integer delayInSeconds) {
		Assert.assertTrue(Math.abs(date1.getTime() - date2.getTime()) < delayInSeconds * 1000l);
	}

	protected EntityManager getEntityManager() {
		return entityManagerUtils.getEntityManager();
	}

	protected void entityManagerOpen() {
		entityManagerUtils.openEntityManager();
	}

	protected void entityManagerClose() {
		entityManagerUtils.closeEntityManager();
	}

	protected void entityManagerReset() {
		entityManagerClose();
		entityManagerOpen();
	}

	protected void entityManagerClear() {
		entityManagerUtils.getEntityManager().clear();
	}
	
	protected void entityManagerDetach(Object object) {
		entityManagerUtils.getEntityManager().detach(object);
	}
	
	private void populate() throws ServiceException {
		Language language = new Language();
		language.setCode(ENGLISH_LANGUAGE_CODE);
		languageService.create(language);
		
		Currency currency = new Currency();
		currency.setCurrency(java.util.Currency.getInstance(EURO_CURRENCY_CODE));
		currencyService.create(currency);
		
		Country country = new Country(FRA_COUNTRY_CODE);
		countryService.create(country);
	}
}
