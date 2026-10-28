package com.demo.foundation.domain.user;

/**
 * 登录用户上下文基类：承载用户 id、用户名。
 * 由 LoginUserHandlerMethodArgumentResolver 从线程上下文与请求头装配，
 * 控制器方法参数标注 @LoginUser 且类型为 UserContext 及其子类时生效。
 */
public class UserContext extends ClientContext implements IUserContext {

    private String userId;

    private String userName;

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
