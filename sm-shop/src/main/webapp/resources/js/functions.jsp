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


</script>