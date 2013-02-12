package com.salesmanager.core.business.shipping.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.shipping.model.PackageDetails;
import com.salesmanager.core.business.shipping.model.ShippingConfiguration;
import com.salesmanager.core.business.shipping.model.ShippingProduct;
import com.salesmanager.core.business.shipping.model.ShippingQuote;
import com.salesmanager.core.business.shipping.model.ShippingType;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.business.system.model.MerchantConfiguration;
import com.salesmanager.core.business.system.service.MerchantConfigurationService;
import com.salesmanager.core.business.system.service.ModuleConfigurationService;
import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.modules.integration.IntegrationException;
import com.salesmanager.core.modules.integration.shipping.model.ShippingQuoteModule;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.core.utils.reference.ConfigurationModulesLoader;

@Service("shippingService")
public class ShippingServiceImpl implements ShippingService {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ShippingServiceImpl.class);
	
	
	private final static String SUPPORTED_COUNTRIES = "SUPPORTED_CNTR";
	private final static String SHIPPING_MODULES = "SHIPPING";
	
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductPriceUtils productPriceUtils;
	
	@Autowired
	private ModuleConfigurationService moduleConfigurationService;
	
	@Autowired
	@Resource(name="shippingModules")
	private Map<String,ShippingQuoteModule> shippingModules;
	
	@Override
	public ShippingConfiguration getShippingConfiguration(MerchantStore store) throws ServiceException {
		
		
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(ShippingConstants.SHIPPING_CONFIGURATION, store);
		
		ShippingConfiguration shippingConfiguration = null;
		
		if(configuration!=null) {
			String value = configuration.getValue();
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				shippingConfiguration = mapper.readValue(value, ShippingConfiguration.class);
			} catch(Exception e) {
				throw new ServiceException("Cannot parse json string " + value);
			}
		}
		return shippingConfiguration;
		
	}
	
	@Override
	public void saveShippingConfiguration(ShippingConfiguration shippingConfiguration, MerchantStore store) throws ServiceException {
		
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(ShippingConstants.SHIPPING_CONFIGURATION, store);

		if(configuration==null) {
			configuration = new MerchantConfiguration();
			configuration.setMerchantStore(store);
			configuration.setKey(ShippingConstants.SHIPPING_CONFIGURATION);
		}
		
		String value = shippingConfiguration.toJSONString();
		configuration.setValue(value);
		merchantConfigurationService.saveOrUpdate(configuration);
		
	}
	

	@Override
	public List<IntegrationModule> getShippingMethods(MerchantStore store) throws ServiceException {
		
		List<IntegrationModule> modules =  moduleConfigurationService.getIntegrationModules(SHIPPING_MODULES);
		List<IntegrationModule> returnModules = new ArrayList<IntegrationModule>();
		
		for(IntegrationModule module : modules) {
			if(module.getRegionsSet().contains(store.getCountry().getIsoCode())
					|| module.getRegionsSet().contains("*")) {
				
				returnModules.add(module);
			}
		}
		
		return returnModules;
	}
	
	@Override
	public void saveShippingQuoteModuleConfiguration(IntegrationConfiguration configuration, MerchantStore store) throws ServiceException {
		
		
			
			//validate entries
			try {
				
				String moduleCode = configuration.getModuleCode();
				
				ShippingQuoteModule quoteModule = (ShippingQuoteModule)shippingModules.get(moduleCode);

				if(quoteModule==null) {
					throw new ServiceException("Shipping quote module " + moduleCode + " does not exist");
				}
				
				quoteModule.validateModuleConfiguration(configuration, store);
				
			} catch (IntegrationException ie) {
				throw ie;
			}
			
			try {
				Map<String,IntegrationConfiguration> modules = new HashMap<String,IntegrationConfiguration>();
				MerchantConfiguration merchantConfiguration = merchantConfigurationService.getMerchantConfiguration(SHIPPING_MODULES, store);
				if(merchantConfiguration!=null) {
					if(!StringUtils.isBlank(merchantConfiguration.getValue())) {
						modules = ConfigurationModulesLoader.loadIntegrationConfigurations(merchantConfiguration.getValue());
					}
				} else {
					merchantConfiguration = new MerchantConfiguration();
					merchantConfiguration.setMerchantStore(store);
					merchantConfiguration.setKey(SHIPPING_MODULES);
				}
				modules.put(configuration.getModuleCode(), configuration);
				
				String configs =  ConfigurationModulesLoader.toJSONString(modules);
				merchantConfiguration.setValue(configs);
				merchantConfigurationService.saveOrUpdate(merchantConfiguration);
				
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		
	}
	
	
	@Override
	public void removeShippingQuoteModuleConfiguration(String moduleCode, MerchantStore store) throws ServiceException {
		
		

		try {
			Map<String,IntegrationConfiguration> modules = new HashMap<String,IntegrationConfiguration>();
			MerchantConfiguration merchantConfiguration = merchantConfigurationService.getMerchantConfiguration(SHIPPING_MODULES, store);
			if(merchantConfiguration!=null) {
				if(!StringUtils.isBlank(merchantConfiguration.getValue())) {
					modules = ConfigurationModulesLoader.loadIntegrationConfigurations(merchantConfiguration.getValue());
				}
				
				modules.remove(moduleCode);
				String configs =  ConfigurationModulesLoader.toJSONString(modules);
				merchantConfiguration.setValue(configs);
				merchantConfigurationService.saveOrUpdate(merchantConfiguration);
				
				
			} 

			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	
}
	
	@Override
	public Map<String,IntegrationConfiguration> getShippingModulesConfigured(MerchantStore store) throws ServiceException {
		try {
			

			Map<String,IntegrationConfiguration> modules = new HashMap<String,IntegrationConfiguration>();
			MerchantConfiguration merchantConfiguration = merchantConfigurationService.getMerchantConfiguration(SHIPPING_MODULES, store);
			if(merchantConfiguration!=null) {
				if(!StringUtils.isBlank(merchantConfiguration.getValue())) {
					
					modules = ConfigurationModulesLoader.loadIntegrationConfigurations(merchantConfiguration.getValue());
					
				}
			}
			return modules;
		
		
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
	public ShippingQuote getShippingQuote(MerchantStore store, Customer customer, List<ShippingProduct> products, Locale locale) throws ServiceException  {
		
		ShippingQuote shippingQuote = new ShippingQuote();
		ShippingQuoteModule shippingQuoteModule = null;
		
		try {
		
			//get configuration
			ShippingConfiguration shippingConfiguration = getShippingConfiguration(store);
			ShippingType shippingType = ShippingType.INTERNATIONAL;
			
			if(shippingConfiguration!=null) {
				if(shippingConfiguration.getShippingType()!=null) {
					shippingType = shippingConfiguration.getShippingType();
				}
			}
	
			//look if customer country code excluded
			Country shipCountry = customer.getDelivery().getCountry();
			if(shipCountry==null) {
				shipCountry = customer.getCountry();
			}
			
			//a ship to country is required
			Validate.notNull(shipCountry);
			Validate.notNull(store.getCountry());
			
			if(shippingType.name().equals(ShippingType.NATIONAL.name())){
				
				//customer country must match store country
				if(!shipCountry.getIsoCode().equals(store.getCountry().getIsoCode())) {
					shippingQuote.setShippingReturnCode(ShippingQuote.NO_SHIPPING_TO_SELECTED_COUNTRY + " " + shipCountry.getIsoCode());
					return shippingQuote;
				}
				
				
			} else if(shippingType.name().equals(ShippingType.INTERNATIONAL.name())){
				
				//customer shipping country code must be in accepted list
				List<String> supportedCountries = this.getSupportedCountries(store);
				if(!supportedCountries.contains(shipCountry.getIsoCode())) {
					shippingQuote.setShippingReturnCode(ShippingQuote.NO_SHIPPING_TO_SELECTED_COUNTRY + " " + shipCountry.getIsoCode());
					return shippingQuote;
				}
			}
			
			//must have a shipping module configured
			Map<String, IntegrationConfiguration> modules = this.getShippingModulesConfigured(store);
			if(modules==null){
				shippingQuote.setShippingReturnCode(ShippingQuote.NO_SHIPPING_MODULE_CONFIGURED);
				return shippingQuote;
			}
			
			for(String module : modules.keySet()) {
				
				IntegrationConfiguration configuration = modules.get(module);
				//use the first active module
				if(configuration.isActive()) {
					shippingQuoteModule = this.shippingModules.get(module);
					break;
				}
			}
			
			if(shippingQuoteModule==null){
				shippingQuote.setShippingReturnCode(ShippingQuote.NO_SHIPPING_MODULE_CONFIGURED);
				return shippingQuote;
			}
			
			//calculate order total
		
			BigDecimal orderTotal = calculateOrderTotal(products,store);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return shippingQuote;
		
		
	}

	@Override
	public List<String> getSupportedCountries(MerchantStore store) throws ServiceException {
		
		List<String> supportedCountries = new ArrayList<String>();
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(SUPPORTED_COUNTRIES, store);
		
		if(configuration!=null) {
			
			String countries = configuration.getValue();
			if(!StringUtils.isBlank(countries)) {

				Object objRegions=JSONValue.parse(countries); 
				JSONArray arrayRegions=(JSONArray)objRegions;
				@SuppressWarnings("rawtypes")
				Iterator i = arrayRegions.iterator();
				while(i.hasNext()) {
					supportedCountries.add((String)i.next());
				}
			}
			
		}
		
		return supportedCountries;
	}
	

	@Override
	public void setSupportedCountries(MerchantStore store, List<String> countryCodes) throws ServiceException {
		
		
		//transform a list of string to json entry
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String value  = mapper.writeValueAsString(countryCodes);
			
			MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(SUPPORTED_COUNTRIES, store);
			
			if(configuration==null) {
				configuration = new MerchantConfiguration();
				configuration.
				setKey(SUPPORTED_COUNTRIES);
				configuration.setMerchantStore(store);
			} 
			
			configuration.setValue(value);

			merchantConfigurationService.saveOrUpdate(configuration);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}
	

	private BigDecimal calculateOrderTotal(List<ShippingProduct> products, MerchantStore store) throws Exception {
		
		BigDecimal total = new BigDecimal("0");
		for(ShippingProduct shippingProduct : products) {
			FinalPrice price = productPriceUtils.getFinalPrice(shippingProduct.getProduct());
			
			BigDecimal currentPrice = price.getFinalPrice();
			currentPrice = currentPrice.multiply(new BigDecimal(shippingProduct.getQuantity()));
			total.add(currentPrice);
		}
		
		
		return total;
		
		
	}
	
	@Override
	public List<PackageDetails> getPackagesDetails(
			List<ShippingProduct> products, MerchantStore store) throws ServiceException {

		if (products == null) {
			throw new ServiceException("Product list cannot be null !!");
		}

		double width = 0;
		double length = 0;
		double height = 0;
		double weight = 0;
		double maxweight = 0;

		//int treshold = 0;
		
		
		ShippingConfiguration shippingConfiguration = this.getShippingConfiguration(store);
		if(shippingConfiguration==null) {
			throw new ServiceException("ShippingConfiguration not found for merchant " + store.getCode());
		}
		
		width = new Double(shippingConfiguration.getBoxWidth()).doubleValue();
		length = new Double(shippingConfiguration.getBoxLength()).doubleValue();
		height = new Double(shippingConfiguration.getBoxHeight()).doubleValue();
		weight = new Double(shippingConfiguration.getBoxWeight()).doubleValue();
		maxweight = new Double(shippingConfiguration.getMaxWeight()).doubleValue();
		


		List<PackageDetails> boxes = new ArrayList<PackageDetails>();

		// maximum number of boxes
		int maxBox = 100;
		int iterCount = 0;

		List<Product> individualProducts = new ArrayList<Product>();

		// need to put items individually
		for(ShippingProduct shippingProduct : products){

			Product product = shippingProduct.getProduct();
			if (product.isProductVirtual()) {
				continue;
			}

			int qty = shippingProduct.getQuantity();

			Set<ProductAttribute> attrs = shippingProduct.getProduct().getAttributes();

			// set attributes values
			BigDecimal w = product.getProductWeight();
			if (attrs != null && attrs.size() > 0) {
				for(ProductAttribute attribute : attrs) {
					w = w.add(attribute.getProductAttributeWeight());
				}
			}
			


			if (qty > 1) {

				for (int i = 1; i <= qty; i++) {
					Product temp = new Product();
					temp.setProductHeight(product.getProductHeight());
					temp.setProductLength(product.getProductLength());
					temp.setProductWidth(product.getProductWidth());
					temp.setProductWeight(w);
					temp.setAttributes(product.getAttributes());
					individualProducts.add(temp);
				}
			} else {
				Product temp = new Product();
				temp.setProductHeight(product.getProductHeight());
				temp.setProductLength(product.getProductLength());
				temp.setProductWidth(product.getProductWidth());
				temp.setProductWeight(w);
				temp.setAttributes(product.getAttributes());
				individualProducts.add(temp);
			}
			iterCount++;
		}

		if (iterCount == 0) {
			return null;
		}

		int productCount = individualProducts.size();

		//if (productCount < treshold) {
		//	throw new ServiceException("Number of items smaller than treshold");
		//}

		
		List<PackingBox> boxesList = new ArrayList<PackingBox>();

		//start the creation of boxes
		PackingBox box = new PackingBox();
		// set box max volume
		double maxVolume = width * length * height;

		if (maxVolume == 0 || maxweight == 0) {
/*			LogMerchantUtil.log(merchantId,
					"Check shipping box configuration, it has a volume of "
							+ maxVolume + " and a maximum weight of "
							+ maxweight
							+ ". Those values must be greater than 0.");*/
		}
		
		
		box.setVolumeLeft(maxVolume);
		box.setWeightLeft(maxweight);

		boxesList.add(box);//assign first box

		int boxCount = 1;
		List<Product> assignedProducts = new ArrayList<Product>();

		// calculate the volume for the next object
		if (assignedProducts.size() > 0) {
			individualProducts.removeAll(assignedProducts);
			assignedProducts = new ArrayList<Product>();
		}

		boolean productAssigned = false;

		for(Product p : individualProducts) {

			Set<ProductAttribute> attributes = p.getAttributes();
			productAssigned = false;

			double productWeight = p.getProductWeight().doubleValue();


			// validate if product fits in the box
			if (p.getProductWidth().doubleValue() > width
					|| p.getProductHeight().doubleValue() > height
					|| p.getProductLength().doubleValue() > length) {
				// log message to customer
				return null;
/*				LogMerchantUtil
						.log(
								merchantId,
								"Product "
										+ op.getProductId()
										+ " has a demension larger than the box size specified. Will use per item calculation.");
				// exit this process and let shipping calculator calculate
				// individual items
				throw new Exception(
						"Product configuration exceeds box configuraton");*/
			}

			if (productWeight > maxweight) {
				
				return null;
/*				LogMerchantUtil
						.log(
								merchantId,
								"Product "
										+ op.getProductId()
										+ " has a weight larger than the box maximum weight specified. Will use per item calculation.");
				throw new Exception("Product weight exceeds box maximum weight");*/
			}

			double productVolume = (p.getProductWidth().doubleValue()
					* p.getProductHeight().doubleValue() * p
					.getProductLength().doubleValue());

			if (productVolume == 0) {
				
				return null;
				
/*				LogMerchantUtil
						.log(
								merchantId,
								"Product "
										+ op.getProductId()
										+ " has one of the dimension set to 0 and therefore cannot calculate the volume");
				throw new Exception("Cannot calculate volume");*/
			}
			
			if (productVolume > maxVolume) {
				
				return null;
				
			}

			//List boxesList = boxesList;

			// try each box
			//Iterator boxIter = boxesList.iterator();
			for (PackingBox pbox : boxesList) {
				double volumeLeft = pbox.getVolumeLeft();
				double weightLeft = pbox.getWeightLeft();

				if ((volumeLeft * .75) >= productVolume
						&& pbox.getWeightLeft() >= productWeight) {// fit the item
																	// in this
																	// box
					// fit in the current box
					volumeLeft = volumeLeft - productVolume;
					pbox.setVolumeLeft(volumeLeft);
					weightLeft = weightLeft - productWeight;
					pbox.setWeightLeft(weightLeft);

					assignedProducts.add(p);
					productCount--;

					double w = pbox.getWeight();
					w = w + productWeight;
					pbox.setWeight(w);
					productAssigned = true;
					maxBox--;
					break;

				}

			}

			if (!productAssigned) {// create a new box

				box = new PackingBox();
				// set box max volume
				box.setVolumeLeft(maxVolume);
				box.setWeightLeft(maxweight);

				boxesList.add(box);

				double volumeLeft = box.getVolumeLeft() - productVolume;
				box.setVolumeLeft(volumeLeft);
				double weightLeft = box.getWeightLeft() - productWeight;
				box.setWeightLeft(weightLeft);
				assignedProducts.add(p);
				productCount--;
				double w = box.getWeight();
				w = w + productWeight;
				box.setWeight(w);
				maxBox--;
			}

		}

		// now prepare the shipping info

		// number of boxes

		//Iterator ubIt = usedBoxesList.iterator();

		System.out.println("###################################");
		System.out.println("Number of boxes " + boxesList.size());
		System.out.println("###################################");

		for(PackingBox pb : boxesList) {
			PackageDetails details = new PackageDetails();
			details.setShippingHeight(height);
			details.setShippingLength(length);
			details.setShippingWeight(weight + box.getWeight());
			details.setShippingWidth(width);
			boxes.add(details);
		}

		return boxes;

	}


}



class PackingBox {

	private double volumeLeft;
	private double weightLeft;
	private double weight;

	public double getVolumeLeft() {
		return volumeLeft;
	}

	public void setVolumeLeft(double volumeLeft) {
		this.volumeLeft = volumeLeft;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getWeightLeft() {
		return weightLeft;
	}

	public void setWeightLeft(double weightLeft) {
		this.weightLeft = weightLeft;
	}

}
