package com.salesmanager.web.reference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.utils.LocaleUtils;


/**
 * Used for misc reference objects
 * @author csamson777
 *
 */
@Controller
public class ReferenceController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceController.class);
	
	@Autowired
	private ZoneService zoneService;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private LanguageService languageService;

	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value={"/admin/reference/provinces.html","/shop/reference/provinces.html"}, method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String getProvinces(HttpServletRequest request, HttpServletResponse response) {
		
		String countryCode = request.getParameter("countryCode");
		String lang = request.getParameter("lang");
		LOGGER.debug("Province Country Code " + countryCode);
		AjaxResponse resp = new AjaxResponse();
		
		try {
			
			Language language = null;
			
			if(!StringUtils.isBlank(lang)) {
				language = languageService.getByCode(lang);
			}
			
			if(language==null) {
				language = (Language)request.getAttribute("LANGUAGE");
			}
			
			if(language==null) {
				language = languageService.getByCode(Constants.DEFAULT_LANGUAGE);
			}
			
			
			Map<String,Country> countriesMap = countryService.getCountriesMap(language);
			Country country = countriesMap.get(countryCode);
			List<Zone> zones = zoneService.getZones(country, language);
			if(zones!=null && zones.size()>0) {
				
				
				
				for(Zone zone : zones) {
				
					@SuppressWarnings("rawtypes")
					Map entry = new HashMap();
					entry.put("name", zone.getName());
					entry.put("code", zone.getCode());
					entry.put("id", zone.getId());
		
					resp.addDataEntry(entry);
				
				}
				
				
			}
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		} catch (Exception e) {
			LOGGER.error("GetProvinces()", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		
		String returnString = resp.toJSONString();
		return returnString;
		
	}
	
	@RequestMapping(value="/shop/reference/countryName")
	public @ResponseBody String countryName(@RequestParam String countryCode, HttpServletRequest request) {
		
		try {
			Language language = LocaleUtils.getRequestLanguage(request);
			if(language==null) {
				return countryCode;
			}
			Map<String, Country> countriesMap = countryService.getCountriesMap(language);
			if(countriesMap!=null) {
				Country c = countriesMap.get(countryCode);
				if(c!=null) {
					return c.getName();
				}
			}
		
		} catch (ServiceException e) {
			LOGGER.error("Error while looking up country " + countryCode);
		}
		return countryCode;
	}
	
	@RequestMapping(value="/shop/reference/zoneName")
	public @ResponseBody String zoneName(@RequestParam String zoneCode, HttpServletRequest request) {
		
		try {
			Language language = LocaleUtils.getRequestLanguage(request);
			if(language==null) {
				return zoneCode;
			}
			Map<String, Zone> zonesMap = zoneService.getZones(language);
			if(zonesMap!=null) {
				Zone z = zonesMap.get(zoneCode);
				if(z!=null) {
					return z.getName();
				}
			}
		
		} catch (ServiceException e) {
			LOGGER.error("Error while looking up zone " + zoneCode);
		}
		return zoneCode;
	}

}
