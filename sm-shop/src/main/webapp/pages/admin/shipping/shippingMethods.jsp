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
								<h3><s:message code="label.shipping.title" text="Shipping configuration" /></h3>	
								<br/>
								
								
								


							
							<c:url var="saveShippingMethods" value="/admin/shipping/saveShippingMethods.html"/>
							<form:form method="POST" commandName="configuration" action="${saveShippingMethods}">

      							
      								<form:errors path="*" cssClass="alert alert-error" element="div" />
									<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
								

									  <c:forEach items="${modules}" var="module">
      			  
					      			  <div class="control-group">
					                        <c:choose><c:when></c:when><c:otherwise></c:otherwise></c:choose><label><s:message code="label.shipping.module.${module.code}" text="Shipping method"/></label>
					                  </div>
					                  
					                  </c:forEach>
					                  

            	 			</form:form>
							
							


      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>