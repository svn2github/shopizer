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

<script src="<c:url value="/resources/js/jquery.elevateZoom-3.0.8.min.js" />"></script>



			<jsp:include page="/pages/shop/templates/bootstrap/sections/breadcrumb.jsp" />
            
            <div class="row-fluid">

                <div class="span12" itemscope itemtype="http://data-vocabulary.org/Product">
                    <!--<div class="row">-->
                    	<!-- Image column -->
						<div id="img" class="span4">
							<c:if test="${product.image!=null}">
							<span id="mainImg" class=""><img id="im-<c:out value="${product.image.id}"/>" alt="<c:out value="${product.description.name}"/>" src="<c:url value="${product.image.imageUrl}"/>" data-zoom-image="<sm:shopProductImage imageName="${product.image.imageName}" sku="${product.sku}" size="LARGE"/>"></span>												
							<script>
								$(function() {
									setImageZoom('im-<c:out value="${product.image.id}"/>');
								});	
							</script>
							<c:if test="${product.images!=null && fn:length(product.images)>1}">
								<ul id="imageGallery" class="thumbnails small">
									<c:forEach items="${product.images}" var="thumbnail">								
									<li class="span2">
										<a href="#img" class="thumbImg" title="<c:out value="${thumbnail.imageName}"/>"><img id="im-<c:out value="${thumbnail.id}"/>" src="<c:url value="${thumbnail.imageUrl}"/>" data-zoom-image="<sm:shopProductImage imageName="${thumbnail.imageName}" sku="${product.sku}" size="LARGE"/>" alt="<c:url value="${thumbnail.imageName}"/>" ></a>
									</li>
									</c:forEach>								
								</ul>
							</c:if>
							</c:if>
						</div>
						<!-- Google rich snippets (http://blog.hubspot.com/power-google-rich-snippets-ecommerce-seo-ht) -->
						<!-- Product description column -->
						<div class="span8">
							<h1>${product.description.name}</h1>
							<div class="review">
								<div>
									<img alt="1 reviews" src="catalog/view/theme/pav_foodstore/image/stars-1.png">
									<a onclick="$('a[href=\'#tab-review\']').trigger('click');">1 reviews</a>
  										|  
									<a onclick="$('a[href=\'#tab-review\']').trigger('click');">Write a review</a>
								</div>
							</div>
							<address>
								<strong><s:message code="label.product.brand" text="Brand"/></strong> <span itemprop="brand"><c:out value="${product.manufacturer.description.name}" /></span><br>
								<strong><s:message code="label.product.code" text="Product code"/></strong> <span itemprop="identifier" content="mpn:${product.sku}">${product.sku}</span><br>								
							</address>
							<span itemprop="offerDetails" itemscope itemtype="http://data-vocabulary.org/Offer">
							<meta itemprop="seller" content="${requestScope.MERCHANT_STORE.storename}"/>
							<meta itemprop="currency" content="<c:out value="${requestScope.MERCHANT_STORE.currency.code}" />" />
							<h3>
									<c:choose>
										<c:when test="${product.discounted}">
												<del><c:out value="${product.originalPrice}" /></del>&nbsp;<span class="specialPrice"><span itemprop="price"><c:out value="${product.finalPrice}" /></span></span>
										</c:when>
										<c:otherwise>
												<span itemprop="price"><c:out value="${product.finalPrice}" /></span>
										</c:otherwise>
									</c:choose>
							</h3>
							<address>
								<strong><s:message code="label.product.available" text="Availability"/></strong> <span><c:choose><c:when test="${product.quantity>0}"><span itemprop="availability" content="in_stock">${product.quantity}</span></c:when><c:otherwise><span itemprop="availability" content="out_of_stock"><s:message code="label.product.outofstock" text="Out of stock" /></c:otherwise></c:choose></span><br>								
							</address>
							</span>

							
							<p>
								<form id="input-<c:out value="${product.id}" />">
								<!-- select options -->
								<c:if test="${options!=null}">
									<c:forEach items="${options}" var="option" varStatus="status">
										<div class="control-group"> 
	                        				<label><strong><c:out value="${option.name}"/></strong></label>
	                        				<div class="controls">	       							
											<c:choose>
												<c:when test="${option.type=='select'}">
													<select id="${status.index}" name="${status.index}" class="attribute">
													<c:forEach items="${option.values}" var="optionValue">
														<option value="${optionValue.id}" <c:if test="${optionValue.defaultAttribute==true}"> SELECTED</c:if>>${optionValue.name}<c:if test="${optionValue.price!=null}">&nbsp;<c:out value="${optionValue.price}"/></c:if></option>
													</c:forEach>
													</select>
												</c:when>
												<c:when test="${option.type=='radio'}">
													<c:forEach items="${option.values}" var="optionValue">
														<input type="radio" class="attribute" id="${status.index}" name="${status.index}" value="<c:out value="${optionValue.id}"/>" <c:if test="${optionValue.defaultAttribute==true}"> checked="checked" </c:if> />
														<c:out value="${optionValue.name}"/><c:if test="${optionValue.price!=null}">&nbsp;<c:out value="${optionValue.price}"/></c:if><br/>
													</c:forEach>
												</c:when>
												<c:when test="${option.type=='text'}">
													<input type="text" class="attribute" id="${status.index}" name="${status.index}" class="input-large">
												</c:when>
												<c:when test="${option.type=='checkbox'}">
													<c:forEach items="${option.values}" var="optionValue">
														<input type="checkbox" class="attribute" id="<c:out value="${optionValue.id}"/>" name="${status.index}" value="<c:out value="${optionValue.id}"/>"<c:if test="${optionValue.defaultAttribute==true}"> checked="checked" </c:if>  />
														<c:out value="${optionValue.name}"/><c:if test="${optionValue.price!=null}">&nbsp;<c:out value="${optionValue.price}"/></c:if><br/>
													</c:forEach>
												</c:when>										
											</c:choose>				       							
		                                 	<span class="help-inline"></span>
	                        				</div>
	                    			</div>
									</c:forEach>
								</c:if>
								<br/>
								<div class="form-inline">
								<c:if test="${product.quantityOrderMaximum==-1 || product.quantityOrderMaximum>1}" >
									<input quantity-productId-<c:out value="${product.id}" />" class="input-mini" placeholder="1" type="text">
								</c:if>
									<button class="btn btn-success addToCart" type="button" productId="<c:out value="${product.id}" />"><s:message code="button.label.addToCart" text="Add to cart"/></button>
								</div>
							

							</form>
							

							
	
							
							</p>
							
							
							
						</div>

					</div>
			 </div>
			 <div class="row-fluid">
                    <div class="span12">

							<ul class="nav nav-tabs" id="myTab">
								<li class="active"><a href="#description"><s:message code="label.productedit.productdesc" text="Product description" /></a></li>
								<!--<li><a href="#reviews"><s:message code="label.product.customer.reviews" text="Customer reviews" /></a></li><!-- TODO read only attributes -->
							</ul>							 
							<div class="tab-content">
								<div class="tab-pane active" id="description">
									<c:out value="${product.description.description}" escapeXml="false"/>
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
                        </div>	
                        
                        <!-- Related items -->
                        <c:if test="${relatedProducts!=null}">	
                        			<h1><s:message code="label.product.related.title" text="Related items"/></h1>				
									<ul class="thumbnails product-list">
										<!-- Iterate over featuredItems -->
                         				<c:set var="ITEMS" value="${relatedProducts}" scope="request" />
	                         			<jsp:include page="/pages/shop/templates/bootstrap/sections/productBox.jsp" />
									</ul>
						</c:if>
						
						
                    </div>
                </div>

            

		<script>
			$(function () {
				$('#myTab a:first').tab('show');
				$('#myTab a').click(function (e) {
					e.preventDefault();
					$(this).tab('show');
				})
				$('.thumbImg').click(function (e) {
					img = $(this).find('img').clone();
					$('#mainImg').html(img);
					setImageZoom(img.attr('id'));
				})
			})

			function setImageZoom(id) {
			    $('#' + id).elevateZoom({
		    			zoomType	: "lens",
		    			lensShape : "square",
		    			lensSize    : 240
		   		}); 
			}
			
			
		</script>
    
