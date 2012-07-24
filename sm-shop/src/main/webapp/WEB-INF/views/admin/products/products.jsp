<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Admin</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    
    


    <!-- Le styles -->
    
    <link href="<c:url value="/resources/css/sm-bootstrap.css" />" rel="stylesheet">
    <style type="text/css">


	html {
  		font-size: 100%;
  		-webkit-text-size-adjust: 100%;
  		-ms-text-size-adjust: 100%;
	}

	body {
  		margin: 0;
  		font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
  		font-size: 13px;
  		/**line-height: 18px;**/
  		color: #333333;
  		background-color: #ffffff;
	}



	.table tbody tr.subt:hover td, 
	.table tbody tr.subt:hover th { 
		background-color: transparent; 
	} 
	
	
	.sm-ui-component label {


    color: #333333;
    margin-bottom: 0;
    display: inline;

   }
   
  .sm-ui-component IMG {


    max-width:none;


   }

    </style>
    <link href="<c:url value="/resources/css/sm-bootstrap-responsive.css" />" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/panel-style.css" />" /> 
 


    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <!--<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>-->
    <![endif]-->




    <script>var isomorphicDir="<c:url value="/resources/smart-client/" />";</script>
    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_Core.js" />"></script>
    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_Foundation.js" />"></script>
    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_Containers.js" />"></script>
    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_Grids.js" />"></script>
    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_Forms.js" />"></script>
    <script SRC="<c:url value="/resources/smart-client/system/modules/ISC_DataBinding.js" />"></script>
    <script SRC="<c:url value="/resources/smart-client/skins/Graphite/load_skin.js" />"></script>


  </head>

  <body>

<p>&nbsp;</p>


		  


<div class="sm">

	<div class="container"> 

		<div class="row">

			<div class="span3">


				<ul class="nav nav-list">
					  <c:forEach items="${menus}" var="menu">
					  			<li <c:if test="${activeMenus[menu.code]!=null}"> class="active"</c:if>>
									<a href="<c:url value="${menu.url}" />">
										<i class="${menu.icon}"></i>
											<s:message code="menu.${menu.code}" text="${menu.code}"/>
									</a>
					  			</li>
					  </c:forEach>
				</ul>



			</div>

			<div class="span9">
			
			
			
			
			



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

    					<div class="tab-pane active" id="products">

								
								


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
								
								
								isc.SearchForm.create({
    								ID:"findForm",
    								/**dataSource:"supplyItem",**/
    								left:25,
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

    					<div class="tab-pane" id="categories">

      					<p>Howdy, I'm in Section 2.</p>

    					</div>

    					<div class="tab-pane" id="options">

      					<p>Howdy, I'm in Section 3.</p>

    					</div>

  					</div>

				</div>



			</div>


		</div>



  
		<hr> 
  
  
		<footer> 
 			<p>&copy; Company 2012</p> 
		</footer> 
  
  
	</div> <!-- /container --> 
	
								   <script>
							  
		
									</script>
  
</div>

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="js/jquery.js"></script>
    <script type="text/javascript" src="js/slide.js"></script> 
    <script src="js/bootstrap-transition.js"></script>
    <script src="js/bootstrap-alert.js"></script>
    <script src="js/bootstrap-modal.js"></script>
    <script src="js/bootstrap-dropdown.js"></script>
    <script src="js/bootstrap-scrollspy.js"></script>
    <script src="js/bootstrap-tab.js"></script>
    <script src="js/bootstrap-tooltip.js"></script>
    <script src="js/bootstrap-popover.js"></script>
    <script src="js/bootstrap-button.js"></script>
    <script src="js/bootstrap-collapse.js"></script>
    <script src="js/bootstrap-carousel.js"></script>
    <script src="js/bootstrap-typeahead.js"></script>

  </body>
</html>
 