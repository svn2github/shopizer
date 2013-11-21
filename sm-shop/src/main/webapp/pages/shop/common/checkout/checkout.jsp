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


<script>




$(document).ready(function() {


		<!-- 
			can use maked input for phone (USA - CANADA)
		-->

	
		//if($("#code").val()=="") {
		//	$('.btn').addClass('disabled');
		//}
		<!-- customer state is text -->
		<c:if test="${customer.state!=null && customer.state!=''}">
			$('.zone-list').hide();          
			$('#stateOther').show(); 
			$("input[name='showCustomerStateList']").val('no');
			$('#stateOther').val('<c:out value="${customer.state}"/>');
		</c:if>
		<!-- customer state is a know state -->
		<c:if test="${customer.state==null || customer.state==''}">
			$('.zone-list').show();           
			$('#stateOther').hide();
			$("input[name='showCustomerStateList']").val('yes');
			getZones('<c:out value="${customer.country.isoCode}" />'); 
		</c:if>
		
		<c:if test="${customer.delivery.state!=null && customer.delivery.state!=''}">  
			//$('.delivery-zone-list').hide();  
			//$('#delstateOther').show(); 
			//$("input[name='showDeliveryStateList']").val('no');
			//$('#delstateOther').val('<c:out value="${customer.delivery.state}"/>');
		</c:if>
		<c:if test="${customer.delivery.state==null || customer.delivery.state==''}"> 
			//$('.delivery-zone-list').show();			
			//$('#delstateOther').hide();
			//$("input[name='showDeliveryStateList']").val('yes');
			//getDeliveryZones('<c:out value="${customer.delivery.country.isoCode}" />'); 
		</c:if>
	
		<c:if test="${customer.billing.state!=null && customer.billing.state!=''}">
			//$('.billing-zone-list').hide();          
			//$('#bilstateOther').show(); 
			//$("input[name='showBillingStateList']").val('no');
			//$('#bilstateOther').val('<c:out value="${customer.billing.state}"/>');
		</c:if>
	
		<c:if test="${customer.billing.state==null || customer.billing.state==''}">  
			//$('.billing.zone-list').show();           
			//$('#bilstateOther').hide();
			//$("input[name='showBillingStateList']").val('yes');
			//getBillingZones('<c:out value="${customer.billing.country.isoCode}" />'); 
		</c:if>
	
		
	
		//$(".country-list").change(function() {
		//	getZones($(this).val());
	    //})
	
	    $("#billing-country-list").change(function() {
			getBillingZones($(this).val());
	    })
	
	    $("#delivery-country-list").change(function() {
	    	getDeliveryZones($(this).val());
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
					$("input[name='showCustomerStateList']").val('yes');
					$('.zone-list').show();  
					$('#stateOther').hide();
					$(".zone-list").addItems(data);					
					<c:if test="${customer.zone!=null}">
						$('.zone-list').val('<c:out value="${customer.zone.code}"/>');
					</c:if>
				} else {
					$("input[name='showCustomerStateList']").val('no');
					$('.zone-list').hide();             
					$('#stateOther').show();
					<c:if test="${stateOther!=null}">
						$('#stateOther').val('<c:out value="${customer.state}"/>');
					</c:if>
				}
			} else {
				$('.zone-list').hide();             
				$('#stateOther').show();
			}
	  },
	  error: function(xhr, textStatus, errorThrown) {
	  	alert('error ' + errorThrown);
	  }
	  
	});
}															

$.fn.addDeliveryItems = function(data) {
    $(".delivery-zone-list > option").remove();
        return this.each(function() {
            var list = this;
            $.each(data, function(index, itemData) {
                var option = new Option(itemData.name, itemData.code);
                list.add(option);
            });
     });
};

