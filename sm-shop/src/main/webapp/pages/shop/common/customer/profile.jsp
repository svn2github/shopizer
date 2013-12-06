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
							<p><font color="#FF8C00">Profile</font></p>
						</span>
						<div class="control-group"> 
							<label>Username</label>
    						<div class="controls"> 
      							Read only username
    						</div> 
  						</div>
  						<div class="control-group"> 
							<label>Password</label>
    						<div class="controls"> 
      							<input type="password" id="password" class="input-medium">
    						</div> 
  						</div>
  						 <div class="control-group"> 
							<label>New Password</label>
    						<div class="controls"> 
      							<input type="newpassword" id="newpassword" class="input-medium">
    						</div> 
  						</div>
  						<div class="control-group"> 
							<label>Repeat Password</label>
    						<div class="controls"> 
      							<input type="repeatpassword" id="repeatpassword" class="input-medium">
    						</div> 
  						</div>
					  </div>
				   </div>
			</div>
		</div>
		<!-- close row-fluid--> 
	</div>
	<!--close .container "main-content" -->