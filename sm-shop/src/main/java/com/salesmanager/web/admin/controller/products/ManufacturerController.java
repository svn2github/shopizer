package com.salesmanager.web.admin.controller.products;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.utils.CoreConfiguration;
import com.salesmanager.core.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.controller.customers.CustomerController;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.utils.LabelUtils;

@Controller
public class ManufacturerController {
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private ManufacturerService manufacturerService;
	
	@Autowired
	LabelUtils messages;
	
	@Autowired
	private CoreConfiguration configuration;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/manufacturer/list.html", method=RequestMethod.GET)
	public String getManufacturers(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		this.setMenu(model, request);
		
		return ControllerConstants.Tiles.Product.manufacturerList;
	}
	
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/manufacturer/create.html", method=RequestMethod.GET)
	public String createManufacturer(  Model model,  HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return displayManufacturer(null,model,request,response);		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/manufacturer/edit.html", method=RequestMethod.GET)
	public String editManufacturer(@RequestParam("id") long manufacturerId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		return displayManufacturer(manufacturerId,model,request,response);
	}
	
	private String displayManufacturer(Long manufacturerId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//display menu
		setMenu(model,request);
		
		//List<Language> languages = languageService.getLanguages();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		List<Language> languages = store.getLanguages();
		
		
		com.salesmanager.web.admin.entity.catalog.Manufacturer manufacturer = new com.salesmanager.web.admin.entity.catalog.Manufacturer();		
		List<ManufacturerDescription> descriptions = new ArrayList<ManufacturerDescription>();

		
		if( manufacturerId!=null && manufacturerId.longValue()!=0) {	//edit mode

			Manufacturer dbManufacturer = new Manufacturer();
			dbManufacturer = manufacturerService.getById( manufacturerId );
			
			if(dbManufacturer==null) {
				return ControllerConstants.Tiles.Product.manufacturerList;
			}
			
			if(dbManufacturer.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				return ControllerConstants.Tiles.Product.manufacturerList;
			}
			
			Set<ManufacturerDescription> manufacturerDescriptions = dbManufacturer.getDescriptions();

			
			for(Language l : languages) {
				
				ManufacturerDescription manufDescription = null;
				if(manufacturerDescriptions!=null) {
					
					for(ManufacturerDescription desc : manufacturerDescriptions) {				
						String code = desc.getLanguage().getCode();
						if(code.equals(l.getCode())) {
							manufDescription = desc;
						}

					}
					
				}
				
				if(manufDescription==null) {
					manufDescription = new ManufacturerDescription();
					manufDescription.setLanguage(l);
				}
				
				manufacturer.getDescriptions().add(manufDescription);
				
			}
			
			manufacturer.setManufacturer( dbManufacturer );
		
			
			manufacturer.setOrder( dbManufacturer.getOrder() );
			
		} else {	// Create mode

			Manufacturer manufacturerTmp = new Manufacturer();
			manufacturer.setManufacturer( manufacturerTmp );
			
			for(Language l : languages) {// for each store language
				
				ManufacturerDescription manufacturerDesc = new ManufacturerDescription();
				manufacturerDesc.setLanguage(l);
				descriptions.add(  manufacturerDesc );
				manufacturer.setDescriptions(descriptions);
				
			}
		}

		model.addAttribute("languages",languages);
		model.addAttribute("manufacturer", manufacturer);
		
		return ControllerConstants.Tiles.Product.manufacturerDetails;
	}
		
	@PreAuthorize("hasRole('PRODUCTS')")  
	@RequestMapping(value="/admin/catalogue/manufacturer/save.html", method=RequestMethod.POST)
	public String saveManufacturer( @Valid @ModelAttribute("manufacturer") com.salesmanager.web.admin.entity.catalog.Manufacturer manufacturer, BindingResult result, Model model,  HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		this.setMenu(model, request);
		//save or edit a manufacturer

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		List<Language> languages = languageService.getLanguages();

		if(manufacturer.getDescriptions()!=null && manufacturer.getDescriptions().size()>0) {

			for(ManufacturerDescription description : manufacturer.getDescriptions()) {

				//validate Url Clicked
/*				if ( description.getUrlClicked() != null && !description.getUrlClicked().toString().isEmpty()) {
					try{
						Integer.parseInt( description.getUrlClicked().toString() );

					} catch (Exception e) {

						ObjectError error = new ObjectError("descriptions[${counter.index}].urlClicked","URL Clicked must be a number");
						result.addError(error);
					}
				}*/
			}
		}


	//validate image
		if(manufacturer.getImage()!=null && !manufacturer.getImage().isEmpty()) {

			try {

				String maxHeight = configuration.getProperty("PRODUCT_IMAGE_MAX_HEIGHT_SIZE");
				String maxWidth = configuration.getProperty("PRODUCT_IMAGE_MAX_WIDTH_SIZE");
				String maxSize = configuration.getProperty("PRODUCT_IMAGE_MAX_SIZE");

				BufferedImage image = ImageIO.read(manufacturer.getImage().getInputStream());

				if(!StringUtils.isBlank(maxHeight)) {

					int maxImageHeight = Integer.parseInt(maxHeight);
					if(image.getHeight()>maxImageHeight) {
						ObjectError error = new ObjectError("image",messages.getMessage("message.image.height", locale) + " {"+maxHeight+"}");
						result.addError(error);
					}
				}

				if(!StringUtils.isBlank(maxWidth)) {

					int maxImageWidth = Integer.parseInt(maxWidth);
					if(image.getWidth()>maxImageWidth) {
						ObjectError error = new ObjectError("image",messages.getMessage("message.image.width", locale) + " {"+maxWidth+"}");
						result.addError(error);
					}
				}

				if(!StringUtils.isBlank(maxSize)) {

					int maxImageSize = Integer.parseInt(maxSize);
					if(manufacturer.getImage().getSize()>maxImageSize) {
						ObjectError error = new ObjectError("image",messages.getMessage("message.image.size", locale) + " {"+maxSize+"}");
						result.addError(error);
					}
				}

			} catch (Exception e) {
				LOGGER.error("Cannot validate manufacturer image", e);
			}

		}

		if (result.hasErrors()) {
			model.addAttribute("languages",languages);
			return ControllerConstants.Tiles.Product.manufacturerDetails;
		}

		Manufacturer newManufacturer = manufacturer.getManufacturer();

		if ( manufacturer.getManufacturer().getId() !=null && manufacturer.getManufacturer().getId()  > 0 ){

			newManufacturer = manufacturerService.getById( manufacturer.getManufacturer().getId() );

			if(newManufacturer.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				return ControllerConstants.Tiles.Product.manufacturerList;
			}

		}

//		for(ManufacturerImage image : manufacturer.getImages()) {
//			if(image.isDefaultImage()) {
//				manufacturer.setProductImage(image);
//			}
//		}

		Set<ManufacturerDescription> descriptions = new HashSet<ManufacturerDescription>();
		if(manufacturer.getDescriptions()!=null && manufacturer.getDescriptions().size()>0) {
			
			for(ManufacturerDescription desc : manufacturer.getDescriptions()) {
				
				desc.setManufacturer(newManufacturer);
				descriptions.add(desc);
			}
		}
		newManufacturer.setDescriptions(descriptions );
		newManufacturer.setOrder( manufacturer.getOrder() );
		newManufacturer.setMerchantStore(store);



//		if(manufacturer.getManufacturerImage()!=null && manufacturer.getManufacturerImage().getId() == null) {
//			newManufacturer.setProductImage(null);
//		}



		if(manufacturer.getImage()!=null && !manufacturer.getImage().isEmpty()) {
//
//			String imageName = manufacturer.getImage().getOriginalFilename();
//
//			ManufacturerImage manufacturerImage = new ManufacturerImage();
//			manufacturerImage.setDefaultImage(true);
//			manufacturerImage.setImage(manufacturer.getImage().getInputStream());
//			manufacturerImage.setManufacturerImage(imageName);
//
//			List<ManufacturerImageDescription> imagesDescriptions = new ArrayList<ManufacturerImageDescription>();
//
//			for(Language l : languages) {
//
//				ManufacturerImageDescription imageDescription = new ManufacturerImageDescription();
//				imageDescription.setName(imageName);
//				imageDescription.setLanguage(l);
//				imageDescription.setManufacturerImage(productImage);
//				imagesDescriptions.add(imageDescription);
//
//			}
//
//			manufacturerImage.setDescriptions(imagesDescriptions);
//			manufacturerImage.setProduct(newManufacturer);
//
//			newManufacturer.getImages().add(manufacturerImage);
//
//			manufacturerService.saveOrUpdate(newManufacturer);
//
//			//manufacturer displayed
//			manufacturer.setProductImage(manufacturerImage);


		} else {

			manufacturerService.saveOrUpdate(newManufacturer);
		}

		model.addAttribute("manufacturer", manufacturer);
		model.addAttribute("languages",languages);
		model.addAttribute("success","success");

		return ControllerConstants.Tiles.Product.manufacturerDetails;

	}
	
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/manufacturer/paging.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageManufacturers(HttpServletRequest request, HttpServletResponse response) {
		
		AjaxResponse resp = new AjaxResponse();
		try {
			
			Language language = (Language)request.getAttribute("LANGUAGE");	
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			
			List<Manufacturer> manufacturers = null;				
			manufacturers = manufacturerService.listByStore(store, language);
				
			for(Manufacturer manufacturer : manufacturers) {
				
				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("id", manufacturer.getId());
				
				ManufacturerDescription description = manufacturer.getDescriptions().iterator().next();
				
				entry.put("attribute", description.getName());
				entry.put("order", manufacturer.getOrder());
				resp.addDataEntry(entry);
				
			}
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);	
		
		} catch (Exception e) {
			LOGGER.error("Error while paging Manufacturers", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
		String returnString = resp.toJSONString();
		return returnString;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/catalogue/manufacturer/remove.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String deleteManufacturer(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		Long sid =  Long.valueOf(request.getParameter("id") );
	
	
		AjaxResponse resp = new AjaxResponse();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		try{
			Manufacturer delManufacturer = manufacturerService.getById( sid  );				
			if(delManufacturer==null || delManufacturer.getMerchantStore().getId().intValue() != store.getId().intValue()) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			} 
			
			int count = manufacturerService.getCountManufAttachedProducts( delManufacturer );  
			//IF already attached to products it can't be deleted
			if ( count > 0 ){
				resp.setStatusMessage(messages.getMessage("message.product.association", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			}	

			manufacturerService.delete( delManufacturer );
			
			resp.setStatusMessage(messages.getMessage("message.success", locale));
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
			
		} catch (Exception e) {
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);	
			LOGGER.error("Cannot delete manufacturer.", e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
		
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("manufacturer-list", "manufacturer-list");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
	}

}
