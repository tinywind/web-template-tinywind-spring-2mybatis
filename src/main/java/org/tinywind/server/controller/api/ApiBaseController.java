package org.tinywind.server.controller.api;

import org.tinywind.server.config.Constants;
import org.tinywind.server.controller.BaseController;
import org.tinywind.server.util.JsonResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.UncheckedIOException;
import java.sql.SQLException;
import java.sql.SQLNonTransientException;
import java.util.Arrays;

/**
 * @author tinywind
 */
public abstract class ApiBaseController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ApiBaseController.class);

    @ExceptionHandler(SQLException.class)
    public JsonResult sqlException(HttpServletResponse response, Exception e) {
        return writeResponse(response, e, HttpServletResponse.SC_PRECONDITION_FAILED, "옯바르지 않은 입력값");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public JsonResult illegalArgumentException(HttpServletResponse response, Exception e) {
        return writeResponse(response, e, HttpServletResponse.SC_PRECONDITION_FAILED, "옯바르지 않은 입력값");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public JsonResult duplicateKeyException(HttpServletResponse response, Exception e) {
        return writeResponse(response, e, HttpServletResponse.SC_PRECONDITION_FAILED, "이미 존재하는 정보입니다.");
    }

    @ExceptionHandler(NullPointerException.class)
    public JsonResult nullPointerException(HttpServletResponse response, Exception e) {
        return writeResponse(response, e, HttpServletResponse.SC_PRECONDITION_FAILED, "정의되지 않은 조건");
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public JsonResult unsupportedOperationException(HttpServletResponse response, Exception e) {
        return writeResponse(response, e, HttpServletResponse.SC_NOT_FOUND, "NOT FOUND API");
    }

    @ExceptionHandler(SQLNonTransientException.class)
    public JsonResult sqlNonTransientException(HttpServletResponse response, Exception e) {
        return writeResponse(response, new Exception("서버 접근에 실패했습니다.(DB)", e), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 접근에 실패했습니다.(DB)");
    }

    @ExceptionHandler(Exception.class)
    public JsonResult exception(HttpServletResponse response, Exception e) {
        return writeResponse(response, e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "시스템 예외 발생");
    }

    @ExceptionHandler(UncheckedIOException.class)
    public JsonResult uncheckedIOException(HttpServletResponse response, Exception e) {
        if (StringUtils.isEmpty(e.getMessage()))
            return writeResponse(response, e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "시스템 예외 발생");

        final String exceptionString = "Exception:";
        final int exceptionIndex = e.getMessage().indexOf(exceptionString);

        if (exceptionIndex >= 0) {
            printException(e);
            return writeResponse(response, new Exception(e.getMessage().substring(exceptionIndex + exceptionString.length() + 1)), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "시스템 예외 발생");
        } else {
            return writeResponse(response, e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "시스템 예외 발생");
        }
    }

    private JsonResult writeResponse(HttpServletResponse response, Exception e, int status, String defaultMessage) {
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        response.setStatus(status);
        printException(e);

        final String message = e.getMessage();
        return JsonResult.create(StringUtils.isEmpty(message) ? defaultMessage : message);
    }

    private void printException(Exception e) {
        logger.info(e.getMessage());
        Arrays.stream(e.getStackTrace())
                .filter(exception -> exception.getClassName() != null && exception.getClassName().startsWith(Constants.BASE_PACKAGE) && !exception.getFileName().startsWith("<"))
                .forEach(exception -> logger.info("\t" + exception.toString()));
    }
}
