<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>				
				


<div class="tabbable">

		<jsp:include page="/common/adminTabs.jsp" />		
  		<div class="tab-content">
    		<div class="tab-pane active" id="catalogue-section">
			<div class="sm-ui-component">	

				<h3>
					<c:choose>
						<c:when test="${optionValue.id!=null && optionValue.id>0}">
								<s:message code="label.product.productoptionvalue.edit.title" text="Edit option value" /> <c:out value="${optionValue.descriptions[0].name}"/>
						</c:when>
						<c:otherwise>
								<s:message code="label.product.productoptionvalue.create.title" text="Create option value" />
						</c:otherwise>
					</c:choose>
				</h3>
				<br/>

				<c:url var="optionSave" value="/admin/optionvalue/save.html"/>


				<form:form method="POST" commandName="optionValue" action="${optionSave}">

      							
      				<form:errors path="*" cssClass="alert alert-error" element="div" />
					<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
								
                 <c:forEach items="${optionValue.descriptionsSettoList}" var="description" varStatus="counter">
                  
	                 <div class="control-group">
	                        <label class="required"><s:message code="label.product.productoptions.name" text="Option name"/> (<c:out value="${description.language.code}"/>)</label>
	                        <div class="controls">
	                        			<form:input id="name${counter.index}" path="descriptionsList[${counter.index}].name"/>
	                        			<span class="help-inline"><form:errors path="descriptionsList[${counter.index}].name" cssClass="error" /></span>
	                        </div>
	
	                  </div>
	
	                  
	                  <form:hidden path="descriptionsList[${counter.index}].language.code" />
	                  <form:hidden path="descriptionsList[${counter.index}].id" />
                  
                  </c:forEach>
                  
                  <div class="control-group">
                        <label><s:message code="label.product.productoptionvalue" text="Option value image"/></label>
                        <div class="controls">
                        			<input class="input-file" id="image" name="image" type="file">
                        			<span class="help-inline"><form:errors path="productOptionValueImage" cssClass="error" /></span>
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