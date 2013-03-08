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

	  <!-- footer -->
            <footer>
                <div class="row">                   
                    <div class="span3">
						<div class="company">
							<h4>My Store</h4>
							<p>
								Shopizer<br>
								this street address<br>
								Tel: +444-888-9999
							</p>
						</div>
                    </div>
					 <div class="span3">   
						<h4>Information</h4>
                        <ul>
							<li><a href="http://#WB0M3G9S1/about.html">About Us</a></li>
							<li><a href="#">Delivery Information</a></li>
							<li><a href="#">Privacy Policy</a></li>
							<li><a href="#">Terms &amp; Conditions</a></li>
						</ul>
                    </div>
                    <div class="span3">
                        <h4>My Account</h4>
                        <ul>
							<li><a href="#">My Account</a></li>
							<li><a href="#">Order History</a></li>
							<li><a href="#">Wish List</a></li>
							<li><a href="#">Newsletter</a></li>
						</ul>
                    </div>
                    <div class="span3">
                        <h4>Connect with us</h4>
                        <a href="#">Facebook</a>
                        <br>
                        <a href="#">Twitter</a>
                    </div>				
                </div>
		    <div id="footer-bottom">
				<div class="container">
				   <div class="row">
					<div class="span12 text"> Copyright My Store </div>
				   </div>
				 </div>
		    </div>
            </footer>