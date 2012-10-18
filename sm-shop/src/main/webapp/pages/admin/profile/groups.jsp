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
								<h3>Groups <s:message code="label.groups.title" text="Groups" /></h3>	
								<br/>

			      			     <script>
			      			     
			      			// User Interface
			      			// ---------------------------------------------------------------------

			      											


			      											
			      											isc.RestDataSource.create({ 
			      												ID:"permissions", 
			      												dataFormat:"json", 
			      												operationBindings:[ 
			      													{operationType:"fetch", dataProtocol:"postParams",dataURL: "<c:url value="/admin/groups/paging.html" />"},
			      													{operationType:"remove", dataProtocol:"postParams",dataURL: "<c:url value="/admin/permissions/removePermission.html" />"}
			      												],
			      												transformResponse : function (dsResponse, dsRequest, jsonData) {
			      													var status = isc.XMLTools.selectObjects(jsonData, "/response/status");

			      													if (status != 0 && status !=9999) {

			      														var msg = isc.XMLTools.selectObjects(jsonData, "/response/statusMessage");

			      															alert("! " + msg);

			      													}
			      													
			      													
			      													if(status == 9999) {
			      														
			      														window.location="<c:url value="/admin/groups/groups.html" />";
			      													}
			      												}
			      											}); 
			      											

			      											//iterate from category objects to display data
			      			      							isc.TreeGrid.create({
			      			    								ID:"groupTree",
			      			    								border:0,
			      			    								showResizeBar: false,
			      			    								data: isc.Tree.create({
			      			        								modelType: "parent",
			      			        								nameProperty: "Name",
			      			        								idField: "groupId",
			      			        								parentIdField: "parentId",
			      			        								data: [
			      													{groupId:"-1", parentId:"0", Name:"<s:message code="label.group.root" text="Root" />", isFolder: true},
			      													<c:forEach items="${groups}" var="group" varStatus="status">
			      			            								{groupId:'<c:out value="${group.id}" />', parentId:'-1', Name:'<c:out value="${group.groupName}" />', isFolder: true}
			      			            								<c:if test="${status.count<fn:length(groups)}">,</c:if>
			      			            							</c:forEach>
			      			        								]
			      			    								}),


			      			    								nodeClick:"itemList.fetchData({groupId:node.groupId})",
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
			      			    								dataSource: "permissions",
			      			    								showRecordComponents: true,    
			      			    								showRecordComponentsByCell: true,
			      			    								canRemoveRecords: true,
			      			    								autoFetchData: false,
			      			    								showFilterEditor: true,
			      			    								filterOnKeypress: true,
			      												dataFetchMode:"paged",


			      			    						      fields:[
			      			    						              
			      			    						              
			      													{title:"<s:message code="label.entity.id" text="Id"/>", name:"permissionId", canFilter:false},
			      													{title:"<s:message code="label.entity.name" text="Name"/>", name:"name"},
			      													{title:"<s:message code="label.group.groupId" text="GroupId"/>", name:"groupId",showIf:"false"},
			      											//		{title:"<s:message code="label.product.available" text="Available"/>", name:"available",type:"boolean"},
			      													//{title:"<s:message code="label.quantity" text="Quantity"/>", name:"quantity", canFilter:false},
			      													{title:"<s:message code="label.entity.details" text="Details"/>", name: "buttonField", align: "center",canFilter:false,canSort:false, canReorder:false}  


			      			    							   ],
			      				       						   selectionType: "single",
			      											   removeData: function () {
			      													if (confirm('<s:message code="label.entity.remove.confirm" text="Do you really want to remove this record ?" />')) {
			      														return this.Super("removeData", arguments);
			      													}
			      											   },
			      			    							   createRecordComponent : function (record, colNum) {  
			      			        
			      			        							var fieldName = this.getFieldName(colNum);
			      			        							if (fieldName == "buttonField") {  

			      				        						
			      			    	           						var button = isc.IButton.create({
			      				                						height: 18,
			      				                						width: 65,
			      				               					 		title: "<s:message code="label.entity.details" text="Details"/>",
			      				                						click : function () {
			      				                							window.location='<c:url value="/admin/products/editProduct.html" />?id=' + record["productId"];
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
			      			                {title:"Groups", autoShow:true, items:[groupTree]}
			      			            ]
			      			        }),
			      			        isc.SectionStack.create({
			      			            ID:"rightSideLayout",
			      			            visibilityMode:"multiple",
			      			            animateSections:true,
			      			            sections:[
			      			                {title:"Items", autoShow:true, items:[itemList]}
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