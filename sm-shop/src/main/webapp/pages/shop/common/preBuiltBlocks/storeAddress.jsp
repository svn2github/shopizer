<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>


<!-- 
//is content allowed
-->
<c:if test="${preBuiltBlocks["storeAddress"]!=null}">
 <address>  
 	<strong><c:out value="${requestScope.store.storename}"/></strong><br/>  
 	<c:out value="${requestScope.store.storeaddress}"/><br/>
 	<c:out value="${requestScope.store.storecity}"/>, <c:choose><c:when test="${requestScope.store.storestateprovince!=null}"><c:out value="${requestScope.store.storestateprovince}"/></c:when><c:otherwise><c:out value="${requestScope.store.zone.code}"/></c:otherwise></c:choose> <c:out value="${requestScope.store.zone.storepostalcode}"/><br/>
 	<abbr title="Phone"><s:message code="label.generic.phone" text="Phone" />:</abbr><c:out value="${requestScope.store.storephone}"/>
 </address> 
</c:if>