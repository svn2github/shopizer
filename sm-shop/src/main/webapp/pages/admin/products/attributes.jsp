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
						<s:message code="label.product.attributes" text="Option management" />
				</h3>	
				<br/>
				
									<c:if test="${product.id!=null && product.id>0}">
										<c:set value="${product.id}" var="productId" scope="request"/>
										<jsp:include page="/pages/admin/products/product-menu.jsp" />
									</c:if>
								
								    <ul class="nav nav-pills">
    									<li class="enabled"><a href="<c:url value="/admin/products/attribute/createAttribute.html" />"><s:message code="label.product.attribute.create" text="Create attribute" /></a></li>
    								</ul>

				 <!-- Listing grid include -->
				 <c:set value="/admin/attributes/paging.html?productId=${product.id}" var="pagingUrl" scope="request"/>
				 <c:set value="/admin/attributes/remove.html" var="removeUrl" scope="request"/>
				 <c:set value="/admin/attributes/editAttribute.html" var="editUrl" scope="request"/>
				 <c:set value="/admin/attributes/attributes.html" var="afterRemoveUrl" scope="request"/>
				 <c:set var="entityId" value="attributeId" scope="request"/>
				 <c:set var="componentTitleKey" value="label.product.attributes" scope="request"/>
				 <c:set var="gridHeader" value="/pages/admin/products/attributes-gridHeader.jsp" scope="request"/>
				 <c:set var="canRemoveEntry" value="true" scope="request"/>

            	 <jsp:include page="/pages/admin/components/list.jsp"></jsp:include> 
				 <!-- End listing grid include -->


			      			     
            	 
            	 
	      			     
      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>		      			     