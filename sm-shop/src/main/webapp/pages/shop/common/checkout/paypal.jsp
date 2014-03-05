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


          <div class="control-group">
            <label class="control-label"><s:message code="label.payment.paypal.usepaypal" text="Use PayPal"/></label>
            <div class="controls">
               <input type="radio" name="paymentMethodType" value="<c:out value="${requestScope.paymentMethod.paymentType}"/>" <c:if test="${requestScope.paymentMethod.defaultSelected==true}"> checked</c:if>/>
               <input type="hidden" name="paymentModule" value="${requestScope.paymentMethod.paymentMethodCode}"/>
            </div>
          </div>
		 
		 <div class="control-group">
		 	<!-- PayPal Logo -->
		 	<a href="https://www.paypal.com/webapps/mpp/paypal-popup" title="How PayPal Works" onclick="javascript:window.open('https://www.paypal.com/webapps/mpp/paypal-popup','WIPaypal','toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=1060, height=700'); return false;"><img src="https://www.paypalobjects.com/webstatic/mktg/logo/AM_mc_vs_dc_ae.jpg" width="200" border="0" alt="PayPal Acceptance Mark"></a>
		 </div>