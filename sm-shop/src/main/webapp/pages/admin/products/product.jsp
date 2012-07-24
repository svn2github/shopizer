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
			

      			<div class="control-group">
                        <label><s:message code="label.storename" text="Name"/></label>
                        <div class="controls">
                                    <input type="text" class="input-large" name="store.storename" id="store.storename">
                        </div>
                  </div>
                  
                  <div class="control-group">
                        <label><s:message code="label.storephone" text="Phone"/></label>
                        <div class="controls">
                                    <input type="text" class="input-large" name="store.storephone" id="store.storephone">
                        </div>

                  </div>
                  
                 <div class="control-group">
                        <label><s:message code="label.storeemailaddress" text="Email"/></label>
                        <div class="controls">
                                    <input type="text" class="input-large" name="store.storeEmailAddress" id="store.storeEmailAddress">
                        </div>

                  </div>
			
			      <div class="form-actions">

                  		<div class="pull-right">

                  			<button type="submit" class="btn btn-action"><s:message code="button.label.submit2" text="Submit"/></button>

                  		</div>

            	 </div>


      					

      			     
      			     


      			     
      			     
    


   					</div>

    					


  					</div>
 </div>