package com.demo.foundation.core.exception;

/**
 * 友好异常。
 * 面向用户的可读异常，全局异常处理器直接返回其 message。
 */
public class FriendlyException extends RuntimeException implements ReadableException {

    public FriendlyException(String message) {
        super(message);
    }

    public FriendlyException(String message, Throwable cause) {
        super(message, cause);
    }
}
