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


	<div id="main-content" class="container clearfix">
		<div class="row-fluid">
			<div class="span12">
				   <div class="span6">

					<div class="box">
						<span class="box-title">
							<p><font color="#FF8C00"><s:message code="label.customer.myaccount" text="My account"/></font></p>
						</span>
						<ul>
							<li><a href="<c:out value="/customer/account.html"/>"><s:message code="menu.profile" text="Profile"/></a></li>
							<li><a href="<c:out value="/customer/billing.html"/>"><s:message code="label.customer.billingshipping" text="Billing & shipping information"/></a></li>
						</ul>
					</div>


				   </div>
				   <div class="span6">
					<div class="box">
						<span class="box-title">
							<p><font color="#FF8C00"><s:message code="label.order.pastorders" text="Past orders"/></font></p>
						</span>
						<ul>
							<li><s:message code="label.order.recent" text="Recent orders"/></li>
						</ul>
					</div>

				   </div>
			</div>
		</div>
		<!-- close row-fluid--> 
	</div>
	<!--close .container "main-content" -->