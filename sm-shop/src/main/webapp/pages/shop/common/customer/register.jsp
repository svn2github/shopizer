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

<script src="<c:url value="/resources/js/address.js" />"></script>

<script type="text/javascript">

var RecaptchaOptions = {
	    theme : 'clean'
};

$(document).ready(function() {
	
	getZones($('#registration_country').val(),'<c:out value="${customer.billing.zone}" />');
	$("#hidden_registration_zones").hide();
	$("#registration_country").change(function() {
			getZones($(this).val(),'<c:out value="${customer.billing.zone}" />');
	})
	
	
	isFormValid();
	$("input[type='text']").on("change keyup paste", function(){
		isFormValid();
	});
	
	$("input[type='password']").on("change keyup paste", function(){
		isFormValid();
	});

	$("#registration_country").change(function() {
		isFormValid();	
	})
	
});


 


 function isFormValid() {
		$('#registrationError').hide();//reset error message
		var $inputs = $('#registrationForm').find(':input');
		var valid = true;
		var firstErrorMessage = null;
		$inputs.each(function() {
			if($(this).hasClass('required')) {				
				var fieldValid = isFieldValid($(this));
				if(!fieldValid) {
					if(firstErrorMessage==null) {
						if($(this).attr('title')) {
							firstErrorMessage = $(this).attr('title');
						}
					}
					valid = false;
				}
			}
			//if has class email
			if($(this).hasClass('email')) {	
				var emailValid = validateEmail($(this).val());
				//console.log('Email is valid ? ' + emailValid);
				if(!emailValid) {
					if(firstErrorMessage==null) {
						firstErrorMessage = '<s:message code="messages.invalid.email" text="Invalid email address"/>';
						valid = false;
					}
				}
			}
			
			//user name
			if($(this).hasClass('userName')) {	
				if($(this).val().length<6) {
					if(firstErrorMessage==null) {
						firstErrorMessage = '<s:message code="registration.username.length.invalid" text="User name must be at least 6 characters long"/>';
						valid = false;
					}
				}
			}
			
			//password rules
			if($(this).hasClass('password')) {	
				if($(this).val().length<6) {
					if(firstErrorMessage==null) {
						firstErrorMessage = '<s:message code="message.password.length" text="Password must be at least 6 characters long"/>';
						valid = false;
					}
				}
			}
			
			//repeat password
			if($(this).hasClass('checkPassword')) {	
					var pass = $('.password').val();
					if(($(this).val()!=pass)) {
						if(firstErrorMessage==null) {
							firstErrorMessage = '<s:message code="message.password.checkpassword.identical" text="Both password must match"/>';
							valid = false;
						}
					}
			}
		});
		
		//console.log('Form is valid ? ' + valid);
		if(valid==false) {//disable submit button
			$('#submitRegistration').addClass('btn-disabled');
			$('#submitRegistration').prop('disabled', true);
			$('#registrationError').html(firstErrorMessage);
			$('#registrationError').show();
		} else {
			$('#submitRegistration').removeClass('btn-disabled');
			$('#submitRegistration').prop('disabled', false);
			$('#registrationError').hide();
		}
 }
 
 
 function isFieldValid(field) {
		var validateField = true;
		var fieldId = field.prop('id');
		var value = field.val();
		if (fieldId.indexOf("hidden_registration_zones") >= 0) {
			console.log(field.is(":hidden"));
			if(field.is(":hidden")) {
				return true;
			}
		}
		if(!emptyString(value)) {
			field.css('background-color', '#FFF');
			return true;
		} else {
			field.css('background-color', '#FFC');
			return false;
		} 
 }
 
 
 </script>

<div id="registrationError"  class="alert alert-warning" style="display:none;">

</div>

