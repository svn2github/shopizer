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
								<h3><s:message code="label.categories.hierarchy.title" text="Category hierarchy" /></h3>	
								<br/>
								<div class="well">
									<s:message code="label.category.hierarchy.text" text="Drag categories to re-organize the hierarchy" />
								</div>
								<br/>
      							
			      			     <script>
			      			     
			      			     

///isc.showConsole();
      			     
      			     
// User Interface
// ---------------------------------------------------------------------

/* 								isc.RestDataSource.create({ 
									ID:"categories", 
									dataFormat:"json", 
									dataURL: "<c:url value="/admin/products/paging.html" />",
									data: isc.Tree.create({
        									modelType: "parent",
        									nameProperty: "Name",
        									idField: "categoryId",
        									parentIdField: "parentId",
        									data: [
            									{categoryId:"4", parentId:"1", Name:"Books", isFolder: true},
            									{categoryId:"188", parentId:"4", Name:"Novell", isFolder: true},
            									{categoryId:"189", parentId:"4", Name:"Technology", isFolder: true},
            									{categoryId:"265", parentId:"188", Name:"Romance", isFolder: true},
            									{categoryId:"267", parentId:"188", Name:"Test1", isFolder: true},
            									{categoryId:"264", parentId:"188", Name:"Fiction", isFolder: true}
        									]
    									}),
									operationBindings:[ 
										{operationType:"update", dataProtocol:"postParams"} 
									]
								});  */


 
								
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
										{categoryId:"-1", parentId:"0", Name:"<s:message code="label.category.root" text="Root" />", isFolder: true},
										<c:forEach items="${categories}" var="category" varStatus="counter">
            							{categoryId:'<c:out value="${category.id}" />', parentId:'<c:choose><c:when test="${category.parent!=null}"><c:out value="${category.parent.id}" /></c:when><c:otherwise>-1</c:otherwise></c:choose>', Name:'<c:out value="${category.descriptions[0].name}" />', isFolder: true},

            							</c:forEach>
        								]
    								}),


    								//nodeClick:"itemList.fetchData({categoryId:node.categoryId})",
    								showHeader:false,
    								leaveScrollbarGap:false,
    								animateFolders:true,
    								canReorderRecords: true,
									canAcceptDroppedRecords: true,
    								canReparentNodes:true,
    								selectionType:"single",
    								animateRowsMaxTime:750,
									folderDrop: function (dragRecords, dropFolder, index, sourceWidget) {
										var record=categoryTree.getSelectedRecord();
										//alert(record.SysId + " will now go to " + dropFolder.SysId);
										var newUnit=dropFolder.SysId;
										var newRecord=record;
										newRecord.ReportsTo=newUnit;
										categoryTree.removeData(record);
										categoryTree.data.addList([newRecord],dropFolder, index);
										//alert(index);
										alert(dropFolder.categoryId);
										alert(newRecord.categoryId);
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
                     {title:"<s:message code="label.categories.hierarchy.title" text="Category hierarchy"/>", autoShow:true, items:[categoryTree]}
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
