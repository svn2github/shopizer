<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>




<script>


$(document).ready(function() {
	
	
	<c:choose>
	<c:when test="${store.storestateprovince!=null}">
		$('.zone-list').hide();             
		$('#storestateprovince').show(); 
	</c:when>
	<c:otherwise>
		$('.zone-list').show();             
		$('#storestateprovince').hide();
		getZones('<c:out value="${store.country.isoCode}" />'); 
	</c:otherwise>
	</c:choose>
	

	//alert($(".country-list").val())
	//$('.zone-list').hide();             
	//$('#storestateprovince').show(); 
	//set selected value if zone-list exist
	//if($('.zone-list')) {
	//	alert('zone list exist');
	//}
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
				} else {
	
					$('.zone-list').hide();             
					$('#storestateprovince').show();
				}
			} else {
				$('.zone-list').hide();             
				$('#storestateprovince').show();
			}

	  
	  },
	  error: function(xhr, textStatus, errorThrown) {
	  	alert('fail ' + errorThrown);
	  }
	  
	});
}



</script>


<div class="tabbable">


				<jsp:include page="/common/adminTabs.jsp" />
				
				<h3><s:message code="label.store.title" text="Merchant store" /></h3>	
				<br/>


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
	                        <label><s:message code="label.storecountry" text="Store Country"/></label>
	                        <div class="controls">
	                        					
	                        					<form:select cssClass="country-list" path="country.isoCode">
					  								<form:option value="-1" label="--- Select ---" />
					  								<form:options items="${countries}" itemValue="isoCode" itemLabel="name"/>
				       							</form:select>
	                                   			<span class="help-inline"><form:errors path="country" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                 <div class="control-group">
	                        <label><s:message code="label.storezone" text="Store state / province"/></label>
	                        <div class="controls">

	                        					<form:select cssClass="zone-list" path="zone.code"/>
	                        					<input type="text" id="storestateprovince" name="storestateprovince" value="${store.storestateprovince}" /> 
	                                   			<span class="help-inline"></span>
	                        </div>
	                  </div>
	                  
	                  
	                  
	                  <div class="control-group">
	                        <label><s:message code="label.supportedlanguages" text="Supported languages"/></label>
	                        <div class="controls">

	                        					<form:checkboxes items="${languages}" itemValue="id" itemLabel="code" path="languages" /> 
	                                   			<span class="help-inline"></span>
	                        </div>
	                  </div>
	                  
	                  
	                  <!-- weight, height in business since -->

				
				      <div class="form-actions">
	                  		<div class="pull-right">
	                  			<button type="submit" class="btn btn-action"><s:message code="button.label.submit2" text="Submit"/></button>
	                  		</div>
	            	 </div>


      					
				</form:form>

</div>