function getDeliveryZones(countryCode){
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
					$("input[name='showDeliveryStateList']").val('yes');
					$('.delivery-zone-list').show();  
					$('#delstateOther').hide();
					$(".delivery-zone-list").addDeliveryItems(data);					
					<c:if test="${customer.delivery.zone!=null}">
						$('.delivery-zone-list').val('<c:out value="${customer.delivery.zone.code}"/>');
					</c:if>
				} else {
					$("input[name='showDeliveryStateList']").val('no');
					$('.delivery-zone-list').hide();             
					$('#delstateOther').show();
					<c:if test="${delstateOther!=null}">
						$('#delstateOther').val('<c:out value="${customer.delivery.state}"/>');
					</c:if>
				}
			} else {
				$('.delivery-zone-list').hide();             
				$('#delstateOther').show();
			}

	  
	  },
	  error: function(xhr, textStatus, errorThrown) {
	  	alert('error ' + errorThrown);
	  }
	});
}

$.fn.addBillingItems = function(data) {
	    $(".billing-zone-list > option").remove();
	        return this.each(function() {
	            var list = this;
	            $.each(data, function(index, itemData) {
	                var option = new Option(itemData.name, itemData.code);
	                list.add(option);
	            });
	     });
};

function getBillingZones(countryCode){
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
						$("input[name='showBillingStateList']").val('yes');
						$('.billing-zone-list').show();  
						$('#bilstateOther').hide();
						$(".billing-zone-list").addBillingItems(data);					
						<c:if test="${customer.billing.zone!=null}">
							$('.billing-zone-list').val('<c:out value="${customer.billing.zone.code}"/>');
						</c:if>
					} else {
						$("input[name='showBillingStateList']").val('no');
						$('.billing-zone-list').hide();             
						$('#bilstateOther').show();
						<c:if test="${bilstateOther!=null}">
							$('#bilstateOther').val('<c:out value="${customer.billing.state}"/>');
						</c:if>
					}
				} else {
					$('.billing-zone-list').hide();             
					$('#bilstateOther').show();
				}

		  
		  },
		  error: function(xhr, textStatus, errorThrown) {
		  	alert('error ' + errorThrown);
		  }
		  
		});
}



