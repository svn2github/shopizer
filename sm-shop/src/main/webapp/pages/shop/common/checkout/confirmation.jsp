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
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%> 


	<div id="main-content" class="container clearfix">
		<div class="row-fluid">
			<div class="span12">  


          <p class="lead"><c:out value="${ordermessage}" /></p>
          <p><c:out value="${orderemail}" /></p>
          

          
          <c:if test="${downloads!=null}">
          	<p>
          	<c:choose>
          		<c:when test="${order.status.value=='processed'}">
          		    <strong><s:message code="label.checkout.downloads.completed" text="label.checkout.downloads.completed"/></strong><br/>
          			<c:forEach items="${downloads}" var="download">
          				<a href="<sm:orderProductDownload productDownload="${download}" orderId="${order.id}"/>"><c:out value="${download.fileName}" /></a>
          			</c:forEach>
          		</c:when>
          		<c:otherwise>
					<s:message code="label.checkout.downloads.processing" text="*** An email with your file(s) download instructions will be sent once the payment for this order will be completed."/>
          		</c:otherwise>
          	</c:choose>
			</p>
          </c:if>
          
          <p class="muted"><s:message code="label.checkout.additionaltext" text="If you have any comments or suggestions for us, please send us an email with your order id. We value your feedback."/></p>
          
          
            </div>
            
          </div>
          
      </div>