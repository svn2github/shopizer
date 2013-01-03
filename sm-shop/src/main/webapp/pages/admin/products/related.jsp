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

    					<div class="tab-pane active" id="catalogue-section">

								
								


								<div class="sm-ui-component">
								
				<h3>
						<s:message code="label.product.related.title" text="Related items" />
				</h3>	
				<br/>
				<div class="alert alert-info">
					<s:message code="label.product.featured.meassage" text="Drag and drop product from product list to related items box"/>
				</div>			
		
      			<!-- Listing grid include -->
				 <c:set value="/admin/products/paging.html?productId=${productId}" var="pagingUrl" scope="request"/>
				 <c:set value="/admin/catalogue/related/paging.html" var="containerFetchUrl" scope="request"/>
				 <c:set value="/admin/catalogue/related/removeItem.html" var="containerRemoveUrl" scope="request"/>
				 <c:set value="RELATED" var="removeEntity" scope="request"/>
				 <c:set value="/admin/catalogue/related/addItem.html" var="containerAddUrl" scope="request"/>
				 <c:set value="/admin/catalogue/related/update.html" var="containerUpdateUrl" scope="request"/>
				 <c:set value="/admin/products/editProduct.html" var="editUrl" scope="request"/>
				 <c:set value="/admin/catalogue/related/list.html" var="reloadUrl" scope="request"/>
				 <c:set var="componentTitleKey" value="label.product.related.title" scope="request"/>
				 <!-- same headers than featured -->
				 <c:set var="gridHeader" value="/pages/admin/products/featured-gridHeader.jsp" scope="request"/>
				 <c:set var="gridHeaderContainer" value="/pages/admin/products/featured-gridHeader-items.jsp" scope="request"/>
				 <c:set var="canRemoveEntry" value="true" scope="request"/>

            	 <jsp:include page="/pages/admin/components/product-container.jsp"></jsp:include> 
				 <!-- End listing grid include -->


      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>