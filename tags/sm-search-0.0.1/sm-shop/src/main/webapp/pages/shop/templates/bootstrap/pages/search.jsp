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
 

 <script src="<c:url value="/resources/js/jquery.isotope.min.js" />"></script>

 
 <script>
 
 var START_COUNT_PRODUCTS = 0;
 var MAX_PRODUCTS = 12;
 

 $(function(){
	 
	 //var $container = $('#productsContainer');

	 //$container.isotope({
	 //	 itemSelector : '.item'
	 // });
	  
	 //$('#sort-by a').click(function(){
	 //	 // get href attribute, minus the '#'
	 //	 $('#container').isotope({ sortBy : 'name' });
		// return false;
	 //});
	 
	 search();

 });
 
 	function search() {
 		var url = '<%=request.getContextPath()%>/shop/services/search/<c:out value="${requestScope.MERCHANT_STORE.code}"/>/<c:out value="${requestScope.LANGUAGE.code}"/>/' + START_COUNT_PRODUCTS + '/' + MAX_PRODUCTS + '/term.html';
	 	searchProducts(url,'#productsContainer','<c:out value="${q}"/>',null);
 	}
 
	function callBackSearchProducts(productList) {
			totalCount = productList.totalCount;
			START_COUNT_PRODUCTS = START_COUNT_PRODUCTS + MAX_PRODUCTS;
			if(START_COUNT_PRODUCTS < totalCount) {
					$("#button_nav").show();
			} else {
					$("#button_nav").hide();
			}
			
			//facets
			if(productList.categoryFacets!=null) {
				for (var i = 0; i < productList.categoryFacets.length; i++) {
					var categoryFacets = '<li>';
					categoryFacets = categoryFacets + '<a href="<c:url value="/shop"/>/category/' + productList.categoryFacets[i].friendlyUrl + '.html">' + productList.categoryFacets[i].name;
					if(productList.categoryFacets[i].totalCount>0) {
					   categoryFacets = categoryFacets + '&nbsp;<span class="countItems">(' + productList.categoryFacets[i].totalCount + ')</span>'
					}
					categoryFacets = categoryFacets + '</a>';
					categoryFacets = categoryFacets + '</li>';
					$(categoriesFacets).append(categoryFacets);
				}
			}
			
			$('#productsContainer').hideLoading();

	}
 
 
 
 
</script>


	<div class="row-fluid">
	
	
	   <div class="span12">
      	
      	<!-- left column -->
      	<!--Search facets-->
        <div class="span3">
          <div class="sidebar-nav">
            <ul id="categoriesFacets" class="nav nav-list">
              <!--<li class="nav-header"></li>-->
            </ul>
          </div>
        </div><!--/span-->
        
        <!-- right column -->
        <div class="span9">

          
        	<ul id="productsContainer" class="thumbnails product-list">
				<!-- search ajax -->
			</ul>
			<nav id="button_nav" style="text-align:center;display:none;">
				<button class="btn btn-large" style="width:400px;" onClick="loadProducts();"><s:message code="label.product.moreitems" text="Display more items" />...</button>
			</nav>
			<span id="end_nav" style="display:none;"><s:message code="label.product.nomoreitems" text="No more items to be displayed" /></span>
          
          
        </div><!--/span-->
        
        </div><!-- 12 -->
        
      </div><!-- row fluid -->