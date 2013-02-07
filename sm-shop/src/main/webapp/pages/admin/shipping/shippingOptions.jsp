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
								<h3><s:message code="label.shipping.title" text="Shipping configuration" /> <s:message code="module.shipping.${configuration.moduleCode}" text="No label found - ${configuration.moduleCode}"/></h3>	
								<br/>
								
								
								

							<s:message code="module.shipping.${configuration.moduleCode}.note" text=""/><br/>
							
							<c:url var="saveShippingMethod" value="/admin/shipping/saveShippingMethod.html"/>
							<form:form method="POST" commandName="configuration" action="${saveShippingMethod}">

      							
      								<form:errors path="*" cssClass="alert alert-error" element="div" />
									<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
								

									<div class="control-group">
                        				<label><s:message code="label.entity.enabled" text="Module enabled"/></label>
                        				<div class="controls">
                                    		<form:checkbox path="active" />
                        				</div>
                  					</div>
                  					
                  					<div class="controls">
	                        			<s:message code="label.category.root" text="Root" var="rootVar"/>			
	                        			<form:select path="environment">
					  						<form:options items="${environments}" />
				       					</form:select>
	                                	<span class="help-inline"><form:errors path="environment" cssClass="error" /></span>
	                        		</div>
	                        		
	                        		<jsp:include page="/pages/admin/shipping/${configuration.moduleCode}.jsp"></jsp:include> 
	                        		
	                        		<div class="form-actions">
                  						<div class="pull-right">
                  							<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
                  						</div>
            	 					</div>
					                  

            	 			</form:form>
							
							


      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>