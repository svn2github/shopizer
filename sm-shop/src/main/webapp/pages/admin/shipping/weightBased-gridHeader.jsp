<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>	



					{title:"<s:message code="label.shipping.region" text="Region"/>", name:"region", primaryKey:true, canEdit:false, canFilter:false},
        			{title:"<s:message code="label.generic.country" text="Country"/>", name:"country", canEdit:false, canFilter:true},
					{title:"<s:message code="label.entity.details" text="Details"/>", name: "buttonField", align: "center",canFilter:false,canSort:false, canReorder:false}
