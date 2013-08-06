<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>		

    <script src="<c:url value="/resources/js/jquery.showLoading.min.js" />"></script>
	<link href="<c:url value="/resources/css/showLoading.css" />" rel="stylesheet">		
				
<script>
    $(function() {

        $(".clear-cache").click(function() {
        	$("#alert-error").hide();
        	$("#alert-success").hide();
			$('.tab-content').showLoading();
            var cacheKey = $(this).id;
            $.ajax({
                'type': 'POST',
                'url': "<c:url value="/admin/cache/clear.html"/>?cacheKey=" + cacheKey,
                'contentType': 'application/json',
                'dataType': 'json',
                'success': function(result) {
                   $('.tab-content').hideLoading();
                   var response = result.response;
                   if (response.status==0) {
                   		$("#alert-success").show();
                   } else {
						$("#alert-error").show();
                   }
                }
            });
 
            return false;
        });
    });

</script>


<div class="tabbable">

  					
 					<jsp:include page="/common/adminTabs.jsp" />
  					
  					 <div class="tab-content">
  					
  					
  					<div class="tab-pane active" id="admin-cache">


							<div class="sm-ui-component">
								<h3><s:message code="menu.cache" text="Cache management" /></h3>	
							<br/>
							
							<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>   
	                        <div id="store.error" class="alert alert-error" style="display:none;"><s:message code="message.error" text="An error occured"/></div>
								
							<c:forEach items="${keys}" var="key">
								<form class="form-inline">
								      <label>${key}</label>
								      <button id="${key}" type="submit" class="btn clear-cache"><s:message code="button.label.clear" text="Clear" /></button>
								      <br/>
								</form>
							</c:forEach>

   					</div>


  					</div>

				</div>