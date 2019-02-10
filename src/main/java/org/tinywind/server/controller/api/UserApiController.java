package org.tinywind.server.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.tinywind.server.model.UserEntity;
import org.tinywind.server.model.form.LoginForm;
import org.tinywind.server.repository1.UserRepository;
import org.tinywind.server.service.MultipleTransactionTest;
import org.tinywind.server.util.JsonResult;

import javax.validation.Valid;

/**
 * @author tinywind
 */
@Api(description = "사용자 정보", tags = {"USER"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MultipleTransactionTest transactionTest;

    @ApiOperation("로그인")
    @PostMapping("login")
    public JsonResult login(@RequestBody @Valid LoginForm form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            return JsonResult.create(bindingResult);

        if (userRepository.findOneById(form.getId()) == null)
            return JsonResult.create("존재하지 않는 사용자 ID 입니다.");

        final UserEntity user = userRepository.findOneByIdAndPassword(form);
        if (user == null)
            return JsonResult.create("일치하지 않는 비밀번호입니다.");

        g.invalidateSession();
        g.setCurrentUser(user);

        return JsonResult.create();
    }

    @ApiOperation("로그아웃")
    @GetMapping("logout")
    public JsonResult logout() {
        g.invalidateSession();
        return JsonResult.create();
    }

    @GetMapping("transaction1")
    public JsonResult transaction1() {
        try {
            transactionTest.forceError1();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return JsonResult.create();
    }

    @GetMapping("transaction2")
    public JsonResult transaction2() {
        try {
            transactionTest.forceError2();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return JsonResult.create();
    }
}
