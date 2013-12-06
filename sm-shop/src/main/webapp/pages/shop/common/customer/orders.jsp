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


<div class="span12">
				   <div class="span8">

<div class="box">
						<span class="box-title">
							<p><font color="#FF8C00">Billing information</font></p>
						</span>

						
			<div class="row-fluid">

				<div class="span4">
  				   <div class="control-group"> 
					<label>First</label>
    					<div class="controls"> 
      					<input type="text" id="first" class="input-medium"> 
    					</div> 
  				   </div> 
				</div>


				<div class="span4">
  				   <div class="control-group"> 
					<label>Last</label>
    					<div class="controls"> 
      					<input type="text" id="last" class="input-medium"> 
    					</div> 
  				   </div> 
				</div>

			</div>



			<div class="row-fluid">

				<div class="span4">
  				   <div class="control-group"> 
					<label>Email</label>
    					<div class="controls"> 
      					<input type="text" id="email" class="input-medium"> 
    					</div> 
  				   </div> 
				</div>


				<div class="span4">
  				   <div class="control-group"> 
					<label>Company</label>
    					<div class="controls"> 
      					<input type="text" id="company" class="input-medium"> 
    					</div> 
  				   </div> 
				</div>

			</div>


			<div class="row-fluid">



  			<div class="control-group"> 
				<label>Address</label>
    				<div class="controls"> 
      					<input type="text" id="address1" class="input-xlarge">
    				</div> 
  			</div> 

			</div>

			<div class="row-fluid">


			<div class="span4">
  			<div class="control-group"> 
				<label>City</label>
    				<div class="controls"> 
      					<input type="text" id="city" class="input-medium">
    				</div> 
  			</div>
			</div>



			<div class="span4">
  			<div class="control-group"> 
				<label>State / Province</label>
    				<div class="controls"> 
      					<input type="text" id="state" class="input-medium">
    				</div> 
  			</div>
			</div>


			<div class="span4">
  			<div class="control-group"> 
				<label>Postal code</label>
    				<div class="controls"> 
      					<input type="text" id="postalcode" class="input-small">
    				</div> 
  			</div>
			</div>

			</div>
			<div class="row-fluid">

  			<div class="control-group"> 
				<label>Country</label>
    				<div class="controls"> 
      					<input type="text" id="country" class="input-large">
    				</div> 
  			</div>

  			<div class="control-group"> 
				<label>Phone</label>
    				<div class="controls"> 
      					<input type="text" id="phone" class="input-large">
    				</div> 
  			</div>

			<!-- if clicked open a similar section with shipping (see checkout/checkout.jsp) -->
			<label class="checkbox"> <input type="checkbox" id="useAddress"> Ship to this address</label>
			</div>


			


					</div>


				  </div>


				</div>





		</div>
		<!-- close row-fluid--> 
	</div>
	<!--close .container "main-content" -->