(function($){ 
	$.search = (function(my){ 
		my.searchTerm = function(options, callBackSearch, callBackFacets){ 

			var url = options.url;
			var postCollectionVariableName = options.postCollectionVariableName;
			var postDataVariableName = options.postDataVariableName;
			var filter = null;
			var field = options.field;
			var highlights = options.highlights;
			var facets = null;
		
	
			if ($.isFunction(options.filter)) {
				filter = options.filter.call(this); 
			} else {
				filter = options.filter;
			}
			
			if ($.isFunction(options.facets)) {
				facets = options.facets.call(this); 
			} else {
				facets = options.facets;
			}
			
	
			obj = $(this);
		
			var search = field.val().toLowerCase();
			
			var queryStart = '{';
		
			//var query = '{\"text\" : {\"_all\" : \"' + search + '\" }}';
			var query = '\"query\":{\"text\" : {\"_all\" : \"' + search + '\" }}';
			if(filter!=null && filter!='') {
				query = '\"query\":{\"filtered\":{\"query\":{\"text\":{\"_all\":\"' + search + '\"}},' + filter + '}}';
			}
			if(highlights!=null && highlights!='') {
				query = query + ',' + highlights;
			}
			if(facets!=null && facets!='') {
				query = query + ',' + facets;
			}

			//query = query + ',' + '\"facets\" : { \"tags\" : { \"terms\" : {\"field\" : \"tags\"}}}'; 
			
			var queryEnd = '}';
			
			query = queryStart + query + queryEnd;
			
			//

			/**
			 * 	Query example
			"{
				\"query\":{
					\"filtered\":{
						\"query\":{
							\"text\":{\"_all\":\"beach\"}
						},
						\"filter\":{
							\"numeric_range\":{
								\"age\":{\"from\":\"22\",\"to\":\"45\",\"include_lower\":true,\"include_upper\":true}
							  }
						}
					 }
				},
				\"highlight\":{
					\"fields\":{
						\"description\":{}
					  }
				 },
				\"facets\" : { 
					\"tags\" : { 
						\"terms\" : {\"field\" : \"tags\"} 
					}
				 } 
			}"
			**/
			
			
	
			$.ajax({
	
				url: url,
		  		cache: false,
		  		type:"POST",
		  		dataType:"json",
		  		data:query,
		  		contentType:"application/json;charset=UTF-8",
				success: function(data) {
	
		 			if(data!=null && data.searchHits!=null && data.searchHits.length>0) {

						suggestions = new Array();
		         			
		         			//process response   
		
						$.each(data.searchHits, function(key, value) {
							suggestions.push(value.metaEntries);
		
						})
					
		
	
						if ($.isFunction(callBackSearch)) {
							callBackSearch.call(this, suggestions); 
	
						}
					
		 			}

					if(data!=null && data.facets!=null) {


						facets = new Array();
						
						$.each(data.facets, function(key, value) {
							facets.push(value);
		
						})
						
						if ($.isFunction(callBackFacets)) {
							callBackFacets.call(this, facets); 
	
						}

					 }
			},
			faillure: function() {alert('request failed');}
			});
			//return suggestions;
		}; return my; 
	})({}); 
})(jQuery);