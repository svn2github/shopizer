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


	<div id="main-content" class="container clearfix">
		<div class="row-fluid">
			<div class="span12">
			
			
			<c:if test="${options!=null && fn:length(options)>0}">
				
				<div id="attributesSuccess" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>   
	            <div id="attributesError" class="alert alert-error" style="display:none;"><s:message code="message.error" text="An error occured"/></div>
				<div id="attributesBox" class="box">
						<span class="box-title">
						<p><s:message code="label.customer.attributes" text="Customer attributes" /></p>
						</span>
				
				
					<c:url var="customerOptions" value="/admin/customers/attributes/save.html"/>
					<form id="attributes">
					<input id="customer" type="hidden" value="<c:out value="${customerAttr.id}"/>" name="customer">
					<c:forEach items="${options}" var="option" varStatus="status">
						<div class="control-group"> 
	                        <label><c:out value="${option.name}"/></label>
	                        <div class="controls">	       							
									<c:choose>
										<c:when test="${option.type=='Select'}">
											<select id="<c:out value="${option.id}"/>" name="<c:out value="${option.id}"/>">
											<c:forEach items="${option.availableValues}" var="optionValue">
												<option value="${optionValue.id}" <c:if test="${option.defaultValue!=null && option.defaultValue.id==optionValue.id}"> SELECTED</c:if>>${optionValue.name}</option>
											</c:forEach>
											</select>
										</c:when>
										<c:when test="${option.type=='Radio'}">
											<c:forEach items="${option.availableValues}" var="optionValue">
												<input type="radio" id="<c:out value="${option.id}"/>" name="<c:out value="${option.id}"/>" value="<c:out value="${optionValue.id}"/>" <c:if test="${option.defaultValue!=null && option.defaultValue.id==optionValue.id}"> checked="checked" </c:if> />
												<c:out value="${optionValue.name}"/>
											</c:forEach>
										</c:when>
										<c:when test="${option.type=='Text'}">
											<input class="textAttribute" type="text" id="<c:out value="${option.id}"/>-<c:out value="${option.availableValues[0].id}"/>" name="<c:out value="${option.id}"/>-<c:out value="${option.availableValues[0].id}"/>" class="input-large" value="<c:if test="${option.defaultValue!=null}">${option.defaultValue.name}</c:if>">
										</c:when> 
										<c:when test="${option.type=='Checkbox'}">
											<c:forEach items="${option.availableValues}" var="optionValue">
												<input type="checkbox" id="<c:out value="${option.id}"/>-<c:out value="${optionValue.id}"/>" name="<c:out value="${option.id}"/>-<c:out value="${optionValue.id}"/>" <c:if test="${option.defaultValue!=null && option.defaultValue.id==optionValue.id}"> checked="checked" </c:if>  />
												<c:out value="${optionValue.name}"/>
											</c:forEach>
										</c:when>										
										
										
									</c:choose>				       							
                                 	<span class="help-inline"></span>
	                        </div>
	                    </div> 

					
					</c:forEach>
					
					<div class="form-actions">
                 	  <div class="pull-right">
                 			<button type="submit" class="btn btn-success"><s:message code="button.label.save" text="Save"/></button>
                 	  </div> 
           	  		 </div>


      					
				</form>
				</div>
				</c:if>

			
			
			
			
				   <div class="span6">

					<div class="box">
						<span class="box-title">
							<p><font color="#FF8C00"><s:message code="label.customer.myaccount" text="My account"/></font></p>
						</span>
						<ul>
							<li><a href="<c:out value="/customer/account.html"/>"><s:message code="menu.profile" text="Profile"/></a></li>
							<li><a href="<c:out value="/customer/billing.html"/>"><s:message code="label.customer.billingshipping" text="Billing & shipping information"/></a></li>
						</ul>
					</div>


				   </div>
				   <div class="span6">
					<div class="box">
						<span class="box-title">
							<p><font color="#FF8C00"><s:message code="label.order.pastorders" text="Past orders"/></font></p>
						</span>
						<ul>
							<li><s:message code="label.order.recent" text="Recent orders"/></li>
						</ul>
					</div>

				   </div>
			</div>
		</div>
		<!-- close row-fluid--> 
	</div>
	<!--close .container "main-content" -->