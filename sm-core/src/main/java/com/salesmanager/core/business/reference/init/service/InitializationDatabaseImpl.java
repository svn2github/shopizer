package com.salesmanager.core.business.reference.init.service;

import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.business.catalog.product.model.type.ProductType;
import com.salesmanager.core.business.catalog.product.service.type.ProductTypeService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.model.CountryDescription;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.currency.service.CurrencyService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.model.ZoneDescription;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.system.service.SystemConfigurationService;
import com.salesmanager.core.business.tax.model.taxclass.TaxClass;
import com.salesmanager.core.business.tax.service.TaxClassService;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.Permission;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.business.user.service.PermissionService;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.constants.SchemaConstant;

@Service("initializationDatabase")
public class InitializationDatabaseImpl implements InitializationDatabase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationDatabaseImpl.class);
	

	
	@Autowired
	private ZoneService zoneService;
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	protected MerchantStoreService merchantService;
	
	@Autowired
	private SystemConfigurationService systemConfigurationService;
		
	@Autowired
	protected ProductTypeService productTypeService;
	
	@Autowired
	private TaxClassService taxClassService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	protected PermissionService    permissionService;
	
	@Autowired
	protected GroupService               groupService;
	
	private String name;
	
	public boolean isEmpty() {
		return languageService.count() == 0;
	}
	
	@Transactional
	public void populate(String contextName) throws ServiceException {
		this.name =  contextName;
		
		createLanguages();
		createCountries();
		createZones();
		createCurrencies();
		createSubReferences();
		createMerchant();
		createUser();

	}
	
	private void createUser() throws ServiceException {
		LOGGER.info(String.format("%s : Creating user ", name));
		
		
		  Permission userperm = new Permission("GRANT_USER");
		  permissionService.create(userperm);
		  Permission storeperm = new Permission("GRANT_STORE");
		  permissionService.create(storeperm);
		  Permission catalogperm = new Permission("GRANT_CATALOG");
		  permissionService.create(catalogperm);
		  Permission orderperm = new Permission("GRANT_ORDER");
		  permissionService.create(orderperm);
		  Permission configperm = new Permission("GRANT_CONFIG");
		  permissionService.create(configperm);
		  
		  Group admin = new Group("ADMIN");
		  
		  admin.getPermissions().add(userperm);
		  admin.getPermissions().add(storeperm);
		  admin.getPermissions().add(catalogperm);
		  admin.getPermissions().add(orderperm);
		  admin.getPermissions().add(configperm);
		  
		  groupService.create(admin);
		  //TODO encrypt password
		  User user = new User("admin","password","admin@shopizer.com");
		  user.setFirstName("Administrator");
		  user.setLastName("User");
		  user.getGroups().add(admin);
		  
		  userService.create(user);
	}

	private void createCurrencies() throws ServiceException {
		LOGGER.info(String.format("%s : Populating Currencies ", name));
		for (String code : SchemaConstant.CURRENCY_MAP.keySet()) {
			Currency currency = new Currency();
			try {
				currency.setCurrency(java.util.Currency.getInstance(code));
				currency.setName(SchemaConstant.CURRENCY_MAP.get(code));
				currencyService.create(currency);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				LOGGER.info(String.format("%s : Populating Currencies : bad code : %s", name, code));
			}
			
		}
	}

	private void createCountries() throws ServiceException {
		LOGGER.info(String.format("%s : Populating Countries ", name));
		List<Language> languages = languageService.list();
		for(String code : SchemaConstant.COUNTRY_ISO_CODE) {
			Locale locale = SchemaConstant.LOCALES.get(code);
			if (locale != null) {
				Country country = new Country(code);
				countryService.create(country);
				
				for (Language language : languages) {
					String name = locale.getDisplayCountry(new Locale(language.getCode()));
					CountryDescription description = new CountryDescription(language, name);
					countryService.addCountryDescription(country, description);
				}
			}
		}
	}
	
	private void createZones() throws ServiceException {
		LOGGER.info(String.format("%s : Populating Zones ", name));
		List<Language> languages = languageService.list();
		
		List<Country> countries = countryService.list();
		Map<String,Country> countriesMap = new HashMap<String,Country>();
		for(Country country : countries) {
			
			countriesMap.put(country.getIsoCode(), country);
			
		}
		
		ObjectMapper mapper = new ObjectMapper();

        try {

              InputStream in =
                    this.getClass().getClassLoader().getResourceAsStream("reference/zoneconfig.json");

              @SuppressWarnings("unchecked")
              Map<String,Object> data = mapper.readValue(in, Map.class);
              
              Map<String,Zone> zonesMap = new HashMap<String,Zone>();
              Map<String,List<ZoneDescription>> zonesDescriptionsMap = new HashMap<String,List<ZoneDescription>>();
              Map<String,String> zonesMark = new HashMap<String,String>();
              
              for(Language l : languages) {
	              @SuppressWarnings("rawtypes")
	              List langList = (List)data.get(l.getCode());
	              if(langList!=null) {
		              for(Object z : langList) {
		                    @SuppressWarnings("unchecked")
							Map<String,String> e = (Map<String,String>)z;
		                    String zoneCode = e.get("zoneCode");
		                    ZoneDescription zoneDescription = new ZoneDescription();
		                    zoneDescription.setLanguage(l);
		                    zoneDescription.setName(e.get("zoneName"));
		                    Zone zone = null;
		                    List<ZoneDescription> descriptions = null;
		                    if(!zonesMap.containsKey(zoneCode)) {
		                    	zone = new Zone();
		                    	Country country = countriesMap.get(e.get("countryCode"));
		                    	if(country==null) {
		                    		LOGGER.warn("Country is null for " + zoneCode + " and country code " + e.get("countryCode"));
		                    		continue;
		                    	}
			                    zone.setCountry(country);
		                    	zonesMap.put(zoneCode, zone);
		                    	zone.setCode(zoneCode);

		                    }
		                    
		                    
		                    if(zonesMark.containsKey(l.getCode() + "_" + zoneCode)) {
	                    		LOGGER.warn("This zone seems to be a duplicate !  " + zoneCode + " and language code " + l.getCode());
	                    		continue;
		                    }
		                    
		                    zonesMark.put(l.getCode() + "_" + zoneCode, l.getCode() + "_" + zoneCode);
		                    
		                    if(zonesDescriptionsMap.containsKey(zoneCode)) {
		                    	descriptions = zonesDescriptionsMap.get(zoneCode);
		                    } else {
		                    	descriptions = new ArrayList<ZoneDescription>();
		                    	zonesDescriptionsMap.put(zoneCode, descriptions);
		                    }
		                    
		                    descriptions.add(zoneDescription);

		                }
		             }

              }
              
              for (Map.Entry<String, Zone> entry : zonesMap.entrySet()) {
            	    String key = entry.getKey();
            	    Zone value = entry.getValue();
            	    
            	    if(value.getDescriptions()==null) {
            	    	LOGGER.warn("This zone " + key + " has no descriptions");
            	    	continue;
            	    }
            	    
            	    
            	    zoneService.create(value);
            	    //get descriptions
            	    List<ZoneDescription> descriptions = zonesDescriptionsMap.get(key);
            	    for(ZoneDescription description : descriptions) {
            	    	description.setZone(value);
            	    	zoneService.addDescription(value, description);
            	    }
              }


              
  			
  		} catch (Exception e) {
  			throw new ServiceException(e);
  		}

	}

	private void createLanguages() throws ServiceException {
		LOGGER.info(String.format("%s : Populating Languages ", name));
		for(String code : SchemaConstant.LANGUAGE_ISO_CODE) {
			Language language = new Language(code);
			languageService.create(language);
		}
	}
	
	private void createMerchant() throws ServiceException {
		LOGGER.info(String.format("%s : Creating merchant ", name));
		
		Date date = new Date(System.currentTimeMillis());
		
		Language en = languageService.getByCode("en");
		Country ca = countryService.getByCode("CA");
		Currency currency = currencyService.getByCode("CAD");
		
		List<Language> supportedLanguages = new ArrayList<Language>();
		supportedLanguages.add(en);
		
		//create a merchant
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
		store.setLanguages(supportedLanguages);
		
		merchantService.create(store);
		
		
		TaxClass taxclass = new TaxClass(TaxClass.DEFAULT_TAX_CLASS);
		taxclass.setMerchantSore(store);
		
		taxClassService.create(taxclass);
		
		
	}
	
	private void createSubReferences() throws ServiceException {
		
		LOGGER.info(String.format("%s : Loading catalog sub references ", name));
		

		
		ProductType productType = new ProductType();
		productType.setCode(ProductType.GENERAL_TYPE);
		productTypeService.create(productType);
		

		
		
	}
	

	



}
