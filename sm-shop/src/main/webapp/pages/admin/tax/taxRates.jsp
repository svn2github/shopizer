<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>				
				
<script>


$(document).ready(function() {
	



	$(".country-list").change(function() {
		getZones($(this).val());
    })


});

$.fn.addItems = function(data) {
    $(".zone-list > option").remove();
        return this.each(function() {
            var list = this;
            $.each(data, function(index, itemData) {
                var option = new Option(itemData.name, itemData.code);
                list.add(option);
            });
     });
};

function getZones(countryCode){
	$.ajax({
	  type: 'POST',
	  url: '<c:url value="/admin/reference/provinces.html"/>',
	  data: 'countryCode=' + countryCode,
	  dataType: 'json',
	  success: function(response){

			var status = isc.XMLTools.selectObjects(response, "/response/status");
			if(status==0 || status ==9999) {
				
				var data = isc.XMLTools.selectObjects(response, "/response/data");
				if(data && data.length>0) {
					
					$('.zone-list').show();  
					$('#storestateprovince').hide();
					$(".zone-list").addItems(data);
					<c:if test="${store.zone!=null}">
						$('.zone-list').val('<c:out value="${store.zone.code}"/>');
					</c:if>
				} else {
					$('.zone-list').hide();             
					$('#storestateprovince').show();
					<c:if test="${store.storestateprovince!=null}">
						$('#storestateprovince').val('<c:out value="${store.storestateprovince}"/>');
					</c:if>
				}
			} else {
				$('.zone-list').hide();             
				$('#storestateprovince').show();
			}

	  
	  },
	  error: function(xhr, textStatus, errorThrown) {
	  	alert('error ' + errorThrown);
	  }
	  
	});
}


</script>



<div class="tabbable">

					<jsp:include page="/common/adminTabs.jsp" />
  					
  					 	<div class="tab-content">

    						<div class="tab-pane active" id="taxrates-section">

								<div class="sm-ui-component">	
								<h3><s:message code="label.tax.taxclass.title" text="Tax classes" /></h3>	
								<br/>

								<c:url var="saveTaxRate" value="/admin/tax/taxrate/save.html"/>

								<form:form method="POST" modelAttribute="taxClass" action="${saveTaxRate}">	
				      				<form:errors path="*" cssClass="alert alert-error" element="div" />
									<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
												
				      			  	
				                 
				                    <div class="control-group">
				                        <label><s:message code="label.country" text="Country"/></label>
					                        <div class="controls">
					                        				                        			
		                        					
		                        					<form:select path="country">
						  								<form:options items="${countries}" itemValue="isoCode" itemLabel="name"/>
					       							</form:select>
					                        </div>
				                    </div>

	                 				<div class="control-group">
				                        <label><s:message code="label.storezone" text="Store state / province"/></label>
				                        <div class="controls">
				                        					<form:select cssClass="zone-list highlight" path="zone"/>
				                        					<input type="text" class="input-large highlight" id="storestateprovince" name="storestateprovince" /> 
				                                   			<span class="help-inline"><form:errors path="zone.code" cssClass="error" /></span>
				                        </div>
	                  				</div>
	                  				
	                  				<c:forEach items="${taxRate.descriptions}" var="description" varStatus="counter">

                 

				                        <div class="control-group">
				
				                              <label class="required"><s:message code="label.productedit.productname" text="Product name"/> (<c:out value="${description.language.code}"/>)</label>
				                              <div class="controls">
				                                          <form:input cssClass="input-large highlight" id="name${counter.index}" path="descriptions[${counter.index}].name"/>
				                                          <span class="help-inline"><form:errors path="descriptions[${counter.index}].name" cssClass="error" /></span>
				                              </div>
				
				                       </div>
				                       
				                    </c:forEach>
				                    
				                   <div class="control-group">
				                        <label class="required"><s:message code="label.product.price" text="Price"/></label>
				
				                        <div class="controls">
				                                    <form:input id="productPriceAmount" cssClass="highlight" path="taxRatePrice"/>
				                                    <span id="help-price" class="help-inline"><form:errors path="taxRatePrice" cssClass="error" /></span>
				                        </div>
                  				   </div>
				                    
				                    <div class="control-group">
				                        <label class="required"><s:message code="label.tax.taxclass.name" text="Tax class name"/></label>
					                        <div class="controls">
					                        		<form:input cssClass="input-large" path="title" />
					                        </div>
				                    </div>
				                    
				                   <div class="control-group">
			                        	<label><s:message code="label.product.available" text="Product available"/></label>
			                        	<div class="controls">
			                                    <form:checkbox path="piggyback" />
			                        	</div>
                  					</div>

				                  	<form:hidden path="merchantStore.id" value="${requestScope.store.id}" />
							
							        <div class="form-actions">
				                  		<div class="pull-right">
				                  			<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
				                  		</div>
				            	    </div>
				 
				            	 </form:form>
				            	 
				            	 
				            	 <br/><br/>
				            	 <!-- Listing grid include -->
								 <c:set value="/admin/tax/taxclass/paging.html" var="pagingUrl" scope="request"/>
								 <c:set value="/admin/tax/taxclass/remove.html" var="removeUrl" scope="request"/>
								 <c:set value="/admin/tax/taxclass/list.html" var="refreshUrl" scope="request"/>
								 <c:set value="/admin/tax/taxclass/edit.html" var="editUrl" scope="request"/>
								 <c:set var="entityId" value="taxClassId" scope="request"/>
								 <c:set var="componentTitleKey" value="label.tax.taxclass.title" scope="request"/>
								 <c:set var="gridHeader" value="/pages/admin/tax/taxClass-gridHeader.jsp" scope="request"/>
								 <c:set var="canRemoveEntry" value="true" scope="request"/>

				
				            	 <jsp:include page="/pages/admin/components/list.jsp"></jsp:include> 
								 <!-- End listing grid include -->
				     
	      			     
      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>		      			     