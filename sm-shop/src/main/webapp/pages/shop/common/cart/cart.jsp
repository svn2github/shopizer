<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<c:url value="/shop/removeShoppingCartItem.html"
	var="removeShoppingCartItemUrl" />
<c:url value="/shop/updateShoppingCartItem.html"
	var="updateShoppingCartItemUrl" />

<br />
<br />
<div class="row-fluid show-grid">

<table class="table table-bordered table-striped">

	<c:if test="${not empty cart}">
	   <c:choose>
	     <c:when test="${not empty cart.shoppingCartItems}">

			<c:forEach items="${cart.shoppingCartItems}" var="shoppingCartItem"
				varStatus="itemStatus">
				<c:if test="${itemStatus.index eq 0}">
					<thead>
						<tr>
							<th colspan="2" width="55%">Item</th>
							<th colspan="2" width="15%">Quantity</th>
							<th width="15%">Price</th>
							<th width="15%">Total</th>
						</tr>
					</thead>
					<tbody>
				</c:if>
				<form:form action="${updateShoppingCartItemUrl}"
					id="shoppingCartLineitem_${shoppingCartItem.id}">
					<tr>
						<td width="10%">image</td>

						<td>${shoppingCartItem.name}</td>
						<input type="hidden" name="lineItem_${itemStatus.index}"
							id="lineItem_${itemStatus.index}" value="${shoppingCartItem.id}" />
						<td><input type="text" class="input-small" placeholder="qty"
							value="${shoppingCartItem.quantity}" name="quantity"></td>
						<td><button class="close"
								onclick="javascript:updateLineItem('${shoppingCartItem.id}','${removeShoppingCartItemUrl}');">&times;</button>&nbsp;&nbsp;
							<button class="close"
								onclick="javascript:updateLineItem('${shoppingCartItem.id}','${updateShoppingCartItemUrl}');"
								title="Update Quantity">&#10003;</button></td>

						<td><strong>${shoppingCartItem.price}</strong></td>
						<td><strong>${shoppingCartItem.subTotal}</strong></td>


						<input type="hidden" name="lineItemId" id="lineItemId"
							value="${shoppingCartItem.id}" }"/>


					</tr>
				</form:form>


			</c:forEach>
			<tr class="subt">
				<td colspan="2">&nbsp;</td>
				<td colspan="3"><strong>Sub-total</strong></td>
				<td><strong>${cart.subTotal}</strong></td>
			</tr>

			<tr class="subt">
				<td colspan="2">&nbsp;</td>
				<td colspan="3"><strong>Grand total</strong></td>
				<td><strong>${cart.total}</strong></td>
			</tr>
		</c:when>
		 <c:otherwise>
		   <tr><td><s:message code="cart.empty" text="Your Shopping cart is empty" /></td></tr>
		 </c:otherwise>
	   </c:choose>
		
        
	</c:if>


	</tbody>
</table>
<c:if test="${not empty cart}">
<c:if test="${not empty cart.shoppingCartItems}">
<div class="pull-right">
	<div class="form-actions">
		<button type="submit" class="btn">Recalculate</button>
		<button type="submit" class="btn btn-success">Place order</button>
	</div>
</div>
</c:if>
</c:if>
</div>



