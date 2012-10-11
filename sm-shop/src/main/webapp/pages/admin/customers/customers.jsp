<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ page session="false"%>




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
									operationBindings:[ 
										{operationType:"fetch", dataProtocol:"postParams",dataURL: "<c:url value="/admin/customers/page.html" />"} 
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
	               					 		title: "<s:message code="label.entity.details" text="Details"/>",
	               					 	click : function () {
                							window.location='<c:url value="/admin/customers/customer.html" />?id=' + record["id"];
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
            ID:"rightSideLayout",
            visibilityMode:"multiple",
            animateSections:true,
            sections:[
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