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
<c:set var="ordersAction" value="${pageContext.request.contextPath}/shop/customer/customer-orders.html"/>
<c:set var="customerOrder" value="${pageContext.request.contextPath}/shop/customer/order.html"/>

	<div id="main-content" class="container clearfix">
		<div class="row-fluid">

<div class="white">

				<header class="page-header">
					<h1>Order History</h1>
				</header>
               <c:choose>
                 <c:when test="${not empty customerOrders.orders}">
                 	<div id="shop">

					<!-- HISTORY TABLE -->
					<table class="table table-striped">
						<!-- table head -->
						<thead>
							<tr>
								<th>#</th>
								<th>Order Id</th>
								<th>Order Date</th>
								<th>Amount</th>
								<th>Status</th>
							</tr>
						</thead>
						
						<!-- table items -->
						<tbody>
						<c:forEach items="${customerOrders.orders}" var="order" varStatus="orderStatus">
							<tr><!-- item -->
								<td>${orderStatus.index+1}</td>
								<td><a href="${customerOrder}?orderId=${order.id}">${order.id}</a></td>
								<td><fmt:formatDate type="both" value="${order.datePurchased}" /></td>
								<td><sm:monetary value="${order.total.value}" /><small>(${fn:length(order.products)} items)</small></td>
								<td>${order.orderStatus}</td>
								
							</tr>
						</c:forEach>
							
						</tbody>
					</table>
					<!-- /HISTORY TABLE -->


					<div class="divider half-margins"><!-- divider 30px --></div>

					
					<!-- PAGINATION -->
					<div class="row">
						<div class="col-md-6 text-left">
							<p class="hidden-xs pull-left nomargin padding20">Showing ${(paginationData.offset)}-${((paginationData.offset)-1)+(paginationData.pageSize) } of ${paginationData.totalCount} results.</p>
						</div>

						<div class="col-md-6 responsive-text-center text-right">
							<ul class="pagination">
								<li><a href="#"><i class="fa fa-chevron-left"></i></a></li>
								<c:forEach begin="1" end="${paginationData.totalPages}" varStatus="paginationDataStatus">
								    <li class="${paginationData.currentPage eq (paginationDataStatus.index) ? 'active' : ''}"><a href="${ordersAction}?page=${paginationDataStatus.index}">${paginationDataStatus.index}</a></li>
								</c:forEach>
								<li><a href="#"><i class="fa fa-chevron-right"></i></a></li>
							</ul>
						</div>

					</div>
					<!-- /PAGINATION -->
				

				</div>
                 </c:when>
                 <c:otherwise>
                 
                 </c:otherwise>
               
               </c:choose>
				

			</div>





		</div>
		<!-- close row-fluid--> 
	</div>
	<!--close .container "main-content" -->
	
	<!-- http://theme.stepofweb.com/Alkaline/v1.0/shop-history-summary.html -->