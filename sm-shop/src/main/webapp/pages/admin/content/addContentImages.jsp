<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ page session="false"%>

<div class="tabbable">


	<jsp:include page="/common/adminTabs.jsp" />

	<div class="tab-content">

		<div class="tab-pane active" id="catalogue-section">
		
				<c:url var="saveContentImages" value="/admin/content/saveContentImages.html" />
				<form:form method="POST" enctype="multipart/form-data" commandName="contentImages" action="${saveContentImages}">

					<form:errors path="*" cssClass="alert alert-error" element="div" />
					<div id="store.success" class="alert alert-success"
						style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">
						<s:message code="message.success" text="Request successfull" />
					</div>
					<div class="control-group">
						<label><s:message code="label.contentImages.store"
								text="Merchant Store" /></label>
						<div class="controls">
							<%-- <form:select items="${manufacturers}" itemValue="id"
								itemLabel="descriptions[0].name" path="product.manufacturer.id" /> --%>
								<input type="hidden" name="contentImages.merchantStoreId" value="1"/>
							<span class="help-inline"></span>
						</div>
					</div>
				
					<!-- hidden when creating the product -->
					<div class="control-group">
						<label><s:message code="label.product.image" text="Image" /></label>
						<div class="controls">
							
								<input class="input-file" id="image" name="image[0]" type="file"><br/>
								<input class="input-file" id="image1" name="image[1]" type="file"><br/>
								<input class="input-file" id="image2" name="image[2]" type="file"><br/>
								<input class="input-file" id="image3" name="image[3]" type="file"><br/>
						
						</div>
					</div>
					<div class="form-actions">
						<div class="pull-right">
							<button type="submit" class="btn btn-success">
								<s:message code="button.label.submit2" text="Submit" />
							</button>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>