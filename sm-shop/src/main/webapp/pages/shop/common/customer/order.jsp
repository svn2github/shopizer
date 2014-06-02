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


//	//$('SelectorToPrint').printElement();



	<div id="main-content" class="container clearfix">
		



				<header class="page-header">
					<h1><s:message code="label.order.details" text="Order details" />&nbsp;#&nbsp;<c:out value="${order.id}"/></h1>
				</header>

				
				<div class="row-fluid">
				<!-- note class 'printable' - this area is printable only! -->

					<!-- Buttons -->
					<div class="alert-custom">

						<div class="row-fluid">

							<div class="col-md-8 col-sm-8 pull-left"><!-- left text -->
								<!--
								<p class="nomargin">
									<fmt:formatDate type="both" dateStyle="long" value="${order.datePurchased}" />
								</p>
								<h3>
									<s:message code="label.order.${order.orderStatus.value}" text="${order.orderStatus.value}" />
								</h3>
								-->
							</div><!-- /left text -->

							
							<div class="col-md-4 col-sm-4 text-right pull-right">
								<a onclick="window.print();" class="btn btn-large" href="#"><s:message code="label.generic.print" text="Print" /></a>
							</div>

						</div>

					</div>
					
					<div id="printableOrder" class="row-fluid">
					
					
					<div class="row-fluid">
					
						<div class="col-md-12 col-sm-12 pull-left">
						
							
							<h2><s:message code="label.entity.order" text="Order" />&nbsp;#&nbsp;<c:out value="${order.id}"/><br/></h2>
							<p class="lead">
							<fmt:formatDate type="both" dateStyle="long" value="${order.datePurchased}" /><br/>
							<s:message code="label.order.${order.orderStatus.value}" text="${order.orderStatus.value}" />
							</p>
						</div>
					</div>

					<!-- BILLING and SHIPPING ADDRESS -->
					<div class="row-fluid">
						<div class="col-md-6 col-sm-6">
							<c:if test="${not empty order.billing}">
							<h5><strong><s:message code="label.customer.billingaddress" text="Billing address" /></strong></h5>
							<p>
								<c:set var="address" value="${order.billing}" scope="request" />
								<c:set var="addressType" value="billing" scope="request" />
								<jsp:include page="/pages/shop/common/preBuiltBlocks/customerAddress.jsp"/>
							</p>
							</c:if>
						</div>

						<div class="col-md-6 col-sm-6">
							<c:if test="${not empty order.delivery}">
							<h5><strong><s:message code="label.customer.shippingaddress" text="Shipping address" /></strong></h5>
							<p>
								<c:set var="address" value="${order.delivery}" scope="request" />
								<c:set var="addressType" value="delivery" scope="request" />
								<jsp:include page="/pages/shop/common/preBuiltBlocks/customerAddress.jsp"/>
							</p>
							</c:if>
						</div>						
					</div>
					<!-- /BILLING and SHIPPING ADDRESS -->

					<div id="orderTableTitle">
						<h2>
						<s:message code="label.entity.details" text="Details"/>
						</h2>
					</div>
		
					<!-- PRODUCTS TABLE -->
					<div id="cartContent">
						<!-- cart header -->
						<div class="item head">
							<span class="cartImage"></span>
							<span class="productName"><s:message code="label.productedit.productname" text="Product name" /></span>
							<span class="quantity"><s:message code="label.quantity" text="Quantity" /></span>
							<span class="totalPrice"><s:message code="label.generic.price" text="Price" /></span>
							<span class="subTotal"><s:message code="order.total.subtotal" text="Sub-total" /></span>
							<div class="clearfix"></div>
						</div>
						<!-- /cart header -->

						<!-- cart item -->
						<c:forEach items="${order.products}" var="product">
						<div class="item">
							<div class="cartImage">
							<c:if test="${not empty product.image}">
							<img width="60" src="<sm:shopProductImage imageName="${product.image}" sku="${product.sku}"/>"/>
							</c:if>
							</div>
							<c:choose>
							<c:when test="${product.product!=null}">
								<a class="productName" href="<c:url value="/shop/product/" /><c:out value="${product.product.description.friendlyUrl}"/>.html"><c:out value="${product.productName}"/></a>
							</c:when>
							<c:otherwise>
								<span class="productName"><c:out value="${product.productName}"/></span>
							</c:otherwise>
							</c:choose>
							
							<div class="quantity"><c:out value="${product.orderedQuantity}"/></div>
							<div class="totalPrice"><c:out value="${product.price}"/></div>
							<div class="subTotal"><c:out value="${product.subTotal}"/></div>
							<div class="clearfix"></div>
						</div>
						</c:forEach>
						<!-- /cart item -->


						<!-- cart total -->
						<div class="total pull-right">
							<c:forEach items="${order.totals}" var="orderTotal" varStatus="counter">
								<small class="totalItem">
									<c:if test="${orderTotal.code=='refund'}"><font color="red"></c:if><s:message code="${orderTotal.code}" text="${orderTotal.code}"/>:<c:if test="${orderTotal.code=='refund'}"></font></c:if>
									<span <c:if test="orderTotal.code=='total'">class="totalToPay"</c:if>><strong><c:if test="${orderTotal.code=='refund'}"><font color="red"></c:if><sm:monetary value="${orderTotal.value}" /><c:if test="${orderTotal.code=='refund'}"></font></c:if></strong></span>
								</small>
								<br/>
							</c:forEach>
						</div>
						<!-- /cart total -->

						<div class="clearfix"></div>
					</div>
					<!-- /SUMMARY TABLE -->


				</div>

		



	</div>
	<!--close .container "main-content" -->
