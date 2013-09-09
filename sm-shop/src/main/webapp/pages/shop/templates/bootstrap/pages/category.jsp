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
 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  
 <%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
 <script src="<c:url value="/resources/js/infiniScroll.js" />"></script>
 
 <script>
 
 
 
 
 $(function(){
	 
	 
	 loadProducts(0,12);
	 
	 
	 $('#categoryPane').infiniScroll({ // calls the init method overrides defaults
		    'interval' : 200
		    ,'root_url' : '/shop'
		    ,'loading_elem': 'loading'
		    ,'data_elem': 'leaderboard'
		    ,'num' : 12
	  });
 });
 
 
 
	function loadProducts(start, qty) {
		
		var url = '<%=request.getContextPath()%>/shop/services/products/page/' + start + '/' + qty + '/<c:out value="${requestScope.MERCHANT_STORE.code}"/>/<c:out value="${requestScope.LANGUAGE.code}"/>/<c:out value="${category.friendlyUrl}"/>.html';
		
		$.ajax({
				type: 'POST',
				dataType: "json",
				url: url,
				success: function(products) {
					
					
					//var displayProducts = new Array();
					for (var i = 0; i < products.length; i++) {
					    //alert(products[i].name);
					    //Do something
					    var productHtml = '<li class="span3">';
					    	productHtml = productHtml + '<div class="product-box"><a href="<c:url value="/shop/product/" />' + products[i].friendlyUrl + '.html">';
					    	productHtml = productHtml + '<h4>' + products[i].name +'</h4></a>';
					    	productHtml = productHtml + '<a href="<c:url value="/shop/product/" />' + products[i].friendlyUrl + '.html"><img src="<c:url value="/"/>' + products[i].imageUrl +'"></a>';
					    	productHtml = productHtml + '<h3>' + products[i].productPrice +'</h3>';
					    	productHtml = productHtml + '<div class="bottom"><a href="<c:url value="/shop/product/" />' + products[i].friendlyUrl + '.html" class="view"><s:message code="button.label.view" text="View" /></a> / <a productid="' + products[i].id + '" href="#" class="addToCart"><s:message code="button.label.addToCart" text="Add to cart" /></a></div></div></li>';
					    	//displayProducts[i] = productHtml;
					    	$('#categoryPane').append(productHtml);
					}

					
				},
				error: function(jqXHR,textStatus,errorThrown) { 
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
              <li class="nav-header"><a href="<c:url value="/shop"/>/${category.friendlyUrl}.html"><c:out value="${category.name}" /></a></li>
              <c:forEach items="${subCategories}" var="category">
              	<li><a href="<c:url value="/shop/category/${category.friendlyUrl}.html"/>"><c:out value="${category.name}" /></a></li>
              </c:forEach>
            </ul>
          </div>
        </div><!--/span-->
        
        <!-- right column -->
        <div class="span9">

          
        	<ul id="categoryPane" class="thumbnails product-list">

			</ul>
          
          
        </div><!--/span-->
        
        </div><!-- 12 -->
        
      </div><!-- row fluid -->