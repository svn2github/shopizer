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
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
 

 <script src="<c:url value="/resources/js/jquery.quicksand.js" />"></script>
 <script src="<c:url value="/resources/js/jquery-sort-filter-plugin.js" />"></script>


 
 <script>
 
 var START_COUNT_PRODUCTS = 0;
 var MAX_PRODUCTS = 12;
 var filter = null;
 var filterValue = null;
 

 
 
 
 $(function(){
	 
	 
	 var $filterType = $('#filter input[name="type"]');
	 var $filterSort = $('#filter input[name="sort"]');

	  var $productsContainer = $('#productsContainer');

	  // clone applications to get a second collection
	  var $data = $productsContainer.clone();
	  
	  
	  $filterType.add($filterSort).change(function(e) {
		    //if ($($filterType+':checked').val() == 'all') {
		    //  var $filteredData = $data.find('li');
		    //} else {
		    //  var $filteredData = $data.find('li[data-type=' + $($filterType+":checked").val() + ']');
		    //}

		    var $filteredData = $data.find('li');
		    alert($filteredData)
		    // if sorted by size
		    alert($('#filter input[name="sort"]:checked').val());
		    if ($('#filter input[name="sort"]:checked').val() == "price") {
		      var $sortedData = $filteredData.sorted({
		        by: function(v) {
		          alert('1');
		          alert($(v).find('span[data-type=price]').text());
		          return parseFloat($(v).find('span[data-type=price]').text());
		        }
		      });
		    } else {
		      // if sorted by name
		      var $sortedData = $filteredData.sorted({
		        by: function(v) {
		        	alert('2');
		           alert($(v).find('span[data-type=name]').text().toLowerCase());
		          return $(v).find('span[data-type=name]').text().toLowerCase();
		        }
		      });
		    }   

		    // finally, call quicksand
		    $productsContainer.quicksand($sortedData, {
		      duration: 800
		      //easing: 'easeInOutQuad'
		    });

		  });
	  
	  

	 // Sorting options
	 
	 //$('.sort-options').on('change', function() {
	   
	   //var $items = $('#productsContainer');

			  // clone applications to get a second collection
	   //var $data = $items.clone();
	   //var $filteredData = $data.find('li');
	   //var sort = this.value,
	   //    opts = {};

	   // We're given the element wrapped in jQuery
	   //if ( sort === 'price' ) {
		 // var $sortedData = $filteredData.sorted({
		   //       by: function(v) {
		     //       return $(v).find('item-price').text().toLowerCase();
		      //    }
		  // });
	 //  } else if ( sort === 'name' ) {
		//	  var $sortedData = $filteredData.sorted({
		  //        by: function(v) {
		    //        return $(v).find('item-name').text().toLowerCase();
		    //      }
		   //});
	   //}
	   


	   // Filter elements
	   // $items.quicksand($sortedData, {
	     //   duration: 800
	        //easing: 'easeInOutQuad'//requires easing x.easing //http://razorjack.net/quicksand/docs-and-demos.html
	      //});
	 //});
	 
	 //var $container = $('#productsContainer');

	 //$container.isotope({
	 //	 itemSelector : '.item'
	 // });
	  
	 //$('#sort-by a').click(function(){
	 //	 // get href attribute, minus the '#'
	 //	 $('#container').isotope({ sortBy : 'name' });
		// return false;
	 //});
	 
	loadCategoryProducts();

 });
 
 	function loadCategoryProducts() {
 		var url = '<%=request.getContextPath()%>/shop/services/products/page/' + START_COUNT_PRODUCTS + '/' + MAX_PRODUCTS + '/<c:out value="${requestScope.MERCHANT_STORE.code}"/>/<c:out value="${requestScope.LANGUAGE.code}"/>/<c:out value="${category.description.friendlyUrl}"/>.html';
	 	
 		if(filter!=null) {
 			url = url + '/filter=' + filter + '/filter-value=' + filterValue +'';
 		}
 		loadProducts(url,'#productsContainer');
 	}
 	
 	
 	function loadCategoryByBrand(filterType,filterVal) {
 		//reset product section
 		$('#productsContainer').html('');
 		START_COUNT_PRODUCTS = 0;
 		filter = filterType;
 		filterValue = filterVal;
 		loadCategoryProducts();
 	}
 
	function callBackLoadProducts(productList) {
			totalCount = productList.totalCount;
			START_COUNT_PRODUCTS = START_COUNT_PRODUCTS + MAX_PRODUCTS;
			if(START_COUNT_PRODUCTS < totalCount) {
					$("#button_nav").show();
			} else {
					$("#button_nav").hide();
			}
			$('#productsContainer').hideLoading();
			


	}
 
 
 
 
</script>

    <jsp:include page="/pages/shop/templates/bootstrap/sections/breadcrumb.jsp" />
 


	<div class="row-fluid">
	
	
	   <div class="span12">
      	
      	<!-- left column -->
        <div class="span3">
          <div class="sidebar-nav">
          
			<form id="filter">
			  <fieldset>
			    <legend>Sort by</legend>
			    <label><input type="radio" name="sort" value="size" checked="checked">Size</label>
			    <label><input type="radio" name="sort" value="name">Name</label>      
			  </fieldset>
			</form>
            <br/><br/>
          
            <ul class="nav nav-list">
              <c:if test="${parent!=null}">
              	<li class="nav-header"><c:out value="${parent.description.name}" /></li>
              </c:if>
              <c:forEach items="${subCategories}" var="subCategory">
              	<li>
              		<a href="<c:url value="/shop/category/${subCategory.description.friendlyUrl}.html/ref=${subCategory.id}"/>"><c:out value="${subCategory.description.name}" />
              			<c:if test="${subCategory.productCount>0}">&nbsp;<span class="countItems">(<c:out value="${subCategory.productCount}" />)</span></c:if></a></li>
              </c:forEach>
            </ul>
          </div>
          
          <c:if test="${fn:length(manufacturers) > 0}">
          <br/>
          <div class="sidebar-nav">
            <ul class="nav nav-list">
              <li class="nav-header"><s:message code="label.manufacturer.brand" text="Brands" /></li>
              <c:forEach items="${manufacturers}" var="manufacturer">
              	<li>
              		<a href="javascript:loadCategoryByBrand('BRAND','${manufacturer.id}')"><c:out value="${manufacturer.name}" /></a></li>
              </c:forEach>
            </ul>
          </div>          
          </c:if>
          
          
        </div><!--/span-->
        
        <!-- right column -->
        <div class="span9">

          
        	<ul id="productsContainer" class="thumbnails product-list">

			</ul>
			<nav id="button_nav" style="text-align:center;display:none;">
				<button class="btn btn-large" style="width:400px;" onClick="loadCategoryProducts();"><s:message code="label.product.moreitems" text="Display more items" />...</button>
			</nav>
			<span id="end_nav" style="display:none;"><s:message code="label.product.nomoreitems" text="No more items to be displayed" /></span>
          
          
        </div><!--/span-->
        
        </div><!-- 12 -->
        
      </div><!-- row fluid -->