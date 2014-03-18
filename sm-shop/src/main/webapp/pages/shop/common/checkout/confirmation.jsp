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


          Confirmation ! <strong><c:out value="${order.id}" /></strong>
          
          
          <br/>
          
          <c:if test="${downloads!=null}">
          	<c:forEach items="${downloads}" var="download">
          		-- <c:out value="${download.fileName}" />
          	</c:forEach>
          </c:if>
          
          
            </div>
            
          </div>
          
      </div>