package org.tinywind.server.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tinywind.server.model.UserEntity;
import org.tinywind.server.service.FileService;
import org.tinywind.server.service.storage.SessionStorage;
import org.tinywind.server.util.spring.SpringApplicationContextAware;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@Component
public class RequestGlobal {
    private static final Logger logger = LoggerFactory.getLogger(RequestGlobal.class);
    private static final String REQUEST_GLOBAL_CURRENT_USER = "REQUEST_GLOBAL_CURRENT_USER";
    private static final String REQUEST_GLOBAL_ALERTS = "REQUEST_GLOBAL_ALERTS";

    @Autowired
    private HttpSession session;
    @Autowired
    private FileService fileService;
    @Autowired
    private SessionStorage sessionStorage;

    public UserEntity getUser() {
        return sessionStorage.get(session.getId(), REQUEST_GLOBAL_CURRENT_USER, UserEntity.class);
    }

    public boolean isLogin() {
        return getUser() != null;
    }

    public void setCurrentUser(UserEntity user) {
        sessionStorage.set(session.getId(), REQUEST_GLOBAL_CURRENT_USER, user);
    }

    public void invalidateSession() {
        sessionStorage.expire(session.getId());
    }

    public void alert(String code, Object... objects) {
        final List<String> alerts = getAlerts();
        alerts.add(SpringApplicationContextAware.requestMessage().getText(code, objects));
        sessionStorage.set(session.getId(), REQUEST_GLOBAL_ALERTS, alerts);
    }

    public void alertString(String string) {
        final List<String> alerts = getAlerts();
        alerts.add(string);
        sessionStorage.set(session.getId(), REQUEST_GLOBAL_ALERTS, alerts);
    }

    @SuppressWarnings("unchecked")
    public List<String> getAlerts() {
        final List<String> alerts = sessionStorage.get(session.getId(), REQUEST_GLOBAL_ALERTS, List.class, ArrayList::new);
        return new ArrayList<>(alerts);
    }

    public List<String> popAlerts() {
        final List<String> alerts = getAlerts().stream().distinct().collect(Collectors.toList());
        sessionStorage.remove(session.getId(), REQUEST_GLOBAL_ALERTS);
        return alerts;
    }

    public String urlEncode(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "UTF-8");
    }

    public String now(String format) {
        return dateFormat(new Date(System.currentTimeMillis()), format);
    }

    public String dateFormat(Date date, String format) {
        final DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return dateFormat.format(date);
    }

    public String now() {
        return now("yyyy-MM-dd");
    }

    public String dateFormat(Date date) {
        return dateFormat(date, "yyyy-MM-dd");
    }

    public String escapeQuote(String text) {
        return text
                .replaceAll("'", "\\\\'")
                .replaceAll("\"", "\\\\\"")
                .replaceAll("[\n]", "\\\\n")
                .replaceAll("[\r]", "\\\\r");
    }

    public String htmlQuote(String text) {
        if (StringUtils.isEmpty(text))
            return "";
        return text.replaceAll("\"", "&quot;").replaceAll("'", "&#39;").replaceAll("<", "&lt;");
    }

    public String url(String path) {
        return fileService.url(path);
    }

    public String url(Long fileId) {
        return fileService.url(fileId);
    }
}
