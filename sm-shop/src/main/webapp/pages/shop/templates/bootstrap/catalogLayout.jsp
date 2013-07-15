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
 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  
 <%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  
 <c:set var="lang" scope="request" value="${requestScope.locale.language}"/> 
 
 
 <html xmlns="http://www.w3.org/1999/xhtml"> 
 
 
     <head>
        	 	<meta charset="utf-8">
    			<title>TITLE - <c:out value="${requestScope.PAGE_INFORMATION.pageTitle}" /> -</title>
    			<meta name="viewport" content="width=device-width, initial-scale=1.0">
    			<meta name="description" content="<c:out value="${requestScope.PAGE_INFORMATION.pageDescription}" />">
    			<meta name="author" content="<c:out value="${requestScope.MERCHANT_STORE.storename}"/>">
    			<!-- The one provided with the system -->
    			<script src="<c:url value="/resources/js/bootstrap/jquery.js" />"></script>
    			<script src="<c:url value="/resources/js/functions.js" />"></script>
    			<script src="<c:url value="/resources/js/jquery-cookie.js" />"></script>

  
                <jsp:include page="/pages/shop/templates/bootstrap/sections/shopLinks.jsp" />
 	</head>
 
 	<body>
 	
<div class="container">



				<tiles:insertAttribute name="header"/>


				<tiles:insertAttribute name="navbar"/>
				

				<tiles:insertAttribute name="body"/>
				
				
				<tiles:insertAttribute name="footer"/>
				
				<!-- 

			


			
			<div class="row-fluid">
				<div class="span12"><span id="homeText">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</span></div>
			</div>


			<br>
			<div class="row-fluid">
				<div class="span12">
					<br><h1 class="lbw">Featured items</h1><br>

									<ul class="thumbnails product-list" id="productList"><li class="span3">    <div class="product-box">                                             <a href="product.html?product=short-sleeves-surfer"><h4>Short sleeves black</h4></a>     <a href="product.html?product=short-sleeves-surfer"><img src="img/products/shirt4.jpg" alt=""></a>     <p></p><h3>$29.99</h3><p></p>     <div class="bottom">      <a href="product.html?product=short-sleeves-surfer" class="view">view</a> /       <a productid="40" href="#" class="addcart addToCart">add to cart</a>     </div>    </div>   </li><li class="span3">    <div class="product-box">                                             <a href="product.html?product=short-sleeves-blue"><h4>Short sleeves blue</h4></a>     <a href="product.html?product=short-sleeves-blue"><img src="img/products/shirt2.jpg" alt=""></a>     <p></p><h3>$29.99</h3><p></p>     <div class="bottom">      <a href="product.html?product=short-sleeves-blue" class="view">view</a> /       <a productid="41" href="#" class="addcart addToCart">add to cart</a>     </div>    </div>   </li><li class="span3">    <div class="product-box">                                             <a href="product.html?product=short-sleeves-white"><h4>Short sleeves white</h4></a>     <a href="product.html?product=short-sleeves-white"><img src="img/products/shirt1.jpg" alt=""></a>     <p></p><h3>$29.99</h3><p></p>     <div class="bottom">      <a href="product.html?product=short-sleeves-white" class="view">view</a> /       <a productid="42" href="#" class="addcart addToCart">add to cart</a>     </div>    </div>   </li><li class="span3">    <div class="product-box">                                             <a href="product.html?product=short-sleeves-43"><h4>Short sleeves orange</h4></a>     <a href="product.html?product=short-sleeves-43"><img src="img/products/shirt7.jpg" alt=""></a>     <p></p><h3>$29.99</h3><p></p>     <div class="bottom">      <a href="product.html?product=short-sleeves-43" class="view">view</a> /       <a productid="43" href="#" class="addcart addToCart">add to cart</a>     </div>    </div>   </li></ul>
				</div>
			</div>
            -->
            
            <!--
            <footer>
                <div class="row">                   
                    <div class="span6">
						<div class="company" id="company">
						<h4>Surf T Shop</h4>     <p>           358 Du Languedoc Boucherville<br>               </p></div>
                    </div>


                    <div id="socialLinks" class="span6">

                    <img width="26" src="img/misc/facebook.png">&nbsp;<a href="http://www.facebook.com/Cocoacart">Follow us on Facebook</a><br><img width="26" src="img/misc/twitter.png">&nbsp;<a href="http://www.twitter.com/Cocoacart">Follow us on Twitter</a><br></div>				
                </div>
		        <div id="footer-bottom"><div class="container">        <div class="row">      <div class="span12 text">Copyright 2013 Surf T Shop &nbsp;&nbsp;&nbsp; - &nbsp;&nbsp;&nbsp; Powered by <a href="www.cocoacart.com">Cocoacart</a></div>        </div>      </div></div>
            </footer>
			-->

</div><!-- container -->
	   <jsp:include page="/pages/shop/templates/bootstrap/sections/jsLinks.jsp" />

 	</body>
 
 </html>
 
