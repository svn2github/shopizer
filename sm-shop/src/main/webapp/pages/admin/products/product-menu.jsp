<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>

    <ul class="nav nav-pills">
    
    
    					
    
    	<li <c:if test="${page.details!=null}/>"class="enabled"</if>><a href="<c:url value="/admin/products/editProduct.html" />?id=<c:out value="${productId}"/>"><s:message code="label.product.details" text="Product details" /></a></li>
    	<li <c:if test="${page.prices!=null}/>class="enabled"</if>><a href="<c:url value="/admin/products/prices.html" />">?id=<c:out value="${productId}"/>"><s:message code="label.product.prices" text="Product prices" /></a></li>
    	<li <c:if test="${page.attributes!=null}/>class="enabled"</if>><a href="<c:url value="/admin/products/attributes/list.html" />?id=<c:out value="${productId}"/>"><s:message code="label.product.attributes" text="Attributes" /></a></li>
    	<li <c:if test="${page.reviews!=null}/>class="enabled"</if>><a href="<c:url value="/admin/products/reviews.html" />?id=<c:out value="${productId}"/>"><s:message code="label.product.customer.reviews" text="Reviews" /></a></li>
    	<li <c:if test="${page.categories!=null}/>class="enabled"</if>><a href="<c:url value="/admin/products/displayProductToCategories.html" />?id=<c:out value="${productId}"/>"><s:message code="menu.product.category" text="Associate to categories" /></a></li>
    
    </ul>