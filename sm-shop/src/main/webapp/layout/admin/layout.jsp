<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  
 <%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  
 <c:set var="lang" scope="request" value="${requestScope.locale.language}"/> 
 
 
 <html xmlns="http://www.w3.org/1999/xhtml"> 
 
 
     <head>
     
     
        	 	<meta charset="utf-8">
    			<title><s:message code="label.storeadministration" text="Store administration" /></title>
    			<meta name="viewport" content="width=device-width, initial-scale=1.0">
    			<meta name="description" content="">
    			<meta name="author" content="">
    			
    			<script src="<c:url value="/resources/js/bootstrap/jquery.js" />"></script>
    			<script src="<c:url value="/resources/js/jquery.friendurl.min.js" />"></script>
 
  
                <jsp:include page="/common/adminLinks.jsp" />
                
                


 	
 	
 	</head>
 
 	<body class="body">

     <p>&nbsp;</p>

<div class="sm">

	<div class="container"> 
		<div class="row">

  			<div class="span4"><a class="brand" href="#"><img src="<c:url value="/resources/img/shopizer_small.jpg" />"/></a></div>

  			<div class="span4 offset4">

					<div class="btn-group pull-right">
						<c:url value="/j_spring_security_logout" var="logoutUrl"/>
						<a href="${logoutUrl}"><s:message code="button.label.logout" text="Logout" /></a>
					</div>

  			</div>


   
		
		</div>
		<div class="row">	
		
			<div class="span3">
				<ul class="nav nav-list">
					  <c:forEach items="${requestScope.MENULIST}" var="menu">
					  			<li <c:if test="${activeMenus[menu.code]!=null}"> class="active"</c:if>>
									<a href="<c:url value="${menu.url}" />">
										<i class="${menu.icon}"></i>
											<s:message code="menu.${menu.code}" text="${menu.code}"/>
									</a>
					  			</li>
					  </c:forEach>
				</ul>
			</div><!-- end span 3 -->

			<div class="span9">
				
				
				<tiles:insertAttribute name="body"/>



			</div>


		</div>



  
		<hr> 
  
  
		<footer> 
 			<p>&copy; Company 2012</p> 
		</footer> 
  
  
	</div> <!-- /container --> 
	

  
</div>

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-button.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-modal.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-tab.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-transition.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-alert.js" />"></script>
    
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-dropdown.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-scrollspy.js" />"></script>
    
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-tooltip.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-popover.js" />"></script>
    
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-collapse.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-carousel.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-typeahead.js" />"></script>
     

    
     	
     	
     <script>
	
		$(document).ready(function(){ 
			
			
			

			
			$("#catalogue-products-create-link").click(function() {
				window.location='<c:url value="/admin/products/product.html" />';
			});
			$("#catalogue-categories-list-link").click(function() {
				window.location='<c:url value="/admin/categories/categories.html" />';
			});
			$("#catalogue-products-categories-link").click(function() {
				window.location='<c:url value="/admin/products/product-categories.html" />';
			});
			$("#catalogue-categories-hierarchy-link").click(function() {
				window.location='<c:url value="/admin/categories/hierarchy.html" />';
			});
			$("#catalogue-categories-create-link").click(function() {
  				window.location='<c:url value="/admin/categories/createCategory.html" />';
			});
			$("#catalogue-options-list-link").click(function() {
  				window.location='<c:url value="/admin/options/options.html" />';
			});
			$("#catalogue-options-create-link").click(function() {
  				window.location='<c:url value="/admin/options/option.html" />';
			});
			
			

		}); 
		
		
		function checkCode(code, url) {
			
			
			$.ajax({
					type: 'POST',
					dataType: "json",
					url: url,
					data: "code="+ code,
					success: function(response) { 
						var msg = isc.XMLTools.selectObjects(response, "/response/statusMessage");
						var status = isc.XMLTools.selectObjects(response, "/response/status");
						
						callBackCheckCode(msg,status);

						
					},
					error: function(jqXHR,textStatus,errorThrown) { 
						alert(jqXHR + "-" + textStatus + "-" + errorThrown);
					}
					
			});
			
			
			
		}
	
</script>	
     	
         
 
 	</body>
 
 </html>
 
