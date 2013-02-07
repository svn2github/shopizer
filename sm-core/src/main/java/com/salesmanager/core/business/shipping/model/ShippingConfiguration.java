package com.salesmanager.core.business.shipping.model;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * Object saved in the database maintaining various shipping options
 * @author casams1
 *
 */
public class ShippingConfiguration implements JSONAware {
	
	private ShippingType shippingType;
	private ShippingBasisType shippingBasisType;
	private ShippingOptionPriceType shippingOptionPriceType;
	
	

	private String shipType;
	private String shipBaseType;
	private String shipOptionPriceType;
	
	
	public String getShipType() {
		return shipType;
	}


	public String getShipBaseType() {
		return shipBaseType;
	}


	public String getShipOptionPriceType() {
		return shipOptionPriceType;
	}








	public void setShippingOptionPriceType(ShippingOptionPriceType shippingOptionPriceType) {
		this.shippingOptionPriceType = shippingOptionPriceType;
		this.shipOptionPriceType = this.shippingOptionPriceType.name();
	}


	public ShippingOptionPriceType getShippingOptionPriceType() {
		return shippingOptionPriceType;
	}


	public void setShippingBasisType(ShippingBasisType shippingBasisType) {
		this.shippingBasisType = shippingBasisType;
		this.shipBaseType = this.shippingBasisType.name();
	}


	public ShippingBasisType getShippingBasisType() {
		return shippingBasisType;
	}


	public void setShippingType(ShippingType shippingType) {
		this.shippingType = shippingType;
		this.shipType = this.shippingType.name();
	}


	public ShippingType getShippingType() {
		return shippingType;
	}

	
	/** JSON bindding **/
	public void setShipType(String shipType) {
		this.shipType = shipType;
		ShippingType sType = ShippingType.NATIONAL;
		if(shipType.equals(ShippingType.INTERNATIONAL)) {
			sType = ShippingType.INTERNATIONAL;
		}
		setShippingType(sType);
	}


	public void setShipOptionPriceType(String shipOptionPriceType) {
		this.shipOptionPriceType = shipOptionPriceType;
		ShippingOptionPriceType sType = ShippingOptionPriceType.ALL;
		if(shipOptionPriceType.equals(ShippingOptionPriceType.HIGHEST)) {
			sType = ShippingOptionPriceType.HIGHEST;
		}
		if(shipOptionPriceType.equals(ShippingOptionPriceType.LEAST)) {
			sType = ShippingOptionPriceType.LEAST;
		}
		setShippingOptionPriceType(sType);
	}


	public void setShipBaseType(String shipBaseType) {
		this.shipBaseType = shipBaseType;
		ShippingBasisType sType = ShippingBasisType.SHIPPING;
		if(shipBaseType.equals(ShippingBasisType.BILLING)) {
			sType = ShippingBasisType.BILLING;
		}
		setShippingBasisType(sType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toJSONString() {
		JSONObject data = new JSONObject();
		data.put("shipBaseType", this.getShippingBasisType().name());
		data.put("shipOptionPriceType", this.getShippingOptionPriceType().name());
		data.put("shipType", this.getShippingType().name());
		return data.toJSONString();
	}





}
