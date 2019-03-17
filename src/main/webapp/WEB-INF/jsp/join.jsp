<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="org.tinywind.server.config.RequestGlobal"--%>
<%--@elvariable id="cached" type="org.tinywind.server.config.CachedEntity"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:scripts/>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=440, initial-scale=0.8"/>
    <link rel="shortcut icon" type="image/ico" href="<c:url value="/resources/images/favicon.ico"/>"/>
    <tags:css/>
</head>
<body class="bcolor-back">

<form id="join-form">
    <div>ID: <input name="loginId"/></div>
    <div>PW: <input name="password"/></div>
    <div>PW확인: <input name="passwordConfirm"/></div>
    <div>프로필이미지: <input type="file" name="profileImage" /></div>

    <div>
        <button type="button" onclick="join()">가입하기</button>
    </div>
</form>

<tags:scripts>
    <script>
        const form = $('#join-form');

        function join() {
            form.asJsonData().done(function (data) {
                console.log(data);

                restSelf.post('/api/user', data).done(function () {
                    location.href = contextPath + '/';
                });
            });
        }

    </script>
</tags:scripts>

<div id="scripts">
    <tags:js/>
    <tags:alerts/>
    <tags:scripts method="pop"/>
</div>

</body>
</html>
