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
									ID:"customers", 
									dataFormat:"json", 
									dataURL: "<c:url value="/admin/customers/page.html" />",
									operationBindings:[ 
										{operationType:"fetch", dataProtocol:"postParams"} 
									]
								}); 

							  
							  
							  isc.ListGrid.create({
    								ID: "customersList",
    								border:1,
    								dataSource: "customers",
    								showRecordComponents: true,    
    								showRecordComponentsByCell: true,
    								
    								autoFetchData: true,
    								showFilterEditor: true,
    								filterOnKeypress: true,
									dataFetchMode:"paged",


    						      fields:[
        								{title:"ID", name:"id",canFilter:false},
        								{title:"Name", name:"name",canFilter:true},
        								{title:"Country", name:"country",canFilter:true},
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
    								left:25,
    								top:10,
    								cellPadding:4,
    								numCols:6,
    								fields:[
        								{name:"Name"}
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
            ID:"rightSideLayout",
            visibilityMode:"multiple",
            animateSections:true,
            sections:[
                {title:"Find customer", autoShow:true, items:[
                    isc.Canvas.create({
                        ID:"findPane",
                        height:60,
                        overflow:"auto",
                        styleName:"defaultBorder",
                        children:[findForm,findButton]
                    })                
                ]},
                {title:"Customers", autoShow:true, items:[customersList]}
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