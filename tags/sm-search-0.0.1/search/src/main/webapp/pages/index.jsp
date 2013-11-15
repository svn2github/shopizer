<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>index</title>
	<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />	
	<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/jqueryform/2.8/jquery.form.js" />"></script>
	
	<script type="text/javascript">
	$(document).ready(function() {
		
		
		//var jsonData = '{\"id\":\"444\",\"author\":\"Roger Bégin\",\"description\":\"Book on beautifull nature\", \"keywords\":[\"Roger\",\"Book\",\"nature\"]}';
		//var jsonData = '{\"id\":\"445\",\"author\":\"Carl Samson\",\"description\":\"Best practices on comet usage\", \"keywords\":[\"Carl Samson\",\"Book\",\"comet\"]}';
		//var jsonData = '{\"id\":\"446\",\"author\":\"Men without hats\",\"description\":\"Musical instruments book\", \"keywords\":[\"Musical\",\"Instruments\",\"Bookee\"]}';

		//var bookId = 444;	


		var jsonData = '{\"id\":\"1\",\"name\":\"Iron chair\",\"description\":\"Zen iron chair with blue cussions, nice for a beautifull day at the beach\", \"tags\":[\"chair\",\"iron\",\"iron chair\",\"zen chair\"]}';
		//var id = 1;

		//alert(jsonData);
		
		//$.ajax({
			  //url: "/mvc-showcase/index/json",
			  //cache: false,
			  //type:"GET",
			  //data:{ json: 'test' },
			  //success: function(data) {
				//  alert('success');
			  //},
			  //faillure: function() {alert('fail');}
		//});
		
		$.ajax({
			  //url: "/mvc-showcase/index/books/book/" + bookId,

			  url: "/mvc-showcase/index/delete/product_en/product/12/",
			  cache: false,
			  type:"GET",
			  dataType:"json",
			  data:jsonData,
			  contentType:"application/json;charset=UTF-8",
			  success: function(data) {
				  alert('success ' + data);
			  },
			  faillure: function() {alert('fail');}
		});
		
		//alert('done');
		
 	});
	</script>
	
	
	
</head>
<body>


Index


</body>
</html>