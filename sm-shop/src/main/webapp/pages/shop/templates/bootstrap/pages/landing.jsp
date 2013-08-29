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


			<div class="row-fluid">
				<!--
				<div class="span12">

      					<div id="main-slider" class="carousel slide home">
        					<div class="carousel-inner" id="slider">
        					
        					          <div class="item active">
            							<img src="<c:url value="/resources/templates/bootstrap/img/surf-banner.jpg" />" alt="" height="377"/>
            							<div class="carousel-caption">
              								<h2>Title</h2>
              								<p>Text</p>
            							</div>
          							  </div>

        					</div>
          					<a class="carousel-control left" href="#slider" data-slide="prev">&lsaquo;</a>
          					<a class="carousel-control right" href="#slider" data-slide="next">&rsaquo;</a>
      					</div>

				
				
				

          		</div>
          		-->
          		
          		<c:if test="${page!=null}">
          		    <div class="span12">
          			    <span id="homeText"><c:out value="${page.description}" escapeXml="false"/></span>
          		    </div>
          		</c:if>
          		
			</div>
			
			<br/>
	
			<sm:shopProductGroup groupName="FEATURED_ITEM"/>
			<sm:shopProductGroup groupName="SPECIALS"/>
			
			<c:if test="${requestScope.FEATURED_ITEM!=null || requestScope.SPECIALS!=null}" >

			<div class="row-fluid">
				<div class="span12">
					<ul class="nav nav-tabs home" id="product-tab">
						<c:if test="${requestScope.FEATURED_ITEM!=null" ><li class="active"><a href="#tab1"><s:message code="menu.catalogue-featured" text="Featured items" /></a></li></c:if>
						<c:if test="${requestScope.SPECIALS!=null" ><li><a href="#tab2">Specials</a></li></c:if>
					</ul>							 
					<div class="tab-content">
						<!-- one div by section -->
						<div class="tab-pane active" id="tab1">
									<ul class="thumbnails product-list">
										<!-- Iterate over featuredItems -->
										<c:forEach items="${requestScope.FEATURED_ITEM}" var="product">
											<li class="span3">
												<div class="product-box">                                        
													<a href="<c:url value="/shop/product/" /><c:out value="${product.friendlyUrl}"/>.html"><h4><c:out value="${product.name}"/></h4></a>
													<a href="<c:url value="/shop/product/" /><c:out value="${product.friendlyUrl}"/>.html"><img src="<sm:shopProductImage imageName="${product.image}" sku="${product.sku}"/>" width="200"/></a>
													<h3><c:out value="${product.productPrice}" /></h3>
													<div class="bottom">
														<a href="<c:url value="/shop/product/" /><c:out value="${product.friendlyUrl}"/>.html">view</a> / <a class="addToCart" href="#" productId="${product.id}">add to cart</a>
													</div>
												</div>
										    </li>
										</c:forEach>                             
									</ul>
						</div>
						<div class="tab-pane" id="tab2">
								<ul class="thumbnails product-list">
										<!-- Iterate over specials -->
										<c:forEach items="${requestScope."SPECIALS"}" var="product">
											<li class="span3">
												<div class="product-box">                                        
													<a href="<c:url value="/shop/product/" /><c:out value="${product.friendlyUrl}"/>.html"><h4><c:out value="${product.name}"/></h4></a>
													<a href="<c:url value="/shop/product/" /><c:out value="${product.friendlyUrl}"/>.html"><img src="<sm:shopProductImage imageName="${product.image}" sku="${product.sku}"/>" width="200"/></a>
													<h3><c:out value="${product.productPrice}" /></h3>
													<div class="bottom">
														<a href="<c:url value="/shop/product/" /><c:out value="${product.friendlyUrl}"/>.html">view</a> / <a class="addToCart" href="#" productId="${product.id}">add to cart</a>
													</div>
												</div>
										    </li>
										</c:forEach>                             
									</ul>
						</div>







						</div>
					</div>							
				</div>


			</c:if>
			
		