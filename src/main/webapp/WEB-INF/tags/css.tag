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

<!-- external library common -->

<!-- external library depend -->
<link href="<c:url value="/webjars/font-awesome/5.0.9/web-fonts-with-css/css/fontawesome-all.min.css"/>" rel="stylesheet"/>
<link href="<c:url value="/webjars/jquery-ui/1.12.1/jquery-ui.min.css"/>" rel="stylesheet"/>
<link href="<c:url value="/webjars/toastr/2.1.2/build/toastr.css"/>" rel="stylesheet"/>

<%-- depend --%>
<link href="<c:url value="/resources/css/base.css?version=${version}"/>" rel="stylesheet"/>

<%--external--%>