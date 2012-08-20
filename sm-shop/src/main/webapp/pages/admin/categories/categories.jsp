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
								<h3><s:message code="label.categories.title" text="Categories" /></h3>	
								<br/>

			      			     <script>
			      			     
							


								
								isc.RestDataSource.create({ 
									ID:"categories", 
									dataFormat:"json",  
									operationBindings:[ 
										{operationType:"fetch", dataProtocol:"postParams",dataURL: "<c:url value="/admin/categories/paging.html" />"},
										{operationType:"remove", dataProtocol:"postParams",dataURL: "<c:url value="/admin/categories/remove.html" />"},
									],
									transformResponse : function (dsResponse, dsRequest, jsonData) {
										var status = isc.XMLTools.selectObjects(jsonData, "/response/status");
										if (status != 0) {
											if(status==9999) {//operation completed
												//reload 
												window.location='<c:url value="/admin/categories/categories.html" />';
											}

											var msg = isc.XMLTools.selectObjects(jsonData, "/response/statusMessage");

												alert("! " + msg);

										}
									}
								}); 
								

							  
							  isc.ListGrid.create({
    								ID: "categoriesList",
    								border:1,
    								dataSource: "categories",
    								showRecordComponents: true,    
    								showRecordComponentsByCell: true,
    								canRemoveRecords: true,
    								autoFetchData: true,
    								showFilterEditor: true,
    								filterOnKeypress: true,
									dataFetchMode:"paged",


    						      fields:[
										{title:"<s:message code="label.entity.id" text="Id"/>", name:"categoryId", canFilter:false},
        								{title:"<s:message code="label.entity.name" text="Name"/>", name:"name"},
        								{title:"<s:message code="label.entity.visible" text="Visible"/>", name:"visible", canFilter:false},
        								{title:"<s:message code="label.entity.details" text="Details"/>", name: "buttonField", align: "center",canFilter:false,canSort:false, canReorder:false}

    							],
    							selectionType: "single",
								removeData: function () {
									if (confirm('<s:message code="label.entity.remove.confirm" text="Do you really want to remove this record ?" />')) {
										return this.Super("removeData", arguments);
									}
								},
								fetchData: function () {
									return this.Super("fetchData", arguments);
								},
    							createRecordComponent : function (record, colNum) {  
        
        							var fieldName = this.getFieldName(colNum);
        							if (fieldName == "buttonField") {  

	        						
	           						var button = isc.IButton.create({
	                						height: 18,
	                						width: 65,
	               					 		title: "<s:message code="label.entity.details" text="Details"/>",
	                						click : function () {
	                							window.location='<c:url value="/admin/categories/editCategory.html" />?id=' + record["categoryId"];
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
    width: "680",
    height: "600",
    position:"relative",
    members:[
        isc.SectionStack.create({
            ID:"mainLayout",
            visibilityMode:"multiple",
            animateSections:true,
            sections:[
                {title:"<s:message code="label.categories.title" text="Categories"/>", autoShow:true, items:[categoriesList]}
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