package org.tinywind.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author tinywind
 */
@Component
public class RequestMessage {
    @Autowired
    private MessageSource source;

    @Autowired
    private RequestGlobal g;

    public String getText(String code) {
        return getText(code, new Object[0]);
    }

    public String getText(String code, Object... objects) {
        return source.getMessage(code, objects, "Message not found. code: " + code, Locale.getDefault());
    }

    public String getEnumText(Enum value) {
        return getText(value.getClass().getName() + "." + value.name());
    }
}
