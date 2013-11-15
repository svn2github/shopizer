<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>


    <jsp:include page="/resources/js/functions.jsp" />
    <script src="<c:url value="/resources/templates/bootstrap/jquery/jquery-1.10.2.min.js" />"></script>
    <script src="<c:url value="/resources/js/functions.js" />"></script>
    <script src="<c:url value="/resources/js/jquery-cookie.js" />"></script>
    
    <link href="<c:url value="/resources/templates/bootstrap/css/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/templates/bootstrap/css/bootstrap-responsive.min.css" />" rel="stylesheet">
    <!-- generic css file -->
    <link href="<c:url value="/resources/css/sm.css" />" rel="stylesheet">
    <!-- template css file -->
    <link href="<c:url value="/resources/templates/bootstrap/css/theme.css" />" rel="stylesheet">
 
    <script src="<c:url value="/resources/js/jquery.showLoading.min.js" />"></script>
	<link href="<c:url value="/resources/css/showLoading.css" />" rel="stylesheet">

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <!--<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>-->
    <!--[endif]-->


<!-- Le styles -->
    <link href="css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }


	.minicart {
    		width: 350px;
	}

	.total-box {
  		padding: 7px 2px;
  		margin: 0 0 0px;
		height: 18px;
  		list-style: none;
  		background-color: #fbfbfb;
  		background-image: -moz-linear-gradient(top, #ffffff, #f5f5f5);
  		background-image: -ms-linear-gradient(top, #ffffff, #f5f5f5);
  		background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#ffffff), to(#f5f5f5));
  		background-image: -webkit-linear-gradient(top, #ffffff, #f5f5f5);
  		background-image: -o-linear-gradient(top, #ffffff, #f5f5f5);
  		background-image: linear-gradient(top, #ffffff, #f5f5f5);
  		background-repeat: repeat-x;
  		border: 1px solid #ddd;
  		-webkit-border-radius: 3px;
  		-moz-border-radius: 3px;
  		border-radius: 3px;
  		filter: progid:dximagetransform.microsoft.gradient(startColorstr='#ffffff', endColorstr='#f5f5f5', GradientType=0);
  		-webkit-box-shadow: inset 0 1px 0 #ffffff;
  		-moz-box-shadow: inset 0 1px 0 #ffffff;
  		box-shadow: inset 0 1px 0 #ffffff;
	}

	.total-box-label {
		font-size: 14px;
	}

	.total-box-price {
		color: #FF8C00;
		font-style: bold;
	}

	.cartbox {

		margin:20px 20px 20px 20px; 


	}

	#shoppingcart {

		-webkit-box-sizing: border-box; 
		-moz-box-sizing: border-box; 
		box-sizing: border-box; 

	}




    </style>
