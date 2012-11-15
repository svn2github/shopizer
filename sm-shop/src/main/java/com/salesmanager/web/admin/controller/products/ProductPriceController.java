package com.salesmanager.web.admin.controller.products;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;

@Controller
public class ProductPriceController {
	
	
	@RequestMapping(value="/admin/products/prices.html", method=RequestMethod.GET)
	public String displayOptions(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		
		//TODO need to retrieve PRODUCT ID
		
		//TODO check if product.store.id=store.id

		//TODO set product in model
		
		//do nothing, the list populates with paging
		return "admin-products-prices";
		
	}
	

	@RequestMapping(value="/admin/products/prices/paging.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pagePrices(HttpServletRequest request, HttpServletResponse response) {

		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			//TODO need to get product id
			
			
			Language language = (Language)request.getAttribute("LANGUAGE");	
		
			MerchantStore store = (MerchantStore)request.getAttribute("MERCHANT_STORE");
			

			//List<ProductPrice> prices = productPriceService.getByStore(store,language);
					

/*			for(ProductPrice price : prices) {
				
				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("priceId", price.getId());
				entry.put("price", price.);
				ProductPriceDescription description = price.getDescriptions().iterator().next();
				
				entry.put("name", description.getName());
				resp.addDataEntry(entry);
				
				
			}*/
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			

		
		} catch (Exception e) {
			//LOGGER.error("Error while paging options", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
		
		
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-featured", "catalogue-featured");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
