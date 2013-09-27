<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>		



<div class="tabbable">

  					
 					<jsp:include page="/common/adminTabs.jsp" />
  					
  					 <div class="tab-content">
  					
  					
  					<div class="tab-pane active" id="admin-cache">


							<div class="sm-ui-component">
								<h3><s:message code="menu.system-configurations" text="System configurations" /></h3>	
							<br/>
							
							<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>   
	                        <div id="store.error" class="alert alert-error" style="display:none;"><s:message code="message.error" text="An error occured"/></div>
								
		                        	   <div class="control-group">
	                        				<label><s:message code="label.customer.displaycustomersection" text="Display customer section" /> &nbsp;:&nbsp;</label>
	                        				<div class="controls">
	                        					<form:checkbox path="displayCustomerSection" /> 
	                                   			<span class="help-inline"></span>
	                        				</div>
	                  				   </div>

   					</div>


  					</div>

				</div>