package com.demo.foundation.domain.user;

/**
 * 用户上下文注入契约：请求体反序列化后由参数解析器按网关写入的头信息回填
 * 用户身份字段。实现类仅需声明所需字段的 setter。
 */
public interface IUserContext extends IClientContext {

    void setUserId(String userId);

    default void setUserName(String userName) {
    }

}
