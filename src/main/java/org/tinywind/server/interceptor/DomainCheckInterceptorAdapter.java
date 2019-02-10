package org.tinywind.server.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tinywind
 */
@Component
@PropertySource("classpath:application.properties")
public class DomainCheckInterceptorAdapter extends HandlerInterceptorAdapter {

    @Value("${server.domain}")
    protected String domain;

    protected void redirect(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(
                "<!doctype html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta http-equiv=\"refresh\" content=\"0;url=" + domain + "\" data-state=\"false\">\n" +
                        "    <title>Redirecting</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "</body>\n" +
                        "</html>"
        );
    }

    @Override
    final public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String referer = request.getRequestURL().toString();
        if (StringUtils.isNotEmpty(referer) && !referer.contains(domain)) {
            redirect(request, response);
            return false;
        }
        return true;
    }
}
