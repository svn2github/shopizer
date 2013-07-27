<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>

	<%
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", -1);
	%>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
	<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
	<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>





	<head>
		<meta http-equiv="Pragma" content="no-cache">
			<meta http-equiv="expires" content="0">
				<title><s:message code="label.storeadministration"
						text="Store administration" />
				</title>




<style type=text/css>
#logon {
	margin: 0px auto;
	width: 550px
}

#login-box {
	width: 333px;
	height: 352px;
	padding: 58px 76px 0 76px;
	color: #ebebeb;
	font: 12px Arial, Helvetica, sans-serif;
	background: url('<c:url value="/resources/img/admin/login-box-backg.png" />') no-repeat left top;
}

#login-box h2 {
	padding: 0;
	margin: 0;
	color: #ebebeb;
	font: bold 36px "Calibri", Arial;
	border-bottom: 2px solid;
	padding-bottom: 0px;
}

#login-box {
	margin-left: 30px;
}

#controls {
	margin-left: -50px;
	margin-top: 30px;
}




</style>








				<link
					href="<c:url value="/resources/css/bootstrap/css/sm-bootstrap.css" />"
					rel="stylesheet" />
				<link
					href="<c:url value="/resources/css/sm-bootstrap-responsive.css" />"
					rel="stylesheet" />
				<link href="<c:url value="/resources/css/shopizer.css" />"
					rel="stylesheet" />


				<style type=text/css>
.sm label {
	color: #EBEBEB;
	font-size: 16px;
}

.sm a {
	color: #EBEBEB;
	font-size: 16px;
}





