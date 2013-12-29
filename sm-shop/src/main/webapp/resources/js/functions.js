
 

	$(function(){
		
		initBindings();
		initMiniCart();

	});
	
	function initMiniCart() {
		
		var cartCode = getCartCode();
		if(cartCode!=null) {
			displayMiniCartSummary(cartCode);
		}
		
	}
	
	function initBindings() {
		
		/** add to cart **/
		$(".addToCart").click(function(){
			addToCart($(this).attr("productId"));
	    });
		
	}
	
	/**
	 * Function used for adding a product to the Shopping Cart
	 */
	function addToCart(sku) {
		
		var qty = '#qty-productId-'+ sku;
		var quantity = $(qty).val();
		if(!quantity || quantity==null || quantity==0) {
			quantity = 1;
		}

		var formId = '#input-' + sku +' :input';
		var $inputs = $(formId); 
		
		var values = {};
		if($inputs.length>0) {//check for attributes

			$inputs.each(function() { //attributes
				if($(this).hasClass('attribute')) {
				   //TODO remove class
				   if($(this).hasClass('required') && !$(this).is(':checked')) {
					   		$(this).parent().css('border', '1px solid red'); 
				    }
			        if($(this).is(':checkbox')) {
						if($(this).is(':checked') ) {
							values[this.name] = $(this).val(); 
						}
					} else if ($(this).is(':radio')) {
						if($(this).is(':checked') ) {
							values[this.name] = $(this).val(); 
						}
					} else {
					   if($(this).hasClass('required') && !$(this).val()) {
						   	$(this).css('border', '1px solid red'); 
						   
						}
					   if($(this).val()) {
					       values[this.name] = $(this).val(); 
				       }
					}
				}
			});
		}

		var cartCode = getCartCode();

		
		/**
		 * shopping cart code identifier is <cart>_<storeId>
		 * need to check if the cookie is for the appropriate store
		 */
		
		//cart item
		var prefix = "{";
		var suffix = "}";
		var shoppingCartItem = '';

		if(cartCode!=null && cartCode != '') {
			shoppingCartItem = '"code":' + '"' + cartCode + '"'+',';
		}
		var shoppingCartItem = shoppingCartItem + '"quantity":' + quantity + ',';
		var shoppingCartItem = shoppingCartItem + '"productId":' + sku;
		
		
		var attributes = null;
		//cart attributes
		if(values.length>0) {
			if(values.length>1 ) {
				attributes = '[';
			}
			for (var i = 0; i < values.length; i++) {
				var shoppingAttribute= prefix + '"attributeId":' + values[i] + suffix ;
				if(values.length>1 && i < values.length-1){
					shoppingAttribute = shoppingAttribute + ',';
				}
				attributes = attributes + shoppingAttribute;
			}
			if(values.length>1 ) {
				attributes = attributes + ']';
			}
		}
		
		if(attributes!=null) {
			shoppingCartItem = shoppingCartItem + ',"shoppingCartAttributes:"' + attributes;
		}
		
		var scItem = prefix + shoppingCartItem + suffix;

		/** debug add to cart **/
		//console.log(scItem);
		
		$.ajax({  
			 type: 'POST',  
			 url: getContextPath() + '/shop/addShoppingCartItem.html',  
			 data: scItem, 
			 contentType: 'application/json;charset=utf-8',
			 dataType: 'json', 
			 cache:false,
			 error: function(e) { 
				console.log('Error while adding to cart');
				alert('failure'); 
				 
			 },
			 success: function(cart) {

				 var cartCode = buildCartCode(cart.code);
			     $.cookie('cart',cartCode, { expires: 1024, path:'/' });
			     if(cart.message!=null) { 
			    	 //TODO error message
			    	 console.log('Error while adding to cart ' + cart.message);
			     }
				 
				 displayShoppigCartItems(cart,'#shoppingcartProducts');
				 displayTotals(cart);
			 } 
		});
		
	}
	
function removeLineItem(lineItemId){
	$( "#shoppingCartRemoveLineitem_"+lineItemId).submit();		
}

function updateLineItem(lineItemId,actionURL){
	$("#shoppingCartLineitem_"+lineItemId).attr('action', actionURL);
	$( "#shoppingCartLineitem_"+lineItemId).submit();	
}

//update full cart
function updateCart(cartDiv) {
	var inputs = $(cartDiv).find('.quantity');
	var cartCode = getCartCode();
	if(inputs !=null && cartCode!=null) {
		var items = new Array();
		for(var i = 0; i< inputs.length; i++) {
			var item = new Object();
			
			
			
			var qty = inputs[i].value;
			
			console.log('Qty ' + qty);
			
			var id = inputs[i].id;
			item.productCode = id;
			item.quantity = qty;
			item.code=cartCode;
			items[i] = item;
		}
		//update cart
		json_data = JSON.stringify(items);
		
		
		$.ajax({  
			 type: 'POST',  
			 url: getContextPath() + '/shop/updateShoppingCartItem.html',
			 data: json_data,
			 contentType: 'application/json;charset=utf-8',
			 dataType: 'json', 
			 cache:false,
			 error: function(e) { 
				 console.log('error ' + e);
			 },
			 success: function(response) {
				 console.log(response.status);
			} 
		});
		
		
		
		
	}	
}

