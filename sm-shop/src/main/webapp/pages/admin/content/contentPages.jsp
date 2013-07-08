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
					<s:message code="label.content.pages" text="Content pages" />
				</h3>
				
				

			
				
				
				
				<br />
				<!-- Listing grid include -->
				<c:set value="/admin/content/page.html?contentType=PAGE" var="pagingUrl" scope="request" />
				<c:set value="/admin/content/remove.html" var="removeUrl" scope="request" />
				<c:set value="/admin/content/pages/listContent.html" var="refreshUrl" scope="request" />
				<c:set var="componentTitleKey" value="label.content.pages" scope="request" />
				<c:set var="entityId" value="id" scope="request"/>
				<c:set var="canRemoveEntry" value="true" scope="request" />
				<c:set var="gridHeader" value="/pages/admin/content/content-gridHeader.jsp" scope="request"/>
				<jsp:include page="/pages/admin/components/list.jsp"></jsp:include>
				<!-- End listing grid include -->
			

		</div>
	   </div>
	</div>
</div>	