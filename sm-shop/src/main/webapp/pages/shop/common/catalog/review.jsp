<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<script src="<c:url value="/resources/js/jquery.raty.min.js" />"></script>

					<div id="review" class="row-fluid">

					<h3>Rate item</h3>
					
					<div class="span12 no_margin">
					<div class="span6">
						<table class="table" style="margin-bottom: 35px">
							<tbody>
								<tr>
								<c:if test="${product.image!=null}">
								<td>
									<img width="60" src="<c:url value="${product.image.imageUrl}"/>">
								</td>
								</c:if>
								<td>
									<c:out value="${product.description.name}"/>
								</td>
								<td>
									<c:out value="${product.finalPrice}"/>
								</td>
								</tr>
							</tbody>
						</table>
						<br/>

					<c:url var="saveReview" value="/customer/review/save.html"/>
				    <form:form method="POST" commandName="review" action="${saveReview}">
				    	<form:hidden path="rating"/>
				    	<form:hidden path="customer.id"/>
				    	<form:hidden path="productId"/>
						    <label>Your opinion</label>
						    <textarea name="" rows="6" class="span6" path="description"></textarea>
							<label>&nbsp;</label>
						    <span class="help-block">Product rating (click on the stars to activate rating)</span>
						    <div id="productRating" style="width: 100px;"></div>
									<script>
									$(function() {
										$('#productRating').raty({ 
											readOnly: false, 
											half: true,
											path : '<c:url value="/resources/img/stars/"/>',
											score: 5,
											click: function(score, evt) {
												    $('#rating').val(score);
										    }
										});
									});	
									</script>
							<br/>
						    <button type="submit" class="btn">Submit</button>
				    </form:form>
					</div>
					<div class="span6">&nbsp;</div>
					</div>
					</div>
					

