package com.demo.foundation.web.user;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制器方法参数标注：声明由 LoginUserHandlerMethodArgumentResolver 装配登录上下文，
 * 参数类型须为 UserContext 及其子类。与 @RequestBody IUserContext 注入并行，互不依赖。
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {

    boolean required() default true;

}
