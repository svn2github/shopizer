<%@include file="../include.jsp" %>

<%@include file="menu.jsp" %>


<portlet:renderURL var="homeURL">
	<portlet:param name="action" value="homePage" />
</portlet:renderURL>



<div class="sm">

 

      <div class="container">

 

            <div class="row">

 

                  <div class="span3">

 

 

                     <ul class="nav nav-list">

                                <li>

                                     <a href="#">

                                             <i class="icon-home"></i>

                                             Home

                                     </a>

                             </li>

                             <li>

                                     <a href="#">

                                             <i class="icon-file"></i>

                                             Store

                                     </a>

                             </li>

                             <li class="active">

                                     <a href="#">

                                             <i class="icon-tags"></i>

                                             Catalogue

                                     </a>

                             </li>

                             <li>

                                     <a href="#">

                                             <i class="icon-user"></i>

                                             Customer

                                     </a>

                             </li>

                             <li>

                                     <a href="#">

                                             <i class="icon-shopping-cart"></i>

                                             Order

                                     </a>

                             </li>

                             <li>

                                     <a href="#">

                                             <i class="icon-cog"></i>

                                             Configuration

                                     </a>

                             </li>

                     </ul>

 

 

 

                  </div>

 

                  <div class="span9">

 

 

                     <div class="tabbable">

 

                             <ul class="nav nav-tabs">

 

                                     <li class="active"><a href="#products" data-toggle="tab">Products</a></li>

 

                                     <li><a href="#categories" data-toggle="tab">Categories</a></li>

 

                                     <li><a href="#options" data-toggle="tab">Options</a></li>

 

                             </ul>

 

                             <div class="tab-content">

 

                                     <div class="tab-pane active" id="products">

 

                                             <p>I'm in Section 1.</p>

 

                                     </div>

 

                                     <div class="tab-pane" id="categories">

 

                                             <p>Howdy, I'm in Section 2.</p>

 

                                     </div>

 

                                     <div class="tab-pane" id="options">

 

                                             <p>Howdy, I'm in Section 3.</p>

 

                                     </div>

 

                             </div>

 

                     </div>

 

 

 

                  </div>

 

 

            </div>


  

      </div> <!-- /container -->

  

</div>