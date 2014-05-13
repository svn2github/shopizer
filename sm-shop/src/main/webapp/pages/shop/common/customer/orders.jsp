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

<div class="white">

				<header class="page-header">
					<h1>Order History</h1>
				</header>

				<div id="shop">

					<!-- HISTORY TABLE -->
					<table class="table table-striped">
						<!-- table head -->
						<thead>
							<tr>
								<th>#</th>
								<th>Order Id</th>
								<th>Order Date</th>
								<th>Amount</th>
								<th>Status</th>
							</tr>
						</thead>
						
						<!-- table items -->
						<tbody>
							<tr><!-- item -->
								<td>1</td>
								<td><a href="http://theme.stepofweb.com/Alkaline/v1.0/shop-history-summary.html">6877</a></td>
								<td>29 June 2014 / 08:33:21</td>
								<td>$456.00 <small>(3 items)</small></td>
								<td>Pending</td>
							</tr><!-- /item -->
							<tr><!-- item -->
								<td>2</td>
								<td><a href="http://theme.stepofweb.com/Alkaline/v1.0/shop-history-summary.html">6878</a></td>
								<td>12 Jul 2014 / 12:34:41</td>
								<td>$233.00 <small>(2 items)</small></td>
								<td>Completed</td>
							</tr><!-- /item -->
							<tr><!-- item -->
								<td>3</td>
								<td><a href="http://theme.stepofweb.com/Alkaline/v1.0/shop-history-summary.html">6879</a></td>
								<td>15 September 2014 / 18:23:44</td>
								<td>$987.00 <small>(8 items)</small></td>
								<td>Canceled</td>
							</tr><!-- /item -->
							<tr><!-- item -->
								<td>4</td>
								<td><a href="http://theme.stepofweb.com/Alkaline/v1.0/shop-history-summary.html">6880</a></td>
								<td>21 December 2014 / 08:33:21</td>
								<td>$1897.00 <small>(13 items)</small></td>
								<td>Completed</td>
							</tr><!-- /item -->
							<tr><!-- item -->
								<td>5</td>
								<td><a href="http://theme.stepofweb.com/Alkaline/v1.0/shop-history-summary.html">6898</a></td>
								<td>29 December 2014 / 10:21:32</td>
								<td>$122.00 <small>(2 items)</small></td>
								<td>Completed</td>
							</tr><!-- /item -->
						</tbody>
					</table>
					<!-- /HISTORY TABLE -->


					<div class="divider half-margins"><!-- divider 30px --></div>


					<!-- PAGINATION -->
					<div class="row">

						<div class="col-md-6 text-left">
							<p class="hidden-xs pull-left nomargin padding20">Showing 1-10 of 38 results.</p>
						</div>

						<div class="col-md-6 responsive-text-center text-right">
							<ul class="pagination">
								<li><a href="#"><i class="fa fa-chevron-left"></i></a></li>
								<li class="active"><a href="#">1</a></li>
								<li><a href="#">2</a></li>
								<li><a href="#">3</a></li>
								<li><a href="#"><i class="fa fa-chevron-right"></i></a></li>
							</ul>
						</div>

					</div>
					<!-- /PAGINATION -->

				</div>

			</div>





		</div>
		<!-- close row-fluid--> 
	</div>
	<!--close .container "main-content" -->