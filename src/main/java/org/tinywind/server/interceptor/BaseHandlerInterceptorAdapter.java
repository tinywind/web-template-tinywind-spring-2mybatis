package org.tinywind.server.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.tinywind.server.config.RequestGlobal;
import org.tinywind.server.config.RequestMessage;
import org.tinywind.server.util.JsonResult;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tinywind
 */
public class BaseHandlerInterceptorAdapter extends HandlerInterceptorAdapter {
    @Autowired
    protected RequestGlobal g;
    @Autowired
    protected RequestMessage message;

    protected void redirectMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
        redirect(request, response, "/");
    }

    protected void redirect(HttpServletRequest request, HttpServletResponse response, String url) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(
                "<!doctype html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta http-equiv=\"refresh\" content=\"0;url=" + request.getContextPath() + url + "\" data-state=\"false\">\n" +
                        "    <title>Redirecting</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "</body>\n" +
                        "</html>"
        );
    }

    protected void closePopup(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(
                "<!doctype html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <script>" +
                        "        alert('" + g.escapeQuote(message.getText(g.isLogin() ? "error.access.denied" : "error.login.require")) + "');" +
                        "        (self.opener = self).close();" +
                        "    </script>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "</body>\n" +
                        "</html>"
        );
    }

    protected void sendUnauthorizedJsonResult(HttpServletResponse response) throws IOException {
        sendUnauthorizedJsonResult(response, message.getText(g.isLogin() ? "error.access.denied" : "error.login.require"));
    }

    protected void sendUnauthorizedJsonResult(HttpServletResponse response, String message) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        response.getWriter().write(new ObjectMapper().writeValueAsString(JsonResult.create(message)));
    }
}
