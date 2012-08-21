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
								
								
				<h3>
						<s:message code="label.product.productoptions.title" text="Option management" />
				</h3>	
				<br/>

				<c:url var="optionSave" value="/admin/options/save.html"/>


				<form:form method="POST" commandName="option" action="${optionSave}">

      							
      				<form:errors path="*" cssClass="alert alert-error" element="div" />
					<div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>    
								
                 <c:forEach items="${option.descriptions}" var="description" varStatus="counter">
                  
                 <div class="control-group">
                        <label class="required"><s:message code="label.product.productoptions.name" text="Option name"/> (<c:out value="${description.language.code}"/>)</label>
                        <div class="controls">
                        			<form:input id="name${counter.index}" path="descriptions[${counter.index}].name"/>
                        			<span class="help-inline"><form:errors path="descriptions[${counter.index}].name" cssClass="error" /></span>
                        </div>

                  </div>

                  
                  <form:hidden path="descriptions[${counter.index}].language.code" />
                  <form:hidden path="descriptions[${counter.index}].id" />
                  
                  </c:forEach>

      			  
      			 <div class="control-group">
                        <label><s:message code="label.product.productoptions.type" text="Option type"/></label>
                        <div class="controls">
                                   
	                        <div class="controls">			
	                        		<form:select path="productOptionType">
	                        			<s:message code="label.product.productoption.type.text" text="Text" var="vText" />
	                        			<s:message code="label.product.productoption.type.select" text="Select" var="vSelect"/>
	                        			<s:message code="label.product.productoption.type.radio" text="Radio" var="vRadio"/>
	                        			<s:message code="label.product.productoption.type.checkbox" text="Checkbox" var="vCheckbox"/>
	                        			<form:option value="text" label="${vText}" />
	                        			<form:option value="select" label="${vSelect}" />
	                        			<form:option value="radio" label="${vRadio}" />
	                        			<form:option value="checkbox" label="${vCheckbox}" />
				       				</form:select>
	                                <span class="help-inline"><form:errors path="productOptionType" cssClass="error" /></span>
	                        </div>

                        </div>
                  </div>
      			  
                  
                  <form:hidden path="id" />
			
			      <div class="form-actions">

                  		<div class="pull-right">

                  			<button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
                  			

                  		</div>

            	 </div>
 
            	 </form:form>
            	 
            	 
            	 <br/>
            	 


			      			     <script>
			      			     
							


								
								isc.RestDataSource.create({ 
									ID:"options", 
									dataFormat:"json",  
									operationBindings:[ 
										{operationType:"fetch", dataProtocol:"postParams",dataURL: "<c:url value="/admin/options/paging.html" />"},
										{operationType:"remove", dataProtocol:"postParams",dataURL: "<c:url value="/admin/options/remove.html" />"},
									],
									transformResponse : function (dsResponse, dsRequest, jsonData) {
										var status = isc.XMLTools.selectObjects(jsonData, "/response/status");
										if (status != 0) {
											if(status==9999) {//operation completed
												//reload 
												window.location='<c:url value="/admin/options/options.html" />';
											}

											var msg = isc.XMLTools.selectObjects(jsonData, "/response/statusMessage");

												alert("! " + msg);

										}
									}
								}); 
								

							  
							  isc.ListGrid.create({
    								ID: "optionsList",
    								border:1,
    								dataSource: "options",
    								showRecordComponents: true,    
    								showRecordComponentsByCell: true,
    								canRemoveRecords: true,
    								autoFetchData: true,
    								showFilterEditor: true,
    								filterOnKeypress: true,
									dataFetchMode:"paged",


    						      fields:[
										{title:"<s:message code="label.entity.id" text="Id"/>", name:"optionId", canFilter:false},
        								{title:"<s:message code="label.entity.name" text="Name"/>", name:"name"},
        								{title:"<s:message code="label.entity.type" text="Type"/>", name:"type", canFilter:false},
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
	                							//TODO fill details
	                							window.location='<c:url value="/admin/categories/editCategory.html" />?id=' + record["optionId"];
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
                {title:"<s:message code="label.product.productoptions.list" text="Options list"/>", autoShow:true, items:[optionsList]}
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