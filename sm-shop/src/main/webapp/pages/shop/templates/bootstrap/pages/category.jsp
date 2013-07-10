
	<div class="row-fluid">
      	
      	<!-- left column -->
        <div class="span3">
        	//TEMPORARY category place holder
          <div class="well sidebar-nav">
            <ul class="nav nav-list">
              <li class="nav-header">Sidebar</li>
              <li class="active"><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li class="nav-header">Sidebar</li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li class="nav-header">Sidebar</li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
            </ul>
          </div><!--/.well -->
        </div><!--/span-->
        
        <!-- right column -->
        <div class="span9">
         <!--
          <div class="hero-unit">
            <h1>Hello, world!</h1>
            <p>This is a template for a simple marketing or informational website. It includes a large callout called the hero unit and three supporting pieces of content. Use it as a starting point to create something more unique.</p>
            <p><a class="btn btn-primary btn-large">Learn more &raquo;</a></p>
          </div>
          -->
          
          <ul id="productList" class="thumbnails product-list">
          		<!-- TODO ajax lookup -->
	          	<li class="span3">
					<div class="product-box">                                        
						<a href="product.html?product=${seoName}"><h4>${name}</h4></a>
						<a href="product.html?product=${seoName}"><img alt="" src="img/products/${image}"></a>
						<p><h3>${formatedPrice}</h3></p>
						<div class="bottom">
							<a class="view" href="product.html?product=${seoName}">view</a> / 
							<a class="addcart addToCart" href="#" productId="${id}">add to cart</a>
						</div>
					</div>
				</li> 

	          	<li class="span3">
					<div class="product-box">                                        
						<a href="product.html?product=${seoName}"><h4>${name}</h4></a>
						<a href="product.html?product=${seoName}"><img alt="" src="img/products/${image}"></a>
						<p><h3>${formatedPrice}</h3></p>
						<div class="bottom">
							<a class="view" href="product.html?product=${seoName}">view</a> / 
							<a class="addcart addToCart" href="#" productId="${id}">add to cart</a>
						</div>
					</div>
				</li> 

	          	<li class="span3">
					<div class="product-box">                                        
						<a href="product.html?product=${seoName}"><h4>${name}</h4></a>
						<a href="product.html?product=${seoName}"><img alt="" src="img/products/${image}"></a>
						<p><h3>${formatedPrice}</h3></p>
						<div class="bottom">
							<a class="view" href="product.html?product=${seoName}">view</a> / 
							<a class="addcart addToCart" href="#" productId="${id}">add to cart</a>
						</div>
					</div>
				</li> 
          </ul>
          
          
        </div><!--/span-->
      </div><!-- row fluid -->