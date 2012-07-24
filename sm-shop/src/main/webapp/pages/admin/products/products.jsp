<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>				
				



<div class="tabbable">

  					
					<jsp:include page="/common/adminTabs.jsp" />



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
    								border:0,
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
    								border:0,
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
								
								
								isc.SearchForm.create({
    								ID:"findForm",
    								/**dataSource:"supplyItem",**/
    								left:25,
    								border:0,
    								top:10,
    								cellPadding:4,
    								numCols:6,
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
    									left:250,
    									top:16,
    									width:80,
    									click:"findForm.findItems()",
    									/**icon:"demoApp/icon_find.png",**/
    									iconWidth:24
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
                        border:0,
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