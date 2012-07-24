<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
				
<script>
	

	
</script>


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

    					<div class="tab-pane active" id="catalogue-section">

								
								


								<div class="sm-ui-component">

      							
			      			     <script>

///isc.showConsole();
      			     
      			     
// User Interface
// ---------------------------------------------------------------------

								


								
								isc.RestDataSource.create({ 
									ID:"products", 
									dataFormat:"json", 
									dataURL: "<c:url value="/admin/products/paging.html" />",
									data : {
										criteria: [
											{fieldName: "categoryId", operator: "equals", value: "12345"}
										]
									}, 
									operationBindings:[ 
										{operationType:"fetch", dataProtocol:"postParams"} 
									]
								}); 
								

								


								
								
								//iterate from category objects to display data
      							isc.TreeGrid.create({
    								ID:"categoryTree",
    								//border:1,
    								showResizeBar: false,
    								data: isc.Tree.create({
        								modelType: "parent",
        								nameProperty: "Name",
        								idField: "categoryId",
        								parentIdField: "parentId",
        								data: [
            							{categoryId:"4", parentId:"1", Name:"Books"},
            							{categoryId:"188", parentId:"4", Name:"Novell"},
            							{categoryId:"189", parentId:"4", Name:"Technology"},
            							{categoryId:"265", parentId:"188", Name:"Romance"},
            							{categoryId:"267", parentId:"188", Name:"Test1"},
            							{categoryId:"264", parentId:"188", Name:"Fiction"}
        								]
    								}),


    								nodeClick:"itemList.fetchData({categoryId:node.categoryId})",
    								showHeader:false,
    								leaveScrollbarGap:false,
    								animateFolders:true,
    								canAcceptDroppedRecords:true,
    								canReparentNodes:false,
    								selectionType:"single",
    								animateRowsMaxTime:750
							  });
							  
							  
							  isc.ListGrid.create({
    								ID: "itemList",
    								//border:1,
    								dataSource: "products",
    								showRecordComponents: true,    
    								showRecordComponentsByCell: true,
    								
    								autoFetchData: false,
    								showFilterEditor: true,
    								filterOnKeypress: true,
									dataFetchMode:"paged",
									canDragRecordsOut: true,
    								dragDataAction: "copy",
    								alternateRecordStyles: true,
									


    						      fields:[
        								{title:"Name", name:"name"},
        								{title:"SKU", name:"sku"},
        								{title:"Info", name: "buttonField", align: "center",canFilter:false}
    							],
    							selectionType: "single",
    							createRecordComponent : function (record, colNum) {  
        
        							var fieldName = this.getFieldName(colNum);
        							if (fieldName == "buttonField") {  

	        						
	           						var button = isc.IButton.create({
	                						height: 18,
	                						width: 65,
	               					 		title: "Info",
	                						click : function () {
	                    					isc.say(record["name"] + " info button clicked.");
	                						}
	            					});
	            					return button;  
            				
            					}
 
    						  }


								});
								
								
								isc.SearchForm.create({
    								ID:"findForm",
    								/**dataSource:"supplyItem",**/
    								left:5,
    								top:10,
    								cellPadding:2,
    								numCols:4,
    								fields:[
        								{name:"Product"}
        								//{name:"itemName", editorType:"comboBox", optionDataSource:"supplyItem", 
        								//		pickListWidth:250},
        								//{name:"findInCategory", editorType:"checkbox", 
            						//		title:"Use category", defaultValue:true, shouldSaveValue:false}
    								],
    
    								// Function to actually find items
    								findItems : function () {

    								
    									itemList.fetchData({searchTerm:this.getValues()})
    								
        								/**
        								var findValues;
								        if (this.getValue('findInCategory') && categoryTree.selection.anySelected()) {
								            // use tree category and form values
								            if (categoryName == null) categoryName = categoryTree.getSelectedRecord().categoryName;
								            findValues = {category:categoryName};
								            isc.addProperties(findValues, this.getValues());
								            
								        } else if (categoryName == null) {
								            // use form values only
								            findValues = this.getValues();
								            
								        } else {
								            // use tree category only
								            findValues = {category:categoryName};
								        }
								        
								        itemList.filterData(findValues);
								        
								        itemDetailTabs.clearDetails();
								        **/
								    }
									});
									
									
									isc.IButton.create({
    									ID:"findButton",
    									title:"Find",
    									left:225,
    									top:14,
    									width:48,
    									click:"findForm.findItems()",
    									/**icon:"demoApp/icon_find.png",**/
    									iconWidth:24
									});


									isc.TileGrid.create({
									    ID:"bundlesList",
									    tileWidth:160,
									    tileHeight:120,
									    //width: "100%",
									    //height:"100%",
									    //dataSource:"animals",
									    //autoFetchData:true,
									    animateTileChange:true,
									    canAcceptDroppedRecords: true,
									    canReorderTiles: true,
									    data: [
									    	{
									    		 sku:"789", 
           										 name:"Arabian Camel"
									    		
									    	}
									    ],
									    fields: [
									        {name:"sku"},
									        {name:"name"}
									    ],
									
									    getTile : function (record) {
									        // override getTile() and add a "Remove" button 
									        var canvas = this.Super("getTile", arguments);
									        canvas.addChild(this.getRemoveButton(this.getRecord(record)));
									        return canvas;
									    },
									    
									    getRemoveButton : function (record) {
									        var removeButton = isc.ImgButton.create({
									            src: "[SKINIMG]/Tab/left/close.png",
									            showHover: true,
									            prompt: "Remove product",
									            size: 15,
									            showFocused: false,
									            showRollOver: false,
									            snapTo: "TR",
									            showDown: false,
									            margin: 2,
									            tileGrid: this,
									            record: record,
									            click : function () {
									                //animals.removeData(this.record);
									            }
									        });
									
									        return removeButton;
									    }
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
            width:200,
            showResizeBar:true,
            visibilityMode:"multiple",
            animateSections:true,
            sections:[
                {title:"Categories", autoShow:true, items:[categoryTree]}
                /**{title:"Instructions", autoShow:true, items:[helpCanvas]}**/
            ]
        }),
        isc.SectionStack.create({
            ID:"middleSideLayout",
            width:280,
            visibilityMode:"multiple",
            animateSections:true,
            sections:[
                {title:"Find Items", autoShow:true, items:[
                    isc.Canvas.create({
                        ID:"findPane",
                        height:60,
                        overflow:"auto",
                        styleName:"defaultBorder",
                        children:[findForm,findButton]
                    })                
                ]},
                {title:"Items", autoShow:true, items:[itemList]}
                /**{title:"Item Details", autoShow:true, items:[itemDetailTabs]}**/
            ]
        }),
        isc.Img.create({src:"icons/32/arrow_right.png", width:32, height:32, layoutAlign:"center",
        	click:"Items.transferSelectedData(relatedList)"
    	}),
    	isc.SectionStack.create({
            ID:"rightSideLayout",
            width:180,
            showResizeBar:false,
            visibilityMode:"multiple",
            animateSections:true,
            sections:[
                {title:"Bundle", autoShow:true, items:[bundlesList]}
            ]
        }),
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