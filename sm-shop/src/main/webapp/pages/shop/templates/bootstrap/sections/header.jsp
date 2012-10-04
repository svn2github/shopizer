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

	<div class="row">

  		<div class="span4"><a class="brand" href="#"><h1>Project name</h1></a></div>
  		<div class="span4 offset4">
	    <div class="btn-group pull-right">
		<i class="icon-shopping-cart icon-white"></i>
		<a id="open-cart" class="open noboxshadow dropdown-toggle" data-toggle="dropdown" href="#" style="box-shadow:none;">My Cart</a> 
		<span id="cartinfo">
			<span id="cartqty">(0 items)</span>&nbsp;<span id="cartprice">$0.00</span>
		</span> 
		<!--<a id="close-cart" style="display: none;" class="close" href="#">Close Panel</a>-->
            <ul class="dropdown-menu minicart">
              <li>
			<!--
			<div class="box-content clearfix"  >
				<div class="box-title"><h1>Shopping Cart</h1></div> 
				<div class="leftfull" id="shoppingcart">Empty Cart <br/><br/> </div> 
			</div>
			--> 

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