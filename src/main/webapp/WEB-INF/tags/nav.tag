<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ attribute name="activeTab" %>

<%--@elvariable id="g" type="org.tinywind.server.config.RequestGlobal"--%>
<%--@elvariable id="cached" type="org.tinywind.server.config.CachedEntity"--%>
<%--@elvariable id="message" type="org.tinywind.server.config.RequestMessage"--%>
<%--@elvariable id="user" type="org.tinywind.server.model.resolved.User"--%>

<%--@elvariable id="selectedSite" type="org.tinywind.server.model.resolved.Site"--%>
<%--@elvariable id="sites" type="java.util.List<org.tinywind.server.model.resolved.Site>"--%>


<a href="<c:url value="/"/>">
    <img src="<c:url value="/resources/images/main.png"/>" class="brand"/>
</a>

<div class="tab-container">
    <a class="tab ${activeTab == 'dashboard' ? 'active' : ''}" href="<c:url value="/"/>">
        <label data-target="dashboard">종합상황</label>
    </a>
    <a class="tab ${activeTab == 'alarm' ? 'active' : ''}" href="<c:url value="/alarm/"/>">
        <label data-target="log">알람조회</label>
    </a>
    <a class="tab ${activeTab == 'log' ? 'active' : ''}" href="<c:url value="/log/"/>">
        <label data-target="log">이력조회</label>
    </a>
    <a class="tab ${activeTab == 'equipment' ? 'active' : ''}" href="<c:url value="/equipment/"/>">
        <label data-target="equipment">국소관리</label>
    </a>
    <c:if test="${user.grade.name() == 'MANAGER'}">
        <a class="tab ${activeTab == 'user' ? 'active' : ''}" href="<c:url value="/user/"/>">
            <label data-target="user">사용자관리</label>
        </a>
    </c:if>
</div>

<tags:scripts>
    <script>
    </script>
</tags:scripts>