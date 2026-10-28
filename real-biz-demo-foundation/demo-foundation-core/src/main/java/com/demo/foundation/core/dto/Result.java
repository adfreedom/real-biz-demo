package com.demo.foundation.core.dto;

import org.slf4j.MDC;

import java.io.Serializable;

/**
 * 统一返回体。
 * 字段：code / success / message / data / traceId（取自 MDC，便于链路追踪）。
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -4105211206958724414L;

    private int code;
    private T data;
    private String message;
    private boolean success;
    private String traceId;

    public Result() {
        this(200, true, "", null);
    }

    public Result(int code, boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
        this.traceId = MDC.get("traceId");
    }

    public static Result<Void> success() {
        return new Result<>();
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, true, "操作成功", data);
    }

    public static Result<Void> fail(String message) {
        return new Result<>(500, false, message, null);
    }

    public static Result<Void> fail(Integer code, String message) {
        return new Result<>(code, false, message, null);
    }

    public static <T> Result<T> fail(String message, T data) {
        return new Result<>(500, false, message, data);
    }

    public int getCode() {
        return code;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
