/**
 * Wrapper for autocomplete plug in for personalization search
 */

(function($){
 $.fn.searchAutocomplete = function(options) {
	 
	var defaults = {
			   //autocomplete options
			   minlength: 1,
			   delay: 300,
			   disabled: false,
			   postDataVariableName: '_search',
			   postDataVariableName: 'item',
			   mode:'rest'
	};
	
	//url
	//postCollectionVariableName
	//postDataVariableName
	//filter
	
	//mode (rest - querystring)
	
	var options = $.extend(defaults, options);//set options to autocomplete
	
		
	var url = options.url;
	var postCollectionVariableName = options.postCollectionVariableName;
	var postDataVariableName = options.postDataVariableName;
	var mode = options.mode;
	var filter = null;
	var select = null;
	

	if ($.isFunction(options.filter)) {
		filter = options.filter.call(this); 
	} else {
		filter = options.filter;
	}

	obj = $(this); 
	var id = obj.attr('id');

	return this.autocomplete({


		//define callback to format results
		source: function(req, add){


			var search = $('#' + id).val().toLowerCase(); 
			
			
			var query = '{\"wildcard\" : {\"keyword\" : \"' + search + '*\" }}';
			if(filter!=null && filter!='') {
				query = '{\"filtered\" : {\"query\" : {\"wildcard\" : {\"keyword\" : \"' + search + '*\" }},' + filter + '}}';
			}


    		$.ajax({

  			  	url: url,
			  	cache: false,
			  	type:"POST",
			  	dataType:"json",
			  	data:query,
			  	contentType:"application/json;charset=UTF-8",
				success: function(data) {
			 	if(data!=null && data.inlineSearchList && data.inlineSearchList.length>0) {
                 		var suggestions = [];
                 		//process response   
                 		$.each(data.inlineSearchList, function(i, val){suggestions.push(val);})
						add(suggestions);
			 		}
				},
				faillure: function() {alert('request failed');}
			});
		},
		
		//define select handler
		select: function(e, ui) {


			alert('select');
			
			var search = $("#search").val();
			
			//collection
			
			//
			
			
			
			//query on a selection
			
			//create formatted friend
			//var friend = ui.item.value,
				//span = $("<span>").text(friend),
				//a = $("<a>").addClass("remove").attr({
				//	href: "javascript:",
				//	title: "Remove " + friend
				//}).text("x").appendTo(span);
			
			//add friend to friend div
			//span.insertBefore("#to");
		},
		
		//define select handler
		change: function() {


			//alert('change');
			
			//prevent 'to' field being updated and correct position
			//$("#to").val("").css("top", 2);
		}
	});
 };
})(jQuery);