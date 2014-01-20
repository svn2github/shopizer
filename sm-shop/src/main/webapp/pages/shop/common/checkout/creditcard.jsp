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
		var ccValid = false;


		$(document).ready(function() {
			    var creditCardDiv = '#creditcard_card_number';
			    invalidCreditCardNumber(creditCardDiv);
				$(creditCardDiv).validateCreditCard(function(result) {
					invalidCreditCardNumber(creditCardDiv);
					if(result.card_type!=null) {
							//console.log('CC type: ' + result.card_type.name
							//			      + '\nLength validation: ' + result.length_valid
							//			      + '\nLuhn validation: + result.luhn_valid');
							if(result.luhn_valid==true && result.length_valid==true) {
								validCreditCardNumber(creditCardDiv, result);
							} else {
								invalidCreditCardNumber(creditCardDiv);
							}
					} else {
						invalidCreditCardNumber(creditCardDiv);
					}
					//call parent form valid
					isFormValid();
				},
				{ accept: ['visa', 'mastercard', 'amex'] }
				
				);	

		})
		
		function invalidCreditCardNumber(div) {
			$('#creditcard_card_image').html('');
			$(div).removeClass("valid");
			$(div).css('background-color', '#FFC');
			ccValid = false;
		}
		
		function validCreditCardNumber(div, creditCard) {
			$(div).addClass("valid");
			$(div).css('background-color', '#FFF');
			$('#creditcard_card_image').html('<img src="<c:url value="/resources/img/payment/icons/'+ creditCard.card_type.name +'-straight-32px.png" />"/>');
			ccValid = true;
		}
		
		function isCreditCardValid() {
			return ccValid;
		}
		

</script>


		  
          <div class="control-group">
            <label class="control-label"><c:message code="label.payment.creditcard.usecredicard" text="Use your credit card" /></label>
            <div class="controls">
               <input type="radio" name="paymentMethodType" value="creditcard" <c:if test="${requestScope.paymentMethod.defaultSelected==true}"> checked</c:if>/>
            </div>
          </div>

          <div class="control-group">
            <label class="control-label"><c:message code="label.payment.creditcard.cardowner" text="Card Holder's Name" /></label>
            <div class="controls">
              <s:message code="NotEmpty.order.creditcard.name" text="Credit card holder's name is required" var="msgCardHolderName"/>
              <input type="text" id="creditcard_card_holder" name="" class="input-xlarge required" title="${msgCardHolderName}">
            </div>
          </div>
       
          <div class="control-group">
            <label class="control-label"><c:message code="label.payment.creditcard.cardnumber" text="Card number" /></label>
            <s:message code="NotEmpty.order.creditcard.number" text="A valid credit card number is required" var="msgCardNumber"/>
            <div class="input-append">
            <input id="creditcard_card_number" class="input-xlarge valid required" type="text" name="card_number" autocomplete="off" title="${msgCardNumber}">
          	<span id="creditcard_card_image" class="img-add-on"></span>
          	</div>
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
                  <s:message code="NotEmpty.order.creditcard.cvv" text="Credit card validation digit is required" var="msgCardCvv"/>
                  <input type="text" id="creditcard_card_cvv" name="" class="input-small required" autocomplete="off" maxlength="3" pattern="\d{3}" title="${msgCardCvv}">
                </div>
                <div class="span4">
                  <!-- screenshot may be here -->
                </div>
              </div>
            </div>
          </div>

