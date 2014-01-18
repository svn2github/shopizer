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
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<script src="<c:url value="/resources/js/jquery.creditCardValidator.js" />"></script>

<script type="text/javascript">
		$(document).ready(function() {
			    $("#card_number").removeClass("valid")
				$('#card_number').validateCreditCard(function(result) {
					
					if(result.card_type!=null) {
							//alert('CC type: ' + result.card_type.name
							//			      + '\nLength validation: ' + result.length_valid
							//			      + '\nLuhn validation: + result.luhn_valid');
							$("#card_number").addClass("valid")
					}
				},
				{ accept: ['visa', 'mastercard', 'amex'] }
				
				);
				
				
		})
</script>


		  
          <div class="control-group">
            <label class="control-label">Use your credit card</label>
            <div class="controls">
               <input type="radio" name="paymentMethodType" value="creditcard" <c:if test="${requestScope.paymentMethod.defaultSelected==true}"> checked</c:if>/>
            </div>
          </div>

          <div class="control-group">
            <label class="control-label">Card Holder's Name</label>
            <div class="controls">
              <input type="text" class="input-xlarge required" pattern="\w+ \w+.*" title="Fill your first and last name" required>
            </div>
          </div>
       
          <div class="control-group">
            <label class="control-label">Card Number</label>
            <input id="card_number" class="input-xlarge valid required" type="text" name="card_number" autocomplete="off">
          </div>
       
          <div class="control-group">
            <label class="control-label">Card Expiry Date</label>
            <div class="controls">
              <div class="row-fluid">
                <div class="span3">
                  <select class="input-medium">
                    <option>January</option>
                    <option>...</option>
                    <option>December</option>
                  </select>
                </div>
                <div class="span3">
                  <select class="input-small">
                    <option>2013</option>
                    <option>...</option>
                    <option>2015</option>
                  </select>
                </div>
              </div>
            </div>
          </div>
       
          <div class="control-group">
            <label class="control-label">Card CVV</label>
            <div class="controls">
              <div class="row-fluid">
                <div class="span4">
                  <input type="text" class="input-small required" autocomplete="off" maxlength="3" pattern="\d{3}" title="Three digits at back of your card">
                </div>
                <div class="span4">
                  <!-- screenshot may be here -->
                </div>
              </div>
            </div>
          </div>