<c:set var="register_url" value="${pageContext.request.contextPath}/shop/customer/register.html"/>

	<div id="main-content" class="container clearfix">
		<div class="row-fluid">
			<div class="span7">
				<form:form method="post" action="${register_url}" id="registrationForm" class="form-horizontal" commandName="customer">
					<form:errors path="*" cssClass="alert alert-error" element="div" />
					<fieldset>
						<div class="control-group">
							<label class="required control-label" for="FirstNameRegister"><s:message code="label.generic.firstname" text="First Name"/></label>
							<div class="controls">
							   <s:message code="NotEmpty.customer.firstName" text="First name is required" var="msgFirstName"/>
							   <form:input path="billing.firstName" cssClass="span8 required" id="firstName" title="${msgFirstName}"/>
							   <form:errors path="billing.firstName" cssClass="error" />
								
							</div>
						</div>
						<div class="control-group">
							<label class="required control-label" for="LastNameRegister"><s:message code="label.generic.lastname" text="Last Name"/></label>
							<div class="controls">
							    <s:message code="NotEmpty.customer.lastName" text="Last name is required" var="msgLastName"/>
							    <form:input path="billing.lastName" cssClass="span8 required" id="lastName" title="${msgLastName}"/>
							    <form:errors path="billing.lastName" cssClass="error" />
								
							</div>
						</div>

						<div class="control-group">
							<label class="required control-label" for="sex"><s:message code="label.generic.genre" text="Genre"/></label>
							<div class="controls">
							 <form:select path="gender">
							    <form:option value="M"><s:message code="label.generic.male" text="Male"/></form:option>
							     <form:option value="F"><s:message code="label.generic.female" text="Female"/></form:option>
							 </form:select>
								<form:errors path="gender" cssClass="error" />
							</div>
						</div>

						<div class="control-group">
							<label class="control-label required"><s:message code="label.generic.country" text="Country"/></label>
							<div class="controls">
							<form:select path="billing.country" id="registration_country">
							  <form:options items="${countryList}" itemValue="isoCode" itemLabel="name"/>
							</form:select>
							</div>
						</div>
						
						
		
						<div class="control-group">
							<label class="control-label required"><s:message code="label.generic.stateprovince" text="State / Province"/></label>
							<div class="controls">
							<s:message code="NotEmpty.customer.billing.stateProvince" text="State / Province is required" var="msgStateProvince"/>
							<form:select path="billing.zone" id="registration_zones" >
							</form:select>
							<form:input path="billing.stateProvince" cssClass="span8 required" id="hidden_registration_zones" title="${msgStateProvince}"/>
							</div>
						</div>		
						
						
						<div class="control-group">
							<label class="required control-label" for="username"><s:message code="label.generic.username" text="User name" /></label>
							<div class="controls">
								<s:message code="NotEmpty.customer.userName" text="User name is required" var="msgUserName"/>
								<form:input path="userName" cssClass="span8 required userName" id="userName" title="${msgUserName}"/>
								<form:errors path="userName" cssClass="error" />
							</div>
						</div>
						
						
						
						<div class="control-group">
							<label class="required control-label" for="email"><s:message code="label.generic.email" text="Email address"/></label>
							<div class="controls">
							     <s:message code="NotEmpty.customer.emailAddress" text="Email address is required" var="msgEmail"/>
							     <form:input path="emailAddress" cssClass="span8 required email" id="email" title="${msgEmail}"/>
							     <form:errors path="emailAddress" cssClass="error" />
								
							</div>
						</div>
						
						<div class="control-group">
							<label class="required control-label" for="password"><s:message code="label.generic.password" text="Password"/></label>
							<div class="controls">
							    <s:message code="message.password.required" text="Password is required" var="msgPassword"/>
							    <form:password path="password" class="span8 required password" id="password" title="${msgPassword}"/>
								<form:errors path="password" cssClass="error" />
							</div>
						</div>
						
						<div class="control-group">
							<label class="required control-label" for="passwordAgain"><s:message code="label.generic.repeatpassword" text="Repeat password"/></label>
							<div class="controls">
							     <s:message code="message.password.repeat.required" text="Repeated password is required" var="msgRepeatPassword"/>
							     <form:password path="checkPassword" class="span8 required checkPassword" id="passwordAgain" title="${msgRepeatPassword}"/>
								 <form:errors path="checkPassword" cssClass="error" />
							</div>
						</div>						
		
						<div class="control-group">
							<div class="controls"><!--watch the white space in IOS!-->
								<script type="text/javascript"
    								 src="http://www.google.com/recaptcha/api/challenge?k=<c:out value="${recapatcha_public_key}"/>&hl=${requestScope.LANGUAGE.code}">
  								</script>
							<noscript>
								<iframe
									src="http://www.google.com/recaptcha/api/noscript?k=<c:out value="${recapatcha_public_key}"/>&hl=${requestScope.LANGUAGE.code}"
									height="300" width="500" frameborder="0">
								</iframe>
								<br/>
								<form:textarea path="recaptcha_challenge_field" readonly="3" cols="40"/>
								<form:errors path="recaptcha_challenge_field" cssClass="error" />
     							
								<input type="hidden" name="recaptcha_response_field"
									value="manual_challenge">
							</noscript>
						</div>
						</div>

						<div class="form-actions">
							<input id="submitRegistration" type="submit" value="<s:message code="label.generic.register" text="Register"/>" name="register" class="btn btn-primary btn-large">
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