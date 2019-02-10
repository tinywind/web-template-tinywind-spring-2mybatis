package org.tinywind.server.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tinywind
 * @since 2018-01-13
 */
public class JsonResult<T> implements Serializable {
    private Result result;
    private String reason;
    private T data;
    private List<FieldError> fieldErrors;
    private List<GlobalError> globalErrors;

    public JsonResult() {
        this(Result.success);
    }

    public JsonResult(Result result) {
        this(result, null);
    }

    public JsonResult(Result result, String reason) {
        this(result, reason, null);
    }

    public JsonResult(Exception e) {
        this(Result.failure, e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
    }

    public JsonResult(BindingResult bindingResult) {
        this(bindingResult.hasErrors() ? Result.failure : Result.success, null, bindingResult);
    }

    public JsonResult(Result result, String reason, BindingResult bindingResult) {
        setFieldErrors(new ArrayList<>());
        setGlobalErrors(new ArrayList<>());
        setResult(result);
        setReason(reason);
        if (bindingResult != null)
            setBindingResult(bindingResult);
    }

    public static <T> JsonResult<T> data(T data) {
        final JsonResult<T> result = new JsonResult<>();
        result.setData(data);
        return result;
    }

    public static <T> JsonResult<T> create() {
        return new JsonResult<>();
    }

    public static <T> JsonResult<T> create(Result result) {
        return new JsonResult<>(result);
    }

    public static <T> JsonResult<T> create(Result result, String reason) {
        return new JsonResult<>(result, reason);
    }

    public static <T> JsonResult<T> create(Result result, String reason, BindingResult bindingResult) {
        return new JsonResult<>(result, reason, bindingResult);
    }

    public static <T> JsonResult<T> create(Exception e) {
        return new JsonResult<>(e);
    }

    public static <T> JsonResult<T> create(BindingResult bindingResult) {
        return new JsonResult<>(bindingResult);
    }

    public static <T> JsonResult<T> create(String reason) {
        return new JsonResult<>(Result.failure, reason);
    }

    public void setBindingResult(BindingResult bindingResult) {
        setResult(bindingResult.hasErrors() ? Result.failure : Result.success);

        List<FieldError> fieldErrors = getFieldErrors();
        if (fieldErrors == null)
            fieldErrors = new ArrayList<>();

        for (org.springframework.validation.FieldError error : bindingResult.getFieldErrors())
            fieldErrors.add(new FieldError(error.getField(), error.isBindingFailure(), error.getDefaultMessage()));

        setFieldErrors(fieldErrors);

        List<GlobalError> globalErrors = getGlobalErrors();
        if (globalErrors == null)
            globalErrors = new ArrayList<>();

        for (ObjectError error : bindingResult.getGlobalErrors())
            globalErrors.add(new GlobalError(error.getDefaultMessage()));

        setGlobalErrors(globalErrors);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public List<GlobalError> getGlobalErrors() {
        return globalErrors;
    }

    public void setGlobalErrors(List<GlobalError> globalErrors) {
        this.globalErrors = globalErrors;
    }

    public enum Result {
        success, failure
    }

    public class GlobalError {
        private String defaultMessage;

        public GlobalError(String defaultMessage) {
            this.defaultMessage = defaultMessage;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }

        public void setDefaultMessage(String defaultMessage) {
            this.defaultMessage = defaultMessage;
        }
    }

    public class FieldError extends GlobalError {
        private String field;
        private Boolean typeMatchError;

        public FieldError(String field, Boolean typeMatchError, String defaultMessage) {
            super(defaultMessage);
            this.field = field;
            this.typeMatchError = typeMatchError;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Boolean getTypeMatchError() {
            return typeMatchError;
        }

        public void setTypeMatchError(Boolean typeMatchError) {
            this.typeMatchError = typeMatchError;
        }
    }
}
