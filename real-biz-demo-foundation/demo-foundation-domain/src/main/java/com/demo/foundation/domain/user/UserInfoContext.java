package com.demo.foundation.domain.user;

/**
 * 用户信息线程上下文：请求进入时由拦截器写入，请求结束时清除。
 */
public class UserInfoContext {

    private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();

    public static UserInfo get() {
        return THREAD_LOCAL.get();
    }

    public static void set(UserInfo userInfo) {
        THREAD_LOCAL.set(userInfo);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
