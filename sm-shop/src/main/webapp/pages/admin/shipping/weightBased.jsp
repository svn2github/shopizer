<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
				
<script>
	
function validateRegion() {
	$('#checkCodeStatus').html('<img src="<c:url value="/resources/img/ajax-loader.gif" />');
	$('#checkCodeStatus').show();
	var region = $("#region").val();
	var id = "";
	checkCode(region,id,'<c:url value="/admin/shipping/checkRegionCode.html" />');
}

function callBackCheckCode(msg,code) {
	
	if(code==0) {
		$('.btn-region').removeClass('disabled');
	}
	if(code==9999) {

		$('#checkCodeStatus').html('<font color="green"><s:message code="label.message.region.available" text="This region is available"/></font>');
		$('#checkCodeStatus').show();
		$('.btn-region').removeClass('disabled');
	}
	if(code==9998) {

		$('#checkCodeStatus').html('<font color="red"><s:message code="label.message.region.exist" text="This region already exist"/></font>');
		$('#checkCodeStatus').show();
		$('.btn-region').addClass('disabled');
	}
	
}
	
</script>


<div class="tabbable">

  					
  					<jsp:include page="/common/adminTabs.jsp" />


  					<div class="tab-content">

    					<div class="tab-pane active" id="shipping-section">
    					
    							<div class="sm-ui-component">
								<h3><s:message code="module.shipping.weightBased" text="module.shipping.weightBased" /></h3>	
								<br/>
								
								<form:errors path="*" cssClass="alert alert-error" element="div" />
								<div id="form.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
				

								<form action="<c:url value="/admin/shipping/addCustomRegion.html"/>">  
			      					<label class="required"><s:message code="label.shipping.addregion" text="Add region" /></label>
			      					<input type="text" class="span3" name="region" id="region" onblur="validatecode()"><!-- must be unique -->  
			      					<span class="help-inline"><div id="checkCodeStatus" style="display:none;"></span>  
			      					<br/>   
			      					<button type="submit" class="btn-region btn-action"><s:message code="button.label.submit2" text="Submit"/></button>
			      				</form>	
								<br/>
								
								<c:url var="addShipping" value="/admin/shipping/addCountryToRegion.html"/> 
		                  		<form:form method="POST" commandName="customConfiguration" action="${addShipping}">
				
		                  			 <div class="control-group">
		                        			<label><s:message code="label.region" text="Region"/></label>
		                        			<div class="controls">
		                        					<form:select path="customRegionName">
						  								<form:options items="${customConfiguration.regions}" itemValue="customRegionName" itemLabel="customRegionName"/>
					       							</form:select>
		                        			</div>
		                 			 </div>
		                  			 <div class="control-group">
		                        			<label><s:message code="label.country" text="Country"/></label>
		                        			<div class="controls">
		                        					
		                        					<form:select path="countries[0]">
						  								<form:options items="${shippingCountries}" itemValue="isoCode" itemLabel="name"/>
					       							</form:select>
		                        			</div>
		                 			 </div>
	                        		 <div class="form-actions">
                  						<div class="pull-right">
                  							<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
                  						</div>
            	 					</div>
			      				</form:form>	
			      				
			      				
								 <!-- Listing grid include -->
								 <c:set value="/admin/shipping/weightBased/page.html" var="pagingUrl" scope="request"/>
								 <c:set value="/admin/shipping/weightBased/remove.html" var="removeUrl" scope="request"/>
								 <c:set value="/admin/shipping/weightBased/edit.html" var="editUrl" scope="request"/>
								 <c:set value="/admin/shipping/weightBased.html" var="refreshUrl" scope="request"/>
								 <c:set var="entityId" value="region" scope="request"/>
								 <c:set var="groupByEntity" value="region" scope="request"/>
								 <c:set var="componentTitleKey" value="module.shipping.weightBased" scope="request"/>
								 <c:set var="gridHeader" value="/admin/shipping/weightBased-gridHeader.jsp" scope="request"/>
								 <c:set var="canRemoveEntry" value="true" scope="request"/>
				
				            	 <jsp:include page="/pages/admin/components/list.jsp"></jsp:include> 
								 <!-- End listing grid include -->
			      				
			      				

								<div class="sm-ui-component">

      							
			      			     <script>

