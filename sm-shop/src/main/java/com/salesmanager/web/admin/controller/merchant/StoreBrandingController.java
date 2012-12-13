package com.salesmanager.web.admin.controller.merchant;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.currency.service.CurrencyService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.modules.cms.common.CMSContentImage;
import com.salesmanager.web.admin.entity.merchant.ContentImages;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;

@Controller
public class StoreBrandingController {
	
	@Autowired
	MerchantStoreService merchantStoreService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	ZoneService zoneService;
	
	
	@Autowired
	LanguageService languageService;
	
	@Autowired
	CurrencyService currencyService;
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping(value="/admin/store/storeBranding.html", method=RequestMethod.GET)
	public String displayStoreBranding(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		model.addAttribute("store", store);
		

		
		return "admin-store-branding";
	}
	
	@RequestMapping(value="/admin/store/saveBranding.html", method=RequestMethod.POST)
	public String saveStoreBranding(@ModelAttribute(value="contentImages") @Valid final ContentImages contentImages, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		
		if(contentImages.getImage()!=null && contentImages.getImage().size()>0) {

			String imageName = contentImages.getImage().get(0).getOriginalFilename();
            InputStream inputStream = contentImages.getImage().get(0).getInputStream();
            CMSContentImage cmsContentImage = new CMSContentImage();
            cmsContentImage.setImageName(imageName);
            cmsContentImage.setContentType( contentImages.getImage().get(0).getContentType() );
            cmsContentImage.setFile( inputStream );
            contentService.addLogo(store.getCode(), cmsContentImage);
			
            //Update store
            store.setStoreLogo(imageName);
            merchantStoreService.update(store);
  
		}
		
		model.addAttribute("store", store);

		
		return "admin-store-branding";
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("store", "store");
		activeMenus.put("storeBranding", "storeBranding");

		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("store");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
