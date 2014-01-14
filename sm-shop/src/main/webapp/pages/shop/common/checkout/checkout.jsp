<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<script src="<c:url value="/resources/js/jquery.maskedinput.min.js" />"></script>


<script>

function formChanged() {
	var $inputs = $('#checkoutForm').find(':input');
	$inputs.each(function() {
		if($(this).hasClass('required')) {
			var value = $(this).val();
			if(value!='') {
				$(this).css('background-color', '#FFF');
			} else {
				$(this).css('background-color', '#FFC');
			}
		}
	});
}


$(document).ready(function() {


		<!-- 
			//can use masked input for phone (USA - CANADA)
		-->
		formChanged();
		$('#deliveryBox').hide();
		
		$("input[type='text']").change( function() {
			formChanged();
		});
		
		<c:if test="${order.customer.billing.country!=null}">
			$('.billing-country-list').val('${order.customer.billing.country}');
			setCountrySettings('billing','${order.customer.billing.country}');
		</c:if>
		<c:if test="${order.customer.delivery.country!=null}">
			$('.shipping-country-list').val('${order.customer.delivery.country}');
			setCountrySettings('delivery','${order.customer.delivery.country}');
		</c:if>

		<!-- customer state is text -->
		<c:if test="${order.customer.billing.stateProvince!=null && order.customer.billing.stateProvince!=null !=''}">
			$('#billingStateList').hide();          
			$('#billingStateProvince').show(); 
			$('#billingStateProvince').val('<c:out value="${order.customer.billing.stateProvince}"/>');
		</c:if>
		<!-- customer state is a know state -->
		<c:if test="${order.customer.billing.stateProvince==null || order.customer.billing.stateProvince==''}">
			$('#billingStateList').show();           
			$('#billingStateProvince').hide();
			getZones('#billingStateList','#billingStateProvince','<c:out value="${order.customer.billing.country}" />','<c:out value="${order.customer.billing.zone}" />'); 
		</c:if>
		
		<c:if test="${order.customer.delivery.stateProvince!=null && order.customer.delivery.stateProvince!=''}">  
			$('#shippingStateList').hide();          
			$('#shippingStateProvince').show(); 
			$('#shippingStateProvince').val('<c:out value="${order.customer.delivery.stateProvince}"/>');
		</c:if>
		
		<c:if test="${order.customer.delivery.stateProvince==null || order.customer.delivery.stateProvince==''}">  
			$('#shippingStateList').show();          
			$('#shippingStateProvince').hide();
			getZones('#shippingStateList','#shippingStateProvince','<c:out value="${order.customer.delivery.country}" />','<c:out value="${order.customer.billing.zone}" />');
		</c:if>

		$(".billing-country-list").change(function() {
			getZones('#billingStateList','#billingStateProvince',$(this).val(),'<c:out value="${order.customer.billing.zone}" />');
			setCountrySettings('billing',$(this).val());
	    })
	    
	    $(".shipping-country-list").change(function() {
			getZones('#shippingStateList','#shippingStateProvince',$(this).val(),'<c:out value="${order.customer.delivery.zone}" />');
			setCountrySettings('delivery',$(this).val());
	    })
	    
	    $("#useAddress").click(function() {
	    	if( $(this).is(':checked') ) alert("checked");
	    	//var checkboxClicked = $(this).prop(':checked');
	    	//alert(checkboxClicked);
	    	//if($(this).checked) {
	    	//	$('#deliveryBox').hide();
	    	//} else {
	    	//	$('#deliveryBox').show();
	    	//}
	    });

    
});

function setCountrySettings(prefix, countryCode) {
	//add masks to your country
	//console.log('Apply mask ' + countryCode);
	
	var phoneSelector = '.' + prefix + '-phone';
	var postalCodeSelector = '.' + prefix + '-postalCode';
	
	if(countryCode=='CA') {
		$(phoneSelector).mask("?(999) 999-9999");
		$(postalCodeSelector).mask("?*** ***");
		return;
	}
	if(countryCode=='US') {
		$(phoneSelector).mask("?(999) 999-9999");
		$(postalCodeSelector).mask("?99999");
		return;
	}
	
	$(phoneSelector).unmask();
	$(postalCodeSelector).unmask();

	
}


$.fn.addItems = function(div, data, defaultValue) {
	var selector = div + ' > option';
	var defaultExist = false;
    $(selector).remove();
        return this.each(function() {
            var list = this;
            $.each(data, function(index, itemData) {
            	//console.log(itemData.code + ' ' + defaultValue);
            	if(itemData.code==defaultValue) {
            		defaultExist = true;
            	}
                var option = new Option(itemData.name, itemData.code);
                list.add(option);
            });
            if(defaultExist && (defaultValue!=null && defaultValue!='')) {
           	 	$(div).val(defaultValue);
            }
     });

};

