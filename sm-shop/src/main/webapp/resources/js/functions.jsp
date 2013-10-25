<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>

<script>

function getContextPath() {
   return "${pageContext.request.contextPath}";
}

function getMerchantStore() {
   return "${requestScope.MERCHANT_STORE.id}";
}

function getMerchantStoreCode() {
   return "${requestScope.MERCHANT_STORE.code}";
}

function getLanguageCode() {
   return "${requestScope.LANGUAGE.code}";
}



 
function loadProducts(url,divProductsContainer) {
		$(divProductsContainer).showLoading();
		
		//var url = '<%=request.getContextPath()%>/shop/services/products/page/' + START_COUNT_PRODUCTS + '/' + MAX_PRODUCTS + '/<c:out value="${requestScope.MERCHANT_STORE.code}"/>/<c:out value="${requestScope.LANGUAGE.code}"/>/<c:out value="${category.friendlyUrl}"/>.html';
		
		$.ajax({
				type: 'POST',
				dataType: "json",
				url: url,
				success: function(productList) {

					for (var i = 0; i < productList.products.length; i++) {
					    var productHtml = '<li class="item span3">';
					    	productHtml = productHtml + '<div class="product-box"><a href="<c:url value="/shop/product/" />' + productList.products[i].friendlyUrl + '.html">';
					    	productHtml = productHtml + '<h4 class="name">' + productList.products[i].name +'</h4></a>';
					    	if(productList.products[i].discounted) {
					    		productHtml = productHtml + '<h3 class="number"><del>' + productList.products[i].originalProductPrice +'</del>&nbsp;<span class="specialPrice">' + productList.products[i].productPrice + '</span></h3>';
					    	} else {
					    		productHtml = productHtml + '<h3 class="number">' + productList.products[i].productPrice +'</h3>';
					    	}
					    	if(productList.products[i].imageUrl!=null) {
					    		productHtml = productHtml + '<a href="<c:url value="/shop/product/" />' + productList.products[i].friendlyUrl + '.html"><img src="<c:url value="/"/>' + productList.products[i].imageUrl +'"></a>';
					    	}
					    	productHtml = productHtml + '<div class="bottom"><a href="<c:url value="/shop/product/" />' + productList.products[i].friendlyUrl + '.html" class="view"><s:message code="button.label.view" text="View" /></a> / <a productid="' + productList.products[i].id + '" href="#" class="addToCart"><s:message code="button.label.addToCart" text="Add to cart" /></a></div></div></li>';
					    	//displayProducts[i] = productHtml;
					    	$(divProductsContainer).append(productHtml);

					}
					
					callBackLoadProducts(productList.totalCount);

					//alert('start ' + START_COUNT_PRODUCTS + ' count ' + productList.totalCount);
					//start_count = start_count + max_product;
					//if(start_count < productList.totalCount) {
					//	$("#button_nav").show();
					//} else {
					//	$("#button_nav").hide();
					//}

					//$('#productsContainer').hideLoading();

				},
				error: function(jqXHR,textStatus,errorThrown) { 
					$(divProductsContainer).hideLoading();
					alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
				}
				
		});
		
		
		
	}

</script>