</script>



   <form:form method="POST" enctype="multipart/form-data" commandName="order" action="#">
	   

	<div class="row-fluid" id="checkout">
				<div class="span12">


		<!-- If error messages -->
		<c:if test="${errorMessages!=null}">
			<div id="store.error" class="alert alert-error"><c:out value="${errorMessages!=null}" /></div><br/>
		</c:if>

				

		<div class="span8">

					<div class="box">
						<span class="box-title">
							<p class="p-title"><s:message code="label.customer.billinginformation" text="Billing information"/></p>
						</span>

						
			<div class="row-fluid">

				<div class="span4">
  				   <div class="control-group"> 
					<label><s:message code="label.customer.firstname" text="First Name"/></label>
    					<div class="controls"> 
      					<form:input id="customer.firstName" cssClass="input-medium highlight" path="customer.firstName"/>
    					</div> 
  				   </div> 
				</div>


				<div class="span4">
  				   <div class="control-group"> 
					<label><s:message code="label.customer.lastname" text="Last Name"/></label>
    					<div class="controls"> 
    					<form:input id="customer.lastName" cssClass="input-medium highlight"  maxlength="32" path="customer.lastName" />
    					</div> 
  				   </div> 
				</div>

			</div>



			<div class="row-fluid">

				<div class="span4">
  				   <div class="control-group"> 
					<label><s:message code="label.customer.email" text="Email"/></label>
    					<div class="controls"> 
    					<form:input id="customer.emailAddress" cssClass="input-medium highlight" path="customer.emailAddress"/>
    					</div> 
  				   </div> 
				</div>


				<div class="span4">
  				   <div class="control-group"> 
					<label><s:message code="label.customer.shipping.company" text="Company"/></label>
    					<div class="controls"> 
      					<form:input id="customer.billing.company" cssClass="input-medium" path="customer.billing.company"/>
    					</div> 
  				   </div> 
				</div>

			</div>


			<div class="row-fluid">



  			<div class="control-group"> 
				<label><s:message code="label.customer.streetaddress" text="Street address"/></label>
    				<div class="controls"> 
      					<form:input id="customer.billing.address" cssClass="input-medium highlight" path="customer.billing.address"/>
    				</div> 
  			</div> 

			</div>

			<div class="row-fluid">


			<div class="span4">
  			<div class="control-group"> 
				<label><s:message code="label.customer.city" text="City"/></label>
    				<div class="controls"> 
      					<form:input id="customer.billing.city" cssClass="input-medium highlight" path="customer.billing.city"/>
    				</div> 
  			</div>
			</div>



			<div class="span4">
  			<div class="control-group"> 
				<label><s:message code="label.customer.zone" text="State / Province"/></label>
    				<div class="controls"> 
	       				<form:select cssClass="zone-list highlight" path="customer.billing.zone"/>
                      	<form:input  class="input-large highlight" id="stateOther"  maxlength="100" name="stateOther" path="customer.billing.stateProvince" /> 
    				</div> 
  			</div>
			</div>


			<div class="span4">
  			<div class="control-group"> 
				<label><s:message code="label.customer.postalcode" text="Postal code"/></label>
    				<div class="controls"> 
      					<form:input id="customer.billing.postalCode" cssClass="input-medium highlight" path="customer.billing.postalCode"/>
    				</div> 
  			</div>
			</div>

			</div>
			<div class="row-fluid">

	  			<div class="control-group"> 
					<label><s:message code="label.customer.country" text="Country"/></label>
	    				<div class="controls"> 
	       					<form:select cssClass="country-list highlight" path="customer.billing.country">
		  							<form:options items="${countries}" itemValue="isoCode" itemLabel="name"/>
	       					</form:select>
	    				</div> 
	  			</div>
	
	  			<div class="control-group"> 
					<label><s:message code="label.customer.telephone" text="Phone"/></label>
	    				<div class="controls"> 
	      					<form:input id="customer.billing.phone" cssClass="input-large highlight" path="customer.billing.phone"/>
	    				</div> 
	  			</div>


				<label class="checkbox" checked> <input type="checkbox" id="useAddress"> <s:message code="label.customer.shipping.shipaddress" text="Ship to this address" /></label>
			</div>

		</div>

		<div class="span8">

					<div class="box">
						<span class="box-title">
							<p class="p-title"><s:message code="label.customer.shippinginformation" text="Shipping information"/></p>
						</span>

						
			<div class="row-fluid">

				<div class="span4">
  				   <div class="control-group"> 
					<label><s:message code="label.customer.firstname" text="First Name"/></label>
    					<div class="controls"> 
      					<form:input id="customer.firstName" cssClass="input-medium highlight" path="customer.firstName"/>
    					</div> 
  				   </div> 
				</div>


				<div class="span4">
  				   <div class="control-group"> 
					<label><s:message code="label.customer.lastname" text="Last Name"/></label>
    					<div class="controls"> 
      					<form:input id="customer.lastName" cssClass="input-medium highlight" path="customer.lastName"/>
    					</div> 
  				   </div> 
				</div>

			</div>



			<div class="row-fluid">



  			<div class="control-group"> 
				<label><s:message code="label.customer.streetaddress" text="Street address"/></label>
    				<div class="controls"> 
      					<form:input id="customer.delivery.address" cssClass="input-medium highlight" path="customer.delivery.address"/>
    				</div> 
  			</div> 

			</div>

			<div class="row-fluid">


			<div class="span4">
  			<div class="control-group"> 
				<label><s:message code="label.customer.city" text="City"/></label>
    				<div class="controls"> 
      					<form:input id="customer.delivery.city" cssClass="input-medium highlight" path="customer.delivery.city"/>
    				</div> 
  			</div>
			</div>



			<div class="span4">
  			<div class="control-group"> 
				<label><s:message code="label.customer.zone" text="State / Province"/></label>
    				<div class="controls"> 
	       				<form:select cssClass="zone-list highlight" path="customer.delivery.zone"/>
                      	<form:input  class="input-large highlight" id="stateOther"  maxlength="100" name="stateOther" path="customer.delivery.stateProvince" /> 
    				</div> 
  			</div>
			</div>


			<div class="span4">
  			<div class="control-group"> 
				<label><s:message code="label.customer.postalcode" text="Postal code"/></label>
    				<div class="controls"> 
      					<form:input id="customer.delivery.postalCode" cssClass="input-medium highlight" path="customer.delivery.postalCode"/>
    				</div> 
  			</div>
			</div>

			</div>
			<div class="row-fluid">

	  			<div class="control-group"> 
					<label>Country</label>
	    				<div class="controls"> 
	      					<input type="text" class="input-large" id="country">
	    				</div> 
	  			</div>
	
	  			<div class="control-group"> 
					<label><s:message code="label.customer.telephone" text="Phone"/></label>
	    				<div class="controls"> 
	      					<form:input id="customer.delivery.phone" cssClass="input-large highlight" path="customer.delivery.phone"/>
	    				</div> 
	  			</div>


			</div>


			


		</div>




					<br/>

					<div class="box">
						<span class="box-title">
						<p>Payment</p>
						</span>

						<!--<ul class="nav nav-tabs">
							<li id="pp-tab"  class="active"><a href="#" id="pp-link" data-toggle="tab">PPAL</a></li>
							<li id="cc-tab"  class="active"><a href="#" id="cc-link" data-toggle="tab">CC</a></li>
						</ul>-->


    <div class="tabbable"> 
    	<ul class="nav nav-tabs">
    		<li class="active"><a href="#tab1" data-toggle="tab">Pay</a></li>
    		<li><a href="#tab2" data-toggle="tab">Credit</a></li>
            <li><a href="#tab3" data-toggle="tab">Check</a></li>
      </ul>
    	<div class="tab-content">
    		<div class="tab-pane active" id="tab1">
    			<p>Pay<p>
    		</div>
    		<div class="tab-pane" id="tab2">


			<div class="control-group">
				<label class="control-label">Card Holder's Name</label>
				<div class="controls">
					<input type="text" class="input-large" pattern="\w+ \w+.*" title="Fill your first and last name" required>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Card Number</label>
				<div class="controls">
					<div class="row-fluid">
						<input type="text" class="input-large" autocomplete="off"  required>
					</div>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Card Expiry Date</label>
				<div class="controls">
					<div class="row-fluid">
					<div class="span3">
						<select class="input-medium">
							<option>January</option>
							<option>...</option>
							<option>December</option>
						</select>
					</div>
					<div class="span3">
						<select class="input-small">
							<option>2013</option>
							<option>...</option>
							<option>2015</option>
						</select>
					</div>
				</div>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Card CVV</label>
				<div class="controls">
				<div class="row-fluid">
					<div class="span3">
						<input type="text" class="input-small" autocomplete="off" maxlength="3" pattern="\d{3}" title="Three digits at back of your card" required>
					</div>
				<div class="span8">
				<!-- screenshot may be here -->
				</div>
			</div>
	</div>
