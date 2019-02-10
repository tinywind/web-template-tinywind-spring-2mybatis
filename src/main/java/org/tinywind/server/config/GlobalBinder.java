package org.tinywind.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.tinywind.server.model.UserEntity;
import org.tinywind.server.util.spring.TagExtender;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author tinywind
 */
@ControllerAdvice
@PropertySource("classpath:application.properties")
public class GlobalBinder {
    @Autowired
    private TagExtender tagExtender;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RequestGlobal g;
    @Autowired
    private CachedEntity cachedEntity;
    @Autowired
    private RequestMessage requestMessage;

    @Value("${application.version}")
    private String version;
    @Value("${application.devel:true}")
    private Boolean devel;

    @ModelAttribute("devel")
    public Boolean devel() {
        return devel;
    }

    @ModelAttribute("version")
    public String version() {
        return version;
    }

    @ModelAttribute("cached")
    public CachedEntity cachedEntity() {
        return cachedEntity;
    }

    @ModelAttribute("g")
    public RequestGlobal requestGlobal() {
        return g;
    }

    @ModelAttribute("request")
    public HttpServletRequest request() {
        return request;
    }

    @ModelAttribute("tagExtender")
    public TagExtender tagExtender() {
        return tagExtender;
    }

    @ModelAttribute("user")
    public UserEntity user() {
        return g.getUser();
    }

    @ModelAttribute("message")
    public RequestMessage message() {
        return requestMessage;
    }

    @InitBinder
    public void registerCustomEditors(WebDataBinder binder) {
        bindDateFormat(binder, Date.class, "yyyy-MM-dd");
        bindDateFormat(binder, Timestamp.class, "yyyy-MM-dd HH:mm:ss");
        bindDateFormat(binder, Timestamp.class, "yyyy-MM-dd HH:mm");

        bindDateFormat(binder, java.util.Date.class, "yyyy-MM-dd");
        bindDateFormat(binder, java.util.Date.class, "yyyy-MM-dd HH:mm");
        bindDateFormat(binder, java.util.Date.class, "yyyy-MM-dd HH:mm:ss");
    }

    private void bindDateFormat(WebDataBinder binder, Class klass, String pattern) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(klass, new CustomDateEditor(dateFormat, true));
    }

}
