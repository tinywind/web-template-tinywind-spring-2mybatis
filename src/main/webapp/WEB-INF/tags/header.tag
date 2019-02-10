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
<%--@elvariable id="user" type="org.tinywind.server.model.resolved.User"--%>

<%--@elvariable id="selectedSite" type="org.tinywind.server.model.resolved.Site"--%>
<%--@elvariable id="sites" type="java.util.List<org.tinywind.server.model.resolved.Site>"--%>

<div class="header-item" id="alarm-container">
    <button id="alarm-button">
        <img src="<c:url value="/resources/images/icon/top-alarm.png"/>"/>
        <sup>0</sup>
    </button>
    <div id="alarm-list-container" style="display: none;">
        <div class="alarm-head" data-value="alarm">알람</div>
        <div class="alarm-body item" id="empty-alarm-indicator" style="color: #373b49;"><i>알람이 존재하지 않습니다.</i></div>
        <ul id="alarm-list" class="alarm-body"></ul>
        <div class="alarm-foot" data-value="alarm">
            <a href="<c:url value="/alarm/"/>">모두보기</a>
        </div>
        <div class="alarm-foot" data-value="alarm">
            <input type="checkbox" id="alarm-sound" ${user.alarmWebSound ? 'checked' : ''} onchange="updateAlarmWebCondition()"/><label for="alarm-sound">경고음</label>
        </div>
    </div>
</div>

<div class="header-item">
    <img src="<c:url value="/resources/images/icon/${user.grade.name() == 'MANAGER' ? 'user-green.png' : 'user-blue.png'}"/>"/>
    ${g.htmlQuote(user.name)}
</div>
<div class="header-item" onclick="logout()">
    <img src="<c:url value="/resources/images/icon/top-logout.png"/>"/>
</div>

<tags:scripts>
    <script>
        var beepAudio = new Audio(contextPath + '/resources/sound/beep.mp3');

        function logout() {
            restSelf.get("/api/user/logout").done(function () {
                location.href = contextPath + '/';
            });
        }

        const alarmSound = $('#alarm-sound');
        const alarmButton = $('#alarm-button');
        const alarmCount = $('#alarm-button sup');
        const alarmListContainer = $('#alarm-list-container');
        const alarmList = $('#alarm-list');

        alarmButton.click(function () {
            if (alarmListContainer.css('display') === 'none')
                alarmListContainer.show();
            else
                alarmListContainer.hide();
        });

        function updateAlarmWebCondition() {
            restSelf.put('/api/me/alarm/web', {alarmWebSound: alarmSound.is(":checked")});
        }

        $(document).ready(function () {
            function showAlarms() {
                restSelf.get('/api/me/alarms', undefined, function () {
                }, true).done(function (response) {
                    const alarms = response.data;
                    alarmList.empty();
                    $.each(alarms, function (i) {
                        if (i > 5) return;

                        alarmList.append($('<li/>', {
                            'class': 'item',
                            style: 'white-space: nowrap;',
                            html:
                                '<div class="item-title">' + this.eqName + '</div>'
                                + '<div class="item-desc">' + this.msg + '</div>'
                        }));
                    });

                    alarmCount.text(alarms.length);
                    alarmCount.css({
                        'background-color': alarms.length === 0 ? '' : 'red',
                        'color': alarms.length === 0 ? '' : 'white'
                    });

                    $('#empty-alarm-indicator')[alarms.length ? 'hide' : 'show']();
                });
            }

            showAlarms();
            setInterval(showAlarms, 10000);
        });

        $(document).ready(function () {
            setInterval(function () {
                const count = parseInt(alarmCount.text());
                if (isFinite(count) && !isNaN(count) && count > 1 && alarmSound.is(":checked")) {
                    beepAudio.play();
                    if (beepAudio.currentTime > 0)
                        beepAudio.currentTime = 0;
                }
            }, 3000);
        });

        $(document).click(function (event) {
            if ($(event.target).closest('#alarm-container').length === 0)
                alarmListContainer.hide();
        });
    </script>
</tags:scripts>