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

<script>
    $(function() {
    	
    	$("#signinDrop").click(function(e){
    		$("#loginError").hide();
    		e.preventDefault();
    	});

        $("#login").submit(function() {
        	$('#signinPane').showLoading();
 			$("#loginError").hide();
            var data = $(this).serializeObject();
            $.ajax({
                'type': 'POST',
                'url': "<c:url value="/customer/logon.html"/>",
                'contentType': 'application/json',
                'data': JSON.stringify(data),
                'dataType': 'json',
                'success': function(result) {
                   $('#signinPane').hideLoading();
                   var response = result.response;
                   if (response.status==0) {
                        location.href="<c:url value="/customer/dashboard.html" />";
                   } else {
                	   
                        $("#loginError").html("<s:message code="message.username.password" text="Login Failed. Username or Password is incorrect."/>");
                        $("#loginError").show();
                   }
                }
            });
 
            return false;
        });
    });
 	//TODO ??
    $.fn.serializeObject = function() {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
</script>


			<!-- header -->
			<div id="mainmenu" class="row-fluid">
				
					<ul class="nav nav-pills pull-left" id="linkMenuLinks">
						<li class="active"><a href="<c:url value="/shop"/>"><s:message code="menu.home" text="Home"/></a></li>
						<c:forEach items="${requestScope.CONTENT_PAGE}" var="content">
    							<li class="">
    								<a href="<c:url value="/shop/pages/${content.seUrl}.html"/>" class="current"> 
    									<span class="name">${content.name}</span> 
    								</a>
    							</li>
						</c:forEach>
						<c:if test="${requestScope.CONFIGS['displayContactUs']==true}">
						<li><a href="#contactformlink"><s:message code="label.customer.contactus" text="Contact us"/></a></li>
						</c:if>
					</ul>

 					<div style="padding-top: 8px;padding-bottom:10px;" class="btn-group pull-right">
            					&nbsp;&nbsp;&nbsp;
            					<i class="icon-shopping-cart icon-black"></i>
            					<a style="box-shadow:none;color:FF8C00;" href="#" data-toggle="dropdown" class="open noboxshadow dropdown-toggle" id="open-cart">My Cart</a>
								<jsp:include page="/pages/shop/common/cart/minicartinfo.jsp" />
            				<c:choose>
	           		 			 <c:when test="${requestScope.SHOPPING_CART != null}">
		            					<ul class="dropdown-menu minicart">
		              						<li>
												<jsp:include page="/pages/shop/common/cart/minicart.jsp" />
		              						</li>
		            					</ul>
            					</c:when>
            					<c:otherwise>
            							<ul class="dropdown-menu minicart">
	                  						<h4 class="cartmessage"><s:message code="label.emptycart" text="No items in your shopping cart" /></h4>
	                  					</ul>
	                  			</c:otherwise>
                  			</c:choose>
					
					</div>
					
					<!-- TODO display if not logged in -->
					<ul class="pull-right" style="list-style-type: none;padding-top: 8px;z-index:500000;">
					  <li id="fat-menu" class="dropdown">
					    <a href="#" id="signinDrop" role="button" class="dropdown-toggle noboxshadow" data-toggle="dropdown"><s:message code="button.label.signin" text="Signin" /><b class="caret"></b></a>
					
					
							<div id="signinPane" class="dropdown-menu" style="padding: 15px; padding-bottom: 0px;">
								<div id="loginError" class="alert alert-error" style="display:none;"></div>
								<form id="login" method="post" accept-charset="UTF-8">
									<div class="control-group">
	                        				<label><s:message code="label.username" text="Username" /></label>
					                        <div class="controls">
												<input id="userName" style="margin-bottom: 15px;" type="text" name="userName" size="30" />
											</div>
									</div>
									<div class="control-group">
	                        				<label><s:message code="label.password" text="Password" /></label>
					                        <div class="controls">
												<input id="password" style="margin-bottom: 15px;" type="password" name="password" size="30" />
											</div>
									</div>
									<input id="storeCode" name="storeCode" type="hidden" value="<c:out value="${requestScope.MERCHANT_STORE.code}"/>"/>
									<!--input id="user_remember_me" style="float: left; margin-right: 10px;" type="checkbox" name="user[remember_me]" value="1" /-->
									<!--<label class="string optional" for="user_remember_me"> Remember me</label>-->						 
									<button type="submit" style="width:100%" class="btn"><s:message code="button.label.login" text="Login" /></button>
									
								</form>
								<a href="<c:url value="/shop/customer/registration.html" />" role="button" class="" data-toggle="modal"><s:message code="button.label.register" text="Register"/></a>
							</div>
					  </li>
					</ul>





			</div>
			<!-- End main menu -->