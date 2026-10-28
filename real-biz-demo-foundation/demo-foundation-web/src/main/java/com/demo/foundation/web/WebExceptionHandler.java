package com.demo.foundation.web;

import com.demo.foundation.core.dto.ResponseCode;
import com.demo.foundation.core.dto.Result;
import com.demo.foundation.core.exception.BizException;
import com.demo.foundation.core.exception.ConcurrentException;
import com.demo.foundation.core.exception.FriendlyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 全局异常处理器。
 * 去掉了 RobotNotifyEvent（机器人告警）等 pms 私有事件，保留核心异常归一为 Result。
 * 该类需被组件扫描到：启动类或 @Configuration 扫描 com.demo.foundation.web。
 */
@Slf4j
@RestControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handle404(NoHandlerFoundException ex) {
        return Result.fail(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(FriendlyException.class)
    public Result<Void> friendlyException(HttpServletRequest req, FriendlyException e) {
        log.error("error occurred: ", e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(BizException.class)
    public Result<Void> bizException(HttpServletRequest req, BizException e) {
        log.error("error occurred: ", e);
        if (e.getCode() > 0) {
            return Result.fail(e.getCode(), e.getMessage());
        }
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public Result<Void> assertExceptionsHandler(HttpServletRequest req, Exception e) {
        log.error("error occurred: ", e);
        return Result.fail(ResponseCode.PARAMETER_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> methodArgumentTypeMismatchException(HttpServletRequest req, MethodArgumentTypeMismatchException e) {
        log.error("{} 校验错误: {}", req.getRequestURL().toString(), e.getParameter());
        return Result.fail(ResponseCode.PARAMETER_ERROR.getCode(), e.getLocalizedMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> missingServletRequestParameterException(HttpServletRequest req, MissingServletRequestParameterException e) {
        log.error("{} {} 输入参数错误 {}", req.getRequestURL().toString(), req.getMethod(), e.getParameterName());
        return Result.fail(ResponseCode.PARAMETER_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(ConcurrentException.class)
    public Result<Void> concurrentException(HttpServletRequest req, HttpServletResponse response, ConcurrentException e) {
        log.error("{} {} 请勿重复操作", req.getRequestURL().toString(), req.getMethod(), e);
        response.setHeader("code", String.valueOf(ResponseCode.NOT_REPEAT.getCode()));
        return Result.fail(ResponseCode.NOT_REPEAT.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> methodArgumentNotValidException(HttpServletRequest req, HttpServletResponse response, MethodArgumentNotValidException e) {
        log.error("{} {} 输入参数错误", req.getRequestURL().toString(), req.getMethod(), e);
        response.setHeader("code", String.valueOf(ResponseCode.PARAMETER_ERROR.getCode()));
        return handlerValid(e.getBindingResult());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> methodNotSupportedException(HttpServletRequest req, HttpServletResponse response, HttpRequestMethodNotSupportedException e) {
        log.error("{} {} 请求方法不支持", req.getRequestURL().toString(), req.getMethod(), e);
        response.setHeader("code", String.valueOf(ResponseCode.METHOD_ERROR.getCode()));
        return Result.fail(ResponseCode.METHOD_ERROR.getCode(), "错误的请求方式 " + req.getMethod() + " not supported");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> httpMessageNotReadableException(HttpServletRequest req, HttpServletResponse response, HttpMessageNotReadableException e) {
        return defaultErrorHandler(req, response, e);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> defaultErrorHandler(HttpServletRequest req, HttpServletResponse response, Exception e) {
        log.error("{} 系统异常，异常信息", req.getRequestURL().toString(), e);
        response.setHeader("code", String.valueOf(ResponseCode.INTERNAL_ERROR.getCode()));
        return Result.fail(ResponseCode.INTERNAL_ERROR.getCode(), ResponseCode.INTERNAL_ERROR.getDesc());
    }

    @ExceptionHandler(BindException.class)
    public Result<Void> validationParameterException(HttpServletRequest req, BindException exception) {
        return handlerValid(exception.getBindingResult());
    }

    private Result<Void> handlerValid(BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            StringBuilder errorMessage = new StringBuilder();
            errors.forEach(p -> {
                FieldError fieldError = (FieldError) p;
                errorMessage.append(fieldError.getDefaultMessage()).append(";");
            });
            return Result.fail(ResponseCode.PARAMETER_ERROR.getCode(), errorMessage.toString());
        }
        return Result.fail("");
    }
}