function displayMiniCart(){
	var cartCode = getCartCode();
	if(cartCode==null) {
		return;
	}
	$('#shoppingcartProducts').html('');
	$('#cart-box').addClass('loading-indicator-overlay');/** manage manually cart loading**/
	$('#cartShowLoading').show();

	$.ajax({  
		 type: 'GET',  
		 url: getContextPath() + '/shop/displayMiniCartByCode.html?shoppingCartCode='+cartCode,  
		 cache:false,
		 error: function(e) { 
			 $('#cart-box').removeClass('loading-indicator-overlay');/** manage manually cart loading**/
			 $('#cartShowLoading').hide();
			 console.log('error ' + e);
			 //nothing
			 
		 },
		 success: function(miniCart) {
			 if($.isEmptyObject(miniCart)){
				 emptyCartLabel();
			 }
			 else{
				 displayShoppigCartItems(miniCart,'#shoppingcartProducts');//cart content
				 displayTotals(miniCart);//header
			 }
			 $('#cart-box').removeClass('loading-indicator-overlay');/** manage manually cart loading**/
			 $('#cartShowLoading').hide();
		} 
	});
}



 /**
  * JS function responsible for removing give line item from
  * the Cart.
  * For more details see MiniCartController.
  * 
  * Controller will return JSON as response and it will be parsed to update
  * mini-cart section.
  * @param lineItemId
  */
function removeItemFromMinicart(cartCode, lineItemId){
	$.ajax({  
		 type: 'GET',
		 cache:false,
		 url: getContextPath() + '/shop/removeMiniShoppingCartItem.html?lineItemId='+lineItemId + '&shoppingCartCode=' + cartCode,  
		 error: function(e) { 
			 console.log('error ' + e);
			 
		 },
		 success: function(miniCart) {
			 if(miniCart==null) {
				 emptyCartLabel();
			 } else {
				 if(miniCart.shoppingCartItems!=null) {
					 displayShoppigCartItems(miniCart,'#shoppingcartProducts');
					 displayTotals(miniCart);
				 } else {
					 emptyCartLabel();
				 }
			 }
		} 
	});
}

function displayMiniCartSummary(code){
	$.ajax({  
		 type: 'GET',  
		 url: getContextPath() + '/shop/displayMiniCartByCode.html?shoppingCartCode='+code,  
		 error: function(e) { 
			// do nothing
			console('error while getting cart');
			 
		 },
		 success: function(cart) {
			 if(cart==null) {
					emptyCartLabel();
					$.cookie('cart',null, { expires: 1, path:'/' });
			 } else {
				 displayTotals(cart);
			 }
		} 
	});
}



function showCartTotal(cart){
	$("#mini-cart-total-block").html(cart.total);
	$("#checkout-total-plus").html(cart.total);
	
}



function viewShoppingCartPage(){
	window.location.href=getContextPath() + '/shop/shoppingCart.html';
	
}

 
function displayShoppigCartItems(cart, div) {
	
    //TODO Hogan template
	 $(div).html('');
	 if(cart.shoppingCartItems==null) {
		 emptyCartLabel();
		 return;
	 }
	 
	 $('#cartMessage').hide();
	 $('#shoppingcart').show();

	 var cartCode = getCartCode();
	 for (var i = 0; i < cart.shoppingCartItems.length; i++) {
			var shoppingCartItem = cart.shoppingCartItems[i];
			var item = '<tr id="' + shoppingCartItem.productId + '" class="cart-product">';
			item = item + '<td>';
			if(shoppingCartItem.image!=null){
				item = item + '<img width="40" height="40" src="' + getContextPath() + '/' + shoppingCartItem.image + '">';
			} else {
				item = item + '&nbsp;';
			}
			item = item + '</td>';
			item = item + '<td>' + shoppingCartItem.quantity + '</td>';
			item = item + '<td>' + shoppingCartItem.name + '</td>';
			item = item + '<td>' + shoppingCartItem.price + '</td>';
			var onClickEvent = "removeItemFromMinicart('" + cartCode + "','" + shoppingCartItem.id + "');";
			item = item + '<td><button productid="' + shoppingCartItem.productId + '" class="close removeProductIcon" onclick="' + onClickEvent + '">x</button></td>';
			item = item + '</tr>';

			$(div).append(item);
	 }

}

function displayTotals(cart) {
	if(cart.quantity==0) {
		emptyCartLabel();
	} else {
		cartInfoLabel(cart);
		$('#total-box').html(cartSubTotal(cart));
	}


}


/** returns the cart code **/
function getCartCode() {
	
	var cart = $.cookie('cart'); //should be [storecode_cartid]
	var code = new Array();
	
	if(cart!=null) {
		code = cart.split('_');
		if(code[0]==getMerchantStoreCode()) {
			return code[1];
		}
	}
}

function buildCartCode(code) {
	var cartCode = getMerchantStoreCode() + '_' + code;
	return cartCode;
}


