package com.salesmanager.core.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.CurrencyValidator;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.merchant.model.MerchantStore;


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
	 * This is the final price calculated from all configured prices
	 * and all possibles discounts
	 * @param store
	 * @param product
	 * @param locale
	 * @return
	 */
	public FinalPrice getFinalPrice(Product product) {


		FinalPrice finalPrice = new FinalPrice();

		Date today = new Date();
		
		
		BigDecimal defaultPrice = new BigDecimal(0);

		Set<ProductAvailability> availabilities = product.getAvailabilities();
		for(ProductAvailability availability : availabilities) {
			
			Set<ProductPrice> prices = availability.getPrices();
			for(ProductPrice price : prices) {
				
				if(price.isDefaultPrice()) {
					defaultPrice = price.getProductPriceAmount();
					//calculate discount price
					boolean hasDiscount = false;
					if(price.getProductPriceSpecialStartDate()!=null
							|| price.getProductPriceSpecialEndDate()!=null) {
						
						
						if(price.getProductPriceSpecialStartDate()!=null) {
							if(price.getProductPriceSpecialStartDate().before(today)) {
								if(price.getProductPriceSpecialEndDate()!=null) {
										if(price.getProductPriceSpecialEndDate().after(today)) {
											hasDiscount = true;
											finalPrice.setDiscountEndDate(price.getProductPriceSpecialEndDate());
										}
								} 
									
							}
						}
						
						
						if(!hasDiscount && price.getProductPriceSpecialStartDate()==null && price.getProductPriceSpecialEndDate()!=null) {
							if(price.getProductPriceSpecialEndDate().after(today)) {
								hasDiscount = true;
								finalPrice.setDiscountEndDate(price.getProductPriceSpecialEndDate());
							}
						}

					}
					
					if(hasDiscount) {
						
						finalPrice.setDiscounted(true);
						finalPrice.setDiscountedPrice(price.getProductPriceSpecialAmount());
						double arith = price.getProductPriceSpecialAmount().doubleValue() / price.getProductPriceAmount().doubleValue();
						double fsdiscount = 100 - arith * 100;
						Float percentagediscount = new Float(fsdiscount);
						int percent = percentagediscount.intValue();
						finalPrice.setDiscountPercent(percent);
						
					}

				}
				
			}
			
		}
		
		finalPrice.setOriginalPrice(defaultPrice);
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



}
