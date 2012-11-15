<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>			


    <link href="<c:url value="/resources/css/bootstrap/css/datepicker.css" />" rel="stylesheet"></link>
	<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>
	<script src="<c:url value="/resources/js/ckeditor/ckeditor.js" />"></script>
	
	
	
		<script type="text/javascript">
	

	
	$(function(){
		//if($("#code").val()=="") {
		//	$('.btn').addClass('disabled');
		//}
		<c:forEach items="${product.descriptions}" var="description" varStatus="counter">		
			$("#name${counter.index}").friendurl({id : 'seUrl${counter.index}'});
		</c:forEach>
	});
	
	
	function validateCode() {
		$('#checkCodeStatus').html('<img src="<c:url value="/resources/img/ajax-loader.gif" />');
		$('#checkCodeStatus').show();
		var code = $("#code").val();
		var id = $("#id").val();
		checkCode(code,id,'<c:url value="/admin/categories/checkCategoryCode.html" />');
	}
	
	function callBackCheckCode(msg,code) {
		
		if(code==0) {
			$('.btn').removeClass('disabled');
		}
		if(code==9999) {

			$('#checkCodeStatus').html('<font color="green"><s:message code="message.code.available" text="This code is available"/></font>');
			$('#checkCodeStatus').show();
			$('.btn').removeClass('disabled');
		}
		if(code==9998) {

			$('#checkCodeStatus').html('<font color="red"><s:message code="message.code.exist" text="This code already exist"/></font>');
			$('#checkCodeStatus').show();
			$('.btn').addClass('disabled');
		}
		
	}
	
	
	</script>
	
				
