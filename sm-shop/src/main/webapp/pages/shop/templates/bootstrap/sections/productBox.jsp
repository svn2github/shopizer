<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


										<c:forEach items="${requestScope.ITEMS}" var="product">
											<li class="span3">
												<div class="product-box">                                        
													<a href="<c:url value="/shop/product/" /><c:out value="${product.friendlyUrl}"/>.html"><h4><c:out value="${product.name}"/></h4></a>
													<h3>
														<c:choose>
															<c:when test="${product.discounted}">
																<del><c:out value="${product.originalProductPrice}" /></del>&nbsp;<span class="specialPrice"><c:out value="${product.productPrice}" /></span>
															</c:when>
															<c:otherwise>
																<c:out value="${product.productPrice}" />
															</c:otherwise>
														</c:choose>
													</h3>
													<a href="<c:url value="/shop/product/" /><c:out value="${product.friendlyUrl}"/>.html"><img src="<sm:shopProductImage imageName="${product.image}" sku="${product.sku}"/>"/></a>
													<div class="bottom">
														<a href="<c:url value="/shop/product/" /><c:out value="${product.friendlyUrl}"/>.html">view</a> / <a class="addToCart" href="#" productId="${product.id}">add to cart</a>
													</div>
												</div>
										    </li>
										</c:forEach>   