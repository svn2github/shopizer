<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>


<!-- 
//is content allowed
-->

 <address>  
 	<strong><c:out value="${requestScope.MERCHANT_STORE.storename}"/></strong><br/>  
 	<c:out value="${requestScope.MERCHANT_STORE.storeaddress}"/><br/>
 	<c:out value="${requestScope.MERCHANT_STORE.storecity}"/>, <c:choose><c:when test="${requestScope.MERCHANT_STORE.storestateprovince!=null}"><c:out value="${requestScope.MERCHANT_STORE.storestateprovince}"/></c:when><c:otherwise><c:out value="${requestScope.MERCHANT_STORE.zone.code}"/></c:otherwise></c:choose> <c:out value="${requestScope.MERCHANT_STORE.storepostalcode}"/><br/>
 	<abbr title="Phone"><s:message code="label.generic.phone" text="Phone" />:</abbr><c:out value="${requestScope.MERCHANT_STORE.storephone}"/>
 </address> 
