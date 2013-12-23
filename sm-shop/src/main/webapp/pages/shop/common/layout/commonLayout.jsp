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
 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  
 <%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  
 <c:set var="lang" scope="request" value="${requestScope.locale.language}"/> 
 
 
 <html xmlns="http://www.w3.org/1999/xhtml"> 
 
 
     <head>
        	 	<meta charset="utf-8">
    			<title>//TODO<s:message code="label.storeadministration" text="Store administration" /></title>
    			<meta name="viewport" content="width=device-width, initial-scale=1.0">
    			<meta name="description" content="">
    			<meta name="author" content="<c:out value="${requestScope.store.name}"/>">
    			<!-- The one provided with the system -->
    			<script src="<c:url value="/resources/js/bootstrap/jquery.js" />"></script>

  
                <jsp:include page="/pages/shop/templates/bootstrap/sections/shopLinks.jsp" />
 	</head>
 
 	<body>
 	
 	<div class="container">
		<tiles:insertAttribute name="header" ignore="true"/>



    
		<tiles:insertAttribute name="navbar" ignore="true"/> 


		<tiles:insertAttribute name="body"/>   

	 	 <tiles:insertAttribute name="footer" ignore="true"/>
	 	 
	</div>
	<jsp:include page="/pages/shop/templates/bootstrap/sections/jsLinks.jsp" />

 	</body>
 
 </html>
 
