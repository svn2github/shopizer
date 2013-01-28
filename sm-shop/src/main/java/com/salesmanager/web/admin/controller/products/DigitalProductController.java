package com.salesmanager.web.admin.controller.products;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.file.DigitalProductService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.digital.ProductFiles;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;

@Controller
public class DigitalProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private DigitalProductService digitalProductService;
	
	@Secured("PRODUCTS")
	@RequestMapping(value={"/admin/products/digitalProduct.html"}, method=RequestMethod.GET)
	public String getDigitalProduct(@RequestParam("id") long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		//TODO check if the product is virtual, get the virtual product information //digitalProductService.getByProduct
		
		Product product = productService.getById(productId);
		
		if(product==null || product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			return "redirect:/admin/products/products.html";
		}
		
		ProductFiles productFiles = new ProductFiles();
		productFiles.setProduct(product);
		
		model.addAttribute("file", null);
		
		
		return ControllerConstants.Tiles.Product.digitalProduct;
		
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
