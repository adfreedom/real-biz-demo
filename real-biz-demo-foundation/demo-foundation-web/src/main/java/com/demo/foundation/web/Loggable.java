package com.demo.foundation.web;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记需要记录方法执行日志的方法或类型。由 {@link LoggingInterceptor} 切面环绕，
 * 统一打印请求 URL、方法、参数、响应与耗时。
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Loggable {
}
