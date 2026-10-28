package com.demo.foundation.web.user;

/**
 * 网关写入的请求头名称常量。网关在转发请求前，将登录信息以头形式注入。
 */
public final class Constants {

    private Constants() {
    }

    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String REQUEST_SOURCE = "Request-Source";
}
