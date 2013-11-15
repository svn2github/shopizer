package com.salesmanager.web.reference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.utils.ajax.AjaxResponse;


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
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/admin/reference/provinces.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String getProvinces(HttpServletRequest request, HttpServletResponse response) {
		
		String countryCode = request.getParameter("countryCode");
		
		LOGGER.debug("Province Country Code " + countryCode);
		
		AjaxResponse resp = new AjaxResponse();
		
		
		try {
			

		
		Language language = (Language)request.getAttribute("LANGUAGE");
		
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

}