</div>

    		</div>
	    		<div class="tab-pane" id="tab3">
	    			<p>Check<p>
	    		</div>
       		</div>
    </div>
					</div>

				</div>



				<div class="span4">


					<div class="box">
						<span class="box-title">
						<p><font color="#FF8C00">Order Summary</font></p>
						</span>



		<table class="table table-condensed">
			<thead> 
				<tr> 
					<th width="55%">Item</th> 
					<th width="15%">Quantity</th> 
					<th width="15%">Price</th>
					<th width="15%">Total</th>  
				</tr> 
			</thead> 

			<tbody> 
				<tr> 
					<td>Vertical (default)</td> 
					<td >2</td> 
					<td><strong>$49.99</strong></td> 
					<td><strong>$99.98</strong></td> 
				</tr>

				<tr class="subt"> 
					<td colspan="3"><font color="red">[Vertical item $6.00]</font></td> 
					<td><font color="red"><strong>($6.00)</strong></font></td> 
				</tr> 

				<tr class="subt"> 
					<td colspan="3"><strong>Sub-total</strong></td> 
					<td><strong>$99.98</strong></td> 
				</tr> 

			</tbody> 
		</table>


<div class="total-box">
<span style="float:right">
<font class="total-box-label">
Total 
<font class="total-box-price">$99.98</font>
</font>
</span>
</div>

		<div class="form-actions">
			<div class="pull-right"> 
				<button type="submit" class="btn btn-success <c:if test="${errorMessages!=null}">btn-disabled</if>" <c:if test="${errorMessages!=null}">disabled="true"</if>>Submit order</button>
			</div>
		</div> 


					</div>


				</div>

			    </div>

			</div>
			
	</form:form>