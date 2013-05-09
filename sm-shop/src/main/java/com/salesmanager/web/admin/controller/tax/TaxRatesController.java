package com.salesmanager.web.admin.controller.tax;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.tax.model.taxclass.TaxClass;
import com.salesmanager.core.business.tax.model.taxrate.TaxRate;
import com.salesmanager.core.business.tax.model.taxrate.TaxRateDescription;
import com.salesmanager.core.business.tax.service.TaxClassService;
import com.salesmanager.core.business.tax.service.TaxRateService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;


@Controller
public class TaxRatesController {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaxRatesController.class);
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private TaxRateService taxRateService;
	
	@Autowired
	private TaxClassService taxClassService;
	
	@Secured("TAX")
	@RequestMapping(value={"/admin/tax/taxrates/list.html"}, method=RequestMethod.GET)
	public String displayTaxRates(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model, request);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		List<Country> countries = countryService.getCountries(language);
		TaxRate taxRate = new TaxRate();
		List<Language> languages = store.getLanguages();
		
		for(Language l : languages) {
			
			TaxRateDescription taxRateDescription = new TaxRateDescription();
			taxRateDescription.setLanguage(l);
			taxRate.getDescriptions().add(taxRateDescription);
		}
		
		taxRate.setMerchantStore(store);
		taxRate.setCountry(store.getCountry());
		
		List<TaxRate> taxRates = taxRateService.listByStore(store);
		List<TaxClass> taxClasses = taxClassService.listByStore(store);
		
		model.addAttribute("taxRate", taxRate);
		model.addAttribute("countries", countries);
		model.addAttribute("taxRates", taxRates);
		model.addAttribute("taxClasses", taxClasses);
		
		return com.salesmanager.web.admin.controller.ControllerConstants.Tiles.Tax.taxRates;
	}
	
	@Secured("TAX")
	@RequestMapping(value = "/admin/tax/taxrates/page.html", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String pageTaxRates(HttpServletRequest request,
			HttpServletResponse response) {

		AjaxResponse resp = new AjaxResponse();


		try {
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			Language language = (Language)request.getAttribute("LANGUAGE");
			List<TaxRate> taxRates = taxRateService.listByStore(store);

			if(taxRates!=null) {
				for (TaxRate rate : taxRates) {

					Map<String,String> entry = new HashMap<String,String> ();
					entry.put("id", String.valueOf(rate.getId()));
					entry.put("code", rate.getCode());
					List<TaxRateDescription> descriptions = rate.getDescriptions();
					TaxRateDescription desc = descriptions.get(0);
					for(TaxRateDescription description : descriptions) {
						if(description.getLanguage().getCode().equals(language.getCode())) {
							desc = description;
							break;
						}
					}
					
					entry.put("name", desc.getName());
					
					//TODO rate
					
					
					resp.addDataEntry(entry);

				}
			}

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);

		} catch (Exception e) {
			LOGGER.error("Error while paging custom weight based", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();

		return returnString;
	}
	
	private void setMenu(Model model, HttpServletRequest request)
	throws Exception {

		// display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("tax", "tax");
		activeMenus.put("taxrates", "taxrates");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request
				.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu) menus.get("tax");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
