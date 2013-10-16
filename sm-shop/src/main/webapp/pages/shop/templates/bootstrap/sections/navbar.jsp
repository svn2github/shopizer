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
<%@ taglib uri="/WEB-INF/shopizer-functions.tld" prefix="display" %>  

<script type="text/javascript">

$(document).ready(function() { 

	var autoCompleteUrl = '/shop/services/search/' + getMerchantStoreCode() + '/' + getLanguageCode() + '/autocomplete.html';
	$('#search').searchAutocomplete({
		url: '<c:url value="/shop/services/search/{store}/{language}/autocomplete.html"/>';
     });

});

</script>

<c:set var="req" value="${request}" />
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


            <!-- Start Navbar-->
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


						<nav id="menu" class="pull-right">
                    					<ul id="mainMenu">
											<li class="">  
	                    					       <a href="<c:url value="/shop"/>" class="current">          
	                    					            <span class="name"><s:message code="menu.home" text="Home"/></span>     
	                    								<span class="desc"><s:message code="menu.home" text="Home"/></span>                                  
	                    						   </a>                         
	                    					</li>
	
	                    		            
	                    		            <c:forEach items="${requestScope.TOP_CATEGORIES}" var="category">
	    										<li class="">
	    											<a href="<c:url value="/shop/category/${category.description.seUrl}.html/ref=${category.id}"/>" class="current"> 
	    												<span class="name">${category.description.name}</span>
	    												<span class="desc">${category.description.categoryHighlight}</span> 
	    											</a>
	    										</li> 
											</c:forEach>
                    		            </ul>
						</nav>

				</div>
            </div>
            		
			<div class="row-fluid">
					<div style="padding-top: 4px;margin-right:8px;" class="btn-group pull-right">
						<form id="searchForm" class="form-inline">
 							<input id="search" type="text" placeholder="" class="input-medium">
							<button class="btn" type="submit">Search</button>
						</form>
					</div>
			</div>
			<!-- End Navbar-->