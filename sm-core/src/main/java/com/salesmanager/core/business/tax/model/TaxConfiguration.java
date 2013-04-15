package com.salesmanager.core.business.tax.model;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * Set of various tax configuration settings saved in MerchantConfiguration
 * @author carl samson
 *
 */
public class TaxConfiguration implements JSONAware {
	
	private TaxBasisCalculation taxBasisCalculation = TaxBasisCalculation.SHIPPINGADDRESS;

	@SuppressWarnings("unchecked")
	@Override
	public String toJSONString() {
		JSONObject data = new JSONObject();
		data.put("taxBasisCalculation", this.getTaxBasisCalculation().name());
		
		return data.toJSONString();
	}

	public void setTaxBasisCalculation(TaxBasisCalculation taxBasisCalculation) {
		this.taxBasisCalculation = taxBasisCalculation;
	}

	public TaxBasisCalculation getTaxBasisCalculation() {
		return taxBasisCalculation;
	}

}
