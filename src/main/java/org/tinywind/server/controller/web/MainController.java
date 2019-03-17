package org.tinywind.server.controller.web;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.tinywind.server.config.CachedEntity;
import org.tinywind.server.controller.BaseController;
import org.tinywind.server.interceptor.LoginRequired;
import org.tinywind.server.model.form.LoginForm;
import org.tinywind.server.repository1.UserRepository;
import org.tinywind.server.repository2.TestRepository;
import org.tinywind.server.service.FileService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author tinywind
 */
@Controller
@PropertySource("classpath:application.properties")
public class MainController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private CachedEntity cached;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestRepository testRepository;

    @Value("${application.file.location}")
    private String fileLocation;

    @GetMapping("")
    public String loginPage(@ModelAttribute("form") LoginForm form) {
        logger.info(testRepository.test());

        if (g.isLogin())
            return "redirect:/main";
        return "login";
    }

    @GetMapping("join")
    public String joinPage() {
        return "join";
    }

    @LoginRequired
    @GetMapping("main")
    public String mainPage(Model model) {
        return "main";
    }

    @GetMapping(FileService.FILE_PATH)
    public void downloadFile(HttpServletResponse response, @RequestParam(FileService.FILE_REQUEST_PARAM_KEY) String fileName) throws IOException {
        response.setContentType("application/download; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + URLEncoder.encode(fileName, "UTF-8").replaceAll("[+]", "%20") + "\";");
        response.setHeader("Content-Transfer-Encoding", "BINARY");
        IOUtils.copy(new FileInputStream(new File(fileLocation, fileName)), response.getOutputStream());
    }

    @GetMapping("favicon.ico")
    public String favicon() {
        return "redirect:/resources/images/favicon.ico";
    }
}
