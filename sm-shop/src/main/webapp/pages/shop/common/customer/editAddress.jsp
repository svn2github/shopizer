<%
    response.setCharacterEncoding( "UTF-8" );
    response.setHeader( "Cache-Control", "no-cache" );
    response.setHeader( "Pragma", "no-cache" );
    response.setDateHeader( "Expires", -1 );
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<script src="<c:url value="/resources/js/address.js" />"></script>
<c:url var="updateAddress" value="/shop/customer/updateAddress.html"/>
<div id="main-content" class="container clearfix">
	<div class="row-fluid">
		<div class="span12">
			<div class="span8">

				<div class="box">
					<span class="box-title">
						<p>
							<font color="#FF8C00">
							  <c:choose>
							    <c:when test="${address.billingAddress eq true}">
							    	<s:message code="label.customer.edit.billingaddress" text="Edit Billing Address" />
							    </c:when>
							    <c:otherwise>
							    	<s:message code="label.customer.edit.shippinginformation" text="Edit Shipping Address" />
							    </c:otherwise>
							  </c:choose>
							 
							</font>
						</p>
					</span>
						<form:form method="POST" commandName="address" action="${updateAddress}">
				         <input type="hidden" name="customerId" value="${customerId}">
				         <input type="hidden" name="billingAddress" value="${address.billingAddress}">
				       
					<form:errors id="address.error" path="*" cssClass="alert alert-error" element="div" />
					<form:hidden path="${billingAddress}"/>
					
			        <div class="control-group">
	                        <label><s:message code="label.customer.firstname" text="First Name"/></label>
	                        <div class="controls">
	                        		<form:input  cssClass="input-large highlight"  maxlength="64"  path="firstName"/>
	                                <span class="help-inline"><form:errors path="firstName" cssClass="error" /></span>
	                        </div>
	                </div>
	                <div class="control-group">
	                        <label><s:message code="label.customer.lastname" text="Last Name"/></label>
	                        <div class="controls">
	                        		<form:input  cssClass="input-large highlight"  maxlength="64"  path="lastName"/>
	                                <span class="help-inline"><form:errors path="lastName" cssClass="error" /></span>
	                        </div>
	                </div>


					<address>
					
						<div class="controls">
		              		<label><s:message code="label.customer.company" text="Company"/></label>
		              		<form:input  cssClass="input-large"  maxlength="100" path="company"/>	
			            </div>

			            <div class="controls">
			            	<label><s:message code="label.customer.streetaddress" text="Street Address"/></label>
				 			<form:input  cssClass="input-large highlight"  maxlength="256"  path="address"/>		 				
			            </div>
			            <div class="controls">
			            	<label><s:message code="label.customer.city" text="City"/></label>
				 			<form:input  cssClass="input-large highlight"  maxlength="100" path="city"/>
			            </div>
		            
 	 		            <div class="control-group">
	                        <label><s:message code="label.customer.country" text="Country"/></label>
	                        <div class="controls"> 				       							
	       							<form:select cssClass="billing-country-list" path="country" id="registration_country">
		  								<form:options items="${countries}" itemValue="isoCode" itemLabel="name"/>
	       							</form:select>
                                 	<span class="help-inline"><form:errors path="country" cssClass="error" /></span>
	                        </div>  
	                    </div> 
	                 
	                    <div class="control-group"> 
	                        <label><s:message code="label.customer.zone" text="State / Province"/></label>
	                        <div class="controls">		       							
	       							<form:select cssClass="billing-zone-list" path="zone" id="registration_zones"/>
                      				<form:input  class="input-large highlight" id="hidden_registration_zones" maxlength="100"  name="bilstateOther" path="zone" />		       							
                                 	<span class="help-inline"><form:errors path="zone" cssClass="error" /></span>
	                        </div>
	                    </div>  
	                  
	                    <div class="controls">
	                   		<label><s:message code="label.customer.postalcode" text="Postal code"/></label>
			 				<form:input id="billingPostalCode" cssClass="input-large highlight" maxlength="20"  path="postalCode"/>
			 				<span class="help-inline"><form:errors path="postalCode" cssClass="error" /></span>
			            </div>	     
		              	            	            	            				
				  </address>		
					
	                  
	                   <div class="control-group">
	                        <label><s:message code="label.customer.telephone" text="Phone"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large highlight"  maxlength="32" path="phone" />
	                                    <span class="help-inline"><form:errors path="phone" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	               

		  	 <!--</div>--> 
		        <div class="form-actions">
                 	  <div class="pull-right">
                 			<button type="submit" class="btn btn-success"><s:message code="button.label.save" text="Save"/></button>
                 	  </div> 
           	   </div>


      					
				</form:form>
				</div>

				
			</div>
		</div>
	</div>
	<!-- close row-fluid-->
</div>
<!--close .container "main-content" -->
<script>
$(document).ready(function() {
	
	getZones($('#registration_country').val(),'<c:out value="${address.zone}" />');
	$("#hidden_registration_zones").hide();
	$("#registration_country").change(function() {
			getZones($(this).val(),'<c:out value="${address.zone}" />');
	})
	
	
	
	
});
</script>