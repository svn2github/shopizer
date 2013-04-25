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
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  
 <%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


			<!--<div class="row">-->
				<div class="span12">
				
				      	<!--  Carousel -->
      					<div id="main-slider" class="carousel slide home">
        					<div class="carousel-inner" id="slider">
        					
        					          <div class="item active">
            							<img src="<c:url value="/resources/templates/bootstrap/img/surf-banner.jpg" />" alt="" height="377"/>
            							<div class="carousel-caption">
              								<h2>Title</h2>
              								<p>Text</p>
            							</div>
          							  </div>

        					</div><!-- .carousel-inner -->
        					<!--  next and previous controls here
              					href values must reference the id for this carousel -->
          					<a class="carousel-control left" href="#slider" data-slide="prev">&lsaquo;</a>
          					<a class="carousel-control right" href="#slider" data-slide="next">&rsaquo;</a>
      					</div><!-- .carousel -->
      					<!-- end carousel -->
				
				
				

          		</div>
			<!--</div>-->


			<!--<div class="row">-->
				<div class="span12">
					<ul class="nav nav-tabs home" id="product-tab">
						<li class="active"><a href="#tab1">Featured items</a></li>
						<li><a href="#tab2">Specials</a></li>
						<li><a href="#tab3">Top sellers</a></li>
					</ul>							 
					<div class="tab-content">
						<div class="tab-pane active" id="tab1">
							<div class="row">
								<div class="span12">
									<ul class="thumbnails product-list">
									
										<!-- Iterate over featuredItems -->
									
										<li class="span3">
											<div class="product-box">                                        
												<a href="http://#WB0M3G9S1/product_detail.html"><h4>Fusce id molestie massa</h4></a>
												<a href="http://#WB0M3G9S1/product_detail.html"><img alt="" src="index_files/m2.jpg"></a>
												<p>Phasellus consequat sem congue diam congue</p>
												<div class="bottom">
													<a class="view" href="http://#WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://#WB0M3G9S1/cart.html">add to cart</a>
												</div>
											</div>
										</li>       
										<li class="span3">
											<div class="product-box">
												<a href="http://#WB0M3G9S1/product_detail.html"><h4>Praesent tempor sem sodales</h4></a>
												<a href="http://#WB0M3G9S1/product_detail.html"><img alt="" src="index_files/m3.jpg"></a>
												<p>Integer in ligula et erat gravida placerat</p>
												<div class="bottom">
													<a class="view" href="http://#WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://#WB0M3G9S1/cart.html">add to cart</a>
												</div>
											</div>
										</li>
										<li class="span3">
											<div class="product-box">
												<a href="http://#WB0M3G9S1/product_detail.html"><h4>Luctus quam ultrices rutrum</h4></a>
												<a href="http://#WB0M3G9S1/product_detail.html"><img alt="" src="index_files/m4.jpg"></a>
												<p>Suspendisse aliquet orci et nisl iaculis</p>
												<div class="bottom">
													<a class="view" href="http://#WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://#WB0M3G9S1/cart.html">add to cart</a>
												</div>
											</div>
										</li>
										<li class="span3">
											<div class="product-box">
												<a href="http://#WB0M3G9S1/product_detail.html"><h4>Praesent tempor sem sodales</h4></a>
												<a href="http://#WB0M3G9S1/product_detail.html"><img alt="" src="index_files/m5.jpg"></a>
												<p>Nam imperdiet urna nec lectus mollis</p>
												<div class="bottom">
													<a class="view" href="http://#WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://#WB0M3G9S1/cart.html">add to cart</a>
												</div>
											</div>
										</li>                                
									</ul>
								</div>
							</div>
						</div>
						<div class="tab-pane" id="tab2">
							<div class="row">
								<div class="span12">
									<ul class="thumbnails product-list">
										<li class="span3">
											<div class="product-box">
												<a href="http://#WB0M3G9S1/product_detail.html"><h4>Praesent tempor sem sodales</h4></a>
												<a href="http://#WB0M3G9S1/product_detail.html"><img alt="" src="index_files/m6.jpg"></a>
												<p>Nam imperdiet urna nec lectus mollis</p>
												<div class="bottom">
													<a class="view" href="http://#WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://#WB0M3G9S1/cart.html">add to cart</a>
												</div>
											</div>
										</li>
										<li class="span3">
											<div class="product-box">                                        
												<a href="http://#WB0M3G9S1/product_detail.html"><h4>Fusce id molestie massa</h4></a>
												<a href="http://#WB0M3G9S1/product_detail.html"><img alt="" src="index_files/m7.jpg"></a>
												<p>Phasellus consequat sem congue diam congue</p>
												<div class="bottom">
													<a class="view" href="http://#WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://#WB0M3G9S1/cart.html">add to cart</a>
												</div>
											</div>
										</li>       
										<li class="span3">
											<div class="product-box">
												<a href="http://#WB0M3G9S1/product_detail.html"><h4>Praesent tempor sem sodales</h4></a>
												<a href="http://#WB0M3G9S1/product_detail.html"><img alt="" src="index_files/m3.jpg"></a>
												<p>Integer in ligula et erat gravida placerat</p>
												<div class="bottom">
													<a class="view" href="http://#WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://#WB0M3G9S1/cart.html">add to cart</a>
												</div>
											</div>
										</li>										
										<li class="span3">
											<div class="product-box">
												<a href="http://#WB0M3G9S1/product_detail.html"><h4>Luctus quam ultrices rutrum</h4></a>
												<a href="http://#WB0M3G9S1/product_detail.html"><img alt="" src="index_files/m4.jpg"></a>
												<p>Suspendisse aliquet orci et nisl iaculis</p>
												<div class="bottom">
													<a class="view" href="http://#WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://#WB0M3G9S1/cart.html">add to cart</a>
												</div>
											</div>
										</li>
									</ul>
								</div>
							</div>
						</div>
						<div class="tab-pane" id="tab3">
							<div class="row">
								<div class="span12">
									<ul class="thumbnails product-list">
										<li class="span3">
											<div class="product-box">
												<a href="http://#WB0M3G9S1/product_detail.html"><h4>Praesent tempor sem sodales</h4></a>
												<a href="http://#WB0M3G9S1/product_detail.html"><img alt="" src="index_files/m3.jpg"></a>
												<p>Nam imperdiet urna nec lectus mollis</p>
												<div class="bottom">
													<a class="view" href="http://#WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://#WB0M3G9S1/cart.html">add to cart</a>
												</div>
											</div>
										</li>
										<li class="span3">
											<div class="product-box">
												<a href="http://#WB0M3G9S1/product_detail.html"><h4>Luctus quam ultrices rutrum</h4></a>
												<a href="http://#WB0M3G9S1/product_detail.html"><img alt="" src="index_files/m4.jpg"></a>
												<p>Suspendisse aliquet orci et nisl iaculis</p>
												<div class="bottom">
													<a class="view" href="http://#WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://#WB0M3G9S1/cart.html">add to cart</a>
												</div>
											</div>
										</li>
										<li class="span3">
											<div class="product-box">                                        
												<a href="http://#WB0M3G9S1/product_detail.html"><h4>Fusce id molestie massa</h4></a>
												<a href="http://#WB0M3G9S1/product_detail.html"><img alt="" src="index_files/m2.jpg"></a>
												<p>Phasellus consequat sem congue diam congue</p>
												<div class="bottom">
													<a class="view" href="http://#WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://#WB0M3G9S1/cart.html">add to cart</a>
												</div>
											</div>
										</li>       
										<li class="span3">
											<div class="product-box">
												<a href="http://#WB0M3G9S1/product_detail.html"><h4>Praesent tempor sem sodales</h4></a>
												<a href="http://#WB0M3G9S1/product_detail.html"><img alt="" src="index_files/m3.jpg"></a>
												<p>Integer in ligula et erat gravida placerat</p>
												<div class="bottom">
													<a class="view" href="http://#WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://#WB0M3G9S1/cart.html">add to cart</a>
												</div>
											</div>
										</li>										
									</ul>
								</div>
							</div>
						</div>
					</div>							
				</div>
			<!--</div>-->


			
		