<div class="tabbable">


					<jsp:include page="/common/adminTabs.jsp" />
  					
  					 <div class="tab-content">

    					<div class="tab-pane active" id="catalogue-section">


								<div class="sm-ui-component">
								
								
								<c:if test="${product.product.id!=null && product.product.id>0}">
									<c:set value="${product.product.id}" var="productId" scope="request"/>
									<jsp:include page="/pages/admin/products/product-menu.jsp" />
								</c:if>	
								
								
				<h3>
					<c:choose>
						<c:when test="${product.product.id!=null && product.product.id>0}">
								<s:message code="label.product.edit" text="Edit product" /> <c:out value="${product.product.sku}"/>
						</c:when>
						<c:otherwise>
								<s:message code="label.product.create" text="Create product" />
						</c:otherwise>
					</c:choose>
					
				</h3>	
				<br/>
			

      					<c:url var="productSave" value="/admin/products/save.html"/>
                        <form:form method="POST" enctype="multipart/form-data" commandName="product" action="${productSave}">

                            <form:errors path="*" cssClass="alert alert-error" element="div" />
                            <div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>   

                        <div class="control-group">
	                        <label><s:message code="label.product.sku" text="Sku"/></label>
	                        <div class="controls">
	                        		  <form:input cssClass="input-large highlight" path="product.sku"/>
	                                  <span class="help-inline"><form:errors path="product.sku" cssClass="error" /></span>
	                        </div>
                  		</div>

						<form:hidden path="product.id" />
                 

                  		<div class="control-group">
                        	<label><s:message code="label.product.available" text="Product available"/></label>
                        	<div class="controls">
                                    <form:checkbox path="product.available" />
                        	</div>
                  		</div>
                  		
                  		
                  		<div class="control-group">
	                        <label><s:message code="label.product.availabledate" text="Date available"/></label>
	                        <div class="controls">
	                        		 <input id="dateAvailable" value="${product.product.dateAvailable}" class="small" type="text" data-datepicker="datepicker"> 
	                                 <span class="help-inline"></span>
	                        </div>
	                  	</div>
	                  	
	                  	<div class="control-group">
                        	<label><s:message code="label.product.manufacturer" text="Manufacturer"/></label>
                          	<div class="controls">
                          		      <form:select items="${manufacturers}" itemValue="id" itemLabel="descriptions[0].name"  path="product.manufacturer.id"/> 
	                                  <span class="help-inline"></span>
                          	</div>
                    	</div>




                  		<div class="control-group">
                        	<label><s:message code="label.productedit.producttype" text="Product type"/></label>
                        	<div class="controls">
                        		         <form:select items="${productTypes}" itemValue="id" itemLabel="code"  path="product.type.id"/> 
	                                     <span class="help-inline"></span>
                        	</div>
                 		 </div>

                 

                  <c:forEach items="${product.descriptions}" var="description" varStatus="counter">

                 

                        <div class="control-group">

                              <label class="required"><s:message code="label.productedit.productname" text="Product name"/> (<c:out value="${description.language.code}"/>)</label>
                              <div class="controls">
                                          <form:input cssClass="input-large highlight" id="name${counter.index}" path="descriptions[${counter.index}].name"/>
                                          <span class="help-inline"><form:errors path="descriptions[${counter.index}].name" cssClass="error" /></span>
                              </div>

                       </div>

                      
                        <div class="control-group">
                              <label class="required"><s:message code="label.sefurl" text="Search engine friendly url"/> (<c:out value="${description.language.code}"/>)</label>
                              <div class="controls">
                                          <form:input id="seUrl${counter.index}" cssClass="input-large" path="descriptions[${counter.index}].seUrl"/>
                                          <span class="help-inline"><form:errors path="descriptions[${counter.index}].seUrl" cssClass="error" /></span>
                              </div>
                       </div>
                       

                        <div class="control-group">
                              <label class="required"><s:message code="label.productedit.producthl" text="Product highlight"/> (<c:out value="${description.language.code}"/>)</label>
                              <div class="controls">
                                          <form:input cssClass="input-large" path="descriptions[${counter.index}].productHighlight"/>
                                          <span class="help-inline"><form:errors path="descriptions[${counter.index}].productHighlight" cssClass="error" /></span>
                              </div>

                       </div>


                        <div class="control-group">
                              <label class="required"><s:message code="label.productedit.productdesc" text="Product description"/> (<c:out value="${description.language.code}"/>)</label>
                              <div class="controls">
                              	     <textarea cols="30" id="descriptions[${counter.index}].description" class="ckeditor" name="descriptions[${counter.index}].description">
                        				<c:out value="${descriptions[counter.index].description}"/>
                        			 </textarea>
                              </div>
                       </div>
                      

                        <div class="control-group">
                              <label class="required"><s:message code="label.product.title" text="Product title"/> (<c:out value="${description.language.code}"/>)</label>
                              <div class="controls">
                                          <form:input cssClass="input-large" path="descriptions[${counter.index}].metatagTitle"/>
                                          <span class="help-inline"><form:errors path="descriptions[${counter.index}].metatagTitle" cssClass="error" /></span>
                              </div>
                       </div>

                      

                        <div class="control-group">
                              <label class="required"><s:message code="label.metatags.description" text="Metatag description"/> (<c:out value="${description.language.code}"/>)</label>
                              <div class="controls">
                                          <form:input cssClass="input-large" path="descriptions[${counter.index}].metatagDescription"/>
                                          <span class="help-inline"><form:errors path="descriptions[${counter.index}].metatagDescription" cssClass="error" /></span>
                              </div>
                       </div>

                      

                         <form:hidden path="descriptions[${counter.index}].language.id" />
                         <form:hidden path="descriptions[${counter.index}].language.code" />
						 <form:hidden path="descriptions[${counter.index}].id" />

                 

                  </c:forEach>

                 

                 <div class="control-group">

                        <label class="required"><s:message code="label.product.price" text="Price"/></label>

                        <div class="controls">
                                    <form:input cssClass="highlight" path="price.productPriceAmount"/>
                                    <span class="help-inline"><form:errors path="price.productPriceAmount" cssClass="error" /></span>
                        </div>
                  </div>

                 

                 <div class="control-group">

                        <label><s:message code="label.productedit.qtyavailable" text="Quantity available"/></label>
                        <div class="controls">
                                    <form:input cssClass="highlight" path="availability.productQuantity"/>
                                    <span class="help-inline"><form:errors path="availability.productQuantity" cssClass="error" /></span>
                        </div>
                  </div>

                 

                  <div class="control-group">
                        <label><s:message code="label.product.ordermin" text="Quantity order minimum"/></label>
                        <div class="controls">
                                    <form:input cssClass="highlight" path="availability.productQuantityOrderMin"/>
                                    <span class="help-inline"><form:errors path="availability.productQuantityOrderMin" cssClass="error" /></span>

                        </div>
                  </div>

                 

                  <div class="control-group">
                        <label><s:message code="label.product.ordermax" text="Quantity order maximum"/></label>
                        <div class="controls">
                                    <form:input cssClass="highlight" path="availability.productQuantityOrderMax"/>
                                    <span class="help-inline"><form:errors path="availability.productQuantityOrderMax" cssClass="error" /></span>
                        </div>
                  </div>


                 <form:hidden path="availability.region" />
                 <form:hidden path="availability.id" />
                 <form:hidden path="price.id" />
                 
                 <div class="control-group">
                        <label><s:message code="label.product.weight" text="Weight"/></label>
                        <div class="controls">
                                    <form:input cssClass="" path="product.productWeight"/>
                                    <span class="help-inline"><form:errors path="product.productWeight" cssClass="error" /></span>
                        </div>
                  </div>

                 <div class="control-group">
                        <label><s:message code="label.product.height" text="Height"/></label>
                        <div class="controls">
                                    <form:input cssClass="" path="product.productHeight"/>
                                    <span class="help-inline"><form:errors path="product.productHeight" cssClass="error" /></span>
                        </div>
                  </div>
     
     
                 <div class="control-group">
                        <label><s:message code="label.product.width" text="Width"/></label>
                        <div class="controls">
                                    <form:input cssClass="" path="product.productWidth"/>
                                    <span class="help-inline"><form:errors path="product.productWidth" cssClass="error" /></span>
                        </div>
                  </div>
                  
                  <div class="control-group">
                        <label><s:message code="label.product.length" text="Length"/></label>
                        <div class="controls">
                                    <form:input cssClass="" path="product.productLength"/>
                                    <span class="help-inline"><form:errors path="product.productLength" cssClass="error" /></span>
                        </div>
                  </div>          
                 

                  <!-- hidden when creating the product -->

                  <div class="control-group">
                        <label><s:message code="label.product.image" text="Image"/></label>
                        <div class="controls">
                        			
                        			<c:if test="${imageFileName==null}">
                        			
                        
                                    <input class="input-file" id="image" name="image" type="file">
                                    
                                    </c:if>
                        </div>
                  </div>
                  
                  <div class="control-group">
                        	<label><s:message code="label.taxclass" text="Tax class"/></label>
                          	<div class="controls">
                          		      <form:select items="${taxClasses}" itemValue="id" itemLabel="code"  path="product.taxClass.id"/> 
	                                  <span class="help-inline"></span>
                          	</div>
                   </div>


                   <div class="form-actions">
                            <div class="pull-right">
                                    <button type="submit" class="btn btn-success"><s:message code="button.label.submit2" text="Submit"/></button>
                            </div>
                   </div>

                   

                 

 

 

                                   

                        </form:form>
      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>