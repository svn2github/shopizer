<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Admin</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    
    


    <!-- Le styles -->
    
    <link href="<c:url value="/resources/css/sm-bootstrap.css" />" rel="stylesheet">

    <link href="<c:url value="/resources/css/sm-bootstrap-responsive.css" />" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/panel-style.css" />" /> 
    <link href="<c:url value="/resources/css/shopizer.css" />" rel="stylesheet">


    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <!--<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>-->
    <![endif]-->




    <script>var isomorphicDir="<c:url value="/resources/smart-client/" />";</script>
    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_Core.js" />"></script>
    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_Foundation.js" />"></script>
    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_Containers.js" />"></script>
    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_Grids.js" />"></script>
    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_Forms.js" />"></script>
    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_DataBinding.js" />"></script>
    <script SRC="<c:url value="/resources/smart-client/skins/Graphite/load_skin.js" />"></script>




  </head>

  <body>

<p>&nbsp;</p>


		  


<div class="sm">

	<div class="container"> 

		<div class="row">

			<div class="span3">



				<ul class="nav nav-list">
				
					  <c:forEach items="${menus}" var="menu">
					  			<li <c:if test="${activeMenus[menu.code]!=null}"> class="active"</c:if>>
									<a href="<c:url value="${menu.url}" />">
										<i class="${menu.icon}"></i>
											<s:message code="menu.${menu.code}" text="${menu.code}"/>
									</a>
					  			</li>
					  </c:forEach>
				
				</ul>



			</div>

			<div class="span9">
			


				<div class="tabbable">

  					
  					<c:if test="${fn:length(currentMenu.menus)>0}">
						
  						<ul class="nav nav-tabs">
  						<c:forEach items="${currentMenu.menus}" var="menu">
  							<c:choose>
  							    <c:when test="${fn:length(menu.menus)==0}">
  									<li id="${menu.code}-tab" <c:if test="${activeMenus[menu.code]!=null}"> class="active"</c:if>><a href="#" id="${menu.code}-link" data-toggle="tab"><s:message code="menu.${menu.code}" text="${menu.code}"/></a></li>
  							    </c:when>
  							    <c:otherwise>
  									<li class="dropdown <c:if test="${activeMenus[menu.code]!=null}"> active</c:if>" style="z-index:500000;position:relative"> 
  										<a href="#" class="dropdown-toggle" data-toggle="dropdown"><s:message code="menu.${menu.code}" text="${menu.code}"/><b class="caret"></b></a>
  										<ul class="dropdown-menu"> 
  											<c:forEach items="${menu.menus}" var="submenu">
  												<li><a href="#" id="${submenu.code}-link" data-toggle="tab"><s:message code="menu.${submenu.code}" text="${submenu.code}"/></a></li>
  											</c:forEach>
  										</ul> 
  									</li>
  							    </c:otherwise>
  							</c:choose>
  						</c:forEach>
  						</ul>
  					</c:if>


  					<div class="tab-content">

    					<div class="tab-pane active" id="catalogue-products-create-section">

								
								

				<c:url var="merchant" value="/admin/merchant/save.html"/>


				<form:form method="POST" commandName="store" action="${merchant}">
				
				<form:errors path="*" cssClass="alert alert-error" element="div" />
				
				<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
				
	      			<div class="control-group">
	                        <label><s:message code="label.storename" text="Name"/></label>
	                        <div class="controls">
	                        		<form:input cssClass="input-large" path="storename" />
	                                    <span class="help-inline"><form:errors path="storename" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                  <div class="control-group">
	                        <label><s:message code="label.storephone" text="Phone"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large" path="storephone" />
	                                    <span class="help-inline"><form:errors path="storephone" cssClass="error" /></span>
	                        </div>
	
	                  </div>
	                  
	                 <div class="control-group">
	                        <label><s:message code="label.storeemailaddress" text="Email"/></label>
	                        <div class="controls">
	                                    <form:input cssClass="input-large" path="storeEmailAddress" />
	                                    <span class="help-inline"><form:errors path="storeEmailAddress" cssClass="error" /></span>
	                        </div>
	                  </div>
	                  
	                  <div class="control-group">
	                        <label><s:message code="label.country" text="Country"/></label>
	                        <div class="controls">
	                        					
	                        					<form:select path="country.id">
					  								<form:options items="${countries}" itemValue="id" itemLabel="name"/>
				       							</form:select>
	                                   			<span class="help-inline"><form:errors path="country" cssClass="error" /></span>
	                        </div>
	                  </div>
				
				      <div class="form-actions">
	                  		<div class="pull-right">
	                  			<button type="submit" class="btn btn-action"><s:message code="button.label.submit2" text="Submit"/></button>
	                  		</div>
	            	 </div>


      					
				</form:form>
      			     
      			     


      			     
      			     
    


   					</div>

    					
    					<!--
    					<div class="tab-pane" id="categories">

							<a href="#" id="categories-menu-main">Categories</a><br/>
							<a href="#" id="categories-menu-hierarchy">Reorganize category hierarchy</a><br/>
							<a href="#" id="categories-menu-create">Create new category</a><br/><br/>

      						<div class="sm-ui-component sm-iframe-component" id="categories-content">
      							TEST
      						</div>

    					</div>

    					<div class="tab-pane" id="options">

      					<p>Howdy, I'm in Section 3.</p>

    					</div>
    					-->

  					</div>

				</div>



			</div>


		</div>



  
		<hr> 
  
  
		<footer> 
 			<p>&copy; Company 2012</p> 
		</footer> 
  
  
	</div> <!-- /container --> 
	
								   <script>
							  
		
									</script>
  
</div>

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script SRC="<c:url value="/resources/js-bootstrap/jquery.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/js-bootstrap/slide.js" />"></script> 
    <script src="<c:url value="/resources/js-bootstrap/bootstrap-transition.js" />"></script>
    <script src="<c:url value="/resources/js-bootstrap/bootstrap-alert.js" />"></script>
    <script src="<c:url value="/resources/js-bootstrap/bootstrap-modal.js" />"></script>
    <script src="<c:url value="/resources/js-bootstrap/bootstrap-dropdown.js" />"></script>
    <script src="<c:url value="/resources/js-bootstrap/bootstrap-scrollspy.js" />"></script>
    <script src="<c:url value="/resources/js-bootstrap/bootstrap-tab.js" />"></script>
    <script src="<c:url value="/resources/js-bootstrap/bootstrap-tooltip.js" />"></script>
    <script src="<c:url value="/resources/js-bootstrap/bootstrap-popover.js" />"></script>
    <script src="<c:url value="/resources/js-bootstrap/bootstrap-button.js" />"></script>
    <script src="<c:url value="/resources/js-bootstrap/bootstrap-collapse.js" />"></script>
    <script src="<c:url value="/resources/js-bootstrap/bootstrap-carousel.js" />"></script>
    <script src="<c:url value="/resources/js-bootstrap/bootstrap-typeahead.js" />"></script>


	<script>
	
		$(document).ready(function(){   
			$("#catalogue-products-create-link").click(function() {
				window.location='<c:url value="/admin/products/product.html" />';
  				
			});
			$("#catalogue-categories-list-link").click(function() {
				window.location='<c:url value="/admin/categories/categories.html" />';
			});
			$("#catalogue-categories-hierarchy-link").click(function() {
  				
			});

		}); 
	
	</script>

  </body>
</html>