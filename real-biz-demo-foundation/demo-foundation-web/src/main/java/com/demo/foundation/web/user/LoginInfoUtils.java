package com.demo.foundation.web.user;

import javax.servlet.http.HttpServletRequest;

import static com.demo.foundation.web.user.Constants.*;

/**
 * 登录相关头信息读取工具。
 */
public final class LoginInfoUtils {

    private LoginInfoUtils() {
    }

    /**
     * demo 简化：直接回传请求来源头值，未做枚举映射。
     */
    public static String getRequestSource(HttpServletRequest servletRequest) {
        return servletRequest.getHeader(REQUEST_SOURCE);
    }
}
