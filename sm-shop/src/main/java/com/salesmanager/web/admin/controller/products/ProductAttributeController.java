package com.salesmanager.web.admin.controller.products;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
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
	
	@RequestMapping(value="/admin/products/attributes/list.html", method=RequestMethod.GET)
	public String displayProductAttributes(@RequestParam("id") long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		setMenu(model,request);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		Product product = null;
		
		try {
			product = productService.getById(productId);
		} catch (Exception e) {

			return "forward:/admin/products/products.html";
		}
		
		if(product==null) {
			return "forward:/admin/products/products.html";
		}
		
		if(product.getMerchantStore().getId().intValue() != store.getId().intValue()){
			return "forward:/admin/products/products.html";
		}
		
		model.addAttribute("product",product);
		return "admin-product-attributes";
		
	}
	
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
	
	@RequestMapping(value="/admin/products/attributes/editAttribute.html", method=RequestMethod.GET)
	public String displayAttributeEdit(@RequestParam("id") long id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayAttribute(id,model,request,response);

	}
	
	@RequestMapping(value="/admin/products/attribute/createAttribute.html", method=RequestMethod.GET)
	public String displayAttributeCreate(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayAttribute(null,model,request,response);

	}
	
	private String displayAttribute(Long id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

		//display menu
		setMenu(model,request);
		
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		
		
		return "admin-product-attribute-details";
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
