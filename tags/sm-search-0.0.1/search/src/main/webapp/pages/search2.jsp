<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>search</title>

 

    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/blueprint/screen.css" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/blueprint/print.css" type="text/css" media="print">
    <!--[if lt IE 8]><link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->



	<link href="<c:url value="/resources/jqueryui/1.8/themes/base/jquery.ui.all.css" />" rel="stylesheet"  type="text/css" />	
	<script type="text/javascript" src="<c:url value="/resources/jquery/jquery-1.5.1.min.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/jquery/jquery-ui-1.8.14.custom.min.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/search.autocomplete.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/search.js" />"></script>


	
<script type="text/javascript">

$(document).ready(function() {   

		
	$('#search').searchAutocomplete({
		  url: '/mvc-showcase/search/autocomplete/m',
		  filter: function() { 
			return '\"filter\" : {\"numeric_range\" : {\"age\" : {\"from\" : \"22\",\"to\" : \"45\",\"include_lower\" : true,\"include_upper\" : true}}}';
		  }
      });
      $('#searchForm').submit(function() {

			$('#profiles').html('');

			$.search.searchTerm({
				  	field: $('#search'),
				  	url: '/mvc-showcase/search/profile_m',
				  	highlights: '\"highlight\":{\"fields\":{\"description\":{\"pre_tags\" : [\"<strong>\"], \"post_tags\" : [\"</strong>\"]},\"tags\":{\"pre_tags\" : [\"<strong>\"], \"post_tags\" : [\"</strong>\"]}}}',
				  	filter: function() { 
						return '\"filter\" : {\"numeric_range\" : {\"age\" : {\"from\" : \"22\",\"to\" : \"45\",\"include_lower\" : true,\"include_upper\" : true}}}';
				  	}
				  },
				  function(suggestions) {//handle responses
					  var i =1;
					  searchresults = '';
					  $.each(suggestions, function() {
							var s = this.source;
							var h = this.highlightFields;
							var description = s.description;
							
							if(h != null) {
								if(h.description!=null) {
									//alert(h.description.fragments[0]);
									description = h.description.fragments[0];
								}
								if(h.tags!=null) {
									//alert(h.tags.fragments[0]);
								}
							} 
							var c = 'span-5';
							if(i%4==0) {
								c = c + ' last';
							}
							searchresults = searchresults + '<div class="' + c + '" style="height:250px;"><div>' + s.id +'<br/>' + s.age + '<br/>' + description + '</div></div>';
							i++;
				  	  });
					  $('#profiles').html(searchresults);
				  },

				  function(facets) {//handle facets
					  
					  var facet = '';
					  
					  $.each(facets, function() {
						  
						  var name = this.name;
						  facet = facet + '<ul><strong>' + name + '</strong>';
						  var entries = this.entries;
						  $.each(entries, function() {
							  
							  facet = facet + '<li>' + this.name + ' (' + this.count + ')</li>';
							  
						  });
						  
						  facet = facet + '<ul><br/><br/>';
						  
						  
					  });
					  
					  $('#facets').html(facet);
					  
				  }
			);


			return false; 
	});

});




</script>

<style type="text/css"> 
.ui-autocomplete {
    width:80px;
    left:0px;
}

.ui-menu-item {
    list-style: none; 

}

.ui-widget input, .ui-widget select, .ui-widget textarea, .ui-widget button {
    font-size: 18px;
}

.container .label {

     font-family: Verdana, Arial, Helvetica, sans-serif; 
     font-size : 160%;

 }
          
          
.textfield {

      font-family: Verdana, Arial, Helvetica, sans-serif; 
	font-size: 18px; 
      color: #000000; 
      background-color: #FFFFCC; 
      padding: 2px; 
      height: 30px; 
      border: 1px solid #7F9DB9; 

}

.medium {
      width: 300px; 
}

.select {

                                    font-family: Verdana, Arial, Helvetica, sans-serif; 
                                    font-size: 18px; 
                                    color: #000000; 
                                    background-color: #FFFFCC; 
                                    padding: 2px; 
                                    height: 30px; 
                                    border: 1px solid #7F9DB9; 



 

}

            .short {
                                    width: 120px; 
            }

 </style>
	
	
</head>
<body>

<div class="container">

<div class="span-20 last">

	<div class="span-5 label">
	Looking for :
	</div>
	<div class="span-15 last">
	<select name="lookingfora" id="lookingfora" class="select short">
		<option>m</option>
		<option>f</option>
	</select>
	</div>
	<br/><br/>

	<div class="span-5 label">	
	Age :
	</div>
	<div class="span-15 last">
	<select name="lookingfora" id="lookingfora" class="select short">
		<option>18-24</option>
		<option>25-39</option>
		<option>30-34</option>
		<option>35-39</option>
		<option>40-44</option>
		<option>45-49</option>
		<option>50-100</option>
	</select>
	</div>
	<br/><br/>
	<form id="searchForm">
		<DIV class=ui-widget>
		<div class="span-5 label">	
		Search :
		</div>
		<div class="span-15 last">
		<INPUT id="search" size="40" class="textfield medium"><input type="submit" value="Search">
		</div>
		</DIV>
	</form>
</div>

<br/><br/><br/><br/><br/>

<div class="span-20 last">

<div id="profiles"></div>

</div>

</div>
</body>
</html>