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
    								border:1,
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
    								border:1,
    								dataSource: "products",
    								showRecordComponents: true,    
    								showRecordComponentsByCell: true,
    								
    								autoFetchData: false,
    								showFilterEditor: true,
    								filterOnKeypress: true,
									dataFetchMode:"paged",


    						      fields:[
        								{title:"Name", name:"name"},
        								{title:"SKU", name:"sku"},
        								{title:"Cost", name:"cost",canFilter:false},
        								{title:"units", name:"units",canFilter:false},
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
            ID:"rightSideLayout",
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
        })
    ]
});

isc.Page.setEvent("load", "pageLayout.draw()");

      			     

								
								
												</script>		
      							


      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>