</style>

		<script src="<c:url value="/resources/js/bootstrap/jquery.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/jquery-cookie.js"/>"></script>
		<script src="<c:url value="/resources/js/bootstrap/bootstrap-modal.js" />"></script>



    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_Core.js" />"></script>
    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_Foundation.js" />"></script>
    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_Containers.js" />"></script>
    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_Grids.js" />"></script>
    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_DataBinding.js" />"></script>



				<script language="javascript">
				
				function doAjaxPost() {
					 // get the form values 
				$('#securityQtn1Select').empty();
				$('#securityQtn2Select').empty();
				$('#securityQtn3Select').empty();
				$('#answer1').val('');
				$('#answer2').val('');
				$('#answer3').val('');
				var userName = $('#username').val();
		        if(!userName){
				    alert("please enter username");
			    }else{
					$.ajax({
							type: 'POST',
							dataType: "json",
							url: "<c:url value="/admin/users/resetPassword.html" />",
							data: "username="+ userName ,
							success: function(response) { 
								 console.log("responcesajid "+response);
								 console.log(response);
								 var msg = isc.XMLTools.selectObjects(response, "/response/statusMessage");
								 var status = isc.XMLTools.selectObjects(response, "/response/status");
								 if(status==0 || status ==9999) {
									 $("#getPassword").modal('hide'),
									 $('#getSecurityQtn').modal({
    								 backdrop: true
    						   		 })
    						
    								 var data = isc.XMLTools.selectObjects(response, "/response/data");
    						  	     if(data && data.length>0) {
    								 	console.log(data[0]);
    								 	$.each(data[0], function(key, value) {   
    							    	 	$('#securityQtn1Select')
    							        	 	.append($("<option></option>")
    							        	 	.attr("value",key)
    							         	 	.text(value)); 
    							     		 $('#securityQtn2Select')
							         	 		 .append($("<option></option>")
							         	     	 .attr("value",key)
							        	   	     .text(value)); 
    							     		  $('#securityQtn3Select')
							        	 	     .append($("<option></option>")
							        			 .attr("value",key)
							        			 .text(value)); 
    								 	   	   });
    								 } else {

			    					 }
								} else {
									if(msg!=null && msg !='') {
										alert("! " + msg);
									}
								}
								
							}
							/* ,
							 error: function(e){  
							      alert('Error: ' + e);  
							      console.log(e);
							    } */
							}); 
				}
 		        				}
				
				
				function doSecurityQtnSubmit() {
					var question1 = $('#securityQtn1Select').val();
					var question2 = $('#securityQtn2Select').val();
					var question3 = $('#securityQtn3Select').val();
					
					//console.log("question1"+question1);
					//console.log("question2"+question2);
					//console.log("question3"+question3);
					
					var answer1 = $('#answer1').val();
					var answer2 = $('#answer2').val();
					var answer3 = $('#answer3').val();
					
				   		 
					 if(!answer1){
						    alert("please enter answer one");
					 }else if(!answer2){
						 	alert("please enter answer two");
					 }else if(!answer3){
						   alert("please enter answer three");
					 }else{
						 
					
							$.ajax({
									type: 'POST',
									dataType: "json",
									url: "<c:url value="/admin/users/resetPasswordSecurityQtn.html" />",
									data: "question1="+ question1+"&question2="+ question2+"&question3="+ question3+"&answer1="+ answer1+"&answer2="+ answer2+"&answer3="+ answer3,
									success: function(response) { 
										 //console.log("responcesajid "+response);
										 //console.log(response);
										 var msg = isc.XMLTools.selectObjects(response, "/response/statusMessage");
										 var status = isc.XMLTools.selectObjects(response, "/response/status");
										 if(status==0 || status ==9999) {
											 $("#getSecurityQtn").modal('hide')
											  $('#finalWindow').modal({
		    								 backdrop: true
		    						   		 }) 
					 						 $("#finaltext").val (msg);
											 var div = document.getElementById('finaltext1');
											 div.innerHTML =  msg;		    						
		    								 /* var data = isc.XMLTools.selectObjects(response, "/response/data");
		    						  	     if(data && data.length>0) {
		    								 	console.log(data[0]);
		    								 	$.each(data[0], function(key, value) {   
		    							    	 	$('#securityQtn1Select')
		    							        	 	.append($("<option></option>")
		    							        	 	.attr("value",key)
		    							         	 	.text(value)); 
		    							     		 $('#securityQtn2Select')
									         	 		 .append($("<option></option>")
									         	     	 .attr("value",key)
									        	   	     .text(value)); 
		    							     		  $('#securityQtn3Select')
									        	 	     .append($("<option></option>")
									        			 .attr("value",key)
									        			 .text(value)); 
		    								 	   	   });
		    								 } else {
		
					    					 }
		 */								} else {
											if(msg!=null && msg !='') {
												//alert("! " + msg);
												 $("#getSecurityQtn").modal('hide')
												  $('#finalWindow').modal({
			    								 backdrop: true
			    						   		 }) 
						 						 $("#finaltext").val (msg);
												var div = document.getElementById('finaltext1');
												div.innerHTML =  msg;

												 
											}
										}
										
									}/* ,
									error: function(e){  
									      alert('Error: ' + e);  
									      console.log(e);
									    } */
									});
					 }
				}

	$(document)
			.ready(
					function() {
					
					
						//$('#getPassword').modal('hide');
						//alert('After modal');
						

						
						$('#changePassword').click(function() {
							//$('#getPassword').show();
							$('#username').val('');
							$('#getPassword').modal({
    							backdrop: true
    						})
						})
						

						var username = $.cookie('usernamecookie');
						if (username != null && username != '') {
							$('#j_username').val(username);
							$('#remember').attr('checked', true);
						}


						$("#formSubmitButton")
								.click(
										function() {
											
											

											var hasError = false;
											$('#j_username_help').html("");
											$('#j_password_help').html("");
											
											
											if ($('#remember').attr('checked')) {
												$.cookie('usernamecookie', $(
														'#j_username').val(), {
													expires : 1024,
													path : '/'
												});
											} else {
												$.cookie('usernamecookie',
														null, {
															expires : 1024,
															path : '/'
														});
											}
											if ($.trim($('#j_username').val()) == '') {
												hasError = true;
												$('#j_username_help')
														.html(
																"<font color='red' size='4'><strong>*</strong></font>");
											}

											if ($.trim($('#j_password').val()) == '') {
												hasError = true;
												$('#j_password_help')
														.html(
																"<font color='red' size='4'><strong>*</strong></font>");
											}

											if (!hasError) {
												$("#logonForm").submit();
											}

										});

					});
