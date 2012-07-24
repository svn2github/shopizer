<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    
    


    <!-- Le styles -->
    
    <link href="<c:url value="/resources/css/bootstrap/css/sm-bootstrap.css" />" rel="stylesheet">
    <style type="text/css">


	html {
  		font-size: 100%;
  		-webkit-text-size-adjust: 100%;
  		-ms-text-size-adjust: 100%;
	}

	body {
  		margin: 0;
  		font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
  		font-size: 13px;
  		/**line-height: 18px;**/
  		color: #333333;
  		background-color: #ffffff;
	}



	.table tbody tr.subt:hover td, 
	.table tbody tr.subt:hover th { 
		background-color: transparent; 
	} 
	
	
	.sm-ui-component label {


    		color: #333333;
    		margin-bottom: 0;
    		display: inline;

   	}
   
  	.sm-ui-component IMG {


   	 	max-width:none;


   	}


  	.sm-iframe-component {


   	 	width:100%;
		height:620px;
		overflow:hidden;
		border:0;


   	}


    </style>
    <link href="<c:url value="/resources/css/bootstrap/css/sm-bootstrap-responsive.css" />" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/panel-style.css" />" /> 
 


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
				
					  <c:forEach items="${requestScope.MENULIST}" var="menu">
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
			



   					Dashboard content
   					
   					<br/>
   					
   					Last 10 orders

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
    <script SRC="<c:url value="/resources/js/bootstrap/jquery.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/bootstrap/slide.js" />"></script> 
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-transition.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-alert.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-modal.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-dropdown.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-scrollspy.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-tab.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-tooltip.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-popover.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-button.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-collapse.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-carousel.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap/bootstrap-typeahead.js" />"></script>




  </body>
</html>