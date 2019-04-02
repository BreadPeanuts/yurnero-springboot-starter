package com.github.weibo1987.starter.autoconfigure.support;

import com.github.weibo1987.starter.autoconfigure.property.YurneroErrorCodeProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 统一异常处理.
 *
 * @author weibo
 * @since: 2018/4/9 15:00
 */
@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @Autowired
    private YurneroErrorCodeProperties yurneroErrorCodeProperties;

    /**
     * 统一处理參數.
     *
     * @return {@link YurneroJsonResult}
     */
    @ExceptionHandler( {MissingServletRequestParameterException.class,
        MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object validationRequestParameterException() {
        YurneroJsonResult razorJsonResult = YurneroJsonResult.build(
            yurneroErrorCodeProperties.getParamValidateErrorCode(),
            yurneroErrorCodeProperties.getParamValidateErrorMsg()
        );
        return razorJsonResult;
    }

    /**
     * 统一处理参数预校验异常.
     *
     * @param ex 异常.
     * @return {@link YurneroJsonResult}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        YurneroJsonResult razorJsonResult = YurneroJsonResult.build(
            yurneroErrorCodeProperties.getParamValidateErrorCode(),
            fieldErrors.get(0).getDefaultMessage()
        );
        return razorJsonResult;
    }

    /**
     * Validate注解所抛异常.
     *
     * @param exception 异常
     * @return {@link YurneroJsonResult}
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object handle(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> set = exception.getConstraintViolations();
        YurneroJsonResult result = YurneroJsonResult.build(
            yurneroErrorCodeProperties.getParamValidateErrorCode(),
            set.iterator().next().getMessage()
        );
        return result;
    }

    /**
     * 统一的异常处理.
     *
     * @param request http request {@link HttpServletRequest}
     * @return {@link YurneroJsonResult}
     */
    @ExceptionHandler(YurneroException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleException(YurneroException ex, HttpServletRequest request) {
        return handleYurneroException(ex, request);
    }


    /**
     * 统一的未识别异常处理.
     *
     * @return {@link YurneroJsonResult}
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object runtimeException() {
        //封装吐出
        YurneroJsonResult result = YurneroJsonResult.build(
            yurneroErrorCodeProperties.getUndefinedErrorCode(),
            yurneroErrorCodeProperties.getUndefinedErrorMsg()
        );
        return result;
    }

    /**
     * 系统异常处理，比如：500.
     *
     * @param ex 异常
     * @return YurneroJsonResult
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object defaultErrorHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
        YurneroJsonResult rsp = new YurneroJsonResult();
        rsp.setMsg(ex.getMessage());
        rsp.setCode("500");
        return rsp;
    }

    private Object handleYurneroException(YurneroException ex, HttpServletRequest request) {
        //封装吐出
        YurneroJsonResult result = YurneroJsonResult.build(ex.getErrorCode(), ex.getErrorMsg());
        Object data = ex.getData();
        if (null != data) {
            result.setData(data);
        }
        log.debug("contentType={}", request.getContentType());

        return result;
    }
}