</script>
	</head>

	<body>

		<div class="sm">

			<br />
			<br />

			<div id=logon>









				<div class="row">
					<c:if test="${not empty param.login_error}">
						<div class="alert alert-error">
							<s:message code="errors.invalidcredentials"
								text="Invalid username or password" />
						</div>
					</c:if>
				</div>



				<div id="login-box">


					<div class="row">
						<div style="float: left; width: 180px;">
							<h2>
								<s:message code="button.label.logon" text="Logon" />
							</h2>
						</div>
						<div style="float: right;">
							<img alt="go to www.shopizer.com"
								src="<c:url value="/resources/img/shopizer_small.png" />">
						</div>
					</div>

					<div class="row">
						<div id="controls">

							<form method="post" id="logonForm" class="form-horizontal" action="<c:url value="/admin/j_spring_security_check"/>">
								<div class="control-group">
									<label class="control-label" for="inputUser">
										<s:message code="label.username" text="Username" />
									</label>
									<div class="controls">
										<input type="text" id="j_username" name="j_username"
											placeholder="<s:message code="label.username" text="Username"/>">
											<span id="j_username_help" class="help-inline"></span>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="inputPassword">
										<s:message code="label.password" text="Password" />
									</label>

									<div class="controls">
										<input type="password" id="j_password" name="j_password"
											placeholder="<s:message code="label.password" text="Password"/>">
											<span id="j_password_help" class="help-inline"></span>
									</div>
								</div>
								<div class="control-group">
									<div class="controls">
										<label class="checkbox">
											<input type="checkbox" id="remember">
												<s:message code="label.logonform.rememberusername"
													text="Remember my user name" />
												<br/>
												<a href="#" id="changePassword"><s:message code="label.logonform.forgotpassword" text="Forgot Password"/>?</a>
										</label>
										<a href="#" class="btn" id="formSubmitButton"> <s:message
												code="button.label.logon" text="button.label.submit2" /> </a>
									</div>
								</div>
							</form>
						</div>
					</div>

					<!--
		<form id="loginform" name="loginform" method="post" action="">
		<div style="margin-top:20px;">

			<p><label for="name"><s:message code="label.username" text="Username"/>:</label> <input name="username" id="username" class="form-login" title="<s:message code="label.username" text="Username"/>" value="" size="30" maxlength="60" /></p>
			<p><label for="pass"><s:message code="label.password" text="Password"/>:</label> <input name="password" id="password" type="password" class="form-login" title="<s:message code="label.password" text="Password"/>" value="" size="30" maxlength="30" /></p>
			

			<p>

					<label for="remember">&nbsp;</label>
					<div style="margin-feft:0px;"><input type="checkbox" id="remember" name="remember" value="1" ><s:message code="label.logonform.rememberusername" text="Remember my user name"/></div>
					<br/>
					<label for="forgot">&nbsp;</label><a href="#getPassword" id="changePassword"><s:message code="label.logonform.forgotpassword" text="Forgot Password"/>?</a>

			</p>

			<p class="submit">
					<label for="submit">&nbsp;</label>
					<a href="#" id="logon-button" class="button">
						<div class="button-img"><div class="button-text"><s:message code="button.label.submit2" text="button.label.submit2"/></div></div>
					</a>

			</p>
		</div>
		</form>
		-->
		
		<!-- code for reset password-username prompt   sajid shajahan -->
				<div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" class="modal hide fade" id="getPassword" style="display: none;">
				<!--<div class="modal" id="getPassword" tabindex="-1" role="dialog" class="modal hide fade" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">-->
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">
							×
						</button>
						<h3 id="myModalLabel" style="color:#333333;">
							<s:message code="label.logonform.resetpassword" text="Password reset"/>
						</h3>
					</div>
					<form method="post" id="resetPasswordForm" class="form-horizontal"
								action="#">
					<div class="modal-body">
						<p>

								<div class="control-group">
									<label class="control-label" for="inputUser" style="color:#333333;">
										<s:message code="label.username" text="Username" />
									</label>
									<div class="controls">
										<input type="text" id="username" name="username"
											placeholder="<s:message code="label.username" text="Username"/>">
											

											<span id="username_help" class="help-inline"></span>
									</div>
								</div>
								

							</form>
						</p>
					</div>
					<div class="modal-footer">
									<div class="controls">
										<!-- <a href="#" class="btn" id="passwordResetSubmitButton"> 
										<s:message code="button.label.submit2" text="Submit" /> </a> -->
										<input type="button" value="Submit" onclick="doAjaxPost()" class="btn" >
									</div>
								</div>
					</div>
					</form>
				</div>
				
				


