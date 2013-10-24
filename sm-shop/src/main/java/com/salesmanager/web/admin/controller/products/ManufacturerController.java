package com.salesmanager.web.admin.controller.products;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.image.ProductImageDescription;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerCriteria;
import com.salesmanager.core.business.customer.model.CustomerList;
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
	
	@Secured("MANUFACTURER")
	@RequestMapping(value="/admin/catalogue/manufacturer/list.html", method=RequestMethod.GET)
	public String getManufacturers(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		this.setMenu(model, request);
		//get the list of manufacturers
		
		return ControllerConstants.Tiles.Product.manufacturerList;
	}
	
//	@SuppressWarnings({ "unchecked"})
//	@Secured("PRODUCTS")
//	@RequestMapping(value="/admin/manufacturers/paging.html", method=RequestMethod.POST, produces="application/json")
//	public @ResponseBody String pageManufacturers(HttpServletRequest request, HttpServletResponse response) {
//		String categoryName = request.getParameter("name");
//
//
//		AjaxResponse resp = new AjaxResponse();
//
//		
//		try {
//			
//			Language language = (Language)request.getAttribute("LANGUAGE");
//				
//		
//			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
//			
//			List<Manufacturer> manufacturers = null;
//					
//			if(!StringUtils.isBlank(categoryName)) {
//				
//				
////				categories = categoryService.getByName(store, categoryName, language);
//				
//			} else {
//				
//				manufacturers = manufacturerService.listByStore(store, language);
//				
//			}
//					
//					
//			
//			for(Manufacturer manufacturer : manufacturers) {
//				
//				@SuppressWarnings("rawtypes")
//				Map entry = new HashMap();
//				entry.put("manufacturerId", manufacturer.getId());
//				
//				ManufacturerDescription description = manufacturer.getDescriptions().get(0);
//				
//				entry.put("name", description.getName());
//				entry.put("order", manufacturer.getOrder());
//				resp.addDataEntry(entry);
//				
//				
//			}
//			
//			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
//			
//
//		
//		} catch (Exception e) {
//			LOGGER.error("Error while paging Manufacturers", e);
//			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
//		}
//		
//		String returnString = resp.toJSONString();
//		
//		return returnString;
//	}
	
	@Secured("MANUFACTURER")
	@RequestMapping(value="/admin/catalogue/manufacturer/create.html", method=RequestMethod.GET)
	public String createManufacturer(  Model model,  HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return displayManufacturer(0l,model,request,response);		
	}
	
	@Secured("MANUFACTURER")
	@RequestMapping(value="/admin/catalogue/manufacturer/edit.html", method=RequestMethod.GET)
	public String editManufacturer(@RequestParam("id") long manufacturerId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		return displayManufacturer(manufacturerId,model,request,response);
	}
	
	private String displayManufacturer(Long manufacturerId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//display menu
		setMenu(model,request);
		
		List<Language> languages = languageService.getLanguages();
		com.salesmanager.web.admin.entity.catalog.Manufacturer manufacturer = new com.salesmanager.web.admin.entity.catalog.Manufacturer();		
		List<ManufacturerDescription> descriptions = new ArrayList<ManufacturerDescription>();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		
		if( manufacturerId!=0) {	//edit mode

			Manufacturer dbManufacturer = new Manufacturer();
			dbManufacturer = manufacturerService.getById( manufacturerId );
			
			if(dbManufacturer==null) {
				return ControllerConstants.Tiles.Product.manufacturerList;
			}
			
//			if(dbManufacturer.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
//				return ControllerConstants.Tiles.Product.manufacturerList;
//			}
			
			manufacturer.setManufacturer( dbManufacturer );
			Set<ManufacturerDescription> manufacturerDescriptions = dbManufacturer.getDescriptions();
			
			ManufacturerDescription manufacturerDesc = null;
			for(ManufacturerDescription desc : manufacturerDescriptions) {
				
					manufacturerDesc = desc;
					descriptions.add(desc);
			}

			manufacturer.setDescriptions(descriptions );
			
		} else {	// Create mode

			Manufacturer manufacturerTmp = new Manufacturer();
			manufacturer.setManufacturer( manufacturerTmp );
			ManufacturerDescription manufacturerDesc = new ManufacturerDescription();
			descriptions.add(  manufacturerDesc );
			manufacturer.setDescriptions(descriptions);
		}

		model.addAttribute("languages",languages);
		model.addAttribute("manufacturer", manufacturer);
		
		return ControllerConstants.Tiles.Product.manufacturerDetails;
	}
		
	@Secured("MANUFACTURER")   //  @Valid
	@RequestMapping(value="/admin/catalogue/manufacturer/save.html", method=RequestMethod.POST)
	public String saveManufacturer( @Valid @ModelAttribute("manufacturer") com.salesmanager.web.admin.entity.catalog.Manufacturer manufacturer, BindingResult result, Model model,  HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		this.setMenu(model, request);
		//save or edit a manufacturer
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		List<Language> languages = languageService.getLanguages();
		
		if(manufacturer.getDescriptions()!=null && manufacturer.getDescriptions().size()>0) {
			
			for(ManufacturerDescription description : manufacturer.getDescriptions()) {
				
				//validate Url Clicked
				if ( description.getUrlClicked() != null && !description.getUrlClicked().toString().isEmpty()) {
					try{
						Integer.parseInt( description.getUrlClicked().toString() );
						
					} catch (Exception e) {
						
						ObjectError error = new ObjectError("descriptions[${counter.index}].urlClicked","URL Clicked must be a number");
						result.addError(error);
					}
				}
			}
		}
		
		//validate image
//		if(manufacturer.getImage()!=null && !manufacturer.getImage().isEmpty()) {
//			
//			try {
//				
//				String maxHeight = configuration.getProperty("PRODUCT_IMAGE_MAX_HEIGHT_SIZE");
//				String maxWidth = configuration.getProperty("PRODUCT_IMAGE_MAX_WIDTH_SIZE");
//				String maxSize = configuration.getProperty("PRODUCT_IMAGE_MAX_SIZE");
//				
//				
//				BufferedImage image = ImageIO.read(manufacturer.getImage().getInputStream());
//				
//				
//				if(!StringUtils.isBlank(maxHeight)) {
//					
//					int maxImageHeight = Integer.parseInt(maxHeight);
//					if(image.getHeight()>maxImageHeight) {
//						ObjectError error = new ObjectError("image",messages.getMessage("message.image.height", locale) + " {"+maxHeight+"}");
//						result.addError(error);
//					}
//					
//				}
//				
//				if(!StringUtils.isBlank(maxWidth)) {
//					
//					int maxImageWidth = Integer.parseInt(maxWidth);
//					if(image.getWidth()>maxImageWidth) {
//						ObjectError error = new ObjectError("image",messages.getMessage("message.image.width", locale) + " {"+maxWidth+"}");
//						result.addError(error);
//					}
//					
//				}
//				
//				if(!StringUtils.isBlank(maxSize)) {
//					
//					int maxImageSize = Integer.parseInt(maxSize);
//					if(manufacturer.getImage().getSize()>maxImageSize) {
//						ObjectError error = new ObjectError("image",messages.getMessage("message.image.size", locale) + " {"+maxSize+"}");
//						result.addError(error);
//					}
//					
//				}
//				
//			} catch (Exception e) {
//				LOGGER.error("Cannot validate product image", e);
//			}
//
//		}
		
 		if (result.hasErrors()) {
			model.addAttribute("languages",languages);
			return ControllerConstants.Tiles.Product.manufacturerDetails;
		}		
		
		
		if ( manufacturer.getManufacturer().getId() !=null && manufacturer.getManufacturer().getId()  > 0 ){
			
			Manufacturer dbManufacturer = new Manufacturer();
			
			return ControllerConstants.Tiles.Product.manufacturerDetails;
			
		} else {			
		
	
			Set<ManufacturerDescription> descriptions = new HashSet<ManufacturerDescription>();	 
			
			Manufacturer newManufacturer = new Manufacturer();
			newManufacturer.setMerchantStore(manufacturer.getManufacturer().getMerchantStore() );
			newManufacturer.setOrder( manufacturer.getOrder() );
			
			if(manufacturer.getDescriptions()!=null && manufacturer.getDescriptions().size()>0) {
			
				for(ManufacturerDescription description : manufacturer.getDescriptions()) {
					description.setManufacturer(newManufacturer);
					descriptions.add(description);
				}
			}	
	
			newManufacturer.setDescriptions(descriptions);
			newManufacturer.setMerchantStore(store);
			manufacturerService.saveOrUpdate(newManufacturer);
			manufacturer.setManufacturer( newManufacturer );  
		}
		
		/*
		 if(product.getImage()!=null && !product.getImage().isEmpty()) {			

			
			String imageName = product.getImage().getOriginalFilename();		

			ProductImage productImage = new ProductImage();
			productImage.setDefaultImage(true);
			productImage.setImage(product.getImage().getInputStream());
			productImage.setProductImage(imageName);
			
			
			List<ProductImageDescription> imagesDescriptions = new ArrayList<ProductImageDescription>();

			for(Language l : languages) {
				
				ProductImageDescription imageDescription = new ProductImageDescription();
				imageDescription.setName(imageName);
				imageDescription.setLanguage(l);
				imageDescription.setProductImage(productImage);
				imagesDescriptions.add(imageDescription);
				
			}
			
			productImage.setDescriptions(imagesDescriptions);
			productImage.setProduct(newProduct);
			
			newProduct.getImages().add(productImage);
			
			productService.saveOrUpdate(newProduct);
			
			//product displayed
			product.setProductImage(productImage);
			
			
		} else {
			
			productService.saveOrUpdate(newProduct);
			
		}
		 */
		
		model.addAttribute("manufacturer", manufacturer);		
		model.addAttribute("languages",languages);
		model.addAttribute("success","success");
		
		return ControllerConstants.Tiles.Product.manufacturerDetails;
	}
	
	@Secured("MANUFACTURER")
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
	
	@Secured("MANUFACTURER")
	@RequestMapping(value="/admin/catalogue/manufacturer/remove.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String deleteManufacturer(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("id");
	
		//get the manufacturer
		
		//IF already attached to products it can't be deleted
		
		AjaxResponse resp = new AjaxResponse();
		
		resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
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
		//
		
	}
}
