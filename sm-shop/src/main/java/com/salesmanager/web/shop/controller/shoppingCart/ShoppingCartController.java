package com.salesmanager.web.shop.controller.shoppingCart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.OrderSummary;
import com.salesmanager.core.business.order.model.OrderTotalSummary;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.shoppingcart.service.ShoppingCartService;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shoppingcart.ShoppingCart;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartAttribute;
import com.salesmanager.web.entity.shoppingcart.ShoppingCartItem;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.utils.ImageFilePathUtils;


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
 * <form id="input-${id}">
 *  <input type="text" class="input-small" id="quantity-productId-${id}" placeholder="1" value="1">
 * 	<a href="#" class="addToCart" productId="${id}">Add to cart</a>
 * </form>
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
	
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductAttributeService productAttributeService;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	
	/**
	 * Retrieves a Shopping cart from the database (regular shopping cart)
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
	@RequestMapping(value={"/shop/addShoppingCartItem.html"}, method=RequestMethod.POST)
	public @ResponseBody
	String addShoppingCartItem(@RequestBody ShoppingCartItem item, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		Language language = (Language)request.getAttribute("LANGUAGE");

		ShoppingCart shoppingCart = (ShoppingCart)request.getSession().getAttribute(Constants.SHOPPING_CART);
		//cart exist in http session
		if(shoppingCart!=null) {
			String shoppingCartCode = shoppingCart.getCode();
			if(!StringUtils.isBlank(shoppingCartCode)) {
				if(!item.getCode().equals(shoppingCartCode)) {
					//TODO if different
				}
			}
		}
		
		//Look in the HttpSession to see if a customer is logged in
		Customer customer = (Customer)request.getSession().getAttribute(Constants.CUSTOMER);
		if(customer != null) {
			com.salesmanager.core.business.shoppingcart.model.ShoppingCart customerCart = shoppingCartService.getByCustomer(customer);
			if(customerCart!=null) {
				shoppingCart = convertFromEntity(customerCart,store);
				//TODO if ahoppingCart != null ?? merge
				//TODO maybe they have the same code
				//TODO what if codes are different (-- merge carts, keep the latest one, delete the oldest, switch codes --)
			}
		}
		
		if(!StringUtils.isBlank(item.getCode()))  {
			//get it from the db
			com.salesmanager.core.business.shoppingcart.model.ShoppingCart dbCart = shoppingCartService.getByCode(item.getCode(), store);
			if(dbCart!=null) {
				shoppingCart = convertFromEntity(dbCart,store);
			}
		}
		
		
		//if shoppingCart is null create a new one
		if(shoppingCart==null)  {
			shoppingCart = new ShoppingCart();
			String code = UUID.randomUUID().toString().replaceAll("-", "");
			shoppingCart.setCode(code);
	
		}
		
		//add the item to the cart
		this.addShoppingCartItem(shoppingCart, item, store);
		
		
		//convert to entity
		com.salesmanager.core.business.shoppingcart.model.ShoppingCart entity = this.convertToEntity(shoppingCart, store, customer);
		
		//calculate total
		OrderSummary summary = new OrderSummary();
		List<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> productsList = new ArrayList<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem>();
		productsList.addAll(entity.getLineItems());
		summary.setProducts(productsList);
		
		OrderTotalSummary orderSummary = orderService.calculateShoppingCart(summary, store, language);
		
		shoppingCart.setTotal(pricingService.getDisplayAmount(orderSummary.getTotal(), store));
		shoppingCart.setQuantity(shoppingCart.getShoppingCartItems().size());

		//save or update shopping cart
		shoppingCartService.saveOrUpdate(entity);
		
		/******************************************************/
		//TODO validate all of this
		
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

	private void addShoppingCartItem(ShoppingCart cart, ShoppingCartItem item, MerchantStore store) throws Exception{
		
		List<ShoppingCartItem> items = cart.getShoppingCartItems();
		boolean itemFound = false;
		if(items!=null) {
			for(ShoppingCartItem shoppingCartItem : items) {
				if(shoppingCartItem.getId()==item.getId()) {
					itemFound = true;
					if(CollectionUtils.isEmpty(shoppingCartItem.getShoppingCartAttributes()) 
							&& CollectionUtils.isEmpty(item.getShoppingCartAttributes())) {
						int qty = shoppingCartItem.getQuantity();
						qty = qty + item.getQuantity();
						break;
					} else {
						List<ShoppingCartAttribute> itemAttributes = item.getShoppingCartAttributes();
						List<ShoppingCartAttribute> originalitemAttributes = shoppingCartItem.getShoppingCartAttributes();
						if(!CollectionUtils.isEmpty(itemAttributes) && !CollectionUtils.isEmpty(originalitemAttributes)) {	
								if(itemAttributes.size()==originalitemAttributes.size()) {
									boolean attributesMatch = true;
									for(ShoppingCartAttribute itemAttribute : itemAttributes) {
										boolean singleAttributeMatch = false;
										for(ShoppingCartAttribute originalAttribute : originalitemAttributes) {
											if(originalAttribute.getOptionId()==itemAttribute.getOptionId()) {
												singleAttributeMatch = true;
											}
										}
										if(!singleAttributeMatch) {
											//Attributes are not identical
											createItemInShoppingCart(cart,item,store);
											attributesMatch = false;
											break;
										}
									}
									
									if(attributesMatch) {
										int qty = shoppingCartItem.getQuantity();
										qty = qty + item.getQuantity();
										break;
									}
									
								} else {
									
									createItemInShoppingCart(cart,item,store);
									
								}
						}//end if
					}//end else
					
				}
			}//end for
		}//end if
		if(!itemFound) {
			createItemInShoppingCart(cart,item,store);
		}
	}
	
	private void createItemInShoppingCart(ShoppingCart cart, ShoppingCartItem item, MerchantStore store) throws Exception {
		
		Product product = productService.getById(item.getProductId());
		
		if(product==null) {
			throw new Exception("Item with id " + item.getProductId() + " does not exist");
		}
		
		if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			throw new Exception("Item with id " + item.getProductId() + " does not belong to merchant " + store.getId());
		}
		
		item.setCode(cart.getCode());
		item.setId(product.getId());
		item.setName(product.getProductDescription().getName());
		
		
		List<ShoppingCartAttribute> cartAttributes = item.getShoppingCartAttributes();
		Set<ProductAttribute> attributes = product.getAttributes();
		List<ProductAttribute> pricingAttributes = null;
		if(!CollectionUtils.isEmpty(cartAttributes) && !CollectionUtils.isEmpty(attributes)) {
			List<ShoppingCartAttribute> savedAttributes = new ArrayList<ShoppingCartAttribute>();
			for(ShoppingCartAttribute cartAttribute : cartAttributes) {
				boolean attributeFound = false;
				for(ProductAttribute productAttribute : attributes) {
					if(productAttribute.getId().longValue()==cartAttribute.getAttributeId()){
						attributeFound = true;
						if(pricingAttributes==null) {
							pricingAttributes = new ArrayList<ProductAttribute>();
						}
						pricingAttributes.add(productAttribute);
						cartAttribute.setOptionName(productAttribute.getProductOption().getDescriptionsList().get(0).getName());
						cartAttribute.setOptionValue(productAttribute.getProductOptionValue().getDescriptionsSettoList().get(0).getName());
						cartAttribute.setOptionId(productAttribute.getProductOption().getId());
						cartAttribute.setOptionValueId(productAttribute.getProductOptionValue().getId());
					}
				}
				if(attributeFound) {
					savedAttributes.add(cartAttribute);
				}
			}
			
			item.setShoppingCartAttributes(savedAttributes);
			
			
		} else {//check if product has default attributes
			item.setShoppingCartAttributes(null);
			if(!CollectionUtils.isEmpty(attributes)) {
				
				for(ProductAttribute productAttribute : attributes) {
					
						if(productAttribute.getAttributeDefault()) {
					
							if(pricingAttributes==null) {
								pricingAttributes = new ArrayList<ProductAttribute>();
							}
							pricingAttributes.add(productAttribute);
							ShoppingCartAttribute cartAttribute = new ShoppingCartAttribute();
							cartAttribute.setOptionName(productAttribute.getProductOption().getDescriptionsList().get(0).getName());
							cartAttribute.setOptionValue(productAttribute.getProductOptionValue().getDescriptionsSettoList().get(0).getName());
							cartAttribute.setOptionId(productAttribute.getProductOption().getId());
							cartAttribute.setOptionValueId(productAttribute.getProductOptionValue().getId());
							
						}
				}
				
			}
		}
		
		ProductImage image = product.getProductImage();
		if(image!=null) {
			String imagePath = ImageFilePathUtils.buildProductImageFilePath(store, product.getSku(), image.getProductImage());
			item.setImage(imagePath);
		}
		FinalPrice finalPrice = pricingService.calculateProductPrice(product, pricingAttributes);
		item.setPrice(pricingService.getDisplayAmount(finalPrice.getFinalPrice(), store));
		item.setProductPrice(finalPrice.getFinalPrice());
		

		
	}
	
	private ShoppingCart convertFromEntity(com.salesmanager.core.business.shoppingcart.model.ShoppingCart shoppingCart, MerchantStore store) throws Exception {
		
		ShoppingCart cart = new ShoppingCart();
		cart.setCode(shoppingCart.getShoppingCartCode());
		Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> items = shoppingCart.getLineItems();
		if(items!=null) {
			for(com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem item : items) {
				
				ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
				shoppingCartItem.setCode(cart.getCode());
				shoppingCartItem.setProductCode(item.getProduct().getSku());
				shoppingCartItem.setId(item.getId());
				shoppingCartItem.setName(item.getProduct().getProductDescription().getName());
				shoppingCartItem.setPrice(pricingService.getDisplayAmount(item.getItemPrice(),store));
				shoppingCartItem.setQuantity(item.getQuantity());
				ProductImage image = item.getProduct().getProductImage();
				if(image!=null) {
					String imagePath = ImageFilePathUtils.buildProductImageFilePath(store, item.getProduct().getSku(), image.getProductImage());
					shoppingCartItem.setImage(imagePath);
				}
				Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem> attributes = item.getAttributes();
				if(attributes!=null) {
					List<ShoppingCartAttribute> cartAttributes = new ArrayList<ShoppingCartAttribute>();
					for(com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem attribute : attributes) {
						ShoppingCartAttribute cartAttribute = new ShoppingCartAttribute();
						cartAttribute.setId(attribute.getId());
						cartAttribute.setAttributeId(attribute.getProductAttributeId());
						cartAttribute.setOptionId(attribute.getProductAttribute().getProductOption().getId());
						cartAttribute.setOptionValueId(attribute.getProductAttribute().getProductOptionValue().getId());
						cartAttribute.setOptionName(attribute.getProductAttribute().getProductOption().getDescriptionsList().get(0).getName());
						cartAttribute.setOptionValue(attribute.getProductAttribute().getProductOptionValue().getDescriptionsList().get(0).getName());
						cartAttributes.add(cartAttribute);
					}
					shoppingCartItem.setShoppingCartAttributes(cartAttributes);
				}
			}
		}
		
		return cart;
		
	}
	
	private com.salesmanager.core.business.shoppingcart.model.ShoppingCart convertToEntity(ShoppingCart shoppingCart, MerchantStore store, Customer customer) throws Exception {
	
		com.salesmanager.core.business.shoppingcart.model.ShoppingCart cart = null;
		//if id >0 get the original from the database, override products
		if(shoppingCart.getId()>0) {
			cart = shoppingCartService.getByCode(shoppingCart.getCode(), store);
		} else { //else create a new one
			cart = new com.salesmanager.core.business.shoppingcart.model.ShoppingCart();
			cart.setShoppingCartCode(shoppingCart.getCode());
			cart.setMerchantStore(store);
			if(customer!=null) {
				cart.setCustomerId(customer.getId());
			}
			shoppingCartService.create(cart);
		}

		List<ShoppingCartItem> items = shoppingCart.getShoppingCartItems();
		Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> newItems = new HashSet<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem>();
		if(items!=null && items.size()>0) {
			for(ShoppingCartItem item : items) {
				
				Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem> cartItems = cart.getLineItems();
				if(cartItems!=null && cartItems.size()>0) {
					
					for(com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem dbItem : cartItems) {
						if(dbItem.getId().longValue()==item.getId()) {
							dbItem.setQuantity(item.getQuantity());
							//compare attributes
							Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem> attributes = dbItem.getAttributes();
							Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem> newAttributes = new HashSet<com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem>();
							List<ShoppingCartAttribute> cartAttributes = item.getShoppingCartAttributes();
							if(!CollectionUtils.isEmpty(cartAttributes)) {
								for(ShoppingCartAttribute attribute : cartAttributes) {
									for(com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem dbAttribute : attributes) {
										if(dbAttribute.getId().longValue()==attribute.getId()) {
											newAttributes.add(dbAttribute);
										}
									}
								}
								dbItem.setAttributes(newAttributes);
							} else {
								dbItem.setAttributes(null);
							}
							newItems.add(dbItem);
						}
					}
				} else {//create new item
					//TODO create new item
				}
			}//end for
		}//end if
		
		return cart;

		
	}
	
	private com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem createCartItem(com.salesmanager.core.business.shoppingcart.model.ShoppingCart cart, ShoppingCartItem shoppingCartItem, MerchantStore store) throws Exception {
		
		Product product = productService.getById(shoppingCartItem.getProductId());
		
		if(product==null) {
			throw new Exception("Item with id " + shoppingCartItem.getProductId() + " does not exist");
		}
		
		if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			throw new Exception("Item with id " + shoppingCartItem.getProductId() + " does not belong to merchant " + store.getId());
		}
		
		com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem item = new com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem(cart,product);
		item.setQuantity(shoppingCartItem.getQuantity());
		item.setItemPrice(shoppingCartItem.getProductPrice());
		
		//attributes
		List<ShoppingCartAttribute> cartAttributes = shoppingCartItem.getShoppingCartAttributes();
		if(!CollectionUtils.isEmpty(cartAttributes)) {
			Set<com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem> newAttributes = new HashSet<com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem>();
			for(ShoppingCartAttribute attribute : cartAttributes) {
				ProductAttribute productAttribute = productAttributeService.getById(attribute.getAttributeId());
				if(productAttribute!=null && productAttribute.getProduct().getId().longValue()==product.getId().longValue()) {
					com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem attributeItem = new com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem(item,productAttribute);
					if(attribute.getAttributeId()>0) {
						attributeItem.setId(attribute.getId());
					}
					newAttributes.add(attributeItem);
				}
				
			}
			item.setAttributes(newAttributes);
		}
		
		return item;
		
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
