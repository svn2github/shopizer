package com.salesmanager.web.shop.controller.order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.content.model.FileContentType;
import com.salesmanager.core.business.content.model.OutputContentFile;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.shop.controller.AbstractController;


@Controller
@RequestMapping(Constants.SHOP_URI+"/order")
public class ShoppingOrderDownloadController extends AbstractController {
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private OrderService orderService;
	
	/**
	 * Virtual product(s) download link
	 * @param id
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CUSTOMER_AUTH')")
	@RequestMapping("/download/{orderId}/{id}.html")
	public @ResponseBody byte[] downloadFile(@PathVariable Long orderId, @PathVariable Long id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		
		
		FileContentType fileType = FileContentType.STATIC_FILE;
		
		//get customer and check order
		Order order = orderService.getById(orderId);
		if(order==null) {
			//TODO LOG
			return null;
		}
		
		String fileName = null;//get it from OrderProductDownlaod
		
		// needs to query the new API
		OutputContentFile file =contentService.getContentFile(store.getCode(), fileType, fileName);
		
		
		if(file!=null) {
			return file.getFile().toByteArray();
		} else {
			//empty image placeholder
			return null;
		}
		
		
		// product image
		// example -> /download/12345/120.html
		
		//TODO role customer, verify the order has the requested product to download
	}
	


}
