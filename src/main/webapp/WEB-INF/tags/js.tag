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

<%--@elvariable id="devel" type="java.lang.Boolean"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<!-- external library common -->
<script src="<c:url value="/webjars/jquery/3.3.1/jquery.min.js"/>"></script>
<script src="<c:url value="/webjars/jquery-blockui/2.65/jquery.blockUI.js"/>"></script>
<script src="<c:url value="/webjars/momentjs/2.21.0/min/moment.min.js"/>"></script>
<script src="<c:url value="/webjars/toastr/2.1.2/toastr.js"/>"></script>

<!-- external library depend -->
<script src="<c:url value="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"/>"></script>

<%-- user library --%>
<script src="<c:url value="/resources/js/string.ex.js?version=${version}"/>" data-type="library"></script>
<script src="<c:url value="/resources/js/formData.ex.js?version=${version}"/>" data-type="library"></script>
<script src="<c:url value="/resources/js/jquery.ex.js?version=${version}"/>" data-type="library"></script>
<script src="<c:url value="/resources/js/jquery.leanModal.js?version=${version}"/>" data-type="library"></script>
<script src="<c:url value="/resources/js/jquery.bind.helpers.js?version=${version}"/>" data-type="library"></script>

<%-- functions --%>
<script src="<c:url value="/resources/js/common.func.js?version=${version}"/>" data-type="library"></script>
<script src="<c:url value="/resources/js/depend.func.js?version=${version}"/>" data-type="library"></script>

<%-- use strict --%>
<script src="<c:url value="/resources/js/depend.use.strict.js?version=${version}"/>" data-type="library"></script>
<script src="<c:url value="/resources/js/app.js?version=${version}"/>" data-type="library"></script>

<%-- external --%>

<script>
    window.reloadWhenSelectSite = false;
    window.disableLog = ${devel};
    window.contextPath = '${pageContext.request.contextPath}';
    window.loadingImageSource = contextPath + '/resources/images/loading.gif';
    <c:if test="${g.login}">
    window.userId = '${g.escapeQuote(g.user.id)}';
    </c:if>

    (function () {
        moment.locale('kr', {
            months: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
            monthsShort: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
            weekdays: ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"],
            weekdaysShort: ["일", "월", "화", "수", "목", "금", "토"],
            longDateFormat: {L: "YYYY.MM.DD", LL: "YYYY년 MMMM D일", LLL: "YYYY년 MMMM D일 A h시 mm분", LLLL: "YYYY년 MMMM D일 dddd A h시 mm분"},
            meridiem: {AM: '오전', am: '오전', PM: '오후', pm: '오후'},
            relativeTime: {future: "%s 후", past: "%s 전", s: "몇초", ss: "%d초", m: "일분", mm: "%d분", h: "한시간", hh: "%d시간", d: "하루", dd: "%d일", M: "한달", MM: "%d달", y: "일년", yy: "%d년"},
            ordinal: function (number) {
                return '일';
            }
        });
        moment.locale('kr');
    }());
</script>