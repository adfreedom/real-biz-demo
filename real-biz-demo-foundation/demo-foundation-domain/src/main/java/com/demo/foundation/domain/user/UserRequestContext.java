package com.demo.foundation.domain.user;

/**
 * 请求操作人上下文：登录用户上下文的请求态扩展。
 * 控制器方法标注 @LoginUser UserRequestContext 时由解析器整体装配。
 */
public class UserRequestContext extends UserContext {
}
