<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
				
<script>
	
function validateRegion() {
	$('#checkCodeStatus').html('<img src="<c:url value="/resources/img/ajax-loader.gif" />');
	$('#checkCodeStatus').show();
	var region = $("#region").val();
	var id = "";
	checkCode(region,id,'<c:url value="/admin/shipping/checkRegionCode.html" />');
}

function callBackCheckCode(msg,code) {
	
	if(code==0) {
		$('.btn-region').removeClass('disabled');
	}
	if(code==9999) {

		$('#checkCodeStatus').html('<font color="green"><s:message code="label.message.region.available" text="This region is available"/></font>');
		$('#checkCodeStatus').show();
		$('.btn-region').removeClass('disabled');
	}
	if(code==9998) {

		$('#checkCodeStatus').html('<font color="red"><s:message code="label.message.region.exist" text="This region already exist"/></font>');
		$('#checkCodeStatus').show();
		$('.btn-region').addClass('disabled');
	}
	
}
	
</script>




								<form action="<c:url value="/admin/shipping/addCustomRegion.html"/>" method="POST" class="form-inline">  
			      					<label class="required"><s:message code="label.shipping.addregion" text="Add region" /></label>
			      					<input type="text" class="span3" name="region" id="region" onblur="validatecode()"><!-- must be unique -->  
			      					<span class="help-inline"><div id="checkCodeStatus" style="display:none;"></div></span>
			      					<button type="submit" class="btn btn-success"><s:message code="label.shipping.addregion" text="Add region"/></button>
			      				</form>	
								<br/>
								
								<c:url var="addShipping" value="/admin/shipping/addCountryToRegion.html"/> 
		                  		<form:form method="POST" commandName="customRegion" action="${addShipping}" cssClass="form-inline">
				
		                  			 
		                        			<label><s:message code="label.region" text="Region"/></label>
		                        			
		                        					<form:select path="customRegionName">
						  								<form:options items="${customConfiguration.regions}" itemValue="customRegionName" itemLabel="customRegionName"/>
					       							</form:select>
		                        			
		                 			 
		                  			 
		                        			<label><s:message code="label.country" text="Country"/></label>
		                        					
		                        					<form:select path="countries[0]">
						  								<form:options items="${shippingCountries}" itemValue="isoCode" itemLabel="name"/>
					       							</form:select>

		                 			
	                        		 
                  							<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>

			      				</form:form>	
			      				
			      				
								 <!-- Listing grid include -->
								 <c:set value="/admin/shipping/weightBased/page.html" var="pagingUrl" scope="request"/>
								 <c:set value="/admin/shipping/weightBased/remove.html" var="removeUrl" scope="request"/>
								 <c:set value="/admin/shipping/weightBased/edit.html" var="editUrl" scope="request"/>
								 <c:set value="/admin/shipping/weightBased.html" var="refreshUrl" scope="request"/>
								 <c:set var="entityId" value="region" scope="request"/>
								 <c:set var="groupByEntity" value="region" scope="request"/>
								 <c:set var="componentTitleKey" value="module.shipping.weightBased" scope="request"/>
								 <c:set var="gridHeader" value="/pages/admin/shipping/weightBased-gridHeader.jsp" scope="request"/>
								 <c:set var="canRemoveEntry" value="true" scope="request"/>
				
				            	 <jsp:include page="/pages/admin/components/list.jsp"></jsp:include> 
								 <!-- End listing grid include -->
			      				
			      				

