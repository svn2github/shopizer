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
								<h3><s:message code="label.product.prices" text="Product prices" /></h3>[TODO add product name]	
								<br/>
								
									<c:if test="${product.product.id!=null && product.product.id>0}">
										<c:set value="${product.product.id}" var="productId" scope="request"/>
										<jsp:include page="/pages/admin/products/product-menu.jsp" />
									</c:if>
								
								    <ul class="nav nav-pills">
    									<li class="enabled"><a href="NEED A URL"><s:message code="label.product.price.create" text="Create price" /></a></li>
    								</ul>
								
								
				 <!-- Listing grid include -->
				 <c:set value="/admin/products/prices/paging.html" var="pagingUrl" scope="request"/>
				 <c:set value="/admin/products/price/remove.html" var="removeUrl" scope="request"/>
				 <c:set value="/admin/products/price/edit.html" var="editUrl" scope="request"/>
				 <c:set value="${pagingUrl}" var="afterRemoveUrl" scope="request"/>
				 <c:set var="entityId" value="priceId" scope="request"/>
				 <c:set var="componentTitleKey" value="label.product.prices" scope="request"/>
				 <c:set var="gridHeader" value="/pages/admin/products/prices-gridHeader.jsp" scope="request"/>
				 <c:set var="canRemoveEntry" value="true" scope="request"/>

            	 <jsp:include page="/pages/admin/components/list.jsp"></jsp:include> 
				 <!-- End listing grid include -->
			      			     
			      			     
      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>		      			     