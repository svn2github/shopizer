package com.salesmanager.core.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.CurrencyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.constants.Constants;


/**
 * This class determines the price that is displayed in the catalogue for a given item. 
 * It does not calculate the total price for a given item
 * @author casams1
 *
 */
@Component("priceUtil")
public class ProductPriceUtils {
	
	private final static char DECIMALCOUNT = '2';
	private final static char DECIMALPOINT = '.';
	private final static char THOUSANDPOINT = ',';
	
	private final static Locale DEFAULT_LOCALE = Locale.US;
	private final static Currency DEFAULT_CURRENCY = Currency.getInstance(Locale.US);
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductPriceUtils.class);

	
	
	/**
	 * Get the price without discount
	 * @param store
	 * @param product
	 * @param locale
	 * @return
	 */
	public BigDecimal getPrice(MerchantStore store, Product product, Locale locale) {
		
		BigDecimal defaultPrice = new BigDecimal(0);

		Set<ProductAvailability> availabilities = product.getAvailabilities();
		for(ProductAvailability availability : availabilities) {
			
			Set<ProductPrice> prices = availability.getPrices();
			for(ProductPrice price : prices) {
				
				if(price.isDefaultPrice()) {
					defaultPrice = price.getProductPriceAmount();
				}
				
			}
			
		}
		
		return defaultPrice;
	}
	
	/**
	 * This method calculates the final price taking into account
	 * all attributes included in the product object. The calculation is based
	 * on the default price.
	 * Attributes may be null
	 * @param Product
	 * @param List<ProductAttribute>
	 * @return FinalPrice
	 */
	public FinalPrice getFinalOrderPrice(Product product, List<ProductAttribute> attributes) {


		FinalPrice finalPrice = calculateFinalPrice(product);
		
		//attributes
		BigDecimal attributePrice = new BigDecimal(0);
		if(attributes!=null && attributes.size()>0) {
			for(ProductAttribute attribute : attributes) {
				attributePrice = attributePrice.add(attribute.getOptionValuePrice());
			}
		}
		
		BigDecimal finalProductPrice = finalPrice.getOriginalPrice().add(attributePrice);
		finalPrice.setOriginalPrice(finalProductPrice);
		
		if(finalPrice.isDiscounted()) {
			
			finalPrice.setDiscounted(true);
			
			double arith = finalPrice.getOriginalPrice().doubleValue() / finalPrice.getDefaultPrice().getProductPriceAmount().doubleValue();
			double fsdiscount = 100 - arith * 100;
			Float percentagediscount = new Float(fsdiscount);
			int percent = percentagediscount.intValue();
			finalPrice.setDiscountPercent(percent);
			
			//calculate percent
			BigDecimal price = finalPrice.getOriginalPrice();
			price = price.multiply(new BigDecimal(fsdiscount));
			finalPrice.setDiscountedPrice(price);

			
		}

		return finalPrice;

	}

	
	/**
	 * This is the final price calculated from all configured prices
	 * and all possibles discounts. This price does not calculate the attributes
	 * or other prices than the default one
	 * @param store
	 * @param product
	 * @param locale
	 * @return
	 */
	public FinalPrice getFinalPrice(Product product) {



		FinalPrice finalPrice = calculateFinalPrice(product);
		
		if(finalPrice.isDiscounted()) {
			
			finalPrice.setDiscounted(true);
			finalPrice.setDiscountedPrice(finalPrice.getDefaultPrice().getProductPriceSpecialAmount());
			double arith = finalPrice.getDefaultPrice().getProductPriceSpecialAmount().doubleValue() / finalPrice.getDefaultPrice().getProductPriceAmount().doubleValue();
			double fsdiscount = 100 - arith * 100;
			Float percentagediscount = new Float(fsdiscount);
			int percent = percentagediscount.intValue();
			finalPrice.setDiscountPercent(percent);
			
		}
		if(finalPrice.getDefaultPrice()!=null) {
			finalPrice.setOriginalPrice(finalPrice.getDefaultPrice().getProductPriceAmount());
		}
		return finalPrice;

	}
	

	

	/**
	 * This is the format that will be displayed
	 * in the admin input text fields when editing
	 * an entity having a BigDecimal to be displayed
	 * as a raw amount 1,299.99
	 * The admin user will also be force to input
	 * the amount using that format	
	 * @param store
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public String getAdminFormatedAmount(MerchantStore store, BigDecimal amount) throws Exception {
			
		if(amount==null) {
			return "";
		}
		
		NumberFormat nf = null;

			
		nf = NumberFormat.getInstance(DEFAULT_LOCALE);

		nf.setMaximumFractionDigits(Integer.parseInt(Character
					.toString(DECIMALCOUNT)));
		nf.setMinimumFractionDigits(Integer.parseInt(Character
					.toString(DECIMALCOUNT)));

		return nf.format(amount);
	}
	
	
	/**
	 * This method has to be used to format store front amounts
	 * @param store
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public String getStoreFormatedAmountWithCurrency(MerchantStore store, BigDecimal amount) throws Exception {
		if(amount==null) {
			return "";
		}
		
		String currencyCode = store.getCurrency().getName();
		
		Currency currency = DEFAULT_CURRENCY;
		try {
			currency = Currency.getInstance(currencyCode);
		} catch (Exception e) {
			LOGGER.error("Cannot create currency instance for " + store.getCurrency().getName());
		}

		
		
		
		
	    final NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();
	    currencyInstance.setCurrency(currency);
		
	    
	    return currencyInstance.format(amount.doubleValue());
		

    }
	


	
	/**
	 * This method will return the required formated amount
	 * with the appropriate currency
	 * @param store
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public String getAdminFormatedAmountWithCurrency(MerchantStore store, BigDecimal amount) throws Exception {
		if(amount==null) {
			return "";
		}
		
		
		
		NumberFormat nf = null;

		
		Currency currency = store.getCurrency().getCurrency();
		nf = NumberFormat.getInstance(DEFAULT_LOCALE);
		nf.setCurrency(currency);

/*		nf.setMaximumFractionDigits(Integer.parseInt(Character
				.toString(DECIMALCOUNT)));
		nf.setMinimumFractionDigits(Integer.parseInt(Character
				.toString(DECIMALCOUNT)));*/

		return nf.format(amount);
}

	/**
	 * This amount will be displayed to the end user
	 * @param store
	 * @param amount
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	public String getFormatedAmountWithCurrency(MerchantStore store, BigDecimal amount, Locale locale)
				throws Exception {
		
			NumberFormat nf = null;

			Currency currency = store.getCurrency().getCurrency();
			nf = NumberFormat.getInstance(DEFAULT_LOCALE);
			nf.setCurrency(currency);
	

	
			return nf.format(amount);

	}
	
	/**
	 * Transformation of an amount of money submited by the admin
	 * user to be inserted as a BigDecimal in the database
	 * @param amount
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getAmount(String amount) throws Exception {

		// validations
		/**
		 * 1) remove decimal and thousand
		 * 
		 * String.replaceAll(decimalPoint, ""); String.replaceAll(thousandPoint,
		 * "");
		 * 
		 * Should be able to parse to Integer
		 */
		StringBuffer newAmount = new StringBuffer();
		for (int i = 0; i < amount.length(); i++) {
			if (amount.charAt(i) != DECIMALPOINT
					&& amount.charAt(i) != THOUSANDPOINT) {
				newAmount.append(amount.charAt(i));
			}
		}

		try {
			Integer.parseInt(newAmount.toString());
		} catch (Exception e) {
			throw new Exception("Cannot parse " + amount);
		}

		if (!amount.contains(Character.toString(DECIMALPOINT))
				&& !amount.contains(Character.toString(THOUSANDPOINT))
				&& !amount.contains(" ")) {

			if (matchPositiveInteger(amount)) {
				BigDecimalValidator validator = CurrencyValidator.getInstance();
				BigDecimal bdamount = validator.validate(amount, Locale.US);
				if (bdamount == null) {
					throw new Exception("Cannot parse " + amount);
				} else {
					return bdamount;
				}
			} else {
				throw new Exception("Not a positive integer "
						+ amount);
			}

		} else {
			//TODO should not go this path in this current release
			StringBuffer pat = new StringBuffer();

			if (!StringUtils.isBlank(Character.toString(THOUSANDPOINT))) {
				pat.append("\\d{1,3}(" + THOUSANDPOINT + "?\\d{3})*");
			}

			pat.append("(\\" + DECIMALPOINT + "\\d{1," + DECIMALCOUNT + "})");

			Pattern pattern = Pattern.compile(pat.toString());
			Matcher matcher = pattern.matcher(amount);

			if (matcher.matches()) {

				Locale locale = DEFAULT_LOCALE;
				//TODO validate amount using old test case
				if (DECIMALPOINT == ',') {
					locale = Locale.GERMAN;
				}

				BigDecimalValidator validator = CurrencyValidator.getInstance();
				BigDecimal bdamount = validator.validate(amount, locale);

				return bdamount;
			} else {
				throw new Exception("Cannot parse " + amount);
			}
		}

	}
	
	/**
	 * Determines if a ProductPrice has a discount
	 * @param productPrice
	 * @return
	 */
	public boolean hasDiscount(ProductPrice productPrice) {
		
		
		Date today = new Date();

		//calculate discount price
		boolean hasDiscount = false;
		if(productPrice.getProductPriceSpecialStartDate()!=null
				|| productPrice.getProductPriceSpecialEndDate()!=null) {
			
			
			if(productPrice.getProductPriceSpecialStartDate()!=null) {
				if(productPrice.getProductPriceSpecialStartDate().before(today)) {
					if(productPrice.getProductPriceSpecialEndDate()!=null) {
							if(productPrice.getProductPriceSpecialEndDate().after(today)) {
								hasDiscount = true;
							}
					} 
				}
			}
		}
		
		return hasDiscount;
		
		
		
	}
	
	private boolean matchPositiveInteger(String amount) {

		Pattern pattern = Pattern.compile("^[+]?\\d*$");
		Matcher matcher = pattern.matcher(amount);
		if (matcher.matches()) {
			return true;

		} else {
			return false;
		}
	}
	
	private FinalPrice calculateFinalPrice(Product product) {
		
		FinalPrice finalPrice = new FinalPrice();

		Date today = new Date();
		
		
		BigDecimal fPrice = new BigDecimal(0);
		BigDecimal oPrice = new BigDecimal(0);
		Set<ProductAvailability> availabilities = product.getAvailabilities();
		for(ProductAvailability availability : availabilities) {
			if(availability.getRegion().equals(Constants.ALL_REGIONS)) {
				Set<ProductPrice> prices = availability.getPrices();
				for(ProductPrice price : prices) {
					
					if(price.isDefaultPrice()) {
						fPrice = price.getProductPriceAmount();
						oPrice = price.getProductPriceAmount();
						//calculate discount price
						boolean hasDiscount = false;
						if(price.getProductPriceSpecialStartDate()!=null
								|| price.getProductPriceSpecialEndDate()!=null) {
							
							
							if(price.getProductPriceSpecialStartDate()!=null) {
								if(price.getProductPriceSpecialStartDate().before(today)) {
									if(price.getProductPriceSpecialEndDate()!=null) {
											if(price.getProductPriceSpecialEndDate().after(today)) {
												hasDiscount = true;
												fPrice = price.getProductPriceSpecialAmount();
												finalPrice.setDiscountEndDate(price.getProductPriceSpecialEndDate());
											}
									} 
										
								}
							}
							
							
							if(!hasDiscount && price.getProductPriceSpecialStartDate()==null && price.getProductPriceSpecialEndDate()!=null) {
								if(price.getProductPriceSpecialEndDate().after(today)) {
									hasDiscount = true;
									fPrice = price.getProductPriceSpecialAmount();
									finalPrice.setDiscountEndDate(price.getProductPriceSpecialEndDate());
								}
							}
						}
						finalPrice.setDefaultPrice(price);
					}
					
				}
			}
			
		}
		
		finalPrice.setFinalPrice(fPrice);
		finalPrice.setOriginalPrice(oPrice);
		return finalPrice;
		
		
	}



}
