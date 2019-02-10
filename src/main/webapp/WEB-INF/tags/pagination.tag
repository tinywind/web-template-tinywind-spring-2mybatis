<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ attribute name="navigation" required="true" type="org.tinywind.server.util.page.PageNavigation" %>
<%@ attribute name="ajaxLoaderTarget" type="java.lang.String" %>
<%@ attribute name="ajaxLoaderEnable" type="java.lang.Boolean" %>
<%@ attribute name="url" required="true" %>
<%@ attribute name="pageForm" required="true" type="org.tinywind.server.util.page.PageForm" %>
<%@ attribute name="clickFunction" required="false" type="java.lang.String" %>

<%--@elvariable id="g" type="org.tinywind.server.config.RequestGlobal"--%>
<%--@elvariable id="cached" type="org.tinywind.server.config.CachedEntity"--%>
<%--@elvariable id="message" type="org.tinywind.server.config.RequestMessage"--%>

<div class="pagination">
    <c:if test="${!navigation.contains(navigation.first)}">
        <a href="${url}?${pageForm.getQuery(navigation.first)}" aria-label="First"
           class="item ${(not empty ajaxLoaderTarget) && ajaxLoaderEnable != false ? '-ajax-loader' : ''}"
                <c:if test="${not empty ajaxLoaderTarget}">
                    data-ajaxify="false" data-target="<c:out value="${ajaxLoaderTarget}"/>"
                </c:if>
                <c:if test="${empty ajaxLoaderTarget}">
                    onclick="$.blockUIFixed()"
                </c:if>>
            <i class="fas fa-angle-double-left"></i>
        </a>

        <a href="${url}?${pageForm.getQuery(navigation.previous)}" aria-label="Previous"
           class="item ${(not empty ajaxLoaderTarget) && ajaxLoaderEnable != false ? '-ajax-loader' : ''}"
                <c:if test="${not empty ajaxLoaderTarget}">
                    data-ajaxify="false" data-target="<c:out value="${ajaxLoaderTarget}"/>"
                </c:if>
                <c:if test="${empty ajaxLoaderTarget}">
                    onclick="$.blockUIFixed()"
                </c:if>>
            <i class="fas fa-angle-left"></i>
        </a>
    </c:if>

    <c:forEach items="${navigation.items}" var="i">
        <c:if test="${navigation.page == i}">
            <a href="javascript:" class="item current">${i + 1}</a>
        </c:if>
        <c:if test="${navigation.page != i}">
            <a href="${url}?${pageForm.getQuery(i)}"
               class="item ${(not empty ajaxLoaderTarget) && ajaxLoaderEnable != false ? '-ajax-loader' : ''}"
                    <c:if test="${not empty ajaxLoaderTarget}">
                        data-ajaxify="false" data-target="<c:out value="${ajaxLoaderTarget}"/>"
                    </c:if>
                    <c:if test="${empty ajaxLoaderTarget}">
                        onclick="$.blockUIFixed()"
                    </c:if>>
                    ${i + 1}
            </a>
        </c:if>
    </c:forEach>

    <c:if test="${!navigation.contains(navigation.last)}">
        <a href="${url}?${pageForm.getQuery(navigation.next)}" aria-label="Next"
           class="item ${(not empty ajaxLoaderTarget) && ajaxLoaderEnable != false ? '-ajax-loader' : ''}"
                <c:if test="${not empty ajaxLoaderTarget}">
                    data-ajaxify="false" data-target="<c:out value="${ajaxLoaderTarget}"/>"
                </c:if>
                <c:if test="${empty ajaxLoaderTarget}">
                    onclick="$.blockUIFixed()"
                </c:if>>
            <i class="fas fa-angle-right"></i>
        </a>

        <a href="${url}?${pageForm.getQuery(navigation.last)}" aria-label="Last"
           class="item ${(not empty ajaxLoaderTarget) && ajaxLoaderEnable != false ? '-ajax-loader' : ''}"
                <c:if test="${not empty ajaxLoaderTarget}">
                    data-ajaxify="false" data-target="<c:out value="${ajaxLoaderTarget}"/>"
                </c:if>
                <c:if test="${empty ajaxLoaderTarget}">
                    onclick="$.blockUIFixed()"
                </c:if>>
            <i class="fas fa-angle-double-right"></i>
        </a>
    </c:if>
</div>
