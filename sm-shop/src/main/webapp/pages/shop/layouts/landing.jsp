<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
	
<%
 	StringBuffer path = new StringBuffer();
 	path.append("/pages/shop/templates/").append(request.getAttribute("templateId")).append("/pages/landing.jsp");
 	
%>
 	
<jsp:include page="<%=path.toString()%>" />