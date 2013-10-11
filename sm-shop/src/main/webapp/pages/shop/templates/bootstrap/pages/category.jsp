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
	 
 
	 loadProducts();

 });
 
 
 
	function loadProducts() {
		$('#productsContainer').showLoading();
		var url = '<%=request.getContextPath()%>/shop/services/products/page/' + START_COUNT_PRODUCTS + '/' + MAX_PRODUCTS + '/<c:out value="${requestScope.MERCHANT_STORE.code}"/>/<c:out value="${requestScope.LANGUAGE.code}"/>/<c:out value="${category.friendlyUrl}"/>.html';
		
		$.ajax({
				type: 'POST',
				dataType: "json",
				url: url,
				success: function(productList) {

					for (var i = 0; i < productList.products.length; i++) {
					    var productHtml = '<li class="item span3">';
					    	productHtml = productHtml + '<div class="product-box"><a href="<c:url value="/shop/product/" />' + productList.products[i].friendlyUrl + '.html">';
					    	productHtml = productHtml + '<h4 class="name">' + productList.products[i].name +'</h4></a>';
					    	productHtml = productHtml + '<h3 class="number">' + productList.products[i].productPrice +'</h3>';
					    	productHtml = productHtml + '<a href="<c:url value="/shop/product/" />' + productList.products[i].friendlyUrl + '.html"><img src="<c:url value="/"/>' + productList.products[i].imageUrl +'"></a>';
					    	productHtml = productHtml + '<div class="bottom"><a href="<c:url value="/shop/product/" />' + productList.products[i].friendlyUrl + '.html" class="view"><s:message code="button.label.view" text="View" /></a> / <a productid="' + productList.products[i].id + '" href="#" class="addToCart"><s:message code="button.label.addToCart" text="Add to cart" /></a></div></div></li>';
					    	//displayProducts[i] = productHtml;
					    	$('#productsContainer').append(productHtml);

					}

					//alert('start ' + START_COUNT_PRODUCTS + ' count ' + productList.totalCount);
					START_COUNT_PRODUCTS = START_COUNT_PRODUCTS + MAX_PRODUCTS;
					if(START_COUNT_PRODUCTS < productList.totalCount) {
						$("#button_nav").show();
						//$("#end_nav").hide();
					} else {
						//$("#end_nav").show();
						$("#button_nav").hide();
					}
					
					
					
					$('#productsContainer').hideLoading();

					
				},
				error: function(jqXHR,textStatus,errorThrown) { 
					$('#productsContainer').hideLoading();
					alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
				}
				
		});
		
		
		
	}
 
 
 
 
</script>

    <jsp:include page="/pages/shop/templates/bootstrap/sections/breadcrumb.jsp" />
 


	<div class="row-fluid">
	
	
	   <div class="span12">
      	
      	<!-- left column -->
      	<!--TEST current url : <c:out value="${requestScope['javax.servlet.forward.query_string']}" />-->
        <div class="span3">
          <div class="sidebar-nav">
            <ul class="nav nav-list">
              <li class="nav-header"><a href="<c:url value="/shop"/>/category/${category.friendlyUrl}.html"><c:out value="${category.name}" /></a></li>
              <c:forEach items="${subCategories}" var="subCategory">
              	<li><a href="<c:url value="/shop/category/${subCategory.friendlyUrl}.html/ref=${category.id}"/>"><c:out value="${subCategory.name}" /></a></li>
              </c:forEach>
            </ul>
          </div>
        </div><!--/span-->
        
        <!-- right column -->
        <div class="span9">

          
        	<ul id="productsContainer" class="thumbnails product-list">

			</ul>
			<nav id="button_nav" style="text-align:center;">
				<button class="btn btn-large" style="width:400px;" onClick="loadProducts();"><s:message code="label.product.moreitems" text="Display more items" />...</button>
			</nav>
			<span id="end_nav" style="display:none;"><s:message code="label.product.nomoreitems" text="No more items to be displayed" /></span>
          
          
        </div><!--/span-->
        
        </div><!-- 12 -->
        
      </div><!-- row fluid -->