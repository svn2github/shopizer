<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>		



                 
                   <div class="control-group">
                        <label class="required"><s:message code="module.shipping.canadapost.identifier" text="Account identifier"/></label>
	                        <div class="controls">
	                        		<form:input cssClass="input-large highlight" path="integrationKeys['account']" />
	                        </div>
	                        <span class="help-inline"><form:errors path="integrationKeys['account']" cssClass="error" /></span>
                  </div>


                   <div class="control-group">
                        	<label><strong></strong><s:message code="module.shipping.canadapost.packages" text="Packages/></strong></label>
                        	<div class="controls">
                        			
                                    <form:checkbox path="integrationOptions['packages']" value="PACKAGE"/>
                                    <label for="option1">Package</label>
                                    <form:checkbox path="integrationOptions['packages']" value="ENVELOPE"/>
                                    <label for="option2">Envelope</label>
                        	</div>
                        	<span class="help-inline"><form:errors path="integrationOptions['packages']" cssClass="error" /></span>
                  </div>
                  
            
            
                  
                  