<!-- code for reset password-security question prompt   sajid shajahan -->
				<div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" class="modal hide fade" id="getSecurityQtn" style="display: none;">
				<!--<div class="modal" id="getPassword" tabindex="-1" role="dialog" class="modal hide fade" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">-->
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">
							×
						</button>
						<h3 id="myModalLabel" style="color:#333333;">
							<s:message code="label.logonform.securityquestion" text="Password reset"/>
						</h3>
					</div>
					<form method="post" id="resetPasswordForm" class="form-horizontal"
								action="#">
					<div class="modal-body">
						<p>
								
								<div class="control-group">
								
									<label class="control-label" for="inputUser" style="color:#333333;">
										<s:message code="security.question1" text="" />
									</label>
									<div class="controls">
										<select id="securityQtn1Select">
  											
										</select>
										
										<input type="text" id="answer1" name="answer1"
											placeholder="<s:message code="security.answer.question1.message" text="answer1"/>">
											

									</div>
									
									
								</div>
								
								<div class="control-group">
									<label class="control-label" for="inputUser" style="color:#333333;">
										<s:message code="security.question2" text="" />
									</label>
									<div class="controls">
										<select id="securityQtn2Select">
  											
										</select>
										<input type="text" id="answer2" name="answer2"
											placeholder="<s:message code="security.answer.question2.message" text="answer2"/>">
											

									</div>
								</div>
									
									<div class="control-group">
									<label class="control-label" for="inputUser" style="color:#333333;">
										<s:message code="security.question3" text="" />
									</label>
									<div class="controls">
										<select  id="securityQtn3Select">
  											
										</select>
										<input type="text" id="answer3" name="answer3"
											placeholder="<s:message code="security.answer.question3.message" text="answer3"/>">
										
											

									</div>
								</div>
																							
							</form>
						</p>
					</div>
					<div class="modal-footer">
									<div class="controls">
										<!-- <a href="#" class="btn" id="passwordResetSubmitButton"> 
										<s:message code="button.label.submit2" text="Submit" /> </a> -->
										<input type="button" value="Submit" onclick="doSecurityQtnSubmit()" class="btn" >
									</div>
								</div>
					</div>
					</form>
				</div>



