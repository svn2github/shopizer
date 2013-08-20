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



		<div style="position:relative;">
			<div style="width:20%; position:absolute; right:0">
					<img src="<c:url value="/resources/img/important-icon.png"/>" width="120">
			</div>
			<div style="width:80%; position:absolute; left:0">
					<strong></strong><s:message code="message.resource.notfound=" text="Page not found"/></strong>
			</div>
		</div>


		
