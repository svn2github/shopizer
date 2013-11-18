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

		$.ajax({
				type: 'POST',
				dataType: "json",
				url: url,
				success: function(productList) {

					buildProductsList(productList,divProductsContainer);
					callBackLoadProducts(productList);


				},
				error: function(jqXHR,textStatus,errorThrown) { 
					$(divProductsContainer).hideLoading();
					alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
				}
				
		});
		
		
		
}


function searchProducts(url,divProductsContainer,q,filter) {
		
		$(divProductsContainer).showLoading();
		
		if(q==null || q=='') {
			return;
		}
	
	    //category facets
		var facets = '\"facets\" : { \"categories\" : { \"terms\" : {\"field\" : \"categories\"}}}';
	    var highlights = null;
		var queryStart = '{';

		var query = '\"query\":{\"query_string\" : {\"fields\" : [\"name^3\", \"description\", \"tags\"], \"query\" : \"*' + q + '*", \"use_dis_max\" : true }}';
		if(filter!=null && filter!='') {
			//query = '\"query\":{\"filtered\":{\"query\":{\"text\":{\"_all\":\"' + q + '\"}},' + filter + '}}';
			query = query + ',' + filter + '}}';
		}

		if(facets!=null && facets!='') {
			query = query + ',' + facets;
		}

		var queryEnd = '}';
		
		query = queryStart + query + queryEnd;
	
		//alert(query);

		$.ajax({
	  			cache: false,
	  			type:"POST",
	  			dataType:"json",
	  			url:url,
	  			data:query,
	  			contentType:"application/json;charset=UTF-8",
				success: function(productList) {

					buildProductsList(productList,divProductsContainer);
					//TODO manage total entries found
					callBackSearchProducts(productList);


				},
				error: function(jqXHR,textStatus,errorThrown) { 
					$(divProductsContainer).hideLoading();
					alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
				}
				
		});
		
		
		
}

/**
* Builds the product container div from the product list
**/
function buildProductsList(productList, divProductsContainer) {


		for (var i = 0; i < productList.products.length; i++) {
			var productHtml = '<li class="item span3">';
			productHtml = productHtml + '<div class="product-box"><a href="<c:url value="/shop/product/" />' + productList.products[i].friendlyUrl + '.html">';
			productHtml = productHtml + '<h4 class="name">' + productList.products[i].name +'</h4></a>';
			if(productList.products[i].discounted) {
					   productHtml = productHtml + '<h3><del>' + productList.products[i].originalProductPrice +'</del>&nbsp;<span class="specialPrice">' + productList.products[i].productPrice + '</span></h3>';
			} else {
					    productHtml = productHtml + '<h3>' + productList.products[i].productPrice +'</h3>';
			}
			if(productList.products[i].imageUrl!=null) {
					    productHtml = productHtml + '<a href="<c:url value="/shop/product/" />' + productList.products[i].friendlyUrl + '.html"><img src="<c:url value="/"/>' + productList.products[i].imageUrl +'"></a>';
			}
			productHtml = productHtml + '<div class="bottom"><a href="<c:url value="/shop/product/" />' + productList.products[i].friendlyUrl + '.html" class="view"><s:message code="button.label.view" text="View" /></a> / <a productid="' + productList.products[i].id + '" href="#" class="addToCart"><s:message code="button.label.addToCart" text="Add to cart" /></a></div></div></li>';
			$(divProductsContainer).append(productHtml);

		}
		
		initBindings();

}

</script>