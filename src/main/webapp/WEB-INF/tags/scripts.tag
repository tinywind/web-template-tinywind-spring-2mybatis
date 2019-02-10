<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="org.tinywind.server.config.RequestGlobal"--%>
<%--@elvariable id="cached" type="org.tinywind.server.config.CachedEntity"--%>
<%--@elvariable id="message" type="org.tinywind.server.config.RequestMessage"--%>

<%--@elvariable id="tagExtender" type="org.tinywind.server.util.spring.TagExtender"--%>

<%@attribute name="method" required="false" type="java.lang.String" %>

<jsp:doBody var="TAG_SCRIPTS" scope="request"/>

${tagExtender.stackBody("TAG_SCRIPTS", "TAG_SCRIPTS_LIST")}

<c:choose>
    <c:when test="${method eq 'pop'}">
        <c:forEach items="${tagExtender.getBody('TAG_SCRIPTS_LIST')}" var="i">
            <c:out value="${i}" escapeXml="false"/>
        </c:forEach>
    </c:when>
</c:choose>