<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
				
<script>
	

	
</script>


<div class="tabbable">

  					
 					<jsp:include page="/common/adminTabs.jsp" />
  					
  					 <div class="tab-content">
  					
  					
  					<div class="tab-pane active" id="shipping-methods">


								<div class="sm-ui-component">
								<h3><s:message code="label.shipping.options" text="Shipping options" /></h3>	
								<br/>
								

							<c:url var="saveShippingOptions" value="/admin/shipping/saveShippingOptions.html"/>
							<form:form method="POST" commandName="configuration" action="${saveShippingOptions}">

      							
      								<form:errors path="*" cssClass="alert alert-error" element="div" />
									<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
								

									<div class="control-group">
                        				<label><s:message code="label.shipping.taxonshipping" text="Apply tax on shipping" /></label>
                        				<div class="controls">
                                    		<form:checkbox id="taxOnShipping" path="taxOnShipping" />
                        				</div>
                  					</div>
                  					<div class="control-group">
                        				<label><s:message code="label.shipping.freeshipping" text="Apply free shipping"/></label>
                        				<div class="controls">
                                    		<form:checkbox id="freeShippingEnabled" path="freeShippingEnabled" /><br/>
                                    		<form:radiobutton id="shipFreeType" path="shipFreeType" value="NATIONAL"/>&nbsp;<s:message code="label.shipping.national" text="National" />			
											<form:radiobutton id="shipFreeType" path="shipFreeType" value="INTERNATIONAL"/>&nbsp;<s:message code="label.shipping.international" text="International" />
											<form:input cssClass="input-large" path="orderTotalFreeShippingText" />&nbsp;<s:message code="label.shipping.freeshippingamount" text="Order total over" />
                        				</div>
                        				<span class="help-inline"><form:errors path="orderTotalFreeShippingText" cssClass="error" /></span>
                  					</div>
                  					
                  					<div class="control-group">
                        				<label><s:message code="label.shipping.handlingfees" text="Handling fees"/></label>
                        				<div class="controls">
											<form:input cssClass="input-large" path="handlingFeesText" />
                        				</div>
	                                	<span class="help-inline"><form:errors path="handlingFeesText" cssClass="error" /></span>
	                        		</div>

	                        		<div class="form-actions">
                  						<div class="pull-right">
                  							<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
                  						</div>
            	 					</div>
					                  

            	 			</form:form>
							
							


      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>