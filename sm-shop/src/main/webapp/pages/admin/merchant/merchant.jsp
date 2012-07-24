<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>




<div class="tabbable">


				<jsp:include page="/common/adminTabs.jsp" />


				<c:url var="merchant" value="/admin/merchant/save.html"/>


				<form:form method="POST" commandName="store" action="${merchant}">
				
					<form:errors path="*" cssClass="alert alert-error" element="div" />
					<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
				
	      			<div class="control-group">
	                        <label><s:message code="label.storename" text="Name"/></label>
	                        <div class="controls">
	                        		<form:input cssClass="input-large" path="storename" />
	                                    <span class="help-inline"><form:errors path="storename" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                  <div class="control-group">
	                        <label><s:message code="label.storephone" text="Phone"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large" path="storephone" />
	                                    <span class="help-inline"><form:errors path="storephone" cssClass="error" /></span>
	                        </div>
	
	                  </div>
	                  
	                 <div class="control-group">
	                        <label><s:message code="label.storeemailaddress" text="Email"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large" path="storeEmailAddress" />
	                                    <span class="help-inline"><form:errors path="storeEmailAddress" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                  <div class="control-group">
	                        <label><s:message code="label.country" text="Country"/></label>
	                        <div class="controls">
	                        					
	                        					<form:select path="country.id">
					  								<form:option value="-1" label="--- Select ---" />
					  								<form:options items="${countries}" itemValue="id" itemLabel="name"/>
				       							</form:select>
	                                   			<span class="help-inline"><form:errors path="country" cssClass="error" /></span>
	                        </div>
	                  </div>
				
				      <div class="form-actions">
	                  		<div class="pull-right">
	                  			<button type="submit" class="btn btn-action"><s:message code="button.label.submit2" text="Submit"/></button>
	                  		</div>
	            	 </div>


      					
				</form:form>

</div>