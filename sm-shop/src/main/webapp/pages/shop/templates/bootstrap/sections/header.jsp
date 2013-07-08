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

			<!-- header -->
			<div id="mainmenu" class="row-fluid">
				
					<ul class="nav nav-pills pull-left" id="linkMenuLinks">
						<li class="active"><a href="index.html">Home</a></li>
						<c:forEach items="${requestScope.CONTENT_PAGE}" var="content">
    							<li class="">
    								<a href="<c:url value="/"/> class="current"> 
    									<span class="name">${content.name}</span> 
    								</a>
    							</li> 
						</c:forEach>
						<li><a href="#contactformlink">Contact</a></li>
					</ul>


					<div style="padding-top: 8px;padding-bottom:10px;" class="btn-group pull-right">
            					<i class="icon-shopping-cart icon-black"></i>
            					<a style="box-shadow:none;color:FF8C00;" href="#" data-toggle="dropdown" class="open noboxshadow dropdown-toggle" id="open-cart">My Cart</a>
           		 			    <span id="cartinfo">
           		 			    	<c:choose>
	           		 			    	<c:when test="${requestScope.SHOPPING_CART != null}">
	                  						<span id="cartqty">(<c:out value="${requestScope.SHOPPING_CART.quantity}"/>&nbsp;<c:choose><c:when test="${fn:length(requestScope.SHOPPING_CART.quantity)>1}" ><s:message code="label.generic.items" text="items" /></c:when><c:otherwise><s:message code="label.generic.item" text="item" /></c:otherwise></c:choose>)</span>&nbsp;<span id="cartprice"><c:out value="${requestScope.SHOPPING_CART.total}"/></span>
	                  					</c:when>
	                  					<c:otherwise>
	                  						<span id="cartqty">(0 <s:message code="label.generic.item" text="item" />)</span>
	                  					</c:otherwise>
                  					</c:choose>
            					</span>
            				<c:choose>
            					 
	           		 			 <c:when test="${requestScope.SHOPPING_CART != null}">
		            					<ul class="dropdown-menu minicart">
		              						<li>
		                  						<div class="cartbox" id="cart-box">
		                  							<div class="box-content clearfix">
		                  								<h3 class="lbw">Shopping Cart</h3>&nbsp;<span style="width:15%;display:none;" id="checkout-wait"><img src="img/misc/wait18trans.gif"></span><br>
				                  						<div id="shoppingcart">
				                  						<table style="margin-bottom: 5px" class="table">
				                  						<tbody><tr id="42" class="cart-product">
				                  						<td><img width="40" height="40" src="img/products/shirt1.jpg"></td>
				                  						<td>1 Short sleeves white</td><td>$29.99</td>
				                  						<td><button productid="42" class="close removeProductIcon">x</button></td>
				                  						</tr></tbody></table>
				                  						
				                  						<div style="padding-right:4px;" class="row">
				                  						<div class="pull-right">Shipping costs of $10.00 : $10.00</div>
				                  						</div><div class="total-box">
				                  						
				                  						<div class="pull-right"><font class="total-box-label">Total : <font class="total-box-price"><strong><span id="checkout-total-plus">$39.99</span></strong></font></font></div>
				                  						</div>
				                  						<br/>
				                  						<button class="btn" style="width:100%" type="submit">Check out</button>
				                  						</div>
			                  						</div>
		                  						</div>
		              						</li>
		            					</ul>
            					</c:when>
            					<c:otherwise>
            							<ul class="dropdown-menu minicart">
	                  						//NO ITEMS [TODO]
	                  					</ul>
	                  			</c:otherwise>
                  			</c:choose>
					
					</div>


			</div>
			<!-- End main menu -->