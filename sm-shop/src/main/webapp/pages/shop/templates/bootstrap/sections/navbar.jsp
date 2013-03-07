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





<div class="row" id="mainmenu">
				<div class="span7">
					<ul id="links" class="nav nav-pills pull-left">
						<li class="active"><a href="http://wbpreview.com/previews/WB0M3G9S1/index.html" title="Bitsy Shop">Home</a></li>
						<li><a href="http://wbpreview.com/previews/WB0M3G9S1/products.html" title="All specials">Specials</a></li>
						<li><a href="http://wbpreview.com/previews/WB0M3G9S1/contact.html" title="Contact">Contact</a></li>						
					</ul>
				</div>
				<div class="span5">
					<div class="btn-group pull-right" style="padding-top: 8px;">

						<i class="icon-shopping-cart icon-black"></i>
						<a id="open-cart" class="open noboxshadow dropdown-toggle" data-toggle="dropdown" href="#" style="box-shadow:none;">My Cart</a> 
						<span id="cartinfo">
							<span id="cartqty">(0 items)</span>&nbsp;<span id="cartprice">$0.00</span>
						</span> 
			            <ul class="dropdown-menu minicart">
			              <li>
								<div class="cartbox">
									<div id="shoppingcart">
										<h3>Shopping Cart</h3><br/>
					
										<table class="table">
							
							
											<tr>
												<td></td>
												<td>Product 1</td>
												<td><button class="close">&times;</button></td>
											</tr>
											<tr>
												<td></td>
												<td>Product 1</td>
												<td><button class="close">&times;</button></td>
											</tr>	
										</table>
										<div class="total-box">
												<span style="float:right">
											
													<font class="total-box-label">Total&nbsp;<font class="total-box-price">$183.98 CAD</font></font>
						
												</span>
										</div>
										<br/>
					
										<div class="pull-right"> 
											<a class="btn btn-success" href="#"><i class="icon-shopping-cart icon-white"></i> Checkout</a> 
										</div>
					
								  </div>
							</div>
			
					  </li>
					</ul>
			</div>
	</div>
</div>



<!--<div class="navbar navbar-inverse" style="position: static;">
	<div class="navbar-inner">
		<div class="container">
			<a class="btn btn-navbar" data-target=".subnav-collapse"
				data-toggle="collapse"> <a class="brand" href="#">Title</a>
				<div class="nav-collapse subnav-collapse">
					<ul class="nav">
						<li class="active">
							<a href="#"><s:message code="menu.home" text="Home" /></a>
						</li>
						 Root categories 
						<li>
							<a href="#">Category 1</a>
						</li>
						<li>
							<a href="#">Category 2</a>
						</li>
					</ul>
					<form class="navbar-search pull-right" action="">
						<input class="search-query span2" type="text" placeholder="Search">
					</form>
					<ul class="nav pull-right">
						<li>
							<a href="#">Link</a>
						</li>
						<li class="divider-vertical"></li>
						<li class="dropdown">
							<a class="dropdown-toggle" data-toggle="dropdown" href="#">
								Logon <b class="caret"></b> </a>
							<ul class="dropdown-menu">
								<li>
									<a href="#">Action</a>
								</li>
								<li>
									<a href="#">Another action</a>
								</li>
								<li>
									<a href="#">Something else here</a>
								</li>
								<li class="divider"></li>
								<li>
									<a href="#">Separated link</a>
								</li>
							</ul>
						</li>
					</ul>
				</div>
		</div>
	</div>
</div>-->