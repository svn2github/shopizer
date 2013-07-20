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
	 
	 
	 $('#productList').infiniScroll({ // calls the init method overrides defaults
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
				//data: "code="+ code + "&id=" + id,
				success: function(products) {
					
					
					
					for (var i = 0; i < products.length; i++) {
					    //alert(products[i].name);
					    //Do something
					    var productHtml = '<li class="span3">';
					    	productHtml = productHtml + '<div class="product-box"><a href="http://#WB0M3G9S1/product_detail.html">';
					    	productHtml = productHtml + '<h4>' + product.name +'</h4></a>';
					    	productHtml = productHtml + '<a href="http://#WB0M3G9S1/product_detail.html"><img width="200" src="http://localhost:8080//sm-shop/static/DEFAULT/PRODUCT/TB12345/"></a>';
					    	productHtml = productHtml + '<h3>' + product.productPrice +'</h3>';
					    	productHtml = productHtml + '<div class="bottom"><a href="http://#WB0M3G9S1/product_detail.html" class="view">view</a> / <a productid="1" href="#" class="addToCart">add to cart</a></div></div></li>';
					}
					
					//alert(response);
					
					
					//var msg = isc.XMLTools.selectObjects(response, "/response/statusMessage");
					//var status = isc.XMLTools.selectObjects(response, "/response/status");
					//callBackCheckCode(msg,status);

					
				},
				error: function(jqXHR,textStatus,errorThrown) { 
					alert(jqXHR + "-" + textStatus + "-" + errorThrown);
				}
				
		});
		
		
		
	}
 
 
 
 
</script>

    <div class="row-fluid">
						   <ul class="breadcrumb">
								<li>
									<a href="#"><i class="icon-home"></i></a> <span class="divider">/</span>
								</li>
								<li><a href="http://wbpreview.com/previews/WB0M3G9S1/products.html">Product</a> <span class="divider">/</span></li>
								<li><a href="http://wbpreview.com/previews/WB0M3G9S1/products.html">Women</a> <span class="divider">/</span></li>
								<li class="active">Detail</li>
							</ul>
	</div>
 


	<div class="row-fluid">
	
	
	   <div class="span12">
      	
      	<!-- left column -->
      	<c:out value="${requestScope['javax.servlet.forward.query_string']}" />
        <div class="span3">
          <div class="sidebar-nav">
            <ul class="nav nav-list">
              <li class="nav-header"><c:out value="${category.name}" /></li>
              <c:forEach items="${subCategories}" var="category">
              	<li><a href="<c:url value="/shop/category/${category.friendlyUrl}.html"/>"><c:out value="${category.name}" /></a></li>
              </c:forEach>

              <!--<li class="nav-header">Sidebar</li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>-->
            </ul>
          </div>
        </div><!--/span-->
        
        <!-- right column -->
        <div class="span9">

          
        <ul class="thumbnails product-list">
										<!-- Iterate over featuredItems -->
										
											<li class="span3">
												<div class="product-box">                                        
													<a href="http://#WB0M3G9S1/product_detail.html"><h4>Spring in Action</h4></a>
													<a href="http://#WB0M3G9S1/product_detail.html"><img width="200" src="http://localhost:8080//sm-shop/static/DEFAULT/PRODUCT/TB12345/"></a>
													<h3>$29.99</h3>
													<div class="bottom">
														<a href="http://#WB0M3G9S1/product_detail.html" class="view">view</a> / <a productid="1" href="#" class="addToCart">add to cart</a>
													</div>
												</div>
										    </li>
										
											<li class="span3">
												<div class="product-box">                                        
													<a href="http://#WB0M3G9S1/product_detail.html"><h4>A nice book for you</h4></a>
													<a href="http://#WB0M3G9S1/product_detail.html"><img width="200" src="http://localhost:8080//sm-shop/static/DEFAULT/PRODUCT/NB1111/"></a>
													<h3>$19.99</h3>
													<div class="bottom">
														<a href="http://#WB0M3G9S1/product_detail.html" class="view">view</a> / <a productid="3" href="#" class="addToCart">add to cart</a>
													</div>
												</div>
										    </li>
										
											<li class="span3">
												<div class="product-box">                                        
													<a href="http://#WB0M3G9S1/product_detail.html"><h4>Battle of the worlds 2</h4></a>
													<a href="http://#WB0M3G9S1/product_detail.html"><img width="200" src="http://localhost:8080//sm-shop/static/DEFAULT/PRODUCT/SF333346/"></a>
													<h3>$18.99</h3>
													<div class="bottom">
														<a href="http://#WB0M3G9S1/product_detail.html" class="view">view</a> / <a productid="5" href="#" class="addToCart">add to cart</a>
													</div>
												</div>
										    </li>
										
											<li class="span3">
												<div class="product-box">                                        
													<a href="http://#WB0M3G9S1/product_detail.html"><h4>Life book</h4></a>
													<a href="http://#WB0M3G9S1/product_detail.html"><img width="200" src="http://localhost:8080//sm-shop/static/DEFAULT/PRODUCT/LL333444/"></a>
													<h3>$18.99</h3>
													<div class="bottom">
														<a href="http://#WB0M3G9S1/product_detail.html" class="view">view</a> / <a productid="6" href="#" class="addToCart">add to cart</a>
													</div>
												</div>
										    </li>
										    
										    
										    											<li class="span3">
												<div class="product-box">                                        
													<a href="http://#WB0M3G9S1/product_detail.html"><h4>Life book</h4></a>
													<a href="http://#WB0M3G9S1/product_detail.html"><img width="200" src="http://localhost:8080//sm-shop/static/DEFAULT/PRODUCT/LL333444/"></a>
													<h3>$18.99</h3>
													<div class="bottom">
														<a href="http://#WB0M3G9S1/product_detail.html" class="view">view</a> / <a productid="6" href="#" class="addToCart">add to cart</a>
													</div>
												</div>
										    </li>
										                             
									</ul>
          
          
        </div><!--/span-->
        
        </div><!-- 12 -->
        
      </div><!-- row fluid -->