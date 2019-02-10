package org.tinywind.server.controller.web;

import org.tinywind.server.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author tinywind
 * @since 2017-05-15
 */
@Controller
@RequestMapping("exception")
public class ExceptionController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @GetMapping("")
    public String exception() {
        logger.debug("error.system");

        if (g.getAlerts().size() == 0)
            g.alert("error.system");
        return redirectMain();
    }

    @GetMapping("404")
    public String pageNotFound(HttpServletRequest request) {
        logger.info("error.access.noexist");

        final Enumeration<String> names = request.getAttributeNames();
        while (names.hasMoreElements()) {
            final String name = names.nextElement();
            if (!name.startsWith("javax.servlet")) continue;
            final Object attribute = request.getAttribute(name);
            logger.info(name + ": " + attribute);
        }

        if (g.getAlerts().size() == 0)
            g.alert("error.access.noexist");
        return redirectMain();
    }
}
