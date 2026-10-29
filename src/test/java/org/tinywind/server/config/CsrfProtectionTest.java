package org.tinywind.server.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.tinywind.server.controller.api.UserApiController;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CsrfProtectionTest {
    @Test
    public void requiresTheSessionTokenForEveryUnsafeApiMethod() throws Exception {
        for (String method : new String[]{"POST", "PUT", "PATCH", "DELETE"}) {
            assertEquals(method, 403, perform(new MockHttpServletRequest(method, "/api/user/login"), null).getStatus());
        }
        MockHttpServletRequest wrong = new MockHttpServletRequest("POST", "/api/user/login");
        wrong.addHeader("X-CSRF-TOKEN", "invalid");
        assertEquals(403, perform(wrong, null).getStatus());
    }

    @Test
    public void exposesAMaskedTokenAndAcceptsItOnlyForTheSameSession() throws Exception {
        MockHttpServletRequest page = new MockHttpServletRequest("GET", "/");
        AtomicReference<CsrfToken> token = new AtomicReference<>();
        assertEquals(200, perform(page, token).getStatus());
        assertNotNull(token.get());
        MockHttpServletRequest valid = new MockHttpServletRequest("POST", "/api/user/login");
        valid.setSession((MockHttpSession) page.getSession(false));
        valid.addHeader(token.get().getHeaderName(), token.get().getToken());
        assertEquals(200, perform(valid, null).getStatus());
        MockHttpServletRequest anotherSession = new MockHttpServletRequest("POST", "/api/user/login");
        anotherSession.setSession(new MockHttpSession());
        anotherSession.addHeader(token.get().getHeaderName(), token.get().getToken());
        assertEquals(403, perform(anotherSession, null).getStatus());
    }

    @Test
    public void exemptsOnlyTheSockJsTransportPath() throws Exception {
        MockHttpServletRequest transport = new MockHttpServletRequest("POST", "/ws/000/session/xhr");
        transport.setServletPath("/ws/000/session/xhr");
        assertEquals(200, perform(transport, null).getStatus());
        MockHttpServletRequest api = new MockHttpServletRequest("POST", "/ws-admin");
        api.setServletPath("/ws-admin");
        assertEquals(403, perform(api, null).getStatus());
    }

    @Test
    public void stateChangesUsePostMappings() throws Exception {
        for (String method : new String[]{"logout", "transaction1", "transaction2"}) {
            assertNotNull(method, UserApiController.class.getMethod(method).getAnnotation(PostMapping.class));
        }
    }

    private MockHttpServletResponse perform(MockHttpServletRequest request, AtomicReference<CsrfToken> token) throws Exception {
        Filter[] filters = new WebAppInitializer().getServletFilters();
        MockHttpServletResponse response = new MockHttpServletResponse();
        new FilterChain() {
            private int index;

            @Override
            public void doFilter(ServletRequest current, ServletResponse output) throws IOException, ServletException {
                if (index < filters.length) {
                    filters[index++].doFilter(current, output, this);
                } else if (token != null) {
                    CsrfToken csrf = (CsrfToken) current.getAttribute("_csrf");
                    if (csrf != null) csrf.getToken();
                    token.set(csrf);
                }
            }
        }.doFilter(request, response);
        return response;
    }
}
