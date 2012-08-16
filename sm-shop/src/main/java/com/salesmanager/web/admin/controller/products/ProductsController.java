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

import com.salesmanager.web.admin.entity.web.Menu;

@Controller
public class ProductsController {
	
	
	
	@RequestMapping(value="/admin/products/products.html", method=RequestMethod.GET)
	public String displayProducts(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		
		//get categories
		
		
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-products", "catalogue-products");
		
		Map menus = (Map)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
		return "admin-products";
		
	}

	
	
	@RequestMapping(value="/admin/products/paging.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String pageProducts(HttpServletRequest request, HttpServletResponse response) {
		String categoryId = request.getParameter("categoryId");
		String searchTerm = request.getParameter("searchTerm");
		
		String startRow = request.getParameter("_startRow");
		String endRow = request.getParameter("_endRow");
		
		String totalRows = "180";
		

		
		if(searchTerm!=null) {
			totalRows="6";
		}
		//get sub category & sub categories for input categoryId
		
		//get products using startRow and endRow
		
		//populate response object which has to be converted to JSON 
		
		//will receive name and sku as filter elements
		
		
		System.out.println(request.getParameter("categoryId"));

		StringBuilder res = new StringBuilder().append("{ response:{     status:0,     startRow:" + startRow + ",     endRow:" + endRow + ",     totalRows:" + totalRows + ",     data:" +
				"[           ");
		
				for(int i = Integer.parseInt(startRow); i < Integer.parseInt(endRow); i++) {
					
					
					res.append("{name:\"" + i + "\",sku:\"1wsd5\",cost:\"$29.99\",units:12,categoryIds:[{categoryId:188,categoryId:264,categoryId:4}]}");
					if(i < Integer.parseInt(endRow)) {
						res.append(",");
					}
				}

				res.append("]   } }");
				

			return res.toString();
	}
	
}
