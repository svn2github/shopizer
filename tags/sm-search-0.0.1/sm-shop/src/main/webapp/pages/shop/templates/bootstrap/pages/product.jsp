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

			<jsp:include page="/pages/shop/templates/bootstrap/sections/breadcrumb.jsp" />
            
            
            <div class="row-fluid">


                
                
                <div class="span12" itemscope itemtype="http://data-vocabulary.org/Product">
                    <div class="row">
						<div class="span4">
							<c:if test="${product.image!=null}">
							<a href="<c:out value="${product.imageUrl}"/>" class="thumbnail" data-fancybox-group="group1" title="<c:out value="${product.name}"/>"><img itemprop="image" alt="<c:out value="${product.name}"/>" src="<c:out value="${product.imageUrl}"/>"></a>												
							<c:if test="${product.images!=null}">
							<ul class="thumbnails small">
								<c:forEach items="${product.images}" var="thumbnail">								
								<li class="span1">
									<a href="<c:out value="${thumbnail.imageUrl}"/>" class="thumbnail" data-fancybox-group="group1" title="<c:out value="${thumbnail.name}"/>"><img src="<c:out value="${thumbnail.imageUrl}"/>" alt="<c:out value="${thumbnail.name}"/>"></a>
								</li>
								</c:forEach>								
							</ul>
							</c:if>
							</c:if>
						</div>
						<!-- TDOD Google rich snippets (http://blog.hubspot.com/power-google-rich-snippets-ecommerce-seo-ht) -->
						<div class="span5">
							<address>
								<strong><s:message code="label.product.brand" text="Brand"/></strong> <span itemprop="brand"><c:out value="${product.manufacturer}" /></span><br>
								<strong><s:message code="label.product.code" text="Product code"/></strong> <span itemprop="identifier" content="mpn:${product.sku}">${product.sku}</span><br>								
							</address>
							<span itemprop="offerDetails" itemscope itemtype="http://data-vocabulary.org/Offer">
							<meta itemprop="seller" content="${requestScope.MERCHANT_STORE.storename}"/>
							<meta itemprop="currency" content="<c:out value="${requestScope.MERCHANT_STORE.currency.code}" />" />
							<h4>
									<c:choose>
										<c:when test="${product.discounted}">
												<del><c:out value="${product.originalProductPrice}" /></del>&nbsp;<span class="specialPrice"><span itemprop="price"><c:out value="${product.productPrice}" /></span></span>
										</c:when>
										<c:otherwise>
												<span itemprop="price"><c:out value="${product.productPrice}" /></span>
										</c:otherwise>
									</c:choose>
									
							</h4>
							<address>
								<strong><s:message code="label.product.available" text="Availability"/></strong> <span><c:choose><c:when test="${product.quantity>0}"><span itemprop="availability" content="in_stock">${product.quantity}</span></c:when><c:otherwise><span itemprop="availability" content="out_of_stock">TODO - Out Of Stock</c:otherwise></c:choose></span><br>								
							</address>
							</span>
						</div>
						<div class="span5">
							<form id="input-<c:out value="${product.id}" />" class="form-inline">
								<!-- Options block -->
								<c:if test="${options!=null}">
									<c:forEach items="${options.values}" var="option">
										<div class="control-group"> 
	                        				<label><c:out value="${option.name}"/></label>
	                        				<div class="controls">		       							
											<c:choose>
												<c:when test="${option.type=='Select'}">
													<select id="${option.id}" name="options[${status.index}].id" class="attribute">
													<c:forEach items="${option.option.id}" var="optionValue">
														<option value="${optionValue.id}" <c:if test="${option.defaultValue!=null && option.defaultValue.id==optionValue.id}"> SELECTED</c:if>>${optionValue.name}</option>
													</c:forEach>
													</select>
												</c:when>
												<c:when test="${option.type=='Radio'}">
													<c:forEach items="${option.availableValues}" var="optionValue">
														<input type="radio" class="attribute" id="<c:out value="${optionValue.id}"/>" name="options[${status.index}].id"/>" value="<c:out value="${optionValue.id}"/>"<c:if test="${option.defaultValue!=null && option.defaultValue.id==optionValue.id}"> checked="checked" </c:if> />
														<c:out value="${optionValue.name}"/>
													</c:forEach>
												</c:when>
												<c:when test="${option.type=='Text'}">
													<input type="text" class="attribute" id="${option.id}" name="options[${status.index}].id" class="input-large">
												</c:when>
												<c:when test="${option.type=='Checkbox'}">
													<c:forEach items="${option.availableValues}" var="optionValue">
														<input type="checkbox" class="attribute" id="<c:out value="${optionValue.id}"/>" name="options[${status.index}].id" value="<c:out value="${optionValue.id}"/>"<c:if test="${option.defaultValue!=null && option.defaultValue.id==optionValue.id}"> checked="checked" </c:if>  />
														<c:out value="${optionValue.name}"/>
													</c:forEach>
												</c:when>										
											</c:choose>				       							
		                                 	<span class="help-inline"></span>
	                        				</div>
	                    			</div>
									</c:forEach>
								</c:if>
						
								<c:if test="${product.quantityOrderMaximum==-1 || product.quantityOrderMaximum>1}" >
								<label><s:message code="label.quantity" text="Quantity"/></label>
								<input quantity-productId-<c:out value="${product.id}" />" class="span1" placeholder="1" type="text">
								</c:if>
								<button class="btn" type="submit"><s:message code="button.label.addToCart" text="Add to cart"/></button>
							</form>
						</div>
						<div class="span5">
							<!-- facebook like, twitter, google + and pinterest -->
							<ul class="social">
								<li>									
									<div class="fb-like" send="false" layout="button_count" data-href="example.org"></div>
									<script src="product_detail_files/all.htm"></script>
								</li>
								<li>
									<iframe data-twttr-rendered="true" title="Twitter Tweet Button" style="width: 109px; height: 20px;" class="twitter-share-button twitter-count-horizontal" src="product_detail_files/tweet_button.htm" allowtransparency="true" frameborder="0" scrolling="no"></iframe>
									<script type="text/javascript" src="product_detail_files/widgets.js"></script>
								</li>
							</ul>
						</div>
					</div>
                    <div class="row">
                        <div class="span9">
							<ul class="nav nav-tabs" id="myTab">
								<li class="active"><a href="#description"><s:message code="label.productedit.productdesc" text="Product description" /></a></li>
								<li><a href="#reviews"><s:message code="label.product.customer.reviews" text="Customer reviews" /></a></li><!-- TODO read only attributes -->
							</ul>							 
							<div class="tab-content">
								<div class="tab-pane active" id="description">
									<c:out value="${product.description}"/>
									<br/>
									<br/>
									
									<!--  read only properties -->
									<c:if test="${attributes!=null}">
										<table>
										<c:forEach items="${attributes.values}" var="attribute">
										<tr>
	                        				<td><label><c:out value="${attribute.name}"/></label></td>
											<td><label><c:out value="${attribute.readOnlyValue.name}" /></label></td>
										</tr>
									</c:forEach>
									</table>
								  </c:if>
									
									
								</div>
								<div class="tab-pane" id="reviews">
									<table class="table table-striped shop_attributes">	
										<tbody>
											<tr class="">
												<th>Size</th>
												<td>Large, Medium, Small, X-Large</td>
											</tr>		
											<tr class="alt">
												<th>Colour</th>
												<td>Orange, Yellow</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>							
                        </div>	
                        
                        <!-- Related items -->
                        <c:if test="${relatedProducts!=null}">					
						<div class="span9">	
							<br>
							<h2 class="title"><s:message code="label.product.related.title" text="Related items"/></h2>
							<hr>
							<div id="relatedItems" class="carousel slide">
								<div class="carousel-inner">
									<div class="active item">
										<ul class="thumbnails listing-products">
											<!-- Iterate over featuredItems -->
											<c:set var="ITEMS" value="${relatedProducts}" scope="request" />
	                         				<jsp:include page="/pages/shop/templates/bootstrap/sections/productBox.jsp" />
										</ul>
									</div>

								</div>
							</div>
							<a class="carousel-control left" href="#myCarousel" data-slide="prev">Previous</a>
							<a class="carousel-control right" href="#myCarousel" data-slide="next">Next</a>
							</div>
						</div>
						</c:if>
						
						
                    </div>
                </div>
            </div>
            

		<script>
			$(function () {
				$('#myTab a:first').tab('show');
				$('#myTab a').click(function (e) {
					e.preventDefault();
					$(this).tab('show');
				})
			})
			$(document).ready(function() {
				$('.thumbnail').fancybox({
					openEffect  : 'none',
					closeEffect : 'none'
				});
				
				$('.carousel').carousel({
                    interval: false
                });
			});
		</script>
    
