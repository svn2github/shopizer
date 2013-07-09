package com.salesmanager.web.shop.controller.shoppingCart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.shop.controller.ControllerConstants;


/**
 * LATEST NOTES
 * 
 * A mini shopping cart is available on the public shopping section from the upper menu
 * Landing page, Category page (list of products) and Product details page contains a form
 * that let the user to add an item to the cart
 * 
 * Add To Cart
 * ---------------
 * The add to cart is 100% driven by javascript / ajax. The code is available in webapp\resources\js\functions.js
 * 
 * <!-- Simple add to cart html example ${id} is the product id -->
 * <form id="input-${id}" class="well form-inline">
 *  <input type="text" class="input-small" id="quantity-productId-${id}" placeholder="1" value="1">
 * 	<a href="#" class="addToCart" productId="${id}">Add to cart</a>
 * </form>
 * 
 * This html block is ready to be implemented in webapp\pages\shop\templates\bootstrap\pages\landing.jsp. It requires from the admin to add Featured items
 * 
 * The javascript function creates com.salesmanager.web.entity.shoppingcart.ShoppingCartItem and ShoppingCartAttribute (example to come)
 * The javascript looks in the cookie if a shopping cart code exists ex $.cookie( 'cart' ); // requires jQuery-cookie
 * The javascript posts the ShoppingCartItem and the shopping cart code if present
 * 
 * The ShoppingCartController get the ShoppingCartItem and shopping cart code
 * The ShoppingCartController check if the shoppingcart code belongs to the current merchant store (instructions to come on how to create and understand a cart code)
 * The ShoppingCartController if a cart code is present lookup the cart from the database (Shopping cart services have to be created in sm-core) determines if the merchant store are the same
 * The ShoppingCartController if a cart code is not present creates a new ShoppingCart and save it in the database
 * The ShoppingCartController calculates the total (a new PricingService has to be created in sm-core)
 * The ShoppingCartController returns a JSON representation of the ShoppingCart
 * 
 * The javascript re-creates the shopping cart div item (div id shoppingcart) (see webapp\pages\shop\templates\bootstrap\sections\header.jsp)
 * The javascript set the shopping cart code in the cookie
 * 
 * Display a page
 * ----------------
 * 
 * When a page is displayed from the shopping section, the shopping cart has to be displayed
 * 4 paths 1) No shopping cart 2) A shopping cart exist in the session 3) A shopping cart code exists in the cookie  4) A customer is logeed in and a shopping cart exists in the database
 * 
 * 1) No shopping cart, nothing to do !
 * 
 * 2) StoreFilter will tak care of a ShoppingCart present in the HttpSession
 * 
 * 3) Once a page is displayed and no cart returned from the controller, a javascript looks on load in the cookie to see if a shopping cart code is present
 * 	  If a code is present, by ajax the cart is loaded and displayed
 * 
 * 4) No cart in the session but the customer logs in, the system looks in the DB if a shopping cart exists, if so it is putted in the session so the StoreFilter can manage it and putted in the request
 * 
 * @author Carl Samson
 *
 */

@Controller
public class ShoppingCartController {
	
	
	/**
	 * Retrieves a Shopping cart from the database
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/shop/shoppingCart.html"}, method=RequestMethod.GET)
	public String displayShoppingCart(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//Looks in the HttpSession to see if a customer is logged in
		
		//shoppingCartService.getByCustomer(customerId);
		
		//set the cart in the HttpSession
		
		//calculate the price of each item by using ProductPriceUtils in sm-core
		//set each item price in ShoppingCartItem.price
		//return the ShoppingCartItem entity list in the model
		
		return ControllerConstants.Tiles.ShoppingCart.shoppingCart + ".bootstrap";
		
		
	}
	
	/**
	 * Add an item to the ShoppingCart (AJAX exposed method)
	 * @param id
	 * @param quantity
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/shop/addShoppingCartItem.html"}, method=RequestMethod.GET)
	public @ResponseBody
	String addShoppingCartItem(@ModelAttribute Long id, @ModelAttribute Integer quantity, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		/*** UPDATE **/
		/**
		 * May have to be modified to accept ShoppingCartItem
		 */

		//Looks in the HttpSession to see if a customer is logged in
		//Looks in the HttpSession to see if a shopping cart exists
		
		
		//if a customer exists in http session
			//if a cart does not exist in httpsession
				//get cart from database
					//if a cart exist in the database add the item to the cart and put cart in httpsession and save to the database
					//else a cart does not exist in the database, create a new one, set the customer id, set the cart in the httpsession
			//else a cart exist in the httpsession, add item to httpsession cart and save to the database
		//else no customer in httpsession
			//if a cart does not exist in httpsession
				//create a new one, set the cart in the httpsession
			//else a cart exist in the httpsession, add item to httpsession cart and save to the database
		
		
		/**
		 * my concern is with the following : 
		 * 	what if you add item in the shopping cart as an anonymous user
		 *  later on you log in to process with checkout but the system retrieves a previous shopping cart saved in the database for that customer
		 *  in that case we need to synchronize both carts and the original one (the one with the customer id) supercedes the current cart in session
		 *  the sustem will have to deal with the original one and remove the latest
		 */
		
		
		//**more implementation details
		//calculate the price of each item by using ProductPriceUtils in sm-core
		//for each product in the shopping cart get the product
		//invoke productPriceUtils.getFinalProductPrice
		//from FinalPrice get final price which is the calculated price given attributes and discounts
		//set each item price in ShoppingCartItem.price
		
		//add new item shoppingCartService.create
		
		//create JSON representation of the shopping cart
		
		//return the JSON structure in AjaxResponse
		
		//store the shopping cart in the http session
		
		AjaxResponse resp = new AjaxResponse();
		
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		return resp.toJSONString();
		
		
	}
	
	/**
	 * Removes an item from the Shopping Cart (AJAX exposed method)
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/shop/removeShoppingCartItem.html"}, method=RequestMethod.GET)
	public @ResponseBody
	String removeShoppingCartItem(@ModelAttribute Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

		//Looks in the HttpSession to see if a customer is logged in
		
		//get any shopping cart for this user
		
		//** need to check if the item has property, similar items may exist but with different properties
		String attributes = request.getParameter("attribute");//attributes id are sent as 1|2|5|
		//this will help with hte removal of the appropriate item
		
		//remove the item shoppingCartService.create
		
		//create JSON representation of the shopping cart
		
		//return the JSON structure in AjaxResponse
		
		//store the shopping cart in the http session
		
		AjaxResponse resp = new AjaxResponse();
		
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		return resp.toJSONString();
		
		
	}
	
	/**
	 * Update the quantity of an item in the Shopping Cart (AJAX exposed method)
	 * @param id
	 * @param quantity
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/shop/updateShoppingCartItem.html"}, method=RequestMethod.GET)
	public @ResponseBody
	String updateShoppingCartItem(@ModelAttribute Long id, @ModelAttribute Integer quantity, HttpServletRequest request, HttpServletResponse response) throws Exception {
		


		
		AjaxResponse resp = new AjaxResponse();
		
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		
		return resp.toJSONString();
		
		
	}
		

}
