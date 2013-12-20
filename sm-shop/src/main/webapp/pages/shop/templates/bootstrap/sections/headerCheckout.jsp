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
						<li class="active"><a href="<c:url value="/shop"/>"><s:message code="menu.home" text="Home"/></a></li>
						<c:forEach items="${requestScope.CONTENT_PAGE}" var="content">
    							<li class="">
    								<a href="<c:url value="/shop/pages/${content.seUrl}.html"/>" class="current"> 
    									<span class="name">${content.name}</span> 
    								</a>
    							</li>
						</c:forEach>
						<c:if test="${requestScope.CONFIGS['displayContactUs']==true}">
						<li><a href="<c:url value="/shop/contact/contactus.html"/>"><s:message code="label.customer.contactus" text="Contact us"/></a></li>
						</c:if>
					</ul>


					
					<c:if test="${requestScope.CUSTOMER!=null}">
						<a href="<c:url value="/customer/dashboard.html"/>"><s:message code="label.generic.welcome" text="Welcome" /> <c:out value="${requestScope.CUSTOMER.firstname}"/> <c:out value="${requestScope.CUSTOMER.lastname}"/></a>
					</c:if>

			</div>
			
			
			<!-- End main menu -->
			<div class="row-fluid show-grid">

				<div class="span12">
					<nav class="pull-left logo">
						 <c:choose>
                		<c:when test="${requestScope.MERCHANT_STORE.storeLogo!=null}">
                			<img class="logoImage" src="<sm:storeLogo/>" />
                		</c:when>
                		<c:otherwise>
                			<h1>
                			<a href="<c:url value="/shop/"/>">
                				<c:out value="${requestScope.MERCHANT_STORE.storename}"/>
                			</a>  
                			</h1>
                		</c:otherwise>
                	  </c:choose>  
					</nav>
				 </div>
			 </div>