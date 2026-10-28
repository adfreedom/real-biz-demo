package com.demo.foundation.core.exception;

/**
 * 可读异常标记。
 * 标注此接口的异常视为面向用户的友好异常，全局异常处理器返回其 message 而非系统错误码。
 */
public interface ReadableException {
}
