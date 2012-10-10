<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>   

<%@ page session="false" %>


    <link href="<c:url value="/resources/css/bootstrap/css/datepicker.css" />" rel="stylesheet"></link>
	<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>




<div class="tabbable">


				<jsp:include page="/common/adminTabs.jsp" />
				
				<h3><s:message code="label.store.title" text="Merchant store" /></h3>	
				<br/>



				<c:choose>
				
					<c:when test="${store.storeLogo!=null && store.storeLogo!=''}">
						<a href="<sm:contentImage imageName="${store.storeLogo}" merchant="${store}"/>"/ width="200">
						<br/>
						Delete control
					</c:when>
					<c:otherwise>
					
					
						<c:url var="merchant" value="/admin/store/saveBranding.html"/>
						<form:form method="POST" commandName="store" action="${merchant}">
				
							<form:errors path="*" cssClass="alert alert-error" element="div" />

	      					<div class="control-group">
	                        	<label class="required"><s:message code="label.storelogo" text="Logo"/></label>
	                        	<div class="controls">
	                        			<input type="file" id="file" > 
	                                    <span class="help-inline">
	                                    <form:errors path="storeLogo" cssClass="error" /></span>
	                       		 </div>
	                  		</div>
	                  	                  
	                  		<form:hidden path="id" />
				      		<div class="form-actions">
	                  			<div class="pull-right">
	                  				<button type="submit" class="btn btn-success"><s:message code="button.label.submit2" text="Submit"/></button>
	                  			</div>
	            			 </div>
						</form:form>
					
					
					
					</c:otherwise>
				</c:choose>

				

				
				
				
				

				
				
				
				


</div>