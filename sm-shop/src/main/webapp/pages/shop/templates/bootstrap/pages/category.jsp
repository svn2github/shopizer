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
	 
	loadCategoryProducts();

 });
 
 	function loadCategoryProducts() {
 		var url = '<%=request.getContextPath()%>/shop/services/products/page/' + START_COUNT_PRODUCTS + '/' + MAX_PRODUCTS + '/<c:out value="${requestScope.MERCHANT_STORE.code}"/>/<c:out value="${requestScope.LANGUAGE.code}"/>/<c:out value="${category.friendlyUrl}"/>.html';
	 	loadProducts(url,'#productsContainer');
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
            <ul class="nav nav-list">
              <c:if test="${parent!=null}">
              	<li class="nav-header"><a href="<c:url value="/shop"/>/category/${parent.friendlyUrl}.html/ref=${parent.id}"><c:out value="${parent.name}" /></a></li>
              </c:if>
              <c:forEach items="${subCategories}" var="subCategory">
              	<li><a href="<c:url value="/shop/category/${subCategory.friendlyUrl}.html/ref=${subCategory.id}"/>"><c:out value="${subCategory.name}" /></a></li>
              </c:forEach>
            </ul>
          </div>
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