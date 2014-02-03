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

<c:set var="register_url" value="${pageContext.request.contextPath}/shop/customer/register.html"/>

	<div id="main-content" class="container clearfix">
		<div class="row-fluid">
			<div class="span7">
				<form:form method="post" action="${register_url}" id="registration-form" class="form-horizontal" commandName="persistableCustomer">
				<%-- <form:errors path="*" cssClass="errorblock"/> --%>
					<fieldset>
						<div class="control-group">
							<label class="required control-label" for="FirstNameRegister">First Name <span class="required">*</span></label>
							<div class="controls">
							   <form:input path="firstName" cssClass="span12" id="firstName"/>
							   <form:errors path="firstName" cssClass="error" />
								
							</div>
						</div>
						<div class="control-group">
							<label class="required control-label" for="LastNameRegister">Last Name <span class="required">*</span></label>
							<div class="controls">
							    <form:input path="lastName" cssClass="span12" id="lastName"/>
							    <form:errors path="lastName" cssClass="error" />
								
							</div>
						</div>

						<div class="control-group">
							<label class="required control-label" for="sex">Sex <span class="required">*</span></label>
							<div class="controls">
							 <form:select path="gender">
							    <form:option value="M">Male</form:option>
							     <form:option value="F">Female</form:option>
							 </form:select>
								<form:errors path="gender" cssClass="error" />
							</div>
						</div>

						<%-- <div class="control-group">
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
						</div>  --%>
						<!-- 
							 LOOK IN ADMIN - CUSTOMER ON HOW TO SWITCH ZONES BASED ON COUNTRY
							 AND FOR PROVINCES NOT DEFINED IN THE SYSTEM
						 -->
						<div class="control-group">
							<label class="control-label required">Select Your Country <span class="required">*</span></label>
							<div class="controls">
							<form:select path="country" id="registration_country">
							  <form:options items="${countryList}" itemValue="isoCode" itemLabel="name"/>
							</form:select>
								<%-- <select name="country">
									<option value="US" selected="">United States</option>
									<option value="AF">Afghanistan</option>
									<option value="AL">Albania</option>
									<option value="DZ">Algeria</option>
									<option value="AS">American Samoa</option>
									<option value="AD">Andorra</option>
									<option value="AO">Angola</option>
									<option value="AI">...</option>
								</select> --%>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label required">Select Province <span class="required">*</span></label>
							<div class="controls">
							<form:select path="province" id="registration_zones">
							
							</form:select>
								<%-- <select name="province">
									<option value="AL">Alabama</option>
									<option value="AK">Arkansas</option>
									<option value="AI">...</option>
								</select> --%>
								<form:input path="province" id="hidden_registration_zones"/>
							</div>
						</div>
						<div class="control-group">
							<label class="required control-label" for="username">User Name <span class="required">*</span></label>
							<div class="controls">
								 <form:input path="userName" cssClass="span12" id="userName"/>
								<form:errors path="userName" cssClass="error" />
							</div>
						</div>
						
						<div class="control-group">
							<label class="required control-label" for="email">Email <span class="required">*</span></label>
							<div class="controls">
							     <form:input path="emailAddress" cssClass="span12" id="email"/>
							     <form:errors path="emailAddress" cssClass="error" />
								
							</div>
						</div>
						<div class="control-group">
							<label class="required control-label" for="password">Password <span class="required">*</span></label>
							<div class="controls">
							    <form:password path="pwd" class="span12" id="password"/>
								<form:errors path="pwd" cssClass="error" />
							</div>
						</div>
						<div class="control-group">
							<label class="required control-label" for="passwordAgain">Password again <span class="required">*</span></label>
							<div class="controls">
							     <form:password path="checkPwd" class="span12" id="passwordAgain"/>
								<form:errors path="checkPwd" cssClass="error" />
							</div>
						</div>
						<div class="control-group">
							<div class="controls"><!--watch the white space in IOS!-->
								<label for="registerNewsletter" class="checkbox inline"><input type="checkbox" value="1" id="registerNewsletter" name="registerNewsletter"> Sign up for our newsletter</label>
							</div>
						</div>

						<div class="control-group">
							<div class="controls"><!--watch the white space in IOS!-->
								<script type="text/javascript"
    								 src="http://www.google.com/recaptcha/api/challenge?k=6Lc1Pe0SAAAAADQDlWbv3MYYj7lGEeCEanwC42bv">
  								</script>
							<noscript>
								<iframe
									src="http://www.google.com/recaptcha/api/noscript?k=6Lc1Pe0SAAAAADQDlWbv3MYYj7lGEeCEanwC42bv"
									height="300" width="500" frameborder="0"></iframe>
								<br>
								<form:textarea path="recaptcha_challenge_field" readonly="3" cols="40"/>
								<form:errors path="recaptcha_challenge_field" cssClass="error" />
								<!-- <textarea name="recaptcha_challenge_field" rows="3" cols="40">
     							</textarea> -->
     							
								<input type="hidden" name="recaptcha_response_field"
									value="manual_challenge">
							</noscript>
						</div>
						</div>

						<div class="form-actions">
							<input type="submit" value="Register" name="register" class="btn btn-primary btn-large">
						</div>
					</fieldset>
				</form:form>
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