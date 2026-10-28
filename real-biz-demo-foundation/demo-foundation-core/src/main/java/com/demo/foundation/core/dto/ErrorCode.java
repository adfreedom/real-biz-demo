package com.demo.foundation.core.dto;

/**
 * 业务错误码契约。业务模块可定义枚举实现此接口，统一错误码来源。
 * 用于 BizException(ErrorCode) 构造。
 */
public interface ErrorCode {

    int getCode();

    String getMessage();
}
