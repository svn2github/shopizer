<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
				

<div class="tabbable">

  					
  					<jsp:include page="/common/adminTabs.jsp" />


  					<div class="tab-content">

    					<div class="tab-pane active" id="shipping-section">
    					
    							<div class="sm-ui-component">
    							<a href="<c:url value="/admin/shipping/weightBased.html"/>">Back</a><br/><br/>
								<h3><s:message code="module.shipping.weightBased" text="module.shipping.weightBased" /> - <c:out value="${customRegion.customRegionName}" /></h3>	
								<br/>

								<c:url var="addPrice" value="/admin/shipping/weightBased/addPrice.html"/>
								<form:form method="POST" commandName="customQuote" action="${addPrice}">

      							
      								<form:errors path="*" cssClass="alert alert-error" element="div" />
									<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
								
								
								    <div class="control-group">
                        				<label><s:message code="label.shipping.maximumWeight" text="Maximum weight" /></label>
                        				<div class="controls">
											<input type="text" class="span3" name="maximumWeight" id="maximumWeight">
                        				</div>
	                                	<span class="help-inline"><form:errors path="maximumWeight" cssClass="error" /></span>
	                        		</div>
	                        		
	                        		<div class="control-group">
                        				<label><s:message code="label.generic.price" text="Price" /></label>
                        				<div class="controls">
											<input type="text" class="span3" name="priceText" id="priceText">
                        				</div>
	                                	<span class="help-inline"><form:errors path="priceText" cssClass="error" /></span>
	                        		</div>
	                        		
	                        		<div class="form-actions">
                  						<div class="pull-right">
                  							<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
                  						</div>
            	 					</div>
								
								
								</form:form>


								
								<br/>
								
			      				
			      				
								 <!-- Listing grid include -->
								 <c:set value="/admin/shipping/weightBasedDetails/page.html?region=${customRegion.customRegionName}" var="pagingUrl" scope="request"/>
								 <c:set value="/admin/shipping/weightBasedDetails/remove.html?region=${customRegion.customRegionName}" var="removeUrl" scope="request"/>
								 <c:set var="entityId" value="maximumWeight" scope="request"/>
								 <c:set var="componentTitleKey" value="module.shipping.weightBased" scope="request"/>
								 <c:set var="gridHeader" value="/pages/admin/shipping/weightBasedDetails-gridHeader.jsp" scope="request"/>
								 <c:set var="canRemoveEntry" value="true" scope="request"/>
				
				            	 <jsp:include page="/pages/admin/components/list.jsp"></jsp:include> 
								 <!-- End listing grid include -->
			      				
			      				


      					</div>


   					</div>


  				</div>

			</div>