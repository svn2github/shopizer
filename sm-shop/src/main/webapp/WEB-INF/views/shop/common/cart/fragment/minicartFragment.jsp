
<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>



<div class="cartbox" id="cart-box">
	<div class="box-content clearfix">
		<h3 class="lbw">Shopping Cart</h3>
		<br />
		
		<c:choose>
		 <c:when test="${not empty miniCartData}">
		 <c:if test="${not empty miniCartData.shoppingCartItems}">
		
		<div id="shoppingcart">
		
			<table style="margin-bottom: 5px" class="table">
				<tbody id="shoppingcartProducts">
                  
                    <c:forEach items="${miniCartData.shoppingCartItems}" var="shoppingCartItem" varStatus="itemStatus">
                       <tr id="${shoppingCartItem.productId}" class="cart-product">
                         <td>
                          <c:if test="${not empty shoppingCartItem.image}">
                            <img width="40" height="40" src="${shoppingCartItem.image}">
                          </c:if>
                         </td>
                         <td>${shoppingCartItem.name}</td>
                       
                         <td>${shoppingCartItem.price}</td>
                         <td><button productid="${shoppingCartItem.productId}" class="close removeProductIcon" onclick="javascript:removeItemFromMinicart('${shoppingCartItem.id}');">x</button></td>
                       </tr>
                    </c:forEach>
                
				</tbody>
			</table>

			<div style="padding-right: 4px;" class="row">
				<div class="pull-right">Shipping costs of ${miniCartData.total} : 0.0</div>
			</div>

			<div class="total-box">
				<div class="pull-right">
					<font class="total-box-label">Total : <font
						class="total-box-price"><strong><span
								id="checkout-total-plus">${miniCartData.total}</span></strong></font></font>
				</div>
			</div>
			<br />
			<button class="btn" style="width: 100%" type="submit">Check
				out</button>
		</div>
		</c:if>
		 <c:if test="${empty miniCartData.shoppingCartItems}">
		  	<s:message code="cart.empty" text="Your Shopping cart is empty" />
		  </c:if>
		</c:when>
		<c:otherwise>
		   
				<s:message code="cart.empty" text="Your Shopping cart is empty" />
		  
		</c:otherwise>
		</c:choose>
	</div>
</div>