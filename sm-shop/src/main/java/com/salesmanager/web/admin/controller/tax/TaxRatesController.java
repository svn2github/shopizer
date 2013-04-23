package com.salesmanager.web.admin.controller.tax;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.tax.model.taxrate.TaxRate;
import com.salesmanager.core.business.tax.model.taxrate.TaxRateDescription;
import com.salesmanager.core.business.tax.service.TaxRateService;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;


@Controller
public class TaxRatesController {
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private TaxRateService taxRateService;
	
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
		
		List<TaxRate> taxRates = taxRateService.listByStore(store);
		
		model.addAttribute("taxRate", taxRate);
		model.addAttribute("countries", countries);
		model.addAttribute("taxRates", taxRates);
		
		return com.salesmanager.web.admin.controller.ControllerConstants.Tiles.Tax.taxRates;
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
