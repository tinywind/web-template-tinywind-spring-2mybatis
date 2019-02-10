package org.tinywind.server.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.tinywind.server.model.UserEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tinywind
 * @since 2016-09-03
 */
@Component
public class LoginRequireInterceptor extends AnnotationHandlerInterceptorAdapter<LoginRequired> {
    private static final Logger logger = LoggerFactory.getLogger(LoginRequireInterceptor.class);

    public LoginRequireInterceptor() {
        super(LoginRequired.class);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, LoginRequired annotation) throws Exception {
        final UserEntity user = g.getUser();
        if (user != null)
            return true;

        return processFail(request, response, annotation);
    }

    private boolean processFail(HttpServletRequest request, HttpServletResponse response, LoginRequired annotation) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        if (annotation.type().equals(LoginRequired.Type.API)) {
            sendUnauthorizedJsonResult(response);
            return false;
        }

        if (annotation.type().equals(LoginRequired.Type.POPUP)) {
            closePopup(request, response);
            return false;
        }

        g.alert(g.isLogin() ? "error.access.denied" : "error.login.require");
        redirectMain(request, response);
        return false;
    }
}
