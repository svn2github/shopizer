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
			<div class="span7">  

								<c:set var="contact_url" value="${pageContext.request.contextPath}/shop/submitContactUs.html"/>
                                <form:form action="${contact_url}" method="POST" id="validForm" name="contactForm" commandName="contact">
                                    <form:errors path="*" cssClass="alert alert-error" element="div" />
                                    <div class="control-group">
                                        <label for="inputName" class="control-label">NAME<sup>*</sup></label>
                                        <div class="controls">
										   <s:message code="NotEmpty.customer.name" text="Name is required" var="msgName"/>
										   <form:input path="name" cssClass="required" id="name" title="${msgName}"/>
										   <form:errors path="name" cssClass="error" />
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label for="inputEmail" class="control-label">EMAIL<sup>*</sup></label>
                                        <div class="controls">
                                            <input type="text" placeholder="Email" id="inputEmail" class="span4" name="email">
                                            <span class="help-inline" style="display: none;">Please correct your email</span>
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label" for="textarea">COMMENT<sup>*</sup></label>
                                        <div class="controls">
                                            <textarea id="textarea" class="span8" rows="4" name="comment"></textarea>
                                            <span class="help-inline" style="display: none;">Please write a comment</span>
                                        </div>
                                    </div>

                                    <div class="control-group form-button-offset">
                                        <input type="submit" value="Send Message" class="btn">
                                    </div>
                                </form:form>
              </div>
<!-- END LEFT-SIDE CONTACT FORM AREA -->


<!-- BEGIN RIGHT-SIDE CONTACT FORM AREA -->
              <div class="contact-info span4 offset1">
									<!-- COMPANY ADDRESS -->   
									<c:if test="${requestScope.CONFIGS['displayStoreAddress'] == true}">                                  
                                     <address>  
									 	<div itemscope itemtype="http://schema.org/Organization"> 
									 	<span itemprop="name" class="lead"><c:out value="${requestScope.MERCHANT_STORE.storename}"/></span><br/>  
									 	<div itemprop="address" itemscope itemtype="http://schema.org/PostalAddress"> 
									 	<span itemprop="streetAddress"><c:out value="${requestScope.MERCHANT_STORE.storeaddress}"/> <c:out value="${requestScope.MERCHANT_STORE.storecity}"/></span><br/>
									 	<span itemprop="addressLocality"><c:choose><c:when test="${not empty requestScope.MERCHANT_STORE.storestateprovince}"><c:out value="${requestScope.MERCHANT_STORE.storestateprovince}"/></c:when><c:otherwise><script>$.ajax({url: "<c:url value="/shop/reference/zoneName"/>",type: "GET",data: "zoneCode=${requestScope.MERCHANT_STORE.zone.code}",success: function(data){$('#storeZoneName').html(data)}})</script><span id="storeZoneName"><c:out value="${requestScope.MERCHANT_STORE.zone.code}"/></span></c:otherwise></c:choose>,
									 	<span id="storeCountryName"><script>$.ajax({url: "<c:url value="/shop/reference/countryName"/>",type: "GET",data: "countryCode=${requestScope.MERCHANT_STORE.country.isoCode}",success: function(data){$('#storeCountryName').html(data)}})</script></span></span><br/>
									 	<span itemprop="postalCode"><c:out value="${requestScope.MERCHANT_STORE.storepostalcode}"/></span><br/>
									 	<abbr title="Phone"><s:message code="label.generic.phone" text="Phone" /></abbr>: <span itemprop="telephone"><c:out value="${requestScope.MERCHANT_STORE.storephone}"/></span>
									 	</div>
									 	</div>
									 </address>
									 </c:if>
									                                   
                                    

                                </div>
                     </div>
<!-- END RIGHT-SIDE CONTACT FORM AREA -->
<!-- CUSTOM CONTENT --> 
			<div class="row-fluid">
                                    <c:if test="${content!=null}">
                                    	<br/>
                                        <p>
                                        	<c:out value="${content.description}" escapeXml="false"/>
                                    	</p>
                                    	<br/>
                                    </c:if>
			</div>

<!-- GOOGLE MAP -->  
<c:if test="${requestScope.CONFIGS['displayStoreAddress'] == true}">                              
<iframe src="http://maps.google.com/maps?f=q&amp;source=s_q&amp;hl=en&amp;geocode=&amp;q=Mockingbird+Station,+Dallas,+TX&amp;aq=1&amp;oq=mockinStation,+Dallas,+TX&amp;sll=32.786144,-96.788897&amp;sspn=0.00929,0.018947&amp;ie=UTF8&amp;hq=&amp;hnear=Mockingbird+Station,+Dallas,+Texas+75206&amp;t=m&amp;ll=32.845774,-96.772385&amp;spn=0.043266,0.061712&amp;z=14&amp;iwloc=A&amp;output=embed" style="width: 100%; height: 380px; border: none;"></iframe><br><small><a style="color:#0000FF;text-align:left" href="http://maps.google.com/maps?f=q&amp;source=embed&amp;hl=en&amp;geocode=&amp;q=Mockingbird+Station,+Dallas,+TX&amp;aq=1&amp;oq=mockinStation,+Dallas,+TX&amp;sll=32.786144,-96.788897&amp;sspn=0.00929,0.018947&amp;ie=UTF8&amp;hq=&amp;hnear=Mockingbird+Station,+Dallas,+Texas+75206&amp;t=m&amp;ll=32.845774,-96.772385&amp;spn=0.043266,0.061712&amp;z=14&amp;iwloc=A">View Larger Map</a></small>
</c:if>
 </div>