///isc.showConsole();
      			     
      			     
// User Interface
// ---------------------------------------------------------------------

								


								
								isc.RestDataSource.create({ 
									ID:"regions", 
									dataFormat:"json", 
									dataURL: "<c:url value="/admin/shipping/regions.html" />",
									operationBindings:[ 
										{operationType:"fetch", dataProtocol:"postParams"} 
									]
								}); 
								
								
								isc.RestDataSource.create({ 
									ID:"countries", 
									dataFormat:"json", 
									dataURL: "<c:url value="/admin/shipping/countries.html" />",
									operationBindings:[ 
										{operationType:"fetch", dataProtocol:"postParams"} 
									]
								}); 
								
								
								isc.RestDataSource.create({ 
									ID:"prices", 
									dataFormat:"json", 
									dataURL: "<c:url value="/admin/shipping/prices.html" />",
									operationBindings:[ 
										{operationType:"fetch", dataProtocol:"postParams"},
										{operationType:"update", dataProtocol:"postParams"},
										{operationType:"remove", dataProtocol:"postParams"}  
									],
									transformResponse : function (dsResponse, dsRequest, jsonData) {
										var status = isc.XMLTools.selectObjects(jsonData, "/response/status");
										alert(status);
										alert(regionList.getSelectedRecord().region);
										alert(dsRequest.operationType);
										//if(dsRequest.operationType=='remove') {
											//alert('Fetch again');
											//priceList.fetchData({region:regionList.getSelectedRecord().region});
										//}
										
										//if (status != 0) {
											//alert("error");
											//dsResponse.status = isc.RPCResponse.STATUS_VALIDATION_ERROR;
											//var msg = isc.XMLTools.selectObjects(jsonData, "/response/msg");
											//alert(msg);
											//alert("errors " + errors);
											//dsResponse.errors = errors;
										//}
									}
								});
								
								
								isc.RestDataSource.create({ 
									ID:"delivery", 
									dataFormat:"json", 
									dataURL: "<c:url value="/admin/shipping/delivery.html" />",
									operationBindings:[ 
										{operationType:"fetch", dataProtocol:"postParams"} 
									]
								}); 
								
						  
							  
							  isc.ListGrid.create({
    								ID: "regionList",
    								//border:1,
    								dataSource: "regions",
    								showRecordComponents: true,    
    								showRecordComponentsByCell: true,
    								
    								autoFetchData: true,
    								showFilterEditor: false,
    								filterOnKeypress: false,
									dataFetchMode:"paged",
									canDragRecordsOut: false,
    								//dragDataAction: "copy",
    								//alternateRecordStyles: true,
									//canExpandRecords: true,
    								//expansionMode: "related",
    								//detailDS:"countries",
									canRemoveRecords: true,
									recordClick: "countriesList.fetchData({region:record.region}),priceList.fetchData({region:record.region}),deliveryList.fetchData({region:record.region}),addPriceButton.setDisabled(false)",
									

    						      	fields:[
        								{title:"Region", name:"region"},
        								//{title:"Country", name:"country"}
    								],
    								selectionType: "single"



								});
								
								isc.ListGrid.create({
    								ID: "countriesList",
    								//border:1,
    								dataSource: "countries",
    								showRecordComponents: true,    
    								showRecordComponentsByCell: true,
    								
    								autoFetchData: false,
    								showFilterEditor: false,
    								filterOnKeypress: false,
									canDragRecordsOut: false,
    								//dragDataAction: "copy",
    								alternateRecordStyles: true,
									canExpandRecords: false,
    								expansionMode: "related",
									canRemoveRecords: true,
									//recordClick: "priceList.fetchData({region:record.region})",

    						      	fields:[
        								{title:"Region", name:"region"},
        								{title:"Country", name:"country"}
    								],
    								selectionType: "single"

								});
								

								


								isc.ListGrid.create({
    									ID: "priceList",
    									height: 120,
    									dataSource: "prices",
    									canAcceptDroppedRecords: false,
    									canRemoveRecords: true,
    									alternateRecordStyles: true,    
    									autoFetchData: false,
    									leaveScrollbarGap: false,
    									autoFitMaxRecords: 5,
    									canEdit: true,
    									autoFitData: "vertical",
    									
    									//recordClick: "deliveryList.fetchData({region:record.region})",
    									fields: [
    									    {title:"Id", name: "id", hidden: true, canEdit:false },
    										{title:"Region", name: "region", canEdit:false},
        									{title:"Weight minimum", name: "min"},
        									{title:"Weight maximum", name: "max"},
        									{title:"Price", name: "price"}
    									],
    									removeData: function () {
											if (confirm('Do you want to DELETE this record?')) {
												return this.Super("removeData", arguments);
											}
											//alert(regionList.getSelectedRecord().region);
											//priceList.fetchData({region:regionList.getSelectedRecord().region});
											//return this.Super("fetchData", arguments);
											//this.Super("fetchData", arguments);
										}

										

								});
								
								isc.IButton.create({
    								ID: "addPriceButton",
    								autoDraw: false,
    								title:"Edit New",
    								disabled: true,
    								//function () {
    								// if(countriesList.size()==0) //alert
    								//}
    								click: function () {
    									if(countriesList.getTotalRows() < 6) {
    										alert("No rows");
    									} else {
    										priceList.startEditingNew();
    									}
    								}
    								
    								
    								
								});
								
								isc.ListGrid.create({
    									ID: "deliveryList",
    									//width: 340, height: 264,
    									height:60,
    									dataSource: delivery,
    									canAcceptDroppedRecords: false,
    									canRemoveRecords: false,
    									alternateRecordStyles: true,    
    									autoFetchData: false,
    									leaveScrollbarGap: false,
    									fields: [
    										{title:"Region", name: "region", hidden: "true"},
    										{title:"From", name: "from"},
        									{title:"To", name: "to"}
    									]
								});


