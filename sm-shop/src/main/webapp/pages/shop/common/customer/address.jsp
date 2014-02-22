
<%
    response.setCharacterEncoding( "UTF-8" );
    response.setHeader( "Cache-Control", "no-cache" );
    response.setHeader( "Pragma", "no-cache" );
    response.setDateHeader( "Expires", -1 );
%>
<script>
function editAddress(formId){
	$( "#editBillingAddress_"+formId).submit();	
}
function editShippingAddress(formId){
	$( "#editShippingAddress_"+formId).submit();	
}
function addShippingAddress(formId){
	$( "#addShippingAddress_"+formId).submit();	
}


</script>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<c:url var="editAddress" value="/shop/customer/editAddress.html"/>
<div id="main-content" class="container clearfix">
	<div class="row-fluid">
		<div class="span12">
			<div class="span8">

				<div class="box">
					<span class="box-title">
						<p>
							<font color="#FF8C00"><s:message
									code="label.customer.billingaddress" text="Billing Address" /></font>
						</p>
					</span>

					<c:if test="${not empty customer}">
					
						<form:form action="${editAddress}" id="editBillingAddress_${customer.id}">
						<input type="hidden" name="customerId" value="${customer.id}">
						<input type="hidden" name="billingAddress" value="true"/>
						<address>
							${customer.billing.firstName} &nbsp; ${customer.billing.lastName}
							&nbsp;&nbsp; <a href="javascript:void(0)" onclick="editAddress('${customer.id}');">Edit</a> <br />
							<c:if test="${not empty customer.billing.address}"> ${customer.billing.address} <br />
							</c:if>
							<c:if test="${not empty customer.billing.company}"> ${customer.billing.company} <br />
							</c:if>
							
							<c:if test="${not empty customer.billing.city}">${customer.billing.city} <br />
							</c:if>
							<c:if test="${not empty customer.billing.postalCode}"> ${customer.billing.postalCode}<br />
							</c:if>
								<c:if test="${not empty customer.billing.phone}">${customer.billing.phone} <br/></c:if>
							<c:if test="${not empty customer.billing.zone}">${customer.billing.zone} <br />
							</c:if>
							<c:if test="${not empty customer.billing.country}">${customer.billing.country}
							</c:if>
						
						</address>
					   </form:form>
					</c:if>
				</div>

				<div class="box">
					<span class="box-title">
						<p>
							<font color="#FF8C00"><s:message
									code="label.customer.shippingaddress" text="Shipping Address" /></font>
						</p>
					</span>
					<c:if test="${not empty customer.delivery}">
					<form:form action="${editAddress}" id="editShippingAddress_${customer.id}">
					  <input type="hidden" name="customerId" value="${customer.id}">
						<input type="hidden" name="billingAddress" value="false"/>
						<address>
							${customer.delivery.firstName} &nbsp; ${customer.delivery.lastName}
							&nbsp;&nbsp; <a href="javascript:void(0)" onclick="editShippingAddress('${customer.id}');">Edit</a> <br />
							<c:if test="${not empty customer.delivery.address}"> ${customer.delivery.address} <br />
							</c:if>
							<c:if test="${not empty customer.delivery.company}"> ${customer.delivery.company} <br />
							</c:if>
							
							<c:if test="${not empty customer.delivery.city}">${customer.delivery.city} <br />
							</c:if>
							<c:if test="${not empty customer.delivery.postalCode}"> ${customer.delivery.postalCode}<br />
							</c:if>
								<c:if test="${not empty customer.delivery.phone}">${customer.delivery.phone} <br/></c:if>
							<c:if test="${not empty customer.delivery.zone}">${customer.delivery.zone} <br />
							</c:if>
							<c:if test="${not empty customer.delivery.country}">${customer.delivery.country}
							</c:if>
						
						</address>
					  </form:form>
					</c:if>
					 
					<c:if test="${empty customer.delivery}">
					<form:form action="${editAddress}" id="addShippingAddress_${customer.id}">
					   <input type="hidden" name="customerId" value="${customer.id}">
						<input type="hidden" name="billingAddress" value="false"/>
					    &nbsp;&nbsp; <a href="javascript:void(0)" onclick="addShippingAddress('${customer.id}');">Add New Address</a> <br />
					    </form:form>
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<!-- close row-fluid-->
</div>
<!--close .container "main-content" -->