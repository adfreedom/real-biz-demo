package com.demo.foundation.domain.user;

/**
 * 客户端上下文基类：承载请求来源与客户端 IP。
 * 与登录用户身份解耦，供仅需客户端信息的场景使用。
 */
public class ClientContext implements IClientContext {

    private String requestSource;

    private String clientIp;

    public String getRequestSource() {
        return requestSource;
    }

    @Override
    public void setRequestSource(String requestSource) {
        this.requestSource = requestSource;
    }

    public String getClientIp() {
        return clientIp;
    }

    @Override
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
}
