<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>				
				

<script src="<c:url value="/resources/js/ckeditor/ckeditor.js" />"></script>

<div class="tabbable">

					<jsp:include page="/common/adminTabs.jsp" />
  					
  					 <div class="tab-content">

    					<div class="tab-pane active" id="catalogue-section">



								<div class="sm-ui-component">	
								
								
				<h3><s:message code="label.category.createcategory" text="Edit category" /></h3>	
				<br/>

				<c:url var="categorySave" value="/admin/categories/save.html"/>


				<form:form method="POST" commandName="category" action="${categorySave}">

      							
      				<form:errors path="*" cssClass="alert alert-error" element="div" />
					<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
								


      			  
      			 <div class="control-group">
                        <label><s:message code="label.category.parentcategory" text="Category vsible"/></label>
                        <div class="controls">
                                   
	                        <div class="controls">
	                        		<s:message code="label.category.root" text="Root" var="rootVar"/>			
	                        		<form:select path="parent.id">
	                        			<form:option value="-1" label="${rootVar}" />
					  					<form:options items="${categories}" itemValue="id" itemLabel="descriptions[0].name"/>
				       				</form:select>
	                                <span class="help-inline"><form:errors path="parent.id" cssClass="error" /></span>
	                        </div>

                        </div>
                  </div>
      			  
      			  	
				  <div class="control-group">
                        <label><s:message code="label.category.categoryvisible" text="Category vsible"/></label>
                        <div class="controls">
                                    <form:checkbox path="visible" />

                        </div>
                  </div>
                  
                  <div class="control-group">
                        <label class="required"><s:message code="label.category.code" text="Category code"/></label>
	                        <div class="controls">
	                        		<form:input cssClass="input-large" path="code" />
	                                <span class="help-inline"><form:errors path="code" cssClass="error" /></span>
	                        </div>
                  </div>
                  
                 <c:forEach items="${category.descriptions}" var="description" varStatus="counter">
                  
                 <div class="control-group">
                        <label class="required"><s:message code="label.productedit.categoryname" text="Category name"/> (<c:out value="${description.language.code}"/>)</label>
                        <div class="controls">
                        			<form:input path="descriptions[${counter.index}].name"/>
                        			<span class="help-inline"><form:errors path="descriptions[${counter.index}].name" cssClass="error" /></span>
                        </div>

                  </div>
                  
                  <div class="control-group">
                        <label><s:message code="label.category.categorydescription" text="Category description"/> (<c:out value="${description.language.code}"/>)</label>
                        <div class="controls">
                        

                        
                        <textarea cols="30" id="descriptions[${counter.index}].description" class="ckeditor" name="descriptions[${counter.index}].description">
                        		<c:out value="${category.descriptions[counter.index].description}"/>
                        </textarea>


                        </div>

                  </div>
                  
                  <div class="control-group">
                        <label><s:message code="label.sefurl" text="SEF Url"/> (<c:out value="${description.language.code}"/>)</label>
                        <div class="controls">
                        			<form:input path="descriptions[${counter.index}].seUrl"/>
                        			<span class="help-inline"><form:errors path="descriptions[${counter.index}].seUrl" cssClass="error" /></span>
                        </div>

                  </div>
                  
                  <div class="control-group">
                        <label><s:message code="label.category.title" text="Metatag title"/> (<c:out value="${description.language.code}"/>)</label>
                        <div class="controls">
                        			<form:input path="descriptions[${counter.index}].metatagTitle"/>
                        			<span class="help-inline"><form:errors path="descriptions[${counter.index}].metatagTitle" cssClass="error" /></span>
                        </div>
                  </div>
                  
                  <div class="control-group">
                        <label><s:message code="label.metatags.keywords" text="Metatag keywords"/> (<c:out value="${description.language.code}"/>)</label>
                        <div class="controls">
                        			<form:input path="descriptions[${counter.index}].metatagKeywords"/>
                        			<span class="help-inline"><form:errors path="descriptions[${counter.index}].metatagKeywords" cssClass="error" /></span>
                        </div>
                  </div>
                  
                 <div class="control-group">
                        <label><s:message code="label.metatags.description" text="Metatag description"/> (<c:out value="${description.language.code}"/>)</label>
                        <div class="controls">
                        			<form:input path="descriptions[${counter.index}].metatagDescription"/>
                        			<span class="help-inline"><form:errors path="descriptions[${counter.index}].metatagDescription" cssClass="error" /></span>
                        </div>
                  </div>
                  
                  <form:hidden path="descriptions[${counter.index}].language.code" />
                  
                  </c:forEach>
			
			      <div class="form-actions">

                  		<div class="pull-right">

                  			<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>

                  		</div>

            	 </div>
            	 
            	<%--  //form:radiobuttons path="favNumber" items="${numberList}" />
            	 //form:checkboxes items="${webFrameworkList}" path="favFramework" />
            	 //form:radiobutton path="sex" value="M" />Male form:radiobutton path="sex" value="F" />Female</td>
            	  --%>
            	 
            	 
            	 </form:form>
	      			     
      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>		      			     