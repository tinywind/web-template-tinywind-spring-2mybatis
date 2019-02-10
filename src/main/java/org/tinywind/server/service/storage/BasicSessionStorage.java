package org.tinywind.server.service.storage;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

public class BasicSessionStorage implements SessionStorage {
    @Autowired
    private HttpSession session;

    @Override
    public void set(String sessionId, String key, Object value) {
        session.setAttribute(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String sessionId, String key, Class<T> returnType) {
        return (T) session.getAttribute(key);
    }

    @Override
    public void remove(String sessionId, String key) {
        session.removeAttribute(key);
    }

    @Override
    public void expire(String sessionId) {
        session.invalidate();
    }
}
