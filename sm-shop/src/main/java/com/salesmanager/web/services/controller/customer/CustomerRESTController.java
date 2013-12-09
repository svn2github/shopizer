package com.salesmanager.web.services.controller.customer;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.services.controller.category.ShoppingCategoryRESTController;

@Controller
@RequestMapping("/shop/services/")
public class CustomerRESTController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private MerchantStoreService merchantStoreService;
	
	@Autowired
	private LanguageService languageService;
	

	@Autowired
	private CountryService countryService;
	
	@Autowired
	private ZoneService zoneService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCategoryRESTController.class);
	
	
	/**
	 * Returns a single customer for a given MerchantStore
	 */
	@RequestMapping( value="/private/customer/{store}/{id}", method=RequestMethod.GET)
	@ResponseBody
	public com.salesmanager.web.entity.customer.Customer getCustomer(@PathVariable final String store, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		if(merchantStore!=null) {
			if(!merchantStore.getCode().equals(store)) {
				merchantStore = null;
			}
		}
		
		if(merchantStore== null) {
			merchantStore = merchantStoreService.getByCode(store);
		}
		
		if(merchantStore==null) {
			LOGGER.error("Merchant store is null for code " + store);
			response.sendError(503, "Merchant store is null for code " + store);
			return null;
		}
		
		Customer customer = customerService.getById(id);
		com.salesmanager.web.entity.customer.Customer customerProxy;
		if(customer != null){
			//TODO use customer populator
			//customerProxy = customerUtils.buildProxyCustomer(customer, merchantStore);
		}else{
			response.sendError(404, "No Customer found with id : " + id);
			return null;
		}
		
		
		return null;
	}
	
	
	/**
	 * Returns all customers for a given MerchantStore
	 */
	@RequestMapping( value="/private/customer/{store}", method=RequestMethod.GET)
	@ResponseBody
	public List<com.salesmanager.web.entity.customer.Customer> getCustomers(@PathVariable final String store, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		if(merchantStore!=null) {
			if(!merchantStore.getCode().equals(store)) {
				merchantStore = null;
			}
		}
		
		if(merchantStore== null) {
			merchantStore = merchantStoreService.getByCode(store);
		}
		
		if(merchantStore==null) {
			LOGGER.error("Merchant store is null for code " + store);
			response.sendError(503, "Merchant store is null for code " + store);
			return null;
		}
		
		List<Customer> customers = customerService.listByStore(merchantStore);
		List<com.salesmanager.web.entity.customer.Customer> returnCustomers = new ArrayList<com.salesmanager.web.entity.customer.Customer>();
		for(Customer customer : customers) {
			//TODO use customer populator
			//com.salesmanager.web.entity.customer.Customer customerProxy = customerUtils.buildProxyCustomer(customer, merchantStore);
			//returnCustomers.add(customerProxy);
		}
		
		return returnCustomers;
	}
	
	
	/**
	 * Updates a customer for a given MerchantStore
	 */
	@RequestMapping( value="/private/customer/{store}/{id}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateCustomer(@PathVariable final String store, @PathVariable Long id, @Valid @RequestBody Customer customer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		if(merchantStore!=null) {
			if(!merchantStore.getCode().equals(store)) {
				merchantStore = null;
			}
		}
		
		if(merchantStore== null) {
			merchantStore = merchantStoreService.getByCode(store);
		}
		
		if(merchantStore==null) {
			LOGGER.error("Merchant store is null for code " + store);
			response.sendError(503, "Merchant store is null for code " + store);
		}
		
		Customer oldCustomer = customerService.getById(id);
		if(oldCustomer != null){
			customer.setId(id);
			Country country = (customer.getCountry() != null)?countryService.getByCode(customer.getCountry().getIsoCode().toUpperCase()):null;
			if(country != null){
				customer.setCountry(country);
			}
			
			Country billCountry = (customer.getBilling() != null)?countryService.getByCode(customer.getBilling().getCountry().getIsoCode().toUpperCase()):null;
			if(billCountry != null){
				customer.getBilling().setCountry(billCountry);
			}
			
			Country delCountry = (customer.getDelivery() != null)?countryService.getByCode(customer.getDelivery().getCountry().getIsoCode().toUpperCase()):null;
			if(delCountry != null){
				customer.getDelivery().setCountry(delCountry);
			}
			
			if(customer.getZone() != null){
				customer.setZone(zoneService.getByCode(customer.getZone().getCountry().getIsoCode().toUpperCase()));
				Country zoneCountry = countryService.getByCode(customer.getZone().getCountry().getIsoCode().toUpperCase());
				if(zoneCountry != null){
					customer.getZone().setCountry(zoneCountry);
				}
			}
			
			customer.setMerchantStore(merchantStore);
			customer.setDefaultLanguage(languageService.getByCode(Constants.DEFAULT_LANGUAGE));
			
			customerService.saveOrUpdate(customer);
		}else{
			response.sendError(404, "No Customer found for ID : " + id);
		}
	}
	
	
	/**
	 * Deletes a customer for a given MerchantStore
	 */
	@RequestMapping( value="/private/customer/{store}/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCustomer(@PathVariable final String store, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		try {
			
			Customer customer = customerService.getById(id);
			
			if(customer==null) {
				response.sendError(404, "No Customer found for ID : " + id);
				return;
			} 
				
				MerchantStore merchantStore = merchantStoreService.getByCode(store);
				if(merchantStore == null) {
					response.sendError(404, "Invalid merchant store : " + store);
					return;
				}
				
				if(merchantStore.getId().intValue()!= customer.getMerchantStore().getId().intValue()){
					response.sendError(404, "Customer id: " + id + " is not part of store " + store);
					return;
				}			
				
				customerService.delete(customer);
			
			
		} catch (ServiceException se) {
			LOGGER.error("Cannot delete customer",se);
			response.sendError(404, "An exception occured while removing the customer");
			return;
		}
		
   

	}
	
	
	/**
	 * Create new customer for a given MerchantStore
	 */
	@RequestMapping( value="/private/customer/{store}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public com.salesmanager.web.entity.customer.Customer createCustomer(@PathVariable final String store, @Valid @RequestBody Customer customer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		if(merchantStore!=null) {
			if(!merchantStore.getCode().equals(store)) {
				merchantStore = null;
			}
		}
		
		if(merchantStore== null) {
			merchantStore = merchantStoreService.getByCode(store);
		}
		
		if(merchantStore==null) {
			LOGGER.error("Merchant store is null for code " + store);
			response.sendError(503, "Merchant store is null for code " + store);
			return null;
		}
		
		customer.setMerchantStore(merchantStore);
		customer.setDefaultLanguage(languageService.getByCode(Constants.DEFAULT_LANGUAGE));
		
		Country country = (customer.getCountry() != null)?countryService.getByCode(customer.getCountry().getIsoCode().toUpperCase()):null;
		if(country != null){
			customer.setCountry(country);
		}else{
			response.sendError(503, "Customer Country is a amandatory field!");
			return null;
		}
		
		Country billCountry = (customer.getBilling() != null)?countryService.getByCode(customer.getBilling().getCountry().getIsoCode().toUpperCase()):null;
		if(billCountry != null){
			customer.getBilling().setCountry(billCountry);
		}else{
			response.sendError(503, "Billing Country is a amandatory field!");
			return null;
		}
		
		Country delCountry = (customer.getDelivery() != null)?countryService.getByCode(customer.getDelivery().getCountry().getIsoCode().toUpperCase()):null;
		if(delCountry != null){
			customer.getDelivery().setCountry(delCountry);
		}else{
			response.sendError(503, "Delivery Country is a amandatory field!");
			return null;
		}
		
		if(customer.getZone() != null){
			customer.setZone(zoneService.getByCode(customer.getZone().getCountry().getIsoCode().toUpperCase()));
			Country zoneCountry = countryService.getByCode(customer.getZone().getCountry().getIsoCode().toUpperCase());
			if(zoneCountry != null){
				customer.getZone().setCountry(zoneCountry);
			}
		}
		
		customerService.saveOrUpdate(customer);
		//TODO use customer populator
		//com.salesmanager.web.entity.customer.Customer customerProxy = customerUtils.buildProxyCustomer(customer, merchantStore);
		//return customerProxy;
		return null;
	}
	
}
