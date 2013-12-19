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
			<div class="span7">
				<form method="post" action="{register url}" id="registration-form" class="form-horizontal">
					<fieldset>
						<div class="control-group">
							<label class="required control-label" for="FirstNameRegister">First Name <span class="required">*</span></label>
							<div class="controls">
								<input type="text" class="span12" id="FirstNameRegister" name="FirstNameRegister">
							</div>
						</div>
						<div class="control-group">
							<label class="required control-label" for="LastNameRegister">Last Name <span class="required">*</span></label>
							<div class="controls">
								<input type="text" class="span12" id="LastNameRegister" name="LastNameRegister">
							</div>
						</div>

						<div class="control-group">
							<label class="required control-label" for="sex">Sex <span class="required">*</span></label>
							<div class="controls">
								<select name="sex">
									<option value="M" selected="">M</option>
									<option value="F">F</option>
								</select>
							</div>
						</div>

						<div class="control-group">
							<label class="required control-label" for="address">Address <span class="required">*</span></label>
							<div class="controls">
								<input type="text" class="span12" id="address" name="address">
							</div>
						</div>
						
						<div class="control-group">
							<label class="required control-label" for="city">City <span class="required">*</span></label>
							<div class="controls">
								<input type="text" class="span12" id="city" name="city">
							</div>
						</div>
						<!-- 
							 LOOK IN ADMIN - CUSTOMER ON HOW TO SWITCH ZONES BASED ON COUNTRY
							 AND FOR PROVINCES NOT DEFINED IN THE SYSTEM
						 -->
						<div class="control-group">
							<label class="control-label required">Select Your Country <span class="required">*</span></label>
							<div class="controls">
								<select name="country">
									<option value="US" selected="">United States</option>
									<option value="AF">Afghanistan</option>
									<option value="AL">Albania</option>
									<option value="DZ">Algeria</option>
									<option value="AS">American Samoa</option>
									<option value="AD">Andorra</option>
									<option value="AO">Angola</option>
									<option value="AI">...</option>
								</select>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label required">Select Province <span class="required">*</span></label>
							<div class="controls">
								<select name="province">
									<option value="AL">Alabama</option>
									<option value="AK">Arkansas</option>
									<option value="AI">...</option>
								</select>
							</div>
						</div>
						<div class="control-group">
							<label class="required control-label" for="email">Email <span class="required">*</span></label>
							<div class="controls">
								<input type="email" class="span12" id="email" name="email">
							</div>
						</div>
						<div class="control-group">
							<label class="required control-label" for="password">Password <span class="required">*</span></label>
							<div class="controls">
								<input type="password" class="span12" id="password" name="password">
							</div>
						</div>
						<div class="control-group">
							<label class="required control-label" for="passwordAgain">Password again <span class="required">*</span></label>
							<div class="controls">
								<input type="password" class="span12" id="passwordAgain" name="passwordAgain">
							</div>
						</div>
						<div class="control-group">
							<div class="controls"><!--watch the white space in IOS!-->
								<label for="registerNewsletter" class="checkbox inline"><input type="checkbox" value="1" id="registerNewsletter" name="registerNewsletter"> Sign up for our newsletter</label>
							</div>
						</div>

						<div class="control-group">
							<div class="controls"><!--watch the white space in IOS!-->
								captcha							
							</div>
						</div>

						<div class="form-actions">
							<input type="submit" value="Register" name="register" class="btn btn-primary btn-large">
						</div>
					</fieldset>
				</form>
				<!-- end registration form--> 
				
			</div>
			<!--close .span7-->
			
			<div id="why-join" class="span4 offset1">
				<h3 class="short_headline"><span><s:message code="label.register.whyregister" text="Why register?" /></span></h3>
				<p>
					<s:message code="label.register.registerreasons" text="Simplify a checkout process by having your information pre-filed, re-order an item from one click and get access to premium information." />
				</p>
			</div>
			<!--close span4 offset1--> 
		</div>
		<!-- close row-fluid--> 
	</div>
	<!--close .container "main-content" -->