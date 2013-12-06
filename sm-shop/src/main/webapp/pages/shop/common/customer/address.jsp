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
			<div class="row-fluid" id="orders">
				<div class="span12">
				   <div class="span8">

					<div class="box">
						<span class="box-title">
							<p><font color="#FF8C00">Your orders</font></p>
						</span>
						<!-- An order item -->
						<div class="order-item">

						<div class="row-fluid">
							<div style="width:50%;float:left">
								<p class="box-h1">Ordered on 2013-06-27</p>
								Order number : 012367
							</div>
							<div style="width:50%;float:right">
								<p class="box-h1">Delivered</p>
								<i class="icon-envelope"></i>&nbsp;2013-06-30
							</div>
						</div>
						<br/>
						<div class="row-fluid">
							<div class="span3">
								<ul>
									<li>Print invoice</li>
									<li>Cancel order</li>

								</ul>
							</div>
							<div class="span5">
								Details
							</div>

						</div>
			

						</div>


					</div>


				  </div>


				</div>

			</div>
		<!-- close row-fluid--> 
	</div>
	<!--close .container "main-content" -->