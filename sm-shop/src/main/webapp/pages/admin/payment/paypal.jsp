<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>		



                 
                 <div class="control-group">
                        <label class="required"><s:message code="module.payment.paypal.userid" text="Paypal user id"/></label>
	                        <div class="controls">
	                        		<form:input cssClass="input-large highlight" path="integrationKeys['account']" />
	                        </div>
	                        <span class="help-inline">
	                        	<c:if test="${identifier!=null}">
	                        	<span id="identifiererrors" class="error"><s:message code="module.payment.paypal.message.identifier" text="Field in error"/></span>
	                        	</c:if>
	                        </span>
                  </div>
                  
                   <div class="control-group">
                        <label class="required"><s:message code="module.payment.paypal.apikey" text="Paypal API key"/></label>
	                        <div class="controls">
									<form:input cssClass="input-large highlight" path="integrationKeys['apikey']" />
	                        </div>
	                        <span class="help-inline">
	                        	<c:if test="${apikey!=null}">
	                        		<span id="apikeyerrors" class="error"><s:message code="module.payment.paypal.message.api" text="Field in error"/></span>
	                        	</c:if>
	                        </span>
                  </div>


                  
            
            
                  
                  