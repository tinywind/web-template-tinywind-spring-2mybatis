<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="org.tinywind.server.config.RequestGlobal"--%>
<%--@elvariable id="cached" type="org.tinywind.server.config.CachedEntity"--%>
<%--@elvariable id="user" type="org.tinywind.server.model.resolved.User"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<%--@elvariable id="form" type="org.tinywind.server.model.form.LoginForm"--%>

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

<form:form id="login-form" commandName="form" cssClass="login-form -json-submit" action="${pageContext.request.contextPath}/api/user/login" data-done="after">

    <form:input autocomplete="false" path="id" placeholder="아이디"/>

    <form:password autocomplete="false" path="password" placeholder="비밀번호" cssStyle=""/>


    <div>
        <input type="checkbox" id="remember"/><label for="remember">로그인정보 저장</label>
    </div>

    <div class="row">
        <div class="col-3"></div>
        <div class="col-6">
            <button type="submit" class="color-white bcolor-brand">로그인</button>
        </div>
    </div>

    <style>
        table td, table th {border: solid 1px black; text-align: center; padding: 1em;}
    </style>

    <div class="row">
        <h3>멀티플 트랜잭션 테스트</h3>

        <div style="padding: 1em;">
            <table style="table-layout: fixed; width: 100%">
                <thead>
                <tr>
                    <th style="width: 50%">트랜잭션1</th>
                    <th style="width: 50%">트랜잭션2</th>
                </tr>
                </thead>

                <tbody>
                <tr>
                    <td>
                        <button type="button" onclick="transaction1()">테스트</button>
                    </td>
                    <td>
                        <button type="button" onclick="transaction2()">테스트</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

</form:form>


<tags:scripts>
    <script>
        function transaction1() {
            restSelf.get("/api/user/transaction1").done(function () {
                alert("DB1에 '1234'계정이 없다면 옳바르게 성공한 것입니다.")
            }).fail(function () {
                console.log(arguments);
            });
        }

        function transaction2() {
            restSelf.get("/api/user/transaction2").done(function () {
                alert("DB2에 '1234'계정이 없다면 옳바르게 성공한 것입니다.")
            }).fail(function () {
                console.log(arguments);
            });
        }
    </script>
    <script>
        const STORAGE_KEY = 'loginForm';
        const form = $('#login-form');
        const remember = $('#remember');

        function after() {
            localStorage.setItem(STORAGE_KEY, remember.prop('checked') ? JSON.stringify(form.formDataObject()) : '');
            location.href = contextPath + '/main';
        }

        $(document).ready(function () {
            const storedValues = localStorage.getItem(STORAGE_KEY);
            if (!storedValues) return;

            const values = JSON.parse(storedValues);
            if (!values) return;

            remember.prop('checked', true);

            const inputs = form.find('[name]');
            for (let key in values) {
                if (values.hasOwnProperty(key)) {
                    inputs.filter(function () {
                        return $(this).attr('name') === key;
                    }).val(values[key]);
                }
            }
        });
    </script>
</tags:scripts>

<div id="scripts">
    <tags:js/>
    <tags:alerts/>
    <tags:scripts method="pop"/>
</div>

</body>
</html>
