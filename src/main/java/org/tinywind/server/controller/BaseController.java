package org.tinywind.server.controller;

import org.tinywind.server.config.RequestGlobal;
import org.tinywind.server.config.RequestMessage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author tinywind
 */
public abstract class BaseController {

    @Autowired
    protected RequestGlobal g;

    @Autowired
    protected RequestMessage message;

    protected String redirectMain() {
        return "redirect:/";
    }

    protected String closingPopup() {
        return "closing-popup";
    }
}
