


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
			
			//core properties
			var sku = $(this).attr("productId");
			var qty = '#qty-productId-'+ sku;
			var quantity = $(qty).val();
			if(!quantity || quantity==null || quantity==0) {
				quantity = 1;
			}

			var formId = '#input-' + sku +' :input';
			var $inputs = $(formId); 
			
			
			if($inputs.length>0) {//check for attributes
			
				var values = {}; //TODO ShoppingCartAttribute
				$inputs.each(function() { //properties
					if($(this).hasClass('attribute')) {
					   //TODO remove class
					   if($(this).hasClass('required') && !$(this).is(':checked')) {
								//TODO add class
						   		//$(this).parent().css('border', '1px solid red'); 
						   
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
							    //TODO add class
							   	//$(this).css('border', '1px solid red'); 
							   
							}
	
						   if($(this).val()) {
						       values[this.name] = $(this).val(); 
					       }
						}
					}
				});
			}

	    });
		
		
		
		

	});