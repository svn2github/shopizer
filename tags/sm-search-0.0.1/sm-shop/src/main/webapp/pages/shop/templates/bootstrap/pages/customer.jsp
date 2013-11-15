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
 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  
 <%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 

    <div class="row-fluid">
						   <ul class="breadcrumb">
								<li>
									<a href="#"><i class="icon-home"></i></a> <span class="divider">/</span>
								</li>
								<li><a href="http://wbpreview.com/previews/WB0M3G9S1/products.html">Product</a> <span class="divider">/</span></li>
								<li><a href="http://wbpreview.com/previews/WB0M3G9S1/products.html">Women</a> <span class="divider">/</span></li>
								<li class="active">Detail</li>
							</ul>
	</div>
 


	<div class="row-fluid">
	
	
	   <div class="span12">
      	
      	<!-- left column -->
        <div class="span3">
          <div class="sidebar-nav">
            <ul class="nav nav-list">


              <li class="nav-header">Sidebar</li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
              <li><a href="#">Link</a></li>
            </ul>
          </div>
        </div><!--/span-->
        
        <!-- right column -->
        <div class="span9">
        
        
        					<div class="box">
								<span class="box-title">
								<p>Customer</p>
								</span>
								information
        					</div>
        
        
        </div><!--/span-->
        
        </div><!-- 12 -->
        
      </div><!-- row fluid -->