<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
				
<div class="tabbable">

  					
  					<c:if test="${fn:length(currentMenu.menus)>0}">
						
  						<ul class="nav nav-tabs">
  						<c:forEach items="${currentMenu.menus}" var="menu">
  							<c:choose>
  							    <c:when test="${fn:length(menu.menus)==0}">
  									<li id="${menu.code}-tab" <c:if test="${activeMenus[menu.code]!=null}"> class="active"</c:if>><a href="#" id="${menu.code}-link" data-toggle="tab"><s:message code="menu.${menu.code}" text="${menu.code}"/></a></li>
  							    </c:when>
  							    <c:otherwise>
  									<li class="dropdown <c:if test="${activeMenus[menu.code]!=null}"> active</c:if>" style="z-index:500000;position:relative"> 
  										<a href="#" class="dropdown-toggle" data-toggle="dropdown"><s:message code="menu.${menu.code}" text="${menu.code}"/><b class="caret"></b></a>
  										<ul class="dropdown-menu"> 
  											<c:forEach items="${menu.menus}" var="submenu">
  												<li><a href="#" id="${submenu.code}-link" data-toggle="tab"><s:message code="menu.${submenu.code}" text="${submenu.code}"/></a></li>
  											</c:forEach>
  										</ul> 
  									</li>
  							    </c:otherwise>
  							</c:choose>
  						</c:forEach>
  						</ul>
  					</c:if>

  					<div class="tab-content">

    					<div class="tab-pane active" id="catalogue-products-create-section">

								
								


							<div class="btn-group"> 
								<button class="btn btn-info dropdown-toggle" data-toggle="dropdown">Configure product ... <span class="caret"></span></button> 
								<ul class="dropdown-menu"> 
								<li><a href="<c:url value="/admin/products/price.html" />">Product price</a></li> 
								<li><a href="<c:url value="/admin/catalogue/products/bundles.html" />">Product bundles</a></li> 
								<li><a href="<c:url value="/admin/products/price.html" />">Product options</a></li>
								<li><a href="<c:url value="/admin/catalogue/relatedItems.html" />">Related items</a></li>
								<li><a href="<c:url value="/admin/catalogue/product.html" />">Product details</a></li><!-- set product id in query string -->  
								</ul> 
							</div><!-- /btn-group --> 
							<br/>
							<h3>Configure product</h3>
			

      						<c:url var="productSave" value="/admin/product/save.html"/>
                            <form:form method="POST" commandName="product" action="${productSave}">

                            <form:errors path="*" cssClass="alert alert-error" element="div" />

                            <div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>   

                                               

                                               

 

                        <div class="control-group">

                        <label class="required"><s:message code="label.product.sku" text="Sku"/></label>

                        <div class="controls">

                                    <input type="text" class="input-large" name="sku" id="store.storename">

                                     <span class="help-inline"><form:errors path="sku" cssClass="error" /></span>

                        </div>

                  		</div>

                 

                  		<div class="control-group">

                        <label><s:message code="label.product.visible" text="Product visible"/></label>

                        <div class="controls">

                        </div>

                  		</div>

                 

                  		<!-- eyecon plugin -->

                 		 <div class="control-group">

                        <label><s:message code="label.product.availabledate" text="Date available"/></label>

                        <div class="controls">

                                    <input class="span2" id="appendedInput" size="16" type="text"><span class="add-on">2012-06-30</span>

                        </div>

                 		 </div>

                 

                  

                 		<div class="control-group">

                          <label><s:message code="label.product.manufacturer" text="Manufacturer"/></label>

                          <div class="controls">
    

                          </div>

                    	</div>

                 

                  		<div class="control-group">

                        <label><s:message code="label.productedit.producttype" text="Product type"/></label>

                        <div class="controls">

                                  

                        </div>

 

                 		 </div>

                 

                  <c:forEach items="${product.descriptions}" var="description" varStatus="counter">

                 

                        <div class="control-group">

                              <label class="required"><s:message code="label.productedit.productname" text="Product name"/> (<c:out value="${description.language.code}"/>)</label>

                              <div class="controls">

                                          <form:input path="descriptions[${counter.index}].name"/>

                                          <span class="help-inline"><form:errors path="descriptions[${counter.index}].name" cssClass="error" /></span>

                              </div>

                       </div>

                      

                        <div class="control-group">

                              <label class="required"><s:message code="label.productedit.producthl" text="Product highlight"/> (<c:out value="${description.language.code}"/>)</label>

                              <div class="controls">

                                          <form:input path="descriptions[${counter.index}].productHighlight"/>

                                          <span class="help-inline"><form:errors path="descriptions[${counter.index}].productHighlight" cssClass="error" /></span>

                              </div>

                       </div>

                 

                

                        <div class="control-group">

                              <label class="required"><s:message code="label.productedit.productdesc" text="Product description"/> (<c:out value="${description.language.code}"/>)</label>

                              <div class="controls">

 

                              </div>

                       </div>

                      

                       

                        <div class="control-group">

                              <label class="required"><s:message code="label.productedit.sefurl" text="Search engine friendly url"/> (<c:out value="${description.language.code}"/>)</label>

                              <div class="controls">

                                          <form:input path="descriptions[${counter.index}].seUrl"/>

                                          <span class="help-inline"><form:errors path="descriptions[${counter.index}].seUrl" cssClass="error" /></span>

                              </div>

                       </div>

                      

                        <div class="control-group">

                              <label class="required"><s:message code="label.productedit.metatagtitle*" text="Product title"/> (<c:out value="${description.language.code}"/>)</label>

                              <div class="controls">

                                          <form:input path="descriptions[${counter.index}].metatagTitle"/>

                                          <span class="help-inline"><form:errors path="descriptions[${counter.index}].metatagTitle" cssClass="error" /></span>

                              </div>

                       </div>

                      

                        <div class="control-group">

                              <label class="required"><s:message code="label.productedit.productseometadesc" text="Metatag description"/> (<c:out value="${description.language.code}"/>)</label>

                              <div class="controls">

                                          <form:input path="descriptions[${counter.index}].metatagDescription"/>

                                          <span class="help-inline"><form:errors path="descriptions[${counter.index}].metatagDescription" cssClass="error" /></span>

                              </div>

                       </div>

                      

                         <form:hidden path="descriptions[${counter.index}].language.code" />

                 

                  </c:forEach>

                 

                 <div class="control-group">

                        <label class="required"><s:message code="label.productedit.price*" text="Price"/></label>

                        <div class="controls">

                                    <form:input path="availabilities[0].prices[0].productPriceAmount"/>

                                    <span class="help-inline"><form:errors path="availabilities[0].prices[0].productPriceAmount" cssClass="error" /></span>

                        </div>

                  </div>

                 

                 <div class="control-group">

                        <label><s:message code="label.productedit.qtyavailable" text="Quantity available"/></label>

                        <div class="controls">

                                    <form:input path="availabilities[0].productQuantity"/>

                                    <span class="help-inline"><form:errors path="availabilities[0].productQuantity" cssClass="error" /></span>

                        </div>

                  </div>

                 

                  <div class="control-group">

                        <label><s:message code="label.product.ordermin*" text="Quantity order minimum"/></label>

                        <div class="controls">

                                    <form:input path="availabilities[0].productQuantityOrderMin"/>

                                    <span class="help-inline"><form:errors path="availabilities[0].productQuantityOrderMin" cssClass="error" /></span>

                        </div>

                  </div>

                 

                  <div class="control-group">

                        <label><s:message code="label.product.ordermax" text="Quantity order maximum"/></label>

                        <div class="controls">

                                    <form:input path="availabilities[0].productQuantityOrderMax"/>

                                    <span class="help-inline"><form:errors path="availabilities[0].productQuantityOrderMax" cssClass="error" /></span>

                        </div>

                  </div>


                   <form:hidden path="availabilities[0].region" />

                 

                  <!-- hidden when creating the product -->

                  <div class="control-group">

                        <label><s:message code="label.product.uploadimage" text="Image"/></label>

                        <div class="controls">

                                    <input class="input-file" id="fileInput" type="file">

                        </div>

                  </div>


                        <div class="form-actions">

 

                              <div class="pull-right">

 

                                    <button type="submit" class="btn btn-action"><s:message code="button.label.submit2" text="Submit"/></button>

 

                              </div>

 

                   </div>

                   

                 

 

 

                                   

                        </form:form>

      					

      			     
      			     


      			     
      			     
    


   					</div>

    					


  					</div>
 </div>