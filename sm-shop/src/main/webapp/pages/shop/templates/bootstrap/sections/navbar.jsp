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

<div class="navbar navbar-inverse" style="position: static;">
	<div class="navbar-inner">
		<div class="container"><!--
			<a class="btn btn-navbar" data-target=".subnav-collapse"
				data-toggle="collapse"> <a class="brand" href="#">Title</a>
				--><div class="nav-collapse subnav-collapse">
					<ul class="nav">
						<li class="active">
							<a href="#"><s:message code="menu.home" text="Home" /></a>
						</li>
						<!-- Root categories -->
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
					<ul class="nav pull-right"><!--
						<li>
							<a href="#">Link</a>
						</li>
						--><li class="divider-vertical"></li>
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
</div>