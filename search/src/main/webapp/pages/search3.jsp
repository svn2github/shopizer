<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>search</title>






	<link href="<c:url value="/resources/jqueryui/1.8/themes/base/jquery.ui.all.css" />" rel="stylesheet"  type="text/css" />	
	<script type="text/javascript" src="<c:url value="/resources/jquery/jquery-1.5.1.min.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/jquery/jquery-ui-1.8.14.custom.min.js" />"></script>

<style type="text/css"> 
.ui-autocomplete {
    width:80px;
    left:0px;
}

.ui-menu-item {
    list-style: none; 

}
 </style> 
	
<script type="text/javascript">
			$(function(){
				
				var collection = "m";
				//var objectName = "keyword"
				
				
				
				var filter = '\"filter\" : {\"numeric_range\" : {\"age\" : {\"from\" : \"25\",\"to\" : \"45\",\"include_lower\" : true,\"include_upper\" : true}}}'; 

			        		
			   				
				//attach autocomplete
				$("#search").autocomplete({
					
					//define callback to format results
					source: function(req, add){
					

						var search = $("#search").val();
						
						var query = '{\"wildcard\" : {\"keyord\" : \"' + search + '*\" }}';
						
						if(filter!=null && filter!='') {
						
							query = '{\"filtered\" : {\"query\" : {\"wildcard\" : {\"keyword\" : \"' + search + '*\" }},' + filter + '}}';
							
						}




						var url = "/mvc-showcase/search/inline2/" + collection;

                		$.ajax({

              			  	url: url,
            			  	cache: false,
            			  	type:"POST",
            			  	dataType:"json",
            			  	data:query,
            			  	contentType:"application/json;charset=UTF-8",
							success: function(data) {

						 		//alert(data.searchList);
						
						 		if(data!=null && data.searchList && data.searchList.length>0) {
			                
			                 		var suggestions = [];
			                 		//process response   
			                 		$.each(data.searchList, function(i, val){suggestions.push(val);})
									add(suggestions);
						 		}
							},
							faillure: function() {alert('fail');}
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
				
				//add click handler to friends div
				$("#friends").click(function(){
					
					//focus 'to' field
					//$("#to").focus();
				});
				
				//add live handler for clicks on remove links
				$(".remove", document.getElementById("friends")).live("click", function(){
				
					//remove current friend
					//$(this).parent().remove();
					
					//correct 'to' field position
					//if($("#friends span").length === 0) {
					//	$("#to").css("top", 0);
					//}				
				});				
			});
		</script>

	
	
	
</head>
<body>




<DIV class=ui-widget><LABEL for="Search">Search: </LABEL><INPUT id="search" size="40"></DIV>


</body>
</html>