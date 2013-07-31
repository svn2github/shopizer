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

        $("#login").submit(function() {
 			$("#store.error").hide();
            var data = $(this).serializeObject();
            $.ajax({
                'type': 'POST',
                'url': "<c:url value="/customer/logon.html"/>",
                'contentType': 'application/json',
                'data': JSON.stringify(data),
                'dataType': 'json',
                'success': function(result) {
                   if (result.status==0) {
                        location.href="<c:url value="/customer/dashboard.html" />";
                   } else {
                        $("#store.error").html("<s:message code="message.username.password" text="Login Failed. Username or Password is incorrect."/>");
                        $("#store.error").show();
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
						<!-- TODO configuration for displaying or not contact link -->
						<li><a href="#contactformlink">Contact</a></li>
					</ul>

 					<div style="padding-top: 8px;padding-bottom:10px;" class="btn-group pull-right">
            					&nbsp;&nbsp;&nbsp;
            					<i class="icon-shopping-cart icon-black"></i>
            					<a style="box-shadow:none;color:FF8C00;" href="#" data-toggle="dropdown" class="open noboxshadow dropdown-toggle" id="open-cart">My Cart</a>
           		 			    <span id="cartinfo">
           		 			    	<c:choose>
	           		 			    	<c:when test="${requestScope.SHOPPING_CART != null}">
	                  						<span id="cartqty">(<c:out value="${requestScope.SHOPPING_CART.quantity}"/>&nbsp;<c:choose><c:when test="${fn:length(requestScope.SHOPPING_CART.quantity)>1}" ><s:message code="label.generic.items" text="items" /></c:when><c:otherwise><s:message code="label.generic.item" text="item" /></c:otherwise></c:choose>)</span>&nbsp;<span id="cartprice"><c:out value="${requestScope.SHOPPING_CART.total}"/></span>
	                  					</c:when>
	                  					<c:otherwise>
	                  						<span id="cartqty">(0 <s:message code="label.generic.item" text="item" />)</span>
	                  					</c:otherwise>
                  					</c:choose>
            					</span>
            				<c:choose>
            					 
	           		 			 <c:when test="${requestScope.SHOPPING_CART != null}">
		            					<ul class="dropdown-menu minicart">
		              						<li>
		                  						<div class="cartbox" id="cart-box">
		                  							<div class="box-content clearfix">
		                  								<h3 class="lbw">Shopping Cart</h3>&nbsp;<span style="width:15%;display:none;" id="checkout-wait"><img src="img/misc/wait18trans.gif"></span><br>
				                  						<div id="shoppingcart">
				                  						<table style="margin-bottom: 5px" class="table">
				                  						<tbody><tr id="42" class="cart-product">
				                  						<td><img width="40" height="40" src="img/products/shirt1.jpg"></td>
				                  						<td>1 Short sleeves white</td><td>$29.99</td>
				                  						<td><button productid="42" class="close removeProductIcon">x</button></td>
				                  						</tr></tbody></table>
				                  						
				                  						<div style="padding-right:4px;" class="row">
				                  						<div class="pull-right">Shipping costs of $10.00 : $10.00</div>
				                  						</div><div class="total-box">
				                  						
				                  						<div class="pull-right"><font class="total-box-label">Total : <font class="total-box-price"><strong><span id="checkout-total-plus">$39.99</span></strong></font></font></div>
				                  						</div>
				                  						<br/>
				                  						<button class="btn" style="width:100%" type="submit">Check out</button>
				                  						</div>
			                  						</div>
		                  						</div>
		              						</li>
		            					</ul>
            					</c:when>
            					<c:otherwise>
            							<ul class="dropdown-menu minicart">
	                  						//NO ITEMS [TODO]
	                  					</ul>
	                  			</c:otherwise>
                  			</c:choose>
					
					</div>
					

					<ul class="nav pull-right" style="padding-top: 8px;z-index:500000;">
					  <li id="fat-menu" class="dropdown">
					    <a href="#" id="drop3" role="button" class="dropdown-toggle" data-toggle="dropdown"><s:message code="button.label.signin" text="Signin" /><b class="caret"></b></a>
					
					
							<div class="dropdown-menu" style="padding: 15px; padding-bottom: 0px;">
								<div id="store.error" class="alert alert-error" style="display:none;"></div>
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
								<a href="#myModal" role="button" class="" data-toggle="modal"><s:message code="button.label.register" text="Register"/></a>
							</div>
					  </li>
					</ul>





			</div>
			<!-- End main menu -->