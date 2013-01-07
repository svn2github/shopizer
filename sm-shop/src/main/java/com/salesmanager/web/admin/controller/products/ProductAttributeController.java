package com.salesmanager.web.admin.controller.products;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValue;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValueDescription;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionValueService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.core.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;

@Controller
public class ProductAttributeController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductAttributeController.class);
	
	@Autowired
	private ProductAttributeService productAttributeService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductPriceUtils priceUtil;
	
	@Autowired
	ProductOptionService productOptionService;
	
	@Autowired
	ProductOptionValueService productOptionValueService;
	

	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/products/attributes/list.html", method=RequestMethod.GET)
	public String displayProductAttributes(@RequestParam("id") long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		setMenu(model,request);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		Product product = productService.getById(productId);
		
		if(product==null) {
			return "redirect:/admin/products/products.html";
		}
		
		if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			return "redirect:/admin/products/products.html";
		}
		
		model.addAttribute("product",product);
		return "admin-products-attributes";
		
	}
	
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/products/attributes/page.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageAttributes(HttpServletRequest request, HttpServletResponse response) {

		//String attribute = request.getParameter("attribute");
		String sProductId = request.getParameter("productId");
		
		
		AjaxResponse resp = new AjaxResponse();
		
		Long productId;
		Product product = null;
		
		try {
			productId = Long.parseLong(sProductId);
		} catch (Exception e) {
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("Product id is not valid");
			String returnString = resp.toJSONString();
			return returnString;
		}

		
		try {
			
			
			product = productService.getById(productId);
			


			Language language = (Language)request.getAttribute("LANGUAGE");
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			
			List<ProductAttribute> attributes = productAttributeService.getByProductId(store, product, language);
			
			for(ProductAttribute attr : attributes) {
				
				Map entry = new HashMap();
				entry.put("attributeId", attr.getId());
				entry.put("attribute", attr.getProductOption().getDescriptionsList().get(0));
				entry.put("value", attr.getProductOptionValue().getDescriptionsList().get(0));
				entry.put("order", attr.getProductOptionSortOrder());
				entry.put("price", priceUtil.getAdminFormatedAmountWithCurrency(store,attr.getOptionValuePrice()));

				resp.addDataEntry(entry);
				
				
				
			}

			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;


	}
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/products/attributes/editAttribute.html", method=RequestMethod.GET)
	public String displayAttributeEdit(@RequestParam("id") long id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayAttribute(id,model,request,response);

	}
	
	@Secured("PRODUCTS")
	@RequestMapping(value="/admin/products/attribute/createAttribute.html", method=RequestMethod.GET)
	public String displayAttributeCreate(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayAttribute(null,model,request,response);

	}
	
	private String displayAttribute(Long id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

		//display menu
		setMenu(model,request);
		
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		List<Language> languages = store.getLanguages();
		
		ProductAttribute attribute = null;
		
		//get Options
		List<ProductOption> options = productOptionService.listByStore(store, language);
		//get OptionsValues
		List<ProductOptionValue> optionsValues = productOptionValueService.listByStore(store, language);
		
		if(id!=null && id.intValue()!=0) {//edit mode
			
			attribute = productAttributeService.getById(id);
			
		} else {
			
			attribute = new ProductAttribute();
			ProductOptionValue value = new ProductOptionValue();
			Set<ProductOptionValueDescription> descriptions = new HashSet<ProductOptionValueDescription>();
			for(Language l : languages) {
				
				ProductOptionValueDescription desc = new ProductOptionValueDescription();
				desc.setLanguage(l);
				descriptions.add(desc);
				
				
			}
			
			value.setDescriptions(descriptions);
			attribute.setProductOptionValue(value);
		}
		
		model.addAttribute("optionsValues",optionsValues);
		model.addAttribute("options",options);
		model.addAttribute("attribute",attribute);
		return "admin-products-attribute-details";
	}

	
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-products", "catalogue-products");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
