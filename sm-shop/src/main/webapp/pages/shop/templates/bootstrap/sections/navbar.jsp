<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>
<%@ taglib uri="/WEB-INF/shopizer-functions.tld" prefix="display" %> 

<style>


.tt-dropdown-menu,
.gist {
  text-align: left;
}

/**width of the box (396 - 30) fs 24**/
.typeahead,
.tt-query,
.tt-hint {
  width: 250px;
  height: 15px;
  padding: 8px 12px;
  font-size: 15px;
  line-height: 30px;
  border: 2px solid #ccc;
  outline: none;
}

.twitter-typeahead {
	vertical-align:top;
}

.typeahead {
  background-color: #fff;
}

.typeahead:focus {
  border: 2px solid #0097cf;
}

.tt-query {
  -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
     -moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
          box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
}

.tt-hint {
  color: #999
}

/** dropdown width 422 **/
.tt-dropdown-menu {
  width: 276px;
  margin-top: 12px;
  padding: 8px 0;
  background-color: #fff;
  border: 1px solid #ccc;
  border: 1px solid rgba(0, 0, 0, 0.2);
  -webkit-border-radius: 8px;
     -moz-border-radius: 8px;
          border-radius: 8px;
  -webkit-box-shadow: 0 5px 10px rgba(0,0,0,.2);
     -moz-box-shadow: 0 5px 10px rgba(0,0,0,.2);
          box-shadow: 0 5px 10px rgba(0,0,0,.2);
}

.tt-suggestion {
  padding: 3px 20px;
  font-size: 18px;
  line-height: 24px;
}

.tt-suggestion.tt-is-under-cursor {
  color: #fff;
  background-color: #0097cf;

}

.tt-suggestion p {
  margin: 0;
}

/**.gist {
  font-size: 14px;
}**/

/* example specific styles */
/* ----------------------- */

#searchGroup .tt-suggestion {
  padding: 8px 20px;
}

/**
#searchGroup .tt-suggestion + .tt-suggestion {
  border-top: 1px solid #ccc;
}
**/

#searchGroup .repo-language {
  float: right;
  font-style: italic;
}

#searchGroup .repo-name {
  font-weight: bold;
}

#searchGroup .repo-description {
  font-size: 14px;
}

#searchButton {
  height: 32px;
}



</style>

<!--<script src="<c:url value="/resources/js/search.autocomplete.js" />"></script>--> 

<script src="<c:url value="/resources/js/hogan.js" />"></script>
<script src="<c:url value="/resources/js/typeahead.min.js" />"></script>

<script type="text/javascript">

$(document).ready(function() { 

	//var autoCompleteUrl = '/shop/services/search/' + getMerchantStoreCode() + '/' + getLanguageCode() + '/autocomplete.html';
	//$('#search').searchAutocomplete({
	//	url: '<c:url value="/shop/services/search/${requestScope.MERCHANT_STORE.code}/${requestScope.LANGUAGE.code}/autocomplete.html"/>'
    // });
	
	//jQuery('#search').typeahead({
	    //source : function(query, process) {
	    	//alert(query);
	    	//alert(response);
	        //jQuery.ajax({
	        //    url : "urltobefetched",
	        //    type : 'GET',
	        //    data : {
	        //        "query" : query
	        //    },
	        //    dataType : 'json',
	        //    success : function(json) {
	        //        process(json);
	        //    }
	        //});
	    //},
	    //minLength : 1,
	//});
	
	//$('#search').typeahead([{                              
	    //name: 'Search',
	    //valueKey: 'forename',
	    //prefetch: '<c:url value="/shop/resources/js/typeahead.min.js"/>' 
	    //remote: {
	    	//url: '<c:url value="/shop/services/search/${requestScope.MERCHANT_STORE.code}/${requestScope.LANGUAGE.code}/autocomplete.html"/>?q=%QUERY',
	        //url: 'searchPatient.do?q=%QUERY',
	        //filter: function (parsedResponse) {
	            // parsedResponse is the array returned from your backend
	            //console.log(parsedResponse);

	            // do whatever processing you need here
	            //return parsedResponse;
	        //}
	    //}                                            
	    //template: [                                                                 
	    //    '<p class="name">{{forename}} {{surname}} ({{gender}} {{age}})</p>',
	    //    '<p class="dob">{{dateOfBirth}}</p>'
	    //].join(''),                                                                 
	    //engine: Hogan // download and include http://twitter.github.io/hogan.js/                                                               
	//}]);
	
	
	$('#searchField').typeahead({
		name: 'shopizer-search',
		prefetch: '<c:url value="/resources/repos.json"/>',
	    remote: {
    		url: '<c:url value="/shop/services/search/${requestScope.MERCHANT_STORE.code}/${requestScope.LANGUAGE.code}/autocomplete.html"/>?q=%QUERY',
        	filter: function (parsedResponse) {
            	// parsedResponse is the array returned from your backend
            	console.log(parsedResponse);

            	// do whatever processing you need here
            	return parsedResponse;
        	}
    	},
		template: [
		'<p class="repo-language">{{language}}</p>',
		'<p class="repo-name">{{name}}</p>',
		'<p class="repo-description">{{description}}</p>'
		].join(''),
		engine: Hogan
		});
	
	
	

});

</script>

<c:set var="req" value="${request}" />
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


            <!-- Start Navbar-->
            <div class="row-fluid show-grid">

				<div class="span12">
					<nav class="pull-left logo">
						 <c:choose>
                		<c:when test="${requestScope.MERCHANT_STORE.storeLogo!=null}">
                			<img class="logoImage" src="<sm:storeLogo/>" />
                		</c:when>
                		<c:otherwise>
                			<h1>
                			<a href="<c:url value="/shop/"/>">
                				<c:out value="${requestScope.MERCHANT_STORE.storename}"/>
                			</a>  
                			</h1>
                		</c:otherwise>
                	  </c:choose>  
					</nav>


						<nav id="menu" class="pull-right">
                    					<ul id="mainMenu">
											<li class="">  
	                    					       <a href="<c:url value="/shop"/>" class="current">          
	                    					            <span class="name"><s:message code="menu.home" text="Home"/></span>     
	                    								<span class="desc"><s:message code="menu.home" text="Home"/></span>                                  
	                    						   </a>                         
	                    					</li>
	
	                    		            
	                    		            <c:forEach items="${requestScope.TOP_CATEGORIES}" var="category">
	    										<li class="">
	    											<a href="<c:url value="/shop/category/${category.description.seUrl}.html/ref=${category.id}"/>" class="current"> 
	    												<span class="name">${category.description.name}</span>
	    												<span class="desc">${category.description.categoryHighlight}</span> 
	    											</a>
	    										</li> 
											</c:forEach>
                    		            </ul>
						</nav>

				</div>
            </div>
            		
			<div class="row-fluid">
					<div id="searchGroup" class="btn-group pull-right">
						<form id="searchForm" class="form-inline">
							<input id="searchField" class="tt-query" type="text" placeholder="Search query" autocomplete="off" spellcheck="false" dir="auto">
							<button id="searchButton" class="btn" type="submit">Search</button>
						</form>
					</div>
			</div>
			<!-- End Navbar-->