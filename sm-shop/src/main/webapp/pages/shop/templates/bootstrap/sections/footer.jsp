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
                <div class="row-fluid">  
                	<c:if test="${requestScope.CONFIGS['displayStoreAddress'] == true}">                 
                    <div class="span3">
						<div class="company">
							<p> 
								<jsp:include page="/pages/shop/common/preBuiltBlocks/storeAddress.jsp"/>
							</p>
						</div>
                    </div>
                    </c:if>
                     <c:if test="${not empty requestScope.CONTENT_PAGE}">
					 <div class="span3 contentPages">   
						<h4><s:message code="label.store.information.title" text="Informations"/></h4>
						<!-- Pages -->
                        <ul class="footerLiks">
                        	<c:forEach items="${requestScope.CONTENT_PAGE}" var="content">
							   <li><a href="<c:url value="/shop/pages/${content.seUrl}.html"/>" class="current" style="color: #fff;">${content.name}</a></li>
							</c:forEach>
							<c:if test="${requestScope.CONFIGS['displayContactUs']==true}">
								<li><a href="<c:url value="/shop/contact/contactus.html"/>" style="color: #fff;"><s:message code="label.customer.contactus" text="Contact us"/></a></li>
							</c:if>
						</ul>
                    </div>
                    </c:if>
                    <div class="span3 customerSection">
                    	<c:if test="${requestScope.CONFIGS['displayCustomerSection'] == true}">
                        <h4><s:message code="label.customer.myaccount" text="My Account" /></h4>
                        <ul class="footerLiks">
							<li><a href="#" style="color: #fff;"><s:message code="button.label.login" text="Login" /></a></li>
						</ul>
						</c:if>
                    </div>
                    <div class="span3 socialLinksSection">
                    	<!-- Social links -->
                    	<c:if test="${requestScope.CONFIGS['facebook_page_url'] != null}">
	                        <h4><s:message code="label.social.connect" text="Connect with us"/></h4>
	                        <c:if test="${requestScope.CONFIGS['facebook_page_url'] != null}">
	                        	<a href="<c:out value="${requestScope.CONFIGS['facebook_page_url']}"/>"><img src="<c:url value="/resources/img/facebook-transparent.png" />" width="40"></a>
	                        </c:if>
	                        <c:if test="${requestScope.CONFIGS['twitter_handle'] != null}">
	                        	<a href="<c:out value="${requestScope.CONFIGS['twitter_handle']}"/>"><img src="<c:url value="/resources/img/twitter-transparent.png" />" width="50"></a>
	                        </c:if>
                        </c:if>
                    </div>				
                </div>
		    <div id="footer-bottom">
				<div class="container">
				   <div class="row-fluid">
					<div class="span12 text">&copy;&nbsp;<s:message code="label.generic.providedby" /> <a href="http://www.shopizer.com" class="footer-href" target="_blank">Shopizer</div>
				   </div>
				 </div>
		    </div>
            </footer>