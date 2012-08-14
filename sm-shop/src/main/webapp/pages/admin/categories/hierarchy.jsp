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
								}); 


 
								
								//iterate from category objects to display data
      							isc.TreeGrid.create({
    								ID:"categoryTree",
    								border:1,
    								showResizeBar: false,
								//dataSource:"categories",
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
									alert(index);
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
    members:[categoryTree]
});

isc.Page.setEvent("load", "pageLayout.draw()");
			      			     
			      			     </script>
			      			     
			      			     
			      			     
      					</div>
      					

      			     
      			     


      			     
      			     
    


   					</div>


  					</div>

				</div>	      			     
