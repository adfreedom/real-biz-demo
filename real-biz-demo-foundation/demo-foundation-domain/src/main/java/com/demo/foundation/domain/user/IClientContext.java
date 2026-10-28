package com.demo.foundation.domain.user;

/**
 * 请求来源、客户端IP 等客户端上下文注入契约。
 * 请求体反序列化后由参数解析器按头信息回填，实现类按需覆写相关方法。
 */
public interface IClientContext {

    default void setRequestSource(String requestSource) {
    }

    default void setClientIp(String clientIp) {
    }
}
