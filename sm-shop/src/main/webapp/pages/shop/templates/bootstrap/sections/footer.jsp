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

	  <!-- footer -->
            <footer>
                <div class="row">                   
                    <div class="span3">
						<div class="company">
							<p>
								<jsp:include page="/pages/shop/common/preBuiltBlocks/storeAddress.jsp"/>
							</p>
						</div>
                    </div>
					 <div class="span3">   
						<h4><s:message code="label.store.information.title" text="Informations"/></h4>
						<!-- Pages -->
                        <ul>
                        	<c:forEach items="${requestScope.CONTENT_PAGE}" var="content">
							   <li><a href="<c:url value="/shop/pages/${content.seUrl}.html"/>" class="current">${content.name}</a></li>
							</c:forEach>
						</ul>
                    </div>
                    <div class="span3">
                    	<c:if test="${request.CONFIGS['displayCustomerSection'] != null}">
                        <h4><s:message code="label.customer.myaccount" text="My Account" /></h4>
                        <ul>
							<li><a href="#"><s:message code="button.label.login" text="Login" /></a></li>
						</ul>
						</c:if>
                    </div>
                    <div class="span3">
                    	<!-- Social links -->
                    	<c:if test="${request.CONFIGS['facebook_page_url'] != null}">
	                        <h4><s:message code="label.social.connect" text="Connect with us"/></h4>
	                        <c:if test="${request.CONFIGS['facebook_page_url'] != null}">
	                        <a href="<c:out value="${request.CONFIGS['facebook_page_url']}"/>"><s:message code="label.social.facebook" text="Facebook"/></a>
	                        <br>
	                        </c:if>
	                        <c:if test="${request.CONFIGS['twitter_handle'] != null}">
	                        <a href="<c:out value="${request.CONFIGS['twitter_handle']}"/>"><s:message code="label.social.twitter" text="Twitter"/></a>
	                        </c:if>
                        </c:if>
                    </div>				
                </div>
		    <div id="footer-bottom">
				<div class="container">
				   <div class="row">
					<div class="span12 text">&copy;&nbsp;<s:message code="label.generic.providedby" /> <a href="http://www.shopizer.com" class="footer-href" target="_blank">Shopizer</div>
				   </div>
				 </div>
		    </div>
            </footer>