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





<!-- Start Navbar-->
            <div class="row show-grid">
                <div class="span3 logo">
                	<c:choose>
                		<c:when test="test=${requestScope.MERCHANT_STORE.storeLogo!=null}">
                			<img class="logoImage" src="<sm:storeLogo/>" />
                		</c:when>
                		<c:otherwise>
                			<a href="<c:url value="/shop/"/>">
                				<h1><c:out value="${requestScope.MERCHANT_STORE.storename}"/></h1>
                			</a>  
                		</c:otherwise>
                	</c:choose>                    
                </div>
                <div class="span9 offset0">


						<div class="row">
						<nav class="pull-right" id="menu">
						                    <ul class="sf-js-enabled sf-shadow">
						                        <li class="">
						                            <a class="current" href="#">
						                                <span class="name">Home Page</span>
						                                <span class="desc">welcome page</span>
						                            </a>
						                        </li>
						                        <li class="">
						                            <a href="#" class="sf-with-ul">
						                                <span class="name">Category 1</span>
						                                <span class="desc">Highlight</span>
						                            </a>
						
						                        </li>
										
						                        <li class="">
						                            <a href="#" class="sf-with-ul">
						                                <span class="name">Category 2</span>
						                                <span class="desc">highlight</span>
						                            </a>
						                        </li>
						                    </ul>
						</nav>
					</div>
					<div class="row pull-right">
										<form class="form-search">
											<div class="row">
												<input class="span3" id="inputSearch" type="text"> 
												<button type="submit" class="btn">Search</button>
											</div>
										</form>
					</div>



                </div>
            </div>
 <!-- End Navbar-->