package com.salesmanager.core.utils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

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
public class ProductPriceUtils {
	
	
	/**
	 * Get the price without discount
	 * @param store
	 * @param product
	 * @param locale
	 * @return
	 */
	public static BigDecimal getPrice(MerchantStore store, Product product, Locale locale) {
		
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
	public static FinalPrice getFinalPrice(Product product) {


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
								} else {
									if(price.getProductPriceSpecialDurationDays()!=null) {
										Calendar calendar = Calendar.getInstance();
										calendar.setTime(price.getProductPriceSpecialStartDate());
										calendar.add(Calendar.DAY_OF_YEAR, price.getProductPriceSpecialDurationDays().intValue());
										
										if(calendar.getTime().after(today)) {
											hasDiscount = true;
											finalPrice.setDiscountEndDate(calendar.getTime());
										}
									
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



}
