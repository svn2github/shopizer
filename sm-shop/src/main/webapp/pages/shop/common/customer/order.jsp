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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


	<div id="middle" class="container clearfix">
		

<div class="white">

				<header class="page-header">
					<h1>Order Summary <small>29 June 2014</small></h1>
				</header>

				<div class="" id="shop"><!-- note class 'printable' - this area is printable only! -->

					<!-- CALLOUT -->
					<div class="alert alert-default fade in">

						<div class="row">

							<div class="col-md-8 col-sm-8"><!-- left text -->
								<h4 class="nomargin">Order Date</h4>
								<p class="nomargin">
									Order Date: 29 June 2014 / 10:42:11
								</p>
							</div><!-- /left text -->

							
							<div class="col-md-4 col-sm-4 text-right"><!-- right btn -->
								<a onclick="window.print();" class="btn btn-primary btn-lg" href="#">PRINT</a>
							</div><!-- /right btn -->

						</div>

					</div>
					<!-- /CALLOUT -->
					
					<div class="divider half-margins"><!-- divider 30px --></div>

					<!-- BILLING and SHIPPING ADDRESS -->
					<div class="row">
						<div class="col-md-6 col-sm-6">
							<h5><strong>BILLING ADDRESS</strong></h5>
							<p>
								<span class="block">Country: United States</span>
								<span class="block">Name: John Doe</span>
								<span class="block">Company Name: -</span>
								<span class="block">Address: 5th Avenue</span>
								<span class="block">City: New York</span>
							</p>
						</div>

						<div class="col-md-6 col-sm-6">
							<h5><strong>SHIPPING ADDRESS</strong></h5>
							<p>
								<span class="block">Country: United States</span>
								<span class="block">Name: John Doe</span>
								<span class="block">Company Name: -</span>
								<span class="block">Address: 5th Avenue</span>
								<span class="block">City: New York</span>
							</p>
						</div>						
					</div>
					<!-- /BILLING and SHIPPING ADDRESS -->

					<div class="divider half-margins"><!-- divider 60px --></div>

					<!-- SUMMARY TABLE -->
					<div id="cartContent">
						<!-- cart header -->
						<div class="item head">
							<span class="cart_img"></span>
							<span class="product_name fsize13 bold">PRODUCT NAME</span>
							<span class="total_price fsize13 bold">TOTAL</span>
							<span class="qty fsize13 bold">QUANTITY</span>
							<div class="clearfix"></div>
						</div>
						<!-- /cart header -->

						<!-- cart item -->
						<div class="item">
							<div class="cart_img"><img width="60" alt="" src=""></div>
							<a class="product_name" href="shop-single.html">Man shirt XL</a>
							<div class="total_price">$<span>21.00</span></div>
							<div class="qty">1 x $21.00</div>
							<div class="clearfix"></div>
						</div>
						<!-- /cart item -->

						<!-- cart item -->
						<div class="item">
							<div class="cart_img"><img width="60" alt="" src=""></div>
							<a class="product_name" href="shop-single.html">Great Black Shoes For Man and Only Man...</a>
							<div class="total_price">$<span>32.00</span></div>
							<div class="qty">1 x $32.56</div>
							<div class="clearfix"></div>
						</div>
						<!-- /cart item -->

						<!-- cart item -->
						<div class="item">
							<div class="cart_img"><img width="60" alt="" src=""></div>
							<a class="product_name" href="shop-single.html">Pink Lady Perfect Shoes</a>
							<div class="total_price">$<span>67.00</span></div>
							<div class="qty">1 x $67.19</div>
							<div class="clearfix"></div>
						</div>
						<!-- /cart item -->


						<!-- cart total -->
						<div class="total pull-right">
							<small>
								Shipping: $12.00
							</small>

							<span class="totalToPay styleSecondColor">
								TOTAL: $132.00
							</span>

						</div>
						<!-- /cart total -->

						<div class="clearfix"></div>
					</div>
					<!-- /SUMMARY TABLE -->


				</div>

		





		</div>
		<!-- close row-fluid--> 
	</div>
	<!--close .container "main-content" -->
	
	<!-- http://theme.stepofweb.com/Alkaline/v1.0/shop-history-summary.html -->