// Define application layout
// ---------------------------------------------------------------------

isc.HLayout.create({
    ID:"pageLayout",
    width: "700",
    height: "600",
    position:"relative",
    members:[
        isc.SectionStack.create({
            ID:"leftSideLayout",
            width:300,
            showResizeBar:true,
            visibilityMode:"multiple",
            animateSections:true,
            sections:[
                {title:"Regions", autoShow:true, items:[regionList]}
            ]
        }),
        isc.SectionStack.create({
            ID:"rightSideLayout",
            //width:300,
            visibilityMode:"multiple",
            animateSections:true,
            sections:[

            	{title:"Countries", autoShow:true, items:[countriesList]},
                {title:"Prices", autoShow:true, items:[
                    isc.VStack.create({
    					membersMargin: 10,
    					members: [
        					priceList, addPriceButton
    					]
					})
                ]},
                {title:"Delivery to this region", autoShow:true, items:[deliveryList]}
            ]
        })
    ]
});



/*isc.HStack.create({
	membersMargin:10, 
	height:300,
	position:"relative", 
	members:[
    isc.VStack.create({
        members: [
            categoryTree
        ]
    }),
    isc.VStack.create({
        members: [
            categoryTree
        ]
    }),
    isc.Img.create({src:"icons/32/arrow_right.png", width:32, height:32, layoutAlign:"center",
        click:"projectList.transferSelectedData(employeesList)"
    }),
    isc.VStack.create({
        members: [
            isc.DynamicForm.create({
                width: 300, height: 30,
                fields: [
                    {
                        ID: "projectSelector",
                        name: "projectCode",
                        type: "select",
                        title: "Team for Project",
                        defaultValue: projects[0],
                        valueMap: projects,
                        changed: function(){
                            var crit = this.form.getValuesAsCriteria();
                            projectList.fetchData(crit);
                        }
                    }
                ]
            }),
            projectList
        ]
    })
]});*/





isc.Page.setEvent("load", "pageLayout.draw()");

      			     

								
								
												</script>		
      							


      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>