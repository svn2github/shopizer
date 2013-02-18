package com.salesmanager.core.modules.integration.shipping.impl;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.salesmanager.core.business.customer.model.Delivery;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.shipping.model.PackageDetails;
import com.salesmanager.core.business.shipping.model.ShippingConfiguration;
import com.salesmanager.core.business.shipping.model.ShippingDescription;
import com.salesmanager.core.business.shipping.model.ShippingOption;
import com.salesmanager.core.business.system.model.IntegrationConfiguration;
import com.salesmanager.core.business.system.model.IntegrationModule;
import com.salesmanager.core.business.system.model.MerchantLog;
import com.salesmanager.core.business.system.model.ModuleConfig;
import com.salesmanager.core.business.system.service.MerchantLogService;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.constants.MeasureUnit;
import com.salesmanager.core.modules.integration.IntegrationException;
import com.salesmanager.core.modules.integration.shipping.model.ShippingQuoteModule;
import com.salesmanager.core.utils.DataUtils;

/**
 * Integrates with UPS online API
 * @author casams1
 *
 */
public class UPSShippingQuote implements ShippingQuoteModule {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UPSShippingQuote.class);
	
	@Autowired
	private MerchantLogService merchantLogService;

	@Override
	public void validateModuleConfiguration(
			IntegrationConfiguration integrationConfiguration,
			MerchantStore store) throws IntegrationException {
		
		
		List<String> errorFields = null;
		
		//validate integrationKeys['accessKey']
		Map<String,String> keys = integrationConfiguration.getIntegrationKeys();
		if(keys==null || StringUtils.isBlank(keys.get("accseeKey"))) {
			errorFields = new ArrayList<String>();
			errorFields.add("identifier");
		}

		//validate at least one integrationOptions['packages']
		Map<String,List<String>> options = integrationConfiguration.getIntegrationOptions();
		if(options==null) {
			errorFields = new ArrayList<String>();
			errorFields.add("identifier");
		}
		
		List<String> packages = options.get("packages");
		if(packages==null || packages.size()==0) {
			if(errorFields==null) {
				errorFields = new ArrayList<String>();
			}
			errorFields.add("packages");
		}
		
/*		List<String> services = options.get("services");
		if(services==null || services.size()==0) {
			if(errorFields==null) {
				errorFields = new ArrayList<String>();
			}
			errorFields.add("services");
		}
		
		if(services!=null && services.size()>3) {
			if(errorFields==null) {
				errorFields = new ArrayList<String>();
			}
			errorFields.add("services");
		}*/
		
		if(errorFields!=null) {
			IntegrationException ex = new IntegrationException(IntegrationException.ERROR_VALIDATION_SAVE);
			ex.setErrorFields(errorFields);
			throw ex;
			
		}
		
		

	}

	@Override
	public List<ShippingOption> getShippingQuotes(
			List<PackageDetails> packages, BigDecimal orderTotal,
			Delivery delivery, MerchantStore store,
			IntegrationConfiguration configuration, IntegrationModule module,
			ShippingConfiguration shippingConfiguration, Locale locale)
			throws IntegrationException {

		
		
		BigDecimal total = orderTotal;

		if (packages == null) {
			return null;
		}
		
		List<ShippingOption> options = null;

		// only applies to Canada and US
		Country country = delivery.getCountry();
		if(!country.getIsoCode().equals("US") || !country.equals("CA")) {
			throw new IntegrationException("Canadapost Not configured for shipping in country " + country.getIsoCode());
		}

		// supports en and fr
		String language = locale.getLanguage();
		if (!language.equals(Locale.FRENCH.getLanguage())
				&& !language.equals(Locale.ENGLISH.getLanguage())) {
			language = Locale.ENGLISH.getLanguage();
		}
		
		//CoreModuleService cis = null;
		//StringBuffer xmlbuffer = new StringBuffer();
		//BufferedReader reader = null;
		//PostMethod httppost = null;

		try {

/*			CommonService cservice = (CommonService) ServiceFactory
					.getService(ServiceFactory.CommonService);

			String countrycode = CountryUtil.getCountryIsoCodeById(store
					.getCountry());
			cis = cservice.getModule(countrycode, "upsxml");

			if (cis == null) {
				log.error("Can't retreive an integration service [countryid "
						+ store.getCountry() + " ups subtype 1]");
				// throw new
				// Exception("UPS getQuote Can't retreive an integration service");
			}

			MerchantService service = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			ConfigurationRequest request_prefs = new ConfigurationRequest(store
					.getMerchantId(),
					ShippingConstants.MODULE_SHIPPING_RT_PKG_DOM_INT);
			ConfigurationResponse vo_prefs = service
					.getConfiguration(request_prefs);

			String pack = (String) vo_prefs.getConfiguration("package-upsxml");
			if (pack == null) {
				log
						.debug("Will assign packaging type 02 to UPS shipping for merchantid "
								+ store.getMerchantId());
				pack = "02";
			}

			ConfigurationRequest request = new ConfigurationRequest(store
					.getMerchantId(), ShippingConstants.MODULE_SHIPPING_RT_CRED);
			ConfigurationResponse vo = service.getConfiguration(request);

			if (vo == null) {
				throw new Exception("ConfigurationVO is null upsxml");
			}

			String xmlhead = getHeader(store.getMerchantId(), vo);

			String weightCode = store.getWeightunitcode();
			String measureCode = store.getSeizeunitcode();

			if (weightCode.equals("KG")) {
				weightCode = "KGS";
			} else {
				weightCode = "LBS";
			}

			String xml = "<?xml version=\"1.0\"?><RatingServiceSelectionRequest><Request><TransactionReference><CustomerContext>SalesManager Data</CustomerContext><XpciVersion>1.0001</XpciVersion></TransactionReference><RequestAction>Rate</RequestAction><RequestOption>Shop</RequestOption></Request>";
			StringBuffer xmldatabuffer = new StringBuffer();*/

			/**
			 * <Shipment>
			 * 
			 * <Shipper> <Address> <City></City>
			 * <StateProvinceCode>QC</StateProvinceCode>
			 * <CountryCode>CA</CountryCode> <PostalCode></PostalCode>
			 * </Address> </Shipper>
			 * 
			 * <ShipTo> <Address> <City>Redwood Shores</City>
			 * <StateProvinceCode>CA</StateProvinceCode>
			 * <CountryCode>US</CountryCode> <PostalCode></PostalCode>
			 * <ResidentialAddressIndicator/> </Address> </ShipTo>
			 * 
			 * <Package> <PackagingType> <Code>21</Code> </PackagingType>
			 * <PackageWeight> <UnitOfMeasurement> <Code>LBS</Code>
			 * </UnitOfMeasurement> <Weight>1.1</Weight> </PackageWeight>
			 * <PackageServiceOptions> <InsuredValue>
			 * <CurrencyCode>CAD</CurrencyCode>
			 * <MonetaryValue>100</MonetaryValue> </InsuredValue>
			 * </PackageServiceOptions> </Package>
			 * 
			 * 
			 * </Shipment>
			 * 
			 * <CustomerClassification> <Code>03</Code>
			 * </CustomerClassification> </RatingServiceSelectionRequest>
			 * **/

/*			Map countriesMap = (Map) RefCache.getAllcountriesmap(LanguageUtil
					.getLanguageNumberCode(locale.getLanguage()));
			Map zonesMap = (Map) RefCache.getAllZonesmap(LanguageUtil
					.getLanguageNumberCode(locale.getLanguage()));

			Country storeCountry = (Country) countriesMap.get(store
					.getCountry());

			Country customerCountry = (Country) countriesMap.get(customer
					.getCustomerCountryId());

			int sZone = -1;
			try {
				sZone = Integer.parseInt(store.getZone());
			} catch (Exception e) {
				// TODO: handle exception
			}

			Zone storeZone = (Zone) zonesMap.get(sZone);
			Zone customerZone = (Zone) zonesMap.get(customer
					.getCustomerZoneId());

			xmldatabuffer.append("<PickupType><Code>03</Code></PickupType>");
			// xmldatabuffer.append("<Description>Daily Pickup</Description>");
			xmldatabuffer.append("<Shipment><Shipper>");
			xmldatabuffer.append("<Address>");
			xmldatabuffer.append("<City>");
			xmldatabuffer.append(store.getStorecity());
			xmldatabuffer.append("</City>");
			// if(!StringUtils.isBlank(store.getStorestateprovince())) {
			if (storeZone != null) {
				xmldatabuffer.append("<StateProvinceCode>");
				xmldatabuffer.append(storeZone.getZoneCode());// zone code
				xmldatabuffer.append("</StateProvinceCode>");
			}
			xmldatabuffer.append("<CountryCode>");
			xmldatabuffer.append(storeCountry.getCountryIsoCode2());
			xmldatabuffer.append("</CountryCode>");
			xmldatabuffer.append("<PostalCode>");
			xmldatabuffer.append(com.salesmanager.core.util.ShippingUtil
					.trimPostalCode(store.getStorepostalcode()));
			xmldatabuffer.append("</PostalCode></Address></Shipper>");

			// ship to
			xmldatabuffer.append("<ShipTo>");
			xmldatabuffer.append("<Address>");
			xmldatabuffer.append("<City>");
			xmldatabuffer.append(customer.getCustomerCity());
			xmldatabuffer.append("</City>");
			// if(!StringUtils.isBlank(customer.getCustomerState())) {
			if (customerZone != null) {
				xmldatabuffer.append("<StateProvinceCode>");
				xmldatabuffer.append(customerZone.getZoneCode());// zone code
				xmldatabuffer.append("</StateProvinceCode>");
			}
			xmldatabuffer.append("<CountryCode>");
			xmldatabuffer.append(customerCountry.getCountryIsoCode2());
			xmldatabuffer.append("</CountryCode>");
			xmldatabuffer.append("<PostalCode>");
			xmldatabuffer.append(com.salesmanager.core.util.ShippingUtil
					.trimPostalCode(customer.getCustomerPostalCode()));
			xmldatabuffer.append("</PostalCode></Address></ShipTo>");
			// xmldatabuffer.append("<Service><Code>11</Code></Service>");

			Iterator packagesIterator = packages.iterator();
			while (packagesIterator.hasNext()) {

				PackageDetail detail = (PackageDetail) packagesIterator.next();
				xmldatabuffer.append("<Package>");
				xmldatabuffer.append("<PackagingType>");
				xmldatabuffer.append("<Code>");
				xmldatabuffer.append(pack);
				xmldatabuffer.append("</Code>");
				xmldatabuffer.append("</PackagingType>");

				// weight
				xmldatabuffer.append("<PackageWeight>");
				xmldatabuffer.append("<UnitOfMeasurement>");
				xmldatabuffer.append("<Code>");
				xmldatabuffer.append(weightCode);
				xmldatabuffer.append("</Code>");
				xmldatabuffer.append("</UnitOfMeasurement>");
				xmldatabuffer.append("<Weight>");
				xmldatabuffer.append(new BigDecimal(detail.getShippingWeight())
						.setScale(1, BigDecimal.ROUND_HALF_UP));
				xmldatabuffer.append("</Weight>");
				xmldatabuffer.append("</PackageWeight>");

				// dimension
				xmldatabuffer.append("<Dimensions>");
				xmldatabuffer.append("<UnitOfMeasurement>");
				xmldatabuffer.append("<Code>");
				xmldatabuffer.append(measureCode);
				xmldatabuffer.append("</Code>");
				xmldatabuffer.append("</UnitOfMeasurement>");
				xmldatabuffer.append("<Length>");
				xmldatabuffer.append(new BigDecimal(detail.getShippingLength())
						.setScale(2, BigDecimal.ROUND_HALF_UP));
				xmldatabuffer.append("</Length>");
				xmldatabuffer.append("<Width>");
				xmldatabuffer.append(new BigDecimal(detail.getShippingWidth())
						.setScale(2, BigDecimal.ROUND_HALF_UP));
				xmldatabuffer.append("</Width>");
				xmldatabuffer.append("<Height>");
				xmldatabuffer.append(new BigDecimal(detail.getShippingHeight())
						.setScale(2, BigDecimal.ROUND_HALF_UP));
				xmldatabuffer.append("</Height>");
				xmldatabuffer.append("</Dimensions>");
				xmldatabuffer.append("</Package>");

			}

			xmldatabuffer.append("</Shipment>");
			xmldatabuffer.append("</RatingServiceSelectionRequest>");

			xmlbuffer.append(xmlhead).append(xml).append(
					xmldatabuffer.toString());

			log.debug("UPS QUOTE REQUEST " + xmlbuffer.toString());

			String data = "";

			IntegrationKeys keys = (IntegrationKeys) config
					.getConfiguration("upsxml-keys");

			IntegrationProperties props = (IntegrationProperties) config
					.getConfiguration("upsxml-properties");

			String host = cis.getCoreModuleServiceProdDomain();
			String protocol = cis.getCoreModuleServiceProdProtocol();
			String port = cis.getCoreModuleServiceProdPort();
			String uri = cis.getCoreModuleServiceProdEnv();

			if (props.getProperties1().equals(
					String.valueOf(ShippingConstants.TEST_ENVIRONMENT))) {
				host = cis.getCoreModuleServiceDevDomain();
				protocol = cis.getCoreModuleServiceDevProtocol();
				port = cis.getCoreModuleServiceDevPort();
				uri = cis.getCoreModuleServiceDevEnv();
			}

			HttpClient client = new HttpClient();
			httppost = new PostMethod(protocol + "://" + host + ":" + port
					+ uri);
			RequestEntity entity = new StringRequestEntity(
					xmlbuffer.toString(), "text/plain", "UTF-8");
			httppost.setRequestEntity(entity);

			int result = client.executeMethod(httppost);
			if (result != 200) {
				log.error("Communication Error with ups quote " + result + " "
						+ protocol + "://" + host + ":" + port + uri);
				throw new Exception("UPS quote communication error " + result);
			}
			data = httppost.getResponseBodyAsString();
			log.debug("ups quote response " + data);

			UPSParsedElements parsed = new UPSParsedElements();

			Digester digester = new Digester();
			digester.push(parsed);
			digester.addCallMethod(
					"RatingServiceSelectionResponse/Response/Error",
					"setErrorCode", 0);
			digester.addCallMethod(
					"RatingServiceSelectionResponse/Response/ErrorDescriprion",
					"setError", 0);
			digester
					.addCallMethod(
							"RatingServiceSelectionResponse/Response/ResponseStatusCode",
							"setStatusCode", 0);
			digester
					.addCallMethod(
							"RatingServiceSelectionResponse/Response/ResponseStatusDescription",
							"setStatusMessage", 0);
			digester
					.addCallMethod(
							"RatingServiceSelectionResponse/Response/Error/ErrorDescription",
							"setError", 0);

			digester.addObjectCreate(
					"RatingServiceSelectionResponse/RatedShipment",
					com.salesmanager.core.entity.shipping.ShippingOption.class);
			// digester.addSetProperties(
			// "RatingServiceSelectionResponse/RatedShipment", "sequence",
			// "optionId" );
			digester
					.addCallMethod(
							"RatingServiceSelectionResponse/RatedShipment/Service/Code",
							"setOptionId", 0);
			digester
					.addCallMethod(
							"RatingServiceSelectionResponse/RatedShipment/TotalCharges/MonetaryValue",
							"setOptionPriceText", 0);
			digester
					.addCallMethod(
							"RatingServiceSelectionResponse/RatedShipment/TotalCharges/CurrencyCode",
							"setCurrency", 0);
			digester
					.addCallMethod(
							"RatingServiceSelectionResponse/RatedShipment/Service/Code",
							"setOptionCode", 0);
			digester
					.addCallMethod(
							"RatingServiceSelectionResponse/RatedShipment/GuaranteedDaysToDelivery",
							"setEstimatedNumberOfDays", 0);
			digester.addSetNext("RatingServiceSelectionResponse/RatedShipment",
					"addOption");*/

			// <?xml
			// version="1.0"?><AddressValidationResponse><Response><TransactionReference><CustomerContext>SalesManager
			// Data</CustomerContext><XpciVersion>1.0</XpciVersion></TransactionReference><ResponseStatusCode>0</ResponseStatusCode><ResponseStatusDescription>Failure</ResponseStatusDescription><Error><ErrorSeverity>Hard</ErrorSeverity><ErrorCode>10002</ErrorCode><ErrorDescription>The
			// XML document is well formed but the document is not
			// valid</ErrorDescription><ErrorLocation><ErrorLocationElementName>AddressValidationRequest</ErrorLocationElementName></ErrorLocation></Error></Response></AddressValidationResponse>

			/*Reader xmlreader = new StringReader(data);

			digester.parse(xmlreader);

			if (!StringUtils.isBlank(parsed.getErrorCode())) {
				log.error("Can't process UPS statusCode="
						+ parsed.getErrorCode() + " message= "
						+ parsed.getError());
				return null;
			}
			if (!StringUtils.isBlank(parsed.getStatusCode())
					&& !parsed.getStatusCode().equals("1")) {
				LogMerchantUtil.log(store.getMerchantId(),
						"Can't process UPS statusCode="
								+ parsed.getStatusCode() + " message= "
								+ parsed.getError());
				log.error("Can't process UPS statusCode="
						+ parsed.getStatusCode() + " message= "
						+ parsed.getError());
				return null;
			}

			if (parsed.getOptions() == null || parsed.getOptions().size() == 0) {
				log.warn("No options returned from UPS");
				return null;
			}

			String carrier = getShippingMethodDescription(locale);
			// cost is in CAD, need to do conversion

			
			 * boolean requiresCurrencyConversion = false; String storeCurrency
			 * = store.getCurrency();
			 * if(!storeCurrency.equals(Constants.CURRENCY_CODE_CAD)) {
			 * requiresCurrencyConversion = true; }
			 

			LabelUtil labelUtil = LabelUtil.getInstance();
			Map serviceMap = com.salesmanager.core.util.ShippingUtil
					.buildServiceMap("upsxml", locale);

			*//** Details on whit RT quote information to display **//*
			MerchantConfiguration rtdetails = config
					.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_DISPLAY_REALTIME_QUOTES);
			int displayQuoteDeliveryTime = ShippingConstants.NO_DISPLAY_RT_QUOTE_TIME;
			if (rtdetails != null) {

				if (!StringUtils.isBlank(rtdetails.getConfigurationValue1())) {// display
																				// or
																				// not
																				// quotes
					try {
						displayQuoteDeliveryTime = Integer.parseInt(rtdetails
								.getConfigurationValue1());

					} catch (Exception e) {
						log.error("Display quote is not an integer value ["
								+ rtdetails.getConfigurationValue1() + "]");
					}
				}
			}
			

			Collection returnColl = null;

			List options = parsed.getOptions();
			if (options != null) {

				Map selectedintlservices = (Map) config
						.getConfiguration("service-global-upsxml");

				Iterator i = options.iterator();
				while (i.hasNext()) {
					ShippingOption option = (ShippingOption) i.next();
					// option.setCurrency(store.getCurrency());
					StringBuffer description = new StringBuffer();

					String code = option.getOptionCode();
					option.setOptionCode(code);
					// get description
					String label = (String) serviceMap.get(code);
					if (label == null) {
						log
								.warn("UPSXML cannot find description for service code "
										+ code);
					}

					option.setOptionName(label);

					description.append(option.getOptionName());
					if (displayQuoteDeliveryTime == ShippingConstants.DISPLAY_RT_QUOTE_TIME) {
						if (!StringUtils.isBlank(option
								.getEstimatedNumberOfDays())) {
							description.append(" (").append(
									option.getEstimatedNumberOfDays()).append(
									" ").append(
									labelUtil.getText(locale,
											"label.generic.days.lowercase"))
									.append(")");
						}
					}
					option.setDescription(description.toString());

					// get currency
					if (!option.getCurrency().equals(store.getCurrency())) {
						option.setOptionPrice(CurrencyUtil.convertToCurrency(
								option.getOptionPrice(), option.getCurrency(),
								store.getCurrency()));
					}

					if (!selectedintlservices.containsKey(option
							.getOptionCode())) {
						if (returnColl == null) {
							returnColl = new ArrayList();
						}
						returnColl.add(option);
						// options.remove(option);
					}

				}

				if (options.size() == 0) {
					LogMerchantUtil
							.log(
									store.getMerchantId(),
									" none of the service code returned by UPS ["
											+ selectedintlservices
													.keySet()
													.toArray(
															new String[selectedintlservices
																	.size()])
											+ "] for this shipping is in your selection list");
				}
			}



			return returnColl;*/

		} catch (Exception e1) {
			LOGGER.error("UPS quote error",e1);
			return null;
		} finally {
/*			if (reader != null) {
				try {
					reader.close();
				} catch (Exception ignore) {
				}
			}

			if (httppost != null) {
				httppost.releaseConnection();
			}*/
		}
		
		return null;

}}


class UPSParsedElements  {

	private String statusCode;
	private String statusMessage;
	private String error = "";
	private String errorCode = "";
	private List<ShippingOption> options = new ArrayList<ShippingOption>();

	public void addOption(ShippingOption option) {
		options.add(option);
	}

	public List<ShippingOption> getOptions() {
		return options;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
