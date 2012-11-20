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
								<h3>//TO BE DONE<s:message code="label.orders.title" text="TO BE DEFINED" /></h3>	
								<br/>
								
								
								
								
				 <!-- Listing grid include -->
				 <c:set value="/admin/orders/paging.html" var="pagingUrl" scope="request"/>
				 <c:set value="/admin/orders/remove.html" var="removeUrl" scope="request"/>
				 <c:set value="/admin/orders/editOrder.html" var="editUrl" scope="request"/>
				 <c:set value="/admin/orders/orders.html" var="afterRemoveUrl" scope="request"/>
				 <c:set var="entityId" value="orderId" scope="request"/>
				 <c:set var="componentTitleKey" value="label.orders.title" scope="request"/>
				 <c:set var="gridHeader" value="/pages/admin/orders/orders-gridHeader.jsp" scope="request"/>
				 <c:set var="canRemoveEntry" value="false" scope="request"/>

            	 <jsp:include page="/pages/admin/components/list.jsp"></jsp:include> 
				 <!-- End listing grid include -->
			      			     
			      			     
      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>		      			     