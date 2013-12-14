
 

	$(function(){
		
		initBindings();
		initMiniCart();

	});
	
	function initMiniCart() {
		
		
		var cart = $.cookie('cart'); //should be [storecode_cartid]

		if(cart!=null) {
			var code = new Array();
			code = cart.split('_');
			if(code[0]==getMerchantStore()) {
				alert(code[1]);
				shoppingCart = getShoppingCart(code[1]);
				if(shoppingCart==null) {
					$.cookie('cart',null, { expires: 1 ,path: '/'});
				}
			}
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
		
		//alert('product ' + sku + ' added to cart');
		var merchantStore = getMerchantStore();
		//var cartCode = 'cart_' + merchantStore;
		var cart = $.cookie('cart'); //should be [storecode_cartid]
		
	
		
		var code = new Array();
		
		if(cart!=null) {
			code = cart.split('_');
		}
		
		/**
		 * shopping cart code identifier is <cart>_<storeId>
		 * need to check if the cookie is for the appropriate store
		 */
		
		//cart item
		var prefix = "{";
		var suffix = "}";
		//var shoppingCartItem = '"code":' + "11" + ',';
		var shoppingCartItem = '';
		if(cart!=null && cart != '') {
			shoppingCartItem = '"code":' + '"' + code[1] + '"'+',';
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
		
		var scItem = prefix + shoppingCartItem + suffix

		alert(scItem);
		
		$.ajax({  
			 type: 'POST',  
			 url: getContextPath() + '/shop/addShoppingCartItem.html',  
			 data: scItem, 
			 contentType: 'application/json;charset=utf-8',
			 dataType: 'json', 
			 error: function(e) { 
				alert('failure'); 
				 
			 },
			 success: function(cart) {
				
			     cartDetails = merchantStore + '_' + cart.code;
			     $.cookie('cart',cartDetails, { expires: 360 ,path: '/'});
			     if(cart.message!=null) { 
			    	 //TODO error message
			     } else { 
			    	 
			    	 cartInfoLabel(cart);
			     }
			     //TODO Hogan template
				 //for (var i = 0; i < cart.shoppingCartItems.length; i++) {
				//		var shoppingCartItem = cart.shoppingCartItems[i];
				//		var item = '<tr id="' + shoppingCartItem.productId + '" class="cart-product">';
				//		item = item + '<td>';
				//		if(shoppingCartItem.image!=null){
				//			item = item + '<img width="40" height="40" src="' + getContextPath + '/' + shoppingCartItem.image + '">';
				//		} else {
				//			item = item + '&nbsp;';
				//		}
				//		item = item + '</td>';
				//		item = item + '<td>' + shoppingCartItem.name + '</td>';
				//		item = item + '<td>' + shoppingCartItem.price + '</td>';
				//		item = item + '<td><button productid="' + shoppingCartItem.productId + '" class="close removeProductIcon">x</button></td>';
				//		item = item + '</tr>';
						
				//		$('#shoppingcartProducts').append(item);
				// }
				 
				 displayShoppigCartItems(cart,'#shoppingcartProducts');
				 displayTotals(cart);
			 } 
		});
		
	}
	
function removeLineItem(lineItemId){
	alert(this.attr("action"));
	$( "#shoppingCartRemoveLineitem_"+lineItemId).submit();		
}

function updateLineItem(lineItemId,actionURL){
	$("#shoppingCartLineitem_"+lineItemId).attr('action', actionURL);
	$( "#shoppingCartLineitem_"+lineItemId).submit();	
}

function displayMiniCart(){
	$('#cart-box').addClass('loading-indicator-overlay');/** manage manually cart loading**/
	$('#cartShowLoading').show();
	$.ajax({  
		 type: 'GET',  
		 url: getContextPath() + '/shop/displayMiniCart.html',  
		 error: function(e) { 
			 $('#cart-box').removeClass('loading-indicator-overlay');/** manage manually cart loading**/
			 $('#cartShowLoading').hide();
			 //nothing
			 
		 },
		 success: function(miniCart) {
			 if($.isEmptyObject(miniCart)){
				 emptyCartLabel();
			 }
			 else{
				 //$('#shoppingcartProducts').html(prepareMiniCartLineItemsData(miniCart));
				 showCartTotal(miniCart);
				 displayShoppigCartItems(miniCart,'#shoppingcartProducts');
				 displayTotals(miniCart);
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
function removeItemFromMinicart(lineItemId){
	$.ajax({  
		 type: 'GET',  
		 url: getContextPath() + '/shop/miniCart/removeShoppingCartItem.html?lineItemId='+lineItemId,  
		 error: function(e) { 
			// do nothing
			 
		 },
		 success: function(miniCart) {
			 //$('#shoppingcartProducts').html(prepareMiniCartLineItemsData(miniCart));
			 showCartTotal(miniCart);
			 displayShoppigCartItems(miniCart,'#shoppingcartProducts');
			 displayTotals(miniCart);
		} 
	});
}

function getShoppingCart(code){
	$.ajax({  
		 type: 'GET',  
		 url: getContextPath() + '/shop/displayMiniCartByCode.html?shoppingCartCode='+code,  
		 error: function(e) { 
			// do nothing
			console('error while getting cart');
			 
		 },
		 success: function(cart) {
			 if(cart!=null) {
				return cart; 
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
			item = item + '<td>' + shoppingCartItem.name + '</td>';
			item = item + '<td>' + shoppingCartItem.price + '</td>';
			var onClickEvent = "removeItemFromMinicart(" + shoppingCartItem.id + ");";
			item = item + '<td><button productid="' + shoppingCartItem.productId + '" class="close removeProductIcon" onclick="' + onClickEvent + '">x</button></td>';
			item = item + '</tr>';
			
			
			$(div).append(item);
	 }
	
	
}

function displayTotals(cart) {
	
	$('#total-box').html(cartSubTotal(cart));

}