<!-- code for reset password-final window   sajid shajahan -->
				<div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" class="modal hide fade" id="finalWindow" style="display: none;">
				<!--<div class="modal" id="getPassword" tabindex="-1" role="dialog" class="modal hide fade" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">-->
					<div class="modal-header">
						  <button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">
							×
						</button>
						<h3 id="myModalLabel" style="color:#333333;">
						<s:message code="" text="Shopizer"/>
						</h3>
					</div>
					<form method="post" id="resetPasswordForm" class="form-horizontal"
								action="#">
					<div class="modal-body">
						<p>
								<h3><center><div id="finaltext1"></div></center></h3>
								<!-- <div class="control-group">
								
									<label class="control-label" for="inputUser" style="color:#333333;">
									<h3><div id="finaltext1"></div></h3>
									</label>
									<div class="controls">

									</div>
									
									
								</div>
								 -->
																							
							</form>
						</p>
					</div>
					<div class="modal-footer">
									<div class="controls">
										<button data-dismiss="modal" class="btn">Close</button>
									</div>
								</div>
					</div>
					</form>
				</div>
				
			
	<div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" class="modal hide fade" id="myModal" style="display: none;">
            <div class="modal-header">
              <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
              <h3 id="myModalLabel">Modal Heading</h3>
            </div>
            <div class="modal-body">
              <h4>Text in a modal</h4>
              <p>Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem.</p>

              <h4>Popover in a modal</h4>
              <p>This <a data-content="And here's some amazing content. It's very engaging. right?" class="btn popover-test" role="button" href="#" data-original-title="A Title">button</a> should trigger a popover on hover.</p>

              <h4>Tooltips in a modal</h4>
              <p><a class="tooltip-test" href="#" data-original-title="Tooltip">This link</a> and <a class="tooltip-test" href="#" data-original-title="Tooltip">that link</a> should have tooltips on hover.</p>

              <hr>

              <h4>Overflowing text to show optional scrollbar</h4>
              <p>We set a fixed <code>max-height</code> on the <code>.modal-body</code>. Watch it overflow with all this extra lorem ipsum text we've included.</p>
              <p>Cras mattis consectetur purus sit amet fermentum. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Morbi leo risus, porta ac consectetur ac, vestibulum at eros.</p>
              <p>Praesent commodo cursus magna, vel scelerisque nisl consectetur et. Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor.</p>
              <p>Aenean lacinia bibendum nulla sed consectetur. Praesent commodo cursus magna, vel scelerisque nisl consectetur et. Donec sed odio dui. Donec ullamcorper nulla non metus auctor fringilla.</p>
              <p>Cras mattis consectetur purus sit amet fermentum. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Morbi leo risus, porta ac consectetur ac, vestibulum at eros.</p>
              <p>Praesent commodo cursus magna, vel scelerisque nisl consectetur et. Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor.</p>
              <p>Aenean lacinia bibendum nulla sed consectetur. Praesent commodo cursus magna, vel scelerisque nisl consectetur et. Donec sed odio dui. Donec ullamcorper nulla non metus auctor fringilla.</p>
            </div>
            <div class="modal-footer">
              <button data-dismiss="modal" class="btn">Close</button>
              <button class="btn btn-primary">Save changes</button>
            </div>
          </div>

				<!--
				<div id="change-password" style="display: none;"
					title="<s:message code="label.logonform.resetpassword" text="Reset password"/>">
					<form id="resetPassword" name="resetPassword" action="">
						<div id="loadingimage" style="display: none;">
							<center>`<img
								src="<c:url value="/resources/img/ajax-loader.gif" />"></center>
						</div>
						<div id="resetPasswordMessage" style="display: none;"></div>
						<p>
							<label for="merchantId">
								<s:message code="label.store.merchantid" text="Merchant Id" />
								:
							</label>
							<input name="merchantId" id="merchantId"
								title="<s:message code="label.store.merchantid" text="Merchant Id"/>"
								value="" size="10" maxlength="15" />
						</p>
						<p>
							<label for="adminName">
								<s:message code="label.username" text="Username" />
								:
							</label>
							<input name="adminName" id="adminName"
								title="<s:message code="label.username" text="Username"/>"
								value="" size="20" maxlength="60" />
						</p>


						<p class="submit">

							<label for="submit">
								&nbsp;
							</label>
							<button id="reset" type="submit">
								<s:message code="button.label.submit2" text="Submit" />
							</button>
						</p>
					</form>
				</div>
				-->


				<script type="text/javascript">
				
				
							
	function validateForm() {

		//var username = new LiveValidation( 'username', {validMessage: " ",onlyOnSubmit: true}); 
		//username.add( Validate.Presence,{failureMessage:'*'}); 
		//var password = new LiveValidation( 'password', {validMessage: " ",onlyOnSubmit: true } ); 
		//password.add( Validate.Presence,{failureMessage:'*'}); 

		//var areAllValid = LiveValidation.massValidate( [username,password] );

		//if(areAllValid) {
		//submitForm();

		//}

	}

	function validateResetPassword() {

		//var merchantId = new LiveValidation( 'merchantId', {validMessage: " ",onlyOnSubmit: true}); 
		//merchantId.add( Validate.Presence,{failureMessage:'*'}); 
		//merchantId.add( Validate.Numericality,{failureMessage:''}); 

		//var userName = new LiveValidation( 'adminName', {validMessage: " ",onlyOnSubmit: true } ); 
		//userName.add( Validate.Presence,{failureMessage:'*'});

		//var areAllValid = LiveValidation.massValidate( [merchantId,userName] );

		//if(areAllValid) {

		//return true;

		//} else {

		//return false;
		//}

		return true;

	}
</script>



			</div>
	</body>
</html>