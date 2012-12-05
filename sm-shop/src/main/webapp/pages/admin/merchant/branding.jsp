<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>   

<%@ page session="false" %>


    <link href="<c:url value="/resources/css/bootstrap/css/datepicker.css" />" rel="stylesheet"></link>
	<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>




<div class="tabbable">


				<jsp:include page="/common/adminTabs.jsp" />
				
				<h3><s:message code="label.store.title" text="Merchant store" /></h3>	
				<br/>



				<c:choose>

						<c:url var="merchant" value="/admin/store/saveBranding.html"/>
						<form:form method="POST" commandName="store" action="${merchant}">
				
							<form:errors path="*" cssClass="alert alert-error" element="div" />

			                  <div class="control-group">
			                        <label><s:message code="label.storelogo" text="Store logo"/></label>&nbsp;<c:if test="${store.storeLogo!=null}"><span id="imageControlRemove"> - <a href="#" onClick="removeImage('${store.id}')"><s:message code="label.generic.remove" text="Remove"/></a></span></c:if>
			                        <div class="controls" id="imageControl">
			                        		<c:choose>
				                        		<c:when test="${store.storeLogo==null}">
				                                    <input class="input-file" id="image" name="image" type="file">
				                                </c:when>
				                                <c:otherwise>
				                                	<img src="<%=request.getContextPath()%>/<sm:contentImage imageName="${store.storeLogo}" imageType="LOGO"/>"/>
				                                </c:otherwise>
			                                </c:choose>
			                        </div>
			                  </div>
				                  	                  
	                  		<form:hidden path="id" />
				      		<div class="form-actions">
	                  			<div class="pull-right">
	                  				<button type="submit" class="btn btn-success"><s:message code="button.label.submit2" text="Submit"/></button>
	                  			</div>
	            			 </div>
						</form:form>
					
					
					
					</c:otherwise>
				</c:choose>

				

				
				
				
				

				
				
				
				


</div>