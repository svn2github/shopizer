<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
<%@ page session="false" %>				
				

<link href="<c:url value="/resources/css/bootstrap/css/datepicker.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>
<script src="<c:url value="/resources/js/ckeditor/ckeditor.js" />"></script>
<script src="<c:url value="/resources/js/jquery.formatCurrency-1.4.0.js" />"></script>
<script src="<c:url value="/resources/js/jquery.alphanumeric.pack.js" />"></script>
<script src="<c:url value="/resources/js/adminFunctions.js" />"></script>

<script src="<c:url value="/resources/js/jquery.showLoading.min.js" />"></script>
<link href="<c:url value="/resources/css/showLoading.css" />" rel="stylesheet">	

<script>


	$(document).ready(function(){ 
	
		$("#refundButton").click(function() {
			$('#refundModal').modal();
 			$(".alert-success").hide();
 			$(".alert-error").hide();
		}); 
		
		$(".close-modal").click(function() {
			 location.href="<c:url value="/admin/orders/editOrder.html" />?id=<c:out value="${order.order.id}"/>";
		}); 
		
		
	}); 


    $(function() {

        $("#refund").submit(function() {
 			$('#refundModal').showLoading();
 			$(".alert-success").hide();
 			$(".alert-error").hide();
            var data = $(this).serializeObject();
            $.ajax({
                'type': 'POST',
                'url': "<c:url value="/admin/orders/refundOrder.html"/>",
                'contentType': 'application/json',
                'data': JSON.stringify(data),
                'dataType': 'json',
                'success': function(result) {
                   $('#refundModal').hideLoading();
                   var response = result.response;
                   if (response.status==0) {
                        $(".alert-success").show();
                        $(".cancel-modal").hide();
                        $(".close-modal").show();
                   } else { 
                        $(".alert-error").html(response.statusMessage);
                        $(".alert-error").show();
                   }
                }
            });
 
            return false;
        });
    });
 
    $.fn.serializeObject = function() {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
</script>

<div class="tabbable">

		<jsp:include page="/common/adminTabs.jsp" />
					
		<div class="tab-content">

  		<div class="tab-pane active" id="order-section">

		<div class="sm-ui-component">	


		<h3>
			<div class="control-group">
                      <div class="controls">
                     		 <s:message code="label.order.id2" text="Order ID"/> 
                     		 <c:out value="${order.order.id}" /><br>
                       </div>       
                  </div>
           </h3>
		<br/>
			<br/>
 	       	 	
	     <c:url var="orderSave" value="/admin/orders/save.html"/>
         <form:form method="POST" enctype="multipart/form-data" commandName="order" action="${orderSave}">
	   
                <form:errors path="*" cssClass="alert alert-error" element="div" />
	                <div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>   
	                <div id="store.error" class="alert alert-error" style="display:none;"><s:message code="message.error" text="An error occured"/></div>
		
		  			<form:hidden path="order.id" />
		  			<form:hidden path="order.customerId" />
 			
 		<div class="span8"> 
 		
 			<div class="span4"> 
			
			<h6> <s:message code="label.customer.billinginformation" text="Billing information"/> </h6>
			<address>			        

				<label><s:message code="label.customer.firstname" text="First Name"/></label>
	            <div class="controls">
	            		
			 				<form:input id="customerFirstName" cssClass="input-large highlight" path="order.billing.firstName"/>
			 				<span class="help-inline">
			 				<form:errors path="order.billing.firstName" cssClass="error" /></span>
	            </div>
	            
	            <label><s:message code="label.customer.lastname" text="Last Name"/></label>
	            <div class="controls">
		 					<form:input id="customerLastName" cssClass="input-large highlight" path="order.billing.lastName"/>
		 					<span class="help-inline"><form:errors path="order.billing.lastName" cssClass="error" /></span>
	            </div>
	            
	            
	            <address>
	            
	            		<label><s:message code="label.customer.billing.streetaddress" text="Billing address"/></label>
			            <div class="controls">
				 				<form:input id="billingAdress" cssClass="input-large highlight" path="order.billing.address"/>
				 				<span class="help-inline"><form:errors path="order.billing.address" cssClass="error" /></span>
			            </div>
			            <label><s:message code="label.customer.billing.city" text="Billing city"/></label>
			            <div class="controls">
				 				<form:input id="billingCity" cssClass="input-large highlight" path="order.billing.city"/>
				 				<span class="help-inline"><form:errors path="order.billing.city" cssClass="error" /></span>
			            </div>
			            <label><s:message code="label.customer.billing.zone" text="Billing state / province"/></label>
			            <div class="controls">
				 				<form:input id="billingState" cssClass="input-large highlight" path="order.billing.state"/>
				 				<span class="help-inline"><form:errors path="order.billing.state" cssClass="error" /></span>
			            </div>
			            <label><s:message code="label.customer.billing.country" text="Country"/></label>
			            <div class="controls">
				 				<form:select cssClass="country-list" path="order.billing.country.isoCode">
					  					<form:options items="${countries}" itemValue="isoCode" itemLabel="name"/>
				       			</form:select>
			            </div>
			            <label><s:message code="label.customer.billing.postalcode" text="Billing postal code"/></label>
			            <div class="controls">
				 				<form:input id="billingPostalCode" cssClass="input-large highlight" path="order.billing.postalCode"/>
				 				<span class="help-inline"><form:errors path="order.billing.postalCode" cssClass="error" /></span>
			            </div>	
			    </address>

	            
	           <label><s:message code="label.customer.email" text="Email"/></label>
	            <div class="controls">
		 				<form:input id="customerEmailAddress" cssClass="input-large highlight" path="order.customerEmailAddress"/>
		 				<span class="help-inline"><form:errors path="order.customerEmailAddress" cssClass="error" /></span>
	            </div>
	            
	            </div>
	            
	            <div span="4">
	            
	            
				<h6><s:message code="label.customer.shippinginformation" text="Shipping information"/></h6>
				<address>
						<label><s:message code="label.customer.shipping.firstName" text="Shipping first name"/></label>
			            <div class="controls">
				 				<form:input  cssClass="input-large" path="order.delivery.firstName"/>				 							
			            </div>
			            <label><s:message code="label.customer.shipping.lastName" text="Shipping last name"/></label>
			            <div class="controls">
				 				<form:input  cssClass="input-large" path="order.delivery.lastName"/>				 							
			            </div>
			            <label><s:message code="label.customer.shipping.streetaddress" text="Shipping address"/></label>
			            <div class="controls">
				 				<form:input  cssClass="input-large" path="order.delivery.address"/>		 				
			            </div>
			            <label><s:message code="label.customer.shipping.city" text="Shipping city"/></label>
			            <div class="controls">
				 				<form:input  cssClass="input-large" path="order.delivery.city"/>
			            </div>
			            <label><s:message code="label.customer.shipping.zone" text="State / province"/></label>
			            <div class="controls">
				 				<form:input  cssClass="input-large" path="order.delivery.state"/>
			            </div>
			            <label><s:message code="label.customer.shipping.country" text="Country"/></label>
			            <div class="controls">
				 				<form:select cssClass="country-list" path="order.delivery.country.isoCode">
					  					<form:options items="${countries}" itemValue="isoCode" itemLabel="name"/>
				       			</form:select>
			            </div>
			            <label><s:message code="label.customer.shipping.postalcode" text="Postal code"/></label>
			            <div class="controls">
				 				<form:input  cssClass="input-large" path="order.delivery.postalCode"/>
			            </div>	            	            	            	            				
				</address>	
	            
	            
	            </div>
	            
	            </div>
	
   				<div class="span8">
				<s:message code="label.customer.order.date" text="Order date"/>			 		
			 	<div class="controls">
							<form:input  cssClass="input-large" path="datePurchased"  class="small" type="text"
							 data-date-format="<%=com.salesmanager.core.constants.Constants.DEFAULT_DATE_FORMAT%>" />
							  <script type="text/javascript">
                                 $('#datePurchased').datepicker();
                              </script>
		 						<span class="help-inline"><form:errors path="datePurchased" cssClass="error" /></span>
	            </div>  
	

	            <label><s:message code="label.order.paymentmode" text="Payment mode"/></label>
	            <div class="controls">
		 			 <c:out value="${order.order.paymentModuleCode}"/><form:hidden  path="order.paymentModuleCode"/><br/><br/>
	            </div>	
	            
	            <label><s:message code="label.order.shippingmethod" text="Shipping method"/></label>
	            <div class="controls">
		 			 <c:out value="${order.order.shippingModuleCode}"/><form:hidden  path="order.shippingModuleCode"/>
	            </div>	
	
				</dl>
				
				</div> 
						

		
 
      
      	  <br/>
      
      	  <div class="span8">
		      <table class="table table-bordered table-striped"> 
					<thead> 
						<tr> 
							<th colspan="2" width="55%"><s:message code="label.order.item" text="Item"/></th> 
							<th colspan="1" width="15%"><s:message code="label.quantity" text="Quantity"/></th> 
							<th width="15%"><s:message code="label.order.price" text="Price"/></th>
							<th width="15%"><s:message code="label.order.total" text="Total"/></th>  
						</tr> 
					</thead> 
					
 				    <tbody> 
						<c:forEach items="${order.order.orderProducts}" var="orderProduct" varStatus="counter">	 
			            	<c:set var="total" value="${orderProduct.oneTimeCharge * orderProduct.productQuantity }" />
			            	
							<tr> 
								<td colspan="2"> <c:out value="${orderProduct.productName}" /></td> 
								<td ><c:out value="${orderProduct.productQuantity}" /></td> 
			            		<td><strong><sm:monetary value="${orderProduct.oneTimeCharge}" currency="${order.order.currency}"/></strong> </td>
								<td><strong><sm:monetary value="${total}" currency="${order.order.currency}"/></strong></td> 
							</tr> 
			
						</c:forEach> 
					
					 	<c:forEach items="${order.order.orderTotal}" var="orderTotal" varStatus="counter">	
							<tr class="subt"> 
								<td colspan="2">&nbsp;</td> 
								<td colspan="2" ><c:out value="${orderTotal.title}"  /></td> 
								<td ><strong><sm:monetary value="${orderTotal.value}" currency="${order.order.currency}"/></strong></td> 
							</tr> 
						</c:forEach> 	 
					</tbody>    
				</table>
    	  </div>  

            <br/>   
            <div class="span8">
		           <div class="control-group">
		                  <label><s:message code="label.entity.status" text="Status"/></label>	 
		                  <div class="controls">      
	                   			<form:select path="order.status">
				  						<form:options items="${orderStatusList}" />
			       				</form:select>      
		                   </div>
		           </div>  
		     					
           	       <div class="control-group">
                       <label><s:message code="label.order.history" text="History"/></label>
                       <div class="controls">
							 <dl class="dl-horizontal">
								<c:forEach items="${order.order.orderHistory}" var="orderHistory" varStatus="counter">
									<dd>- <c:out value="${orderHistory.comments}"/>                              
	              				</c:forEach> 
							</dl> 
					   </div>
              	   </div> 
              
	     		   <div class="control-group">  
	                    <label><s:message code="label.entity.status" text="Status"/></label>
	                     <div class="controls">
	                         <form:textarea  cols="10" rows="3" path="orderHistoryComment"/>
	                    </div> 
	               </div>
              
	              <div class="form-actions">
	              		<button  type="submit" class="btn btn-medium btn-primary" ><s:message code="button.label.save" text="Save"/></button>
	              		<button id="refundButton" class="btn btn-medium btn-danger" type="button"><s:message code="label.order.refund" text="Apply refund"/></button>
	      		  </div>
      		</div> 
            <br/>              
              
             <ul class="nav nav-pills">
             				<li><a href="<c:url value="/admin/orders/printInvoice.html?id=${order.id}" />"><s:message code="label.order.printinvoice" text="Print invoice"/></a></li>
							<li class="disabled"><a href="#"><s:message code="label.order.sendinvoice" text="Send email invoice"/></a></li>
							<li class="disabled"><a href="#"><s:message code="label.order.packing" text="Print packing slip"/></a></li>
		    </ul> 
    
    
    	  </div>
   
   		</form:form>       

      </div>
	 </div>
  </div>




<div id="refundModal"  class="modal hide" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-header">
          <button type="button" class="close close-modal" data-dismiss="modal" aria-hidden="true">X</button>
          <h3 id="myModalLabel"><s:message code="label.order.refund" text="Apply refund"/></h3>
  </div>
    <div class="modal-body">
    
    	  <div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>   
	      <div id="store.error" class="alert alert-error" style="display:none;"><s:message code="message.error" text="An error occured"/></div>

           <p>
           		<s:message code="label.order.total" text="Total" />: <strong><c:out value="${order.order.total}"/></strong>
           		<span id="refundMessage" style="display:none;"><s:message code="" text=""/><span id="refundAmount"></span></span>
           </p>
           <p>
           		<form id="refund" class="form-inline">
           		    <label><s:message code="label.generic.amount" text="Amount" /></label>&nbsp;<input type="text" id="amount" name="amount" class="input-small" placeholder="<s:message code="label.generic.amount" text="Amount" />">
           		    <input name="orderId" id="orderId" type="hidden" value="<c:out value="${order.id}"/>">
           		    <button id="refundButton" type="submit" class="btn btn-danger"><s:message code="label.order.refund" text="Apply refund"/></button>
           		 </form>
           
           </p>
             
    </div>  
    <div class="modal-footer">
           <button class="btn cancel-modal" data-dismiss="modal" aria-hidden="true"><s:message code="button.label.cancel" text="Cancel" /></button>
           <button class="btn btn-success close-modal" id="closeModal" data-dismiss="modal" aria-hidden="true" style="display:none;"><s:message code="button.label.close" text="Close" /></button>
    </div>
</div>


     			     