<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>		



                 
                   <div class="control-group">
                        <label class="required"><s:message code="module.shipping.canadapost.identifier" text="Account identifier"/></label>
	                        <div class="controls">
	                        		<form:input cssClass="input-large" path="${integrationKeys['account']}" />
	                        </div>
                  </div>
                  
                   <div class="control-group">
                        	<label><strong></strong><s:message code="module.shipping.canadapost.packages" text="Packages/></strong></label>
                        	<div class="controls">
                                    <form:checkbox path="${integrationOptions['packages']}" />
                        	</div>
                  </div>
                  
            
            
                  
                  <form:hidden path="module['code']" />
