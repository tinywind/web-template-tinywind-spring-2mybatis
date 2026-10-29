<%@ tag pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="org.tinywind.server.config.RequestGlobal"--%>
<%--@elvariable id="cached" type="org.tinywind.server.config.CachedEntity"--%>
<%--@elvariable id="message" type="org.tinywind.server.config.RequestMessage"--%>

<%--@elvariable id="version" type="java.lang.String"--%>

<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<!-- external library common -->

<!-- external library depend -->
<link href="<c:url value="/webjars/font-awesome/7.3.0/css/all.min.css"/>" rel="stylesheet"/>
<link href="<c:url value="/webjars/jquery-ui/1.14.2/jquery-ui.min.css"/>" rel="stylesheet"/>
<link href="<c:url value="/webjars/toastr/2.1.4/build/toastr.css"/>" rel="stylesheet"/>

<%-- depend --%>
<link href="<c:url value="/resources/css/base.css?version=${version}"/>" rel="stylesheet"/>

<%--external--%>