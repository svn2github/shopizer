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
						      			  		<table>
						      			  			<tr>
						      			  				<td>
						      			  					<c:choose>
						      			  						<c:when test="${configuredModules[module.code]!=null && configuredModules[module.code].active==true}">
						      			  							<img src="<c:url value="/resources/img/icon_green_on.gif"/>" width="12">&nbsp;
						      			  						</c:when>
						      			  						<c:otherwise>
						      			  							<img src="<c:url value="/resources/img/icon_red_on.gif"/>" width="12">&nbsp;
						      			  						</c:otherwise>
						      			  					</c:choose>
						      			  				</td>
						      			  				<td><label><a href="<c:url value="/admin/shipping/shippingMethod.html?code="/><c:out value="${module.code}"/>"><s:message code="module.shipping.${module.code}" text="No label found - ${module.code}"/></a></label></td>
						      			  				<td><img src="<c:url value="/resources/img/shipping/${module.image}"/>"></td>
						      			  			</tr>
						      			  		</table>
						                        
						                  </div>
					                  
					                  </c:forEach>
					                  

            	 			</form:form>
							
							


      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>