


	$(function(){
		
		if($('#productPriceAmount')) {
			$('#productPriceAmount').blur(function() {
				$('#help-price').html(null);
				$(this).formatCurrency({ roundToDecimalPlace: 2, eventOnDecimalsEntered: true, symbol: ''});
			})
			.keyup(function(e) {
					var e = window.event || e;
					var keyUnicode = e.charCode || e.keyCode;
					if (e !== undefined) {
						switch (keyUnicode) {
							case 16: break; // Shift
							case 17: break; // Ctrl
							case 18: break; // Alt
							case 27: this.value = ''; break; // Esc: clear entry
							case 35: break; // End
							case 36: break; // Home
							case 37: break; // cursor left
							case 38: break; // cursor up
							case 39: break; // cursor right
							case 40: break; // cursor down
							case 78: break; // N (Opera 9.63+ maps the "." from the number key section to the "N" key too!) (See: http://unixpapa.com/js/key.html search for ". Del")
							case 110: break; // . number block (Opera 9.63+ maps the "." from the number block to the "N" key (78) !!!)
							case 190: break; // .
							default: $(this).formatCurrency({ colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: -1, eventOnDecimalsEntered: true, symbol: ''});
						}
					}
				})
			.bind('decimalsEntered', function(e, cents) {
				if (String(cents).length > 2) {
					var errorMsg = priceFormatMessage + ' (0.' + cents + ')';
					$('#help-price').html(errorMsg);
				}
			});
		}
		
		
		/**
		 * Function used for adding a product to the Shopping Cart
		 */
		
		$(".addToCart").click(function(){ 
			

			var cart = $.cookie( 'cart' );
			//core properties
			var sku = $(this).attr("productId");
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
			
			alert('product ' + sku + ' added to cart');
			var merchantStore = getMerchantStore();
			var cartCode = 'cart_' + merchantStore;
			var code = $.cookie(cartCode);
			
			/**
			 * shopping cart code identifier is <cart>_<storeId>
			 * need to check if the cookie is for the appropriate store
			 */

			//cart item
			var prefix = "{";
			var suffix = "}";
			var shoppingCartItem = '"code":' + code + ',';
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
				 contentType: 'application/json', 
				 dataType: 'json', 
				 error: function() { 
				    alert('failure'); 
				 },
				 success: function(cart) {  
				     alert("Success: " + cart);
				     $.cookie(code,cart.code, { expires: 360 ,path: '/'});
				     if(cart.message!=null) { 
				    	 //TODO error message
				     } else { 
				    	 var labelItem = '<s:message code="label.generic.item" text="item" />';
				    	 if(cart.quantity>1) { 
				    		 labelItem = '<s:message code="label.generic.items" text="item"s />';
				    	 }
				    	 $("#cartinfo").html('<span id="cartqty">(' + cart.quantity + ' ' + labelItem + ')</span><span id="cartprice">' + cart.total + '</span>');
				     }
					 for (var i = 0; i < cart.shoppingCartItems.length; i++) {
							var shoppingCartItem = cart.shoppingCartItems[i];
							var item = '<tr id="' + shoppingCartItem.productId + '" class="cart-product">';
							item = item + '<td>';
							if(cart.image!=null){
								item = item + '<img width="40" height="40" src="' + shoppingCartItem.image + '">';
							} else {
								item = item + '&nbsp;';
							}
							item = item + '</td>';
							item = item + '<td>' + shoppingCartItem.name + '</td>';
							item = item + '<td>' + shoppingCartItem.price + '</td>';
							item = item + '<td><button productid="' + shoppingCartItem.productId + '" class="close removeProductIcon">x</button></td>';
							item = item + '</tr>';
							$('#shoppingcartProducts').append(item);
					 }
				 } 
			}); 

	    });
		
		
		
		

	});