/**
 * 
 */
package com.salesmanager.web.shop.controller.shoppingCart;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.web.entity.shoppingcart.ShoppingCartData;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.shoppingCart.facade.ShoppingCartFacade;

/**
 * @author Umesh A
 *
 */
@Controller
public class MiniCartController extends AbstractController{
	
	protected final Logger LOG= Logger.getLogger( getClass());
	
	@Autowired
	private ShoppingCartFacade shoppingCartFacade;
	private final static String MINICART="shop/common/cart/fragment/minicartFragment";
	
	@RequestMapping(value={"/shop/displayMiniCart.html"},  method = RequestMethod.GET)
	public String displayMiniCart(HttpServletRequest request, Model model){
		model.addAttribute("miniCartData", getShoppingCartFromSession(request));
		return MINICART;
	}

	
@RequestMapping(value={"/shop/miniCart/removeShoppingCartItem.html"},   method = { RequestMethod.GET, RequestMethod.POST })
	
	String removeShoppingCartItem(Long lineItemId, HttpServletRequest request, Model model) throws Exception {
		ShoppingCartData shoppingCartData=shoppingCartFacade.removeCartItem(lineItemId, getShoppingCartFromSession(request).getCode());
		setCartDataToSession(request, shoppingCartData);
		model.addAttribute("miniCartData", getShoppingCartFromSession(request));
		return MINICART;
		
		
		
	}
	
	
}
