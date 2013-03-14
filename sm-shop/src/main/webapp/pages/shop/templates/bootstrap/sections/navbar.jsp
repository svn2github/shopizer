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
                		<c:when test="test=${requestScope.store.storeLogo!=null}">
                			<img class="logoImage" src="<sm:storeLogo/>" />
                		</c:when>
                		<c:otherwise>
                			<a href="<c:url value="/shop/"/>">
                				<h1><c:out value="${requestScope.store.storename}"</h1>
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




<!--<div class="navbar navbar-inverse" style="position: static;">
	<div class="navbar-inner">
		<div class="container">
			<a class="btn btn-navbar" data-target=".subnav-collapse"
				data-toggle="collapse"> <a class="brand" href="#">Title</a>
				<div class="nav-collapse subnav-collapse">
					<ul class="nav">
						<li class="active">
							<a href="#"><s:message code="menu.home" text="Home" /></a>
						</li>
						 Root categories 
						<li>
							<a href="#">Category 1</a>
						</li>
						<li>
							<a href="#">Category 2</a>
						</li>
					</ul>
					<form class="navbar-search pull-right" action="">
						<input class="search-query span2" type="text" placeholder="Search">
					</form>
					<ul class="nav pull-right">
						<li>
							<a href="#">Link</a>
						</li>
						<li class="divider-vertical"></li>
						<li class="dropdown">
							<a class="dropdown-toggle" data-toggle="dropdown" href="#">
								Logon <b class="caret"></b> </a>
							<ul class="dropdown-menu">
								<li>
									<a href="#">Action</a>
								</li>
								<li>
									<a href="#">Another action</a>
								</li>
								<li>
									<a href="#">Something else here</a>
								</li>
								<li class="divider"></li>
								<li>
									<a href="#">Separated link</a>
								</li>
							</ul>
						</li>
					</ul>
				</div>
		</div>
	</div>
</div>-->