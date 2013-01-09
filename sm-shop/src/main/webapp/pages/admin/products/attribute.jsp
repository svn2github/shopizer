<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>				
				
<script src="<c:url value="/resources/js/functions.js" />"></script>

<div class="tabbable">

					<jsp:include page="/common/adminTabs.jsp" />
  					
  					 <div class="tab-content">

    					<div class="tab-pane active" id="catalogue-section">



								<div class="sm-ui-component">	
								
								
				<h3>
						<s:message code="label.product.attribute" text="Attribute" />
				</h3>	
				

				<br/>


				<c:url var="attributeSave" value="/admin/attributes/attribute/save.html"/>


				<form:form method="POST" commandName="attribute" action="${attributeSave}">

      							
      				<form:errors path="*" cssClass="alert alert-error" element="div" />
					<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
						
						
						
			    <div class="control-group">
                        <label><s:message code="label.product.productoptions.name" text="Option name"/></label>
                        <div class="controls">    
	                        <div class="controls">		
	                        		<form:select path="productOption.id">
					  					<form:options items="${options}" itemValue="id" itemLabel="descriptionsSettoList[0].name"/>
				       				</form:select>
	                                <span class="help-inline"><form:errors path="productOption.id" cssClass="error" /></span>
	                        </div>
                        </div>
                 </div>	
                 
                 
                 <div class="control-group" id="optionValue" style="<c:choose><c:when test="productOption.productOptionType!='TEXT'">block;</c:when><c:otherwise>none;</c:otherwise></c:choose>">
                        <label><s:message code="label.product.productoptiosvalue.title" text="Option value name"/></label>
                        <div class="controls">    
	                        <div class="controls">		
	                        		<form:select path="productOptionValue.id">
					  					<form:options items="${optionsValues}" itemValue="id" itemLabel="descriptionsSettoList[0].name"/>
				       				</form:select>
	                                <span class="help-inline"><form:errors path="productOptionValue.id" cssClass="error" /></span>
	                        </div>
                        </div>
                 </div>		
                 
                 <div class="control-group" id="optionValueText">
                 <c:forEach items="${attribute.optionValue.descriptionsSettoList}" var="description" varStatus="counter">
	                  
		                 
		                        <label class="required"><s:message code="label.product.productoptions.name" text="Option name"/> (<c:out value="${description.language.code}"/>)</label>
		                        <div class="controls">
		                        			<form:input id="name${counter.index}" path="optionValue.descriptionsList[${counter.index}].description"/>
		                        			<span class="help-inline"><form:errors path="optionValue.descriptionsList[${counter.index}].description" cssClass="error" /></span>
		                        </div>
		
		                  
		
		                  
		                  		<form:hidden path="optionValue.descriptionsList[${counter.index}].language.code" />
		                  		<form:hidden path="optionValue.descriptionsList[${counter.index}].id" />
	                  
	               </c:forEach>
                   </div>
						
				 <div class="control-group">
                        <label><s:message code="label.product.productoptions.price" text="Price"/></label>
                        <div class="controls">
                                    <form:input id="productPriceAmount" cssClass="highlight" path="priceText"/>
                                    <span class="help-inline"><form:errors path="priceText" cssClass="error" /></span>
                        </div>
                  </div>
                  
                  
                  <div class="control-group">
                        <label class="required"><s:message code="label.entity.order" text="Order"/></label>
                        <div class="controls">
                                    <form:input cssClass="highlight" path="productOptionSortOrder"/>
                                    <span class="help-inline"><form:errors path="productOptionSortOrder" cssClass="error" /></span>

                        </div>
                  </div>	



                  
                  <div class="control-group">
                        <label><s:message code="label.product.attribute.default" text="Default"/></label>
                        <div class="controls">
                                   <form:radiobutton path="attributeDefault" value="true"/>
    								<spring:message code="label.generic.yes"/>
									<form:radiobutton path="attributeDefault" value="false"/>
    								<spring:message code="label.generic.no"/>	
                                    <span class="help-inline"><form:errors path="attributeDefault" cssClass="error" /></span>
                        </div>
                  </div>
                  
                 <div class="control-group">
                        <label><s:message code="label.product.attribute.required" text="Required"/></label>
                        <div class="controls">
                                   <form:radiobutton path="attributeRequired" value="true"/>
    								<spring:message code="label.generic.yes"/>
									<form:radiobutton path="attributeRequired" value="false"/>
    								<spring:message code="label.generic.no"/>	
                                    <span class="help-inline"><form:errors path="attributeRequired" cssClass="error" /></span>
                        </div>
                  </div>
                  
                  <div class="control-group">
                        <label><s:message code="label.product.attribute.display" text="Display only"/></label>
                        <div class="controls">
                                   <form:radiobutton path="attributeDisplayOnly" value="true"/>
    								<spring:message code="label.generic.yes"/>
									<form:radiobutton path="attributeDisplayOnly" value="false"/>
    								<spring:message code="label.generic.no"/>	
                                    <span class="help-inline"><form:errors path="attributeDisplayOnly" cssClass="error" /></span>
                        </div>
                  </div>
                  
                  <div class="control-group well">
                        <label class="required"><s:message code="label.product.attribute.otherweight" text="Additional weight"/></label>
                        <div class="controls">
                                    <form:input cssClass="highlight" path="productAttributeWeight"/>
                                    <span class="help-inline"><form:errors path="productAttributeWeight" cssClass="error" /></span>

                        </div>
                  </div>                 



                  <form:hidden path="id" />
			
			      <div class="form-actions">

                  		<div class="pull-right">

                  			<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
                  			

                  		</div>

            	 </div>
 
            	 </form:form>
            	 
            	 
            	 <br/>
            	 

            	 
            	 
	      			     
      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>		      			     