function getZones(listDiv, textDiv, countryCode, defaultValue){
	$.ajax({
	  type: 'POST',
	  url: '<c:url value="/shop/reference/provinces.html"/>',
	  data: 'countryCode=' + countryCode + '&lang=${requestScope.LANGUAGE.code}',
	  dataType: 'json',
	  success: function(response){
			var status = response.response.status;
			//console.log(status);
			if(status==0 || status ==9999) {
				
				var data = response.response.data;
				//console.log(data);
				if(data && data.length>0) {
					$(listDiv).show();  
					$(textDiv).hide();
					$(listDiv).addItems(listDiv, data, defaultValue);		
				} else {
					$(listDiv).hide();             
					$(textDiv).show();
					if(defaultValue!=null || defaultValue !='') {
						$(textDiv).val(defaultValue);
					}
				}
			} else {
				$(listDiv).hide();             
				$(textDiv).show();
			}
	  },
	    error: function(xhr, textStatus, errorThrown) {
	  	alert('error ' + errorThrown);
	  }

	});
	
}


															




</script>



   <form:form id="checkoutForm" method="POST" enctype="multipart/form-data" commandName="order" action="#">
	   

		<div class="row-fluid" id="checkout">
				<div class="span12">


					<!-- If error messages -->
					<c:if test="${errorMessages!=null}">
						<div id="store.error" class="alert alert-error"><c:out value="${errorMessages!=null}" /></div><br/>
					</c:if>

				
					<!-- left column -->
					<div class="span8">

										<!-- Billing box -->
										<div id="shippingBox" class="box">
											<span class="box-title">
												<p class="p-title"><s:message code="label.customer.billinginformation" text="Billing information"/></p>
											</span>
					
											<!-- First name - Last name -->
											<div class="row-fluid">
													<div class="span4">
									  				   <div class="control-group"> 
														<label><s:message code="label.generic.firstname" text="First Name"/></label>
									    					<div class="controls"> 
									      					<form:input id="customer.firstName" cssClass="input-large required" path="customer.firstName"/>
									    					</div> 
									  				   </div> 
													</div>
													<div class="span4">
									  				   <div class="control-group"> 
														<label><s:message code="label.generic.lastname" text="Last Name"/></label>
									    					<div class="controls"> 
									    					<form:input id="customer.lastName" cssClass="input-large required"  maxlength="32" path="customer.lastName" />
									    					</div> 
									  				   </div> 
													</div>
											</div>
					
					
											<!-- email company -->
											<div class="row-fluid">
													<div class="span4">
									  				   <div class="control-group"> 
														<label><s:message code="label.generic.email" text="Email address"/></label>
									    					<div class="controls"> 
									    					<form:input id="customer.emailAddress" cssClass="input-large required" path="customer.emailAddress"/>
									    					</div> 
									  				   </div> 
													</div>
													<div class="span4">
									  				   <div class="control-group"> 
														<label><s:message code="label.customer.billing.company" text="Billing company"/></label>
									    					<div class="controls"> 
									      					<form:input id="customer.billing.company" cssClass="input-large" path="customer.billing.company"/>
									    					</div> 
									  				   </div> 
													</div>
											</div>
					
											<!--  street address -->
											<div class="row-fluid">
										  			<div class="control-group"> 
														<label><s:message code="label.generic.streetaddress" text="Street address"/></label>
										    				<div class="controls"> 
										      					<form:input id="customer.billing.address" cssClass="input-xxlarge required" path="customer.billing.address"/>
										    				</div> 
										  			</div> 
											</div>
					
											<!-- city - postal code -->
											<div class="row-fluid">
													<div class="span4">
											  			<div class="control-group"> 
															<label><s:message code="label.generic.city" text="City"/></label>
											    				<div class="controls"> 
											      					<form:input id="customer.billing.city" cssClass="input-large required" path="customer.billing.city"/>
											    				</div> 
											  			</div>
													</div>
													<div class="span4">
											  			<div class="control-group"> 
															<label><s:message code="label.generic.postalcode" text="Postal code"/></label>
											    				<div class="controls"> 
											      					<form:input id="customer.billing.postalCode" cssClass="input-large required billing-postalCode" path="customer.billing.postalCode"/>
											    				</div> 
											  			</div>
													</div>
										   </div>
										   
										   <!-- state province -->
										   <div class="row-fluid">
										   			<div class="control-group"> 
														<label><s:message code="label.generic.stateprovince" text="State / Province"/></label>
											    		<div class="controls"> 
												       			<form:select cssClass="zone-list" id="billingStateList" path="customer.billing.zone"/>
											                    <form:input  class="input-large required" id="billingStateProvince"  maxlength="100" name="billingStateProvince" path="customer.billing.stateProvince" /> 
											    		</div> 
											  		</div>
										   </div>
								
										  <!-- country - phone - ship checkbox -->
									       <div class="row-fluid">
										  			<div class="control-group"> 
														<label><s:message code="label.generic.country" text="Country"/></label>
										    				<div class="controls"> 
										       					<form:select cssClass="billing-country-list" path="customer.billing.country">
											  							<form:options items="${countries}" itemValue="isoCode" itemLabel="name"/>
										       					</form:select>
										    				</div> 
										  			</div>
						
										  			<div class="control-group"> 
														<label><s:message code="label.generic.phone" text="Phone number"/></label>
										    				<div class="controls"> 
										      					<form:input id="customer.billing.phone" cssClass="input-large required billing-phone" path="customer.billing.phone"/>
										    				</div> 
										  			</div>
					
													<label id="useAddress" class="checkbox"> <form:checkbox path="shipToBillingAdress" type="checkbox" id="useAddress"/> <s:message code="label.customer.shipping.shipaddress" text="Ship to this address" /></label>
									  	  </div>
								
								
									</div>
									<!-- end billing box -->
					
									<br/>
									<!-- Shipping box -->
									<div id="deliveryBox" class="box">
											<span class="box-title">
												<p class="p-title"><s:message code="label.customer.shippinginformation" text="Shipping information"/></p>
											</span>
					
											<!-- First name - Last name -->
											<div class="row-fluid">
													<div class="span4">
									  				   <div class="control-group"> 
														<label><s:message code="label.customer.shipping.name" text="Shipping name"/></label>
									    					<div class="controls"> 
									      					<form:input id="customer.delivery.name" cssClass="input-xxlarge required" path="customer.delivery.name"/>
									    					</div> 
									  				   </div> 
													</div>
											</div>
					
					
											<!-- company -->
											<div class="row-fluid">
									  				   <div class="control-group"> 
														<label><s:message code="label.customer.shipping.company" text="Shipping company"/></label>
									    					<div class="controls"> 
									      					<form:input id="customer.delivery.company" cssClass="input-large" path="customer.delivery.company"/>
									    					</div> 
									  				   </div> 
											</div>
					
											<!--  street address -->
											<div class="row-fluid">
										  			<div class="control-group"> 
														<label><s:message code="label.customer.shipping.streetaddress" text="Shipping street address"/></label>
										    				<div class="controls"> 
										      					<form:input id="customer.delivery.address" cssClass="input-xxlarge required" path="customer.delivery.address"/>
										    				</div> 
										  			</div> 
											</div>
					
											<!-- city - postal code -->
											<div class="row-fluid">
													<div class="span4">
											  			<div class="control-group"> 
															<label><s:message code="label.customer.shipping.city" text="Shipping city"/></label>
											    				<div class="controls"> 
											      					<form:input id="customer.delivery.city" cssClass="input-large required" path="customer.delivery.city"/>
											    				</div> 
											  			</div>
													</div>
													<div class="span4">
											  			<div class="control-group"> 
															<label><s:message code="label.customer.shipping.postalcode" text="Shipping postal code"/></label>
											    				<div class="controls"> 
											      					<form:input id="customer.delivery.postalCode" cssClass="input-large required delivery-postalCode" path="customer.delivery.postalCode"/>
											    				</div> 
											  			</div>
													</div>
										   </div>
										   
										   <!-- state province -->
										   <div class="row-fluid">
										   			<div class="control-group"> 
														<label><s:message code="label.customer.shipping.zone" text="Shipping state / province"/></label>
											    		<div class="controls"> 
												       			<form:select cssClass="zone-list" id="shippingStateList" path="customer.delivery.zone"/>
											                    <form:input  class="input-large required" id="shippingStateProvince"  maxlength="100" name="shippingStateProvince" path="customer.delivery.stateProvince" /> 
											    		</div> 
											  		</div>
										   </div>
								
										  <!-- country -->
									       <div class="row-fluid">
										  			<div class="control-group"> 
														<label><s:message code="label.customer.shipping.country" text="Shipping country"/></label>
										    				<div class="controls"> 
										       					<form:select cssClass="shipping-country-list" path="customer.delivery.country">
											  							<form:options items="${countries}" itemValue="isoCode" itemLabel="name"/>
										       					</form:select>
										    				</div> 
										  			</div>
									  	  </div>
								
								
									</div>
									<!-- end shipping box -->
									



			
					</div>
					<!-- end left column -->


					<!-- Order summary right column -->
					<div class="span4">
			
			
			
			
					</div>
					<!-- end right column -->

			    </div>
			    <!-- end span 12 -->

		</div>
		<!-- end row fluid -->
			
	</form:form>