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



		                  						<div class="cartbox" id="cart-box">
		                  							<div class="box-content clearfix">
		                  								<h3 class="lbw">Shopping Cart</h3>
		                  								<br/>
				                  						<div id="shoppingcart">
				                  						<table style="margin-bottom: 5px" class="table">
				                  							<tbody id="shoppingcartProducts">
					                  							
				                  							</tbody>
				                  						</table>
				                  						
				                  						<div style="padding-right:4px;" class="row">
				                  							<div class="pull-right">Shipping costs of $10.00 : $10.00</div>
				                  						</div>
				                  						
				                  						<div class="total-box">
				                  							<div class="pull-right"><font class="total-box-label">Total : <font class="total-box-price"><strong><span id="checkout-total-plus">$39.99</span></strong></font></font></div>
				                  						</div>
				                  						<br/>
				                  						<button class="btn" style="width:100%" type="submit">Check out</button>
				                  						</div>
			                  						</div>
		                  						</div>