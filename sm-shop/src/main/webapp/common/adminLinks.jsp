<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>

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

	.error { color: #ff0000; } 

    </style>
    <link href="<c:url value="/resources/css/sm-bootstrap-responsive.css" />" rel="stylesheet">
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

