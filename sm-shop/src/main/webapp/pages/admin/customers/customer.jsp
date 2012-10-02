<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>




<div class="tabbable">


				<jsp:include page="/common/adminTabs.jsp" />
				
				<h3>
					<c:choose>
						<c:when test="${customer.id!=null && customer.id>0}">
								<s:message code="label.customer.editcustomer" text="Edit Customer" /> <c:out value="${category.code}"/>
						</c:when>
						<c:otherwise>
								<s:message code="label.customer.createcustomer" text="Create Customer" />
						</c:otherwise>
					</c:choose>
					
				</h3>	
				<br/>
				


				<c:url var="customer" value="/admin/customers/save.html"/>


				<form:form method="POST" commandName="customer" action="${customer}">
				
					<form:errors path="*" cssClass="alert alert-error" element="div" />
					<div id="customer.success" class="alert alert-success" 
							style="<c:choose>
								<c:when test="${success!=null}">display:block;</c:when>
								<c:otherwise>display:none;</c:otherwise></c:choose>">
								<s:message code="message.success" text="Request successful"/>
					</div>    
				
	      			<div class="control-group">
	                        <label><s:message code="label.customer.firstname" text="First Name"/></label>
	                        <div class="controls">
	                        		<form:input cssClass="input-large" path="firstname" />
	                                    <span class="help-inline"><form:errors path="firstname" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                  <div class="control-group">
	                        <label><s:message code="label.customer.lastname" text="Last Name"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large" path="lastname" />
	                                    <span class="help-inline"><form:errors path="lastname" cssClass="error" /></span>
	                        </div>
	
	                  </div>
	                  
	                 <div class="control-group">
	                        <label><s:message code="label.customer.email" text="Email"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large" path="emailAddress" />
	                                    <span class="help-inline"><form:errors path="emailAddress" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                   <div class="control-group">
	                        <label><s:message code="label.customer.telephone" text="Phone"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large" path="telephone" />
	                                    <span class="help-inline"><form:errors path="telephone" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                  <div class="control-group">
	                        <label><s:message code="label.customer.streetaddress" text="StreetAddress"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large" path="streetAddress" />
	                                    <span class="help-inline"><form:errors path="streetAddress" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                  <div class="control-group">
	                        <label><s:message code="label.customer.city" text="City"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large" path="city" />
	                                    <span class="help-inline"><form:errors path="city" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                  <div class="control-group">
	                        <label><s:message code="label.customer.postalcode" text="Postalcode"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large" path="postalCode" />
	                                    <span class="help-inline"><form:errors path="postalCode" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                  
	                  
	                  <div class="control-group">
	                        <label><s:message code="label.customer.zone" text="State"/></label>
	                        <div class="controls">
	                        					
	                        					<form:select path="zone.id">
					  								<form:option value="-1" label="--- Select ---" />
					  								<form:options items="${zones}" itemValue="id" itemLabel="code"/>
				       							</form:select>
	                                   			<span class="help-inline"><form:errors path="zone" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                  
	                  <div class="control-group">
	                        <label><s:message code="label.customer.country" text="Country"/></label>
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
	                  			<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
	                  		</div>
	            	 </div>


      					
				</form:form>

</div>