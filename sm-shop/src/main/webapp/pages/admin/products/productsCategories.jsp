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
					<s:message code="label.product.category.association" text="Associate to categories" />
				</h3>
				
				
			<!--  Add content images -->
			<c:url var="addCategory" value="/admin/product/addCategory.html" />
			<form:form method="POST" enctype="multipart/form-data" commandName="categories" action="${addCategory}">
			<form:errors path="*" cssClass="alert alert-error" element="div" />
			<div id="store.success" class="alert alert-success"	style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">
					<s:message code="message.success" text="Request successfull" />
			</div>
			
			<div class="control-group">
				<label><s:message code="label.productedit.categoryname" text="Category"/></label>
			  	<div class="controls">
	                        		<form:select path="parent.id">
					  					<form:options items="${categories}" itemValue="id" itemLabel="descriptions[0].name"/>
				       				</form:select>
	                                <span class="help-inline"><form:errors path="parent.id" cssClass="error" /></span>
				</div>
			</div>
			
			
			<div class="form-actions">
                  		<div class="pull-right">
                  			<button type="submit" class="btn btn-success"><s:message code="button.label.upload" text="Upload Images"/></button>
                  		</div>
            	 </div>
			
		  </form:form>
				
				
				
				<br />
				<!-- Listing grid include -->
				<c:set value="/admin/product-categories/paging.html" var="pagingUrl" scope="request" />
				<c:set value="/admin/product-categories/removeCategory.html" var="removeUrl" scope="request" />
				<c:set value="/admin/products/addProductToCategories.html" var="refreshUrl" scope="request" />
				<c:set var="entityId" value="categoryId" scope="request"/>
				<c:set var="componentTitleKey" value="label.categories.title" scope="request" />
				<c:set var="canRemoveEntry" value="true" scope="request" />
				<jsp:include page="/pages/admin/components/list.jsp"></jsp:include>
				<!-- End listing grid include -->
			

		</div>
	   </div>
	</div>
</div>	