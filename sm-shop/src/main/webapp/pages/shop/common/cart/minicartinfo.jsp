<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
  		 			    <span id="cartinfo">
           		 			    	<c:choose>
	           		 			    	<c:when test="${requestScope.SHOPPING_CART != null}">
	                  						<span id="cartqty">
	                  						 (<c:out value="${requestScope.SHOPPING_CART.quantity}"/>
	                  						  <c:choose>
	                  						      <c:when test="${requestScope.SHOPPING_CART.quantity gt 1}" >
	                  						     		<s:message code="label.generic.items" text="items" />
	                  						       </c:when>
	                  						      <c:otherwise>
	                  						         <s:message code="label.generic.item" text="item" />
	                  						        </c:otherwise>
	                  						     </c:choose>
	                  						    </span>)
	                  						    <span id="cartprice"><c:out value="${requestScope.SHOPPING_CART.total}"/></span>
	                  					</c:when>
	                  					<c:otherwise>
	                  						<span id="cartqty">(0&nbsp;<s:message code="label.generic.item" text="item" />)</span>
	                  					</c:otherwise>
                  					</c:choose>
            					</span>