<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ attribute name="inputWrap" type="java.lang.Boolean" %>
<%@ attribute name="type" %>
<%@ attribute name="path" required="true" rtexprvalue="true" %>

<%--@elvariable id="g" type="org.tinywind.server.config.RequestGlobal"--%>
<%--@elvariable id="cached" type="org.tinywind.server.config.CachedEntity"--%>
<%--@elvariable id="message" type="org.tinywind.server.config.RequestMessage"--%>

<c:choose>
    <c:when test="${inputWrap eq true}">
        <div class="input-wrap">
            <tags:formErrors path="${path}"/>
        </div>
    </c:when>
    <c:otherwise>
        <spring:bind path="${path}">
            <c:forEach var="message" items="${status.errorMessages}">
                <div class="wrap tooltip err -form-error-message" style="text-align:left;"
                     data-path="<c:out value="${path}"/>">
                    <span style="color: red"><c:out value="${message}"/></span>
                </div>
                <form:errors path="${path}" cssStyle="color: red"/><br/>
            </c:forEach>
        </spring:bind>
    </c:otherwise>
</c:choose>

<tags:formAlert field="${path}"/>
