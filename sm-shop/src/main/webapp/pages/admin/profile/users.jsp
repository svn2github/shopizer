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
								<h3><s:message code="menu.users" text="Users" /></h3>	
								<br/>
								
								<!-- Listing grid include -->
								 <c:set value="/admin/users/paging.html" var="pagingUrl" scope="request"/>
								 <c:set value="displayUserEdit" var="editUrl" scope="request"/>
								 <c:set var="entityId" value="userId" scope="request"/>
								 <c:set var="componentTitleKey" value="menu.users" scope="request"/>
								 <c:set var="gridHeader" value="/pages/admin/profile/users-gridHeader.jsp" scope="request"/>
								 <sec:authorize access="hasRole('STORE_ADMIN') and fullyAuthenticated">
								 <c:set var="canRemoveEntry" value="true" scope="request"/>
								 </sec:authorize>
								 <sec:authorize access="!hasRole('STORE_ADMIN')">
								 <c:set var="canRemoveEntry" value="false" scope="request"/>
								 </sec:authorize> 
				
				            	 <jsp:include page="/pages/admin/components/list.jsp"></jsp:include> 
								 <!-- End listing grid include -->

			      			     
			      			     
      					</div>

   					</div>
  					</div>

</div>		      			     