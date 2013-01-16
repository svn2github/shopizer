<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
				
<script>
	

	
</script>


<div class="tabbable">

  					
  					<c:if test="${currentMenu!=null && fn:length(currentMenu.menus)>0}">
						
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

    					<div class="tab-pane active" id="shipping-section">

								<form action="<c:url value="/admin/shipping/addRegion.html"/>">  
			      					<label class="required">Add region</label>  
			      					<input type="text" class="span3" name="region" id="region"><!-- must be unique -->  
			      					<span class="help-block">Create a region that will contain one or many countries with shipping rules</span>  
			      					<br/>   
			      					<button type="submit" class="btn btn-action"><s:message code="button.label.submit2" text="Submit"/></button>
			      				</form>	
								<br/>
								
								<form action="<c:url value="/admin/shipping/addCountry.html"/>">  


	                  			<div class="control-group">
	                        			<label><s:message code="label.region" text="Region"/></label>
	                        			<div class="controls">
	                        					
	                        					<form:select path="regions">
					  								<form:option value="-1" label="--- Select ---" />
					  								<form:options items="${regions}"/>
				       							</form:select>
	                        			</div>
	                 			 </div>

	                  			<div class="control-group">
	                        			<label><s:message code="label.country" text="Country"/></label>
	                        			<div class="controls">
	                        					
	                        					<form:select path="countries">
					  								<form:option value="-1" label="--- Select ---" />
					  								<form:options items="${countries}" itemValue="id" itemLabel="name"/>
				       							</form:select>
	                                   			<span class="help-inline"><form:errors path="country" cssClass="error" /></span>
	                        			</div>
	                 			 </div>
	                 			 
	                 			 <!--
				
				      			<div class="form-actions">
	                  					<div class="pull-right">
	                  						<button type="submit" class="btn btn-action"><s:message code="button.label.submit2" text="Submit"/></button>
	                  					</div>
	            	 			</div>


			      				-->
			      				<button type="submit" class="btn btn-action"><s:message code="button.label.submit2" text="Submit"/></button>
			      				
			      				</form>	

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