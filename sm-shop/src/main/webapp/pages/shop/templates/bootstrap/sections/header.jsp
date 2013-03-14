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
						<li class="active"><a href="http://#previews/WB0M3G9S1/index.html" title="Bitsy Shop">Home</a></li>
						<li><a href="http://#previews/WB0M3G9S1/products.html" title="All specials">Specials</a></li>
						<li><a href="http://#previews/WB0M3G9S1/contact.html" title="Contact">Contact</a></li>						
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


					<div class="btn-group pull-right" style="padding-top: 8px;padding-right:12px;">
					<ul class="nav" style="z-index:500000;">
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
					</div><!--
					http://mifsud.me/adding-dropdown-login-form-bootstraps-navbar/
					
					$(function() {
					  // Setup drop down menu
					  $('.dropdown-toggle').dropdown();
					 
					  // Fix input element click problem
					  $('.dropdown input, .dropdown label').click(function(e) {
					    e.stopPropagation();
					  });
					});
					
					<div class="navbar navbar-fixed-top">
						  <div class="navbar-inner">
						    <div class="container"> Collapsable nav bar 
						      <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
						        <span class="icon-bar"></span>
						        <span class="icon-bar"></span>
						        <span class="icon-bar"></span>
						      </a>
						 
						       Your site name for the upper left corner of the site 
						      <a class="brand">My Site</a>
						 
						       Start of the nav bar content 
						      <div class="nav-collapse"> Other nav bar content 
						        ...
						        ...
						 
						         The drop down menu 
						        <ul class="nav pull-right">
						          <li><a href="/users/sign_up">Sign Up</a></li>
						          <li class="divider-vertical"></li>
						          <li class="dropdown">
						            <a class="dropdown-toggle" href="#" data-toggle="dropdown">Sign In <strong class="caret"></strong></a>
						            <div class="dropdown-menu" style="padding: 15px; padding-bottom: 0px;">
						               
						               
						               <form action="[YOUR ACTION]" method="post" accept-charset="UTF-8">
										  <input id="user_username" style="margin-bottom: 15px;" type="text" name="user[username]" size="30" />
										  <input id="user_password" style="margin-bottom: 15px;" type="password" name="user[password]" size="30" />
										  <input id="user_remember_me" style="float: left; margin-right: 10px;" type="checkbox" name="user[remember_me]" value="1" />
										  <label class="string optional" for="user_remember_me"> Remember me</label>
										 
										  <input class="btn btn-primary" style="clear: left; width: 100%; height: 32px; font-size: 13px;" type="submit" name="commit" value="Sign In" />
										</form>
						               
						               
						               
						            </div>
						          </li>
						        </ul>
						      </div>
						    </div>
						  </div>
					</div>
					
					


	--></div>
</div>




<!--






	<div class="row">

  		<div class="span4">
  				<a class="brand" href="#">
  					<h1>Store name</h1>
  				</a>
  		</div>
  		<div class="span4 offset4">
	    <div class="btn-group pull-right">

	    	French | English

	    
	    
		<i class="icon-shopping-cart icon-white"></i>
		<a id="open-cart" class="open noboxshadow dropdown-toggle" data-toggle="dropdown" href="#" style="box-shadow:none;">My Cart</a> 
		<span id="cartinfo">
			<span id="cartqty">(0 items)</span>&nbsp;<span id="cartprice">$0.00</span>
		</span> 
		<a id="close-cart" style="display: none;" class="close" href="#">Close Panel</a>
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
   </div>-->