

			<jsp:include page="/pages/shop/templates/bootstrap/sections/breadcrumb.jsp" />
            
            
            <div class="row-fluid">


                
                
                <div class="span12">
                    <!--<div class="row-fluid">
                        <div class="span12">
                            <h2 class="title">+ Women</h2>
							<hr>
                        </div>
                    </div>
                    -->
                    <div class="row">
						<div class="span4">
							<c:if test="${product.image!=null}"/>
							<a href="<c:out value="${product.imageUrl}"/>" class="thumbnail" data-fancybox-group="group1" title="<c:out value="${product.name}"/>"><img alt="<c:out value="${product.name}"/>" src="<c:out value="${product.imageUrl}"/>"></a>												
							<c:if test="${product.images!=null}"/>
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
						<div class="span5">
							<address>
								<strong><s:message code="label.product.brand" text="Brand"/></strong> <span><c:out value="${product.manufacturer}" /></span><br>
								<strong><s:message code="label.product.code" text="Product code"/></strong> <span>${product.sku}</span><br>
								<strong><s:message code="label.product.available" text="Availability"/></strong> <span><c:choose><c:when test="${product.quantity>0}"/>${product.quantity}</c:when><c:otherwise></c:otherwise></c:choose>Out Of Stock</span><br>								
							</address>
							<h4>
									<c:choose>
										<c:when test="${product.discounted}">
												<del><c:out value="${product.originalProductPrice}" /></del>&nbsp;<span class="specialPrice"><c:out value="${product.productPrice}" /></span>
										</c:when>
										<c:otherwise>
												<c:out value="${product.productPrice}" />
										</c:otherwise>
									</c:choose>
							</h4>
						</div>
						<div class="span5">
							<form class="form-inline">								
								<c:if test="${product.quantityOrderMaximum==-1 || product.quantityOrderMaximum>1}" />
								<label><s:message code="label.quantity" text="Quantity"/></label>
								<input class="span1" placeholder="1" type="text">
								</c:if>
								<button class="btn" type="submit"><s:message code="button.label.addToCart" text="Add to cart"/></button>
							</form>
						</div>
						<div class="span5">
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
								<li class="active"><a href="#home">Description</a></li>
								<li><a href="#profile">Additional Information</a></li>
							</ul>							 
							<div class="tab-content">
								<div class="tab-pane active" id="home">Sed ut perspiciatis unde 
omnis iste natus error sit voluptatem accusantium doloremque laudantium,
 totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi
 architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam 
voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia 
consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. 
Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, 
consectetur, adipisci velit, sed quia non numquam eius modi tempora 
incidunt ut labore et dolore magnam aliquam quaerat voluptatem</div>
								<div class="tab-pane" id="profile">
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
						<div class="span9">	
							<br>
							<h2 class="title">Related Products</h2>
							<hr>
							<div id="myCarousel" class="carousel slide">
								<div class="carousel-inner">
									<div class="active item">
										<ul class="thumbnails listing-products">
											<li class="span3">
												<div class="product-box">
													<a href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html"><h4>Praesent tempor sem sodales</h4></a>
													<a href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html"><img alt="" src="product_detail_files/m3.jpg"></a>
													<p>Integer in ligula et erat gravida placerat</p>
													<div class="bottom">
														<a class="view" href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://wbpreview.com/previews/WB0M3G9S1/cart.html">add to cart</a>
													</div>
												</div>
											</li>
											<li class="span3">
												<div class="product-box">
													<a href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html"><h4>Luctus quam ultrices rutrum</h4></a>
													<a href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html"><img alt="" src="product_detail_files/m4.jpg"></a>
													<p>Suspendisse aliquet orci et nisl iaculis</p>
													<div class="bottom">
														<a class="view" href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://wbpreview.com/previews/WB0M3G9S1/cart.html">add to cart</a>
													</div>
												</div>
											</li>
											<li class="span3">
												<div class="product-box">
													<a href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html"><h4>Praesent tempor sem sodales</h4></a>
													<a href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html"><img alt="" src="product_detail_files/m5.jpg"></a>
													<p>Nam imperdiet urna nec lectus mollis</p>
													<div class="bottom">
														<a class="view" href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://wbpreview.com/previews/WB0M3G9S1/cart.html">add to cart</a>
													</div>
												</div>
											</li>
										</ul>
									</div>
									<div class="item">
										<ul class="thumbnails listing-products">
											<li class="span3">
												<div class="product-box">                                        
													<a href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html"><h4>Fusce id molestie massa</h4></a>
													<a href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html"><img alt="" src="product_detail_files/m2.jpg"></a>
													<p>Phasellus consequat sem congue diam congue</p>
													<div class="bottom">
														<a class="view" href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://wbpreview.com/previews/WB0M3G9S1/cart.html">add to cart</a>
													</div>
												</div>
											</li>       
											<li class="span3">
												<div class="product-box">
													<a href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html"><h4>Praesent tempor sem sodales</h4></a>
													<a href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html"><img alt="" src="product_detail_files/m3.jpg"></a>
													<p>Integer in ligula et erat gravida placerat</p>
													<div class="bottom">
														<a class="view" href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://wbpreview.com/previews/WB0M3G9S1/cart.html">add to cart</a>
													</div>
												</div>
											</li>
											<li class="span3">
												<div class="product-box">
													<a href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html"><h4>Luctus quam ultrices rutrum</h4></a>
													<a href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html"><img alt="" src="product_detail_files/m7.jpg"></a>
													<p>Suspendisse aliquet orci et nisl iaculis</p>
													<div class="bottom">
														<a class="view" href="http://wbpreview.com/previews/WB0M3G9S1/product_detail.html">view</a> / <a class="addcart" href="http://wbpreview.com/previews/WB0M3G9S1/cart.html">add to cart</a>
													</div>
												</div>
											</li>
										</ul>
									</div>
								</div>
								<a class="carousel-control left" href="#myCarousel" data-slide="prev">‹</a>
								<a class="carousel-control right" href="#myCarousel" data-slide="next">›</a>
							</div>
						</div>
                    </div>
                </div>
            </div>
            

        		
    
        <script src="product_detail_files/jquery-1.js"></script>		
		<script src="product_detail_files/bootstrap.js"></script>
		<script src="product_detail_files/jquery.js"></script>
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
    
