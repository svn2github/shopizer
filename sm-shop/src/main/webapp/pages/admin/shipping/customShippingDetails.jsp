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
								<h3><s:message code="module.shipping.weightBased" text="module.shipping.weightBased" /> - <c:out value="${configuration.customRegionName}" /></h3>	
								<br/>

								<form action="<c:url value="/admin/shipping/weightBased/addPrice.html"/>">  
			      					<label class="required"><s:message code="label.shipping.addregion" text="Add region" /></label>
			      					<input type="text" class="span3" name="region" id="region" onblur="validatecode()"><!-- must be unique -->  
			      					<span class="help-inline"><div id="checkCodeStatus" style="display:none;"></span>  
			      					<br/>   
			      					<button type="submit" class="btn-region btn-action"><s:message code="button.label.submit2" text="Submit"/></button>
			      				</form>	
								
								<br/>
								
			      				
			      				
								 <!-- Listing grid include -->
								 <c:set value="/admin/shipping/weightBased/details/page.html" var="pagingUrl" scope="request"/>
								 <c:set value="/admin/shipping/weightBased/details/remove.html?region=${configuration.customRegionName}" var="removeUrl" scope="request"/>
								 <c:set value="/admin/shipping/weightBased/edit.html?region=${configuration.customRegionName}" var="refreshUrl" scope="request"/>
								 <c:set var="entityId" value="maximumWeight" scope="request"/>
								 <c:set var="componentTitleKey" value="module.shipping.weightBased" scope="request"/>
								 <c:set var="gridHeader" value="/admin/shipping/weightBasedDetails-gridHeader.jsp" scope="request"/>
								 <c:set var="canRemoveEntry" value="true" scope="request"/>
				
				            	 <jsp:include page="/pages/admin/components/list.jsp"></jsp:include> 
								 <!-- End listing grid include -->
			      				
			      				


      					</div>


   					</div>


  				</div>

			</div>