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
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	
	@RequestMapping(value={"/shop/displayMiniCart.html"},  method = RequestMethod.GET)
	public @ResponseBody ShoppingCartData displayMiniCart(HttpServletRequest request, Model model){
		return getShoppingCartFromSession(request);
		
	}
	
	@RequestMapping(value={"/shop/displayMiniCartByCode.html"},  method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ShoppingCartData displayMiniCart(final String shoppingCartCode, HttpServletRequest request, Model model){
		
		try {
			ShoppingCartData cart =  getShoppingCartFromSession(request);
			
			if(cart==null) {
				cart = shoppingCartFacade.getShoppingCartData(shoppingCartCode);
			}
			if(cart.getCode().equals(shoppingCartCode)) {
				return cart;
			}
			
			
		} catch(Exception e) {
			LOG.error("Error while getting the shopping cart",e);
		}
		
		return null;

	}

	
	@RequestMapping(value={"/shop/miniCart/removeShoppingCartItem.html"},   method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ShoppingCartData removeShoppingCartItem(Long lineItemId, HttpServletRequest request, Model model) throws Exception {
		ShoppingCartData shoppingCartData=shoppingCartFacade.removeCartItem(lineItemId, getShoppingCartFromSession(request).getCode());
		setCartDataToSession(request, shoppingCartData);
		LOG.debug("removed item" + lineItemId + "from cart");
		return getShoppingCartFromSession(request);
	}
	
	
}
