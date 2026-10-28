package com.demo.foundation.domain.user;

import lombok.Data;

/**
 * 登录用户信息：由网关解析写入请求头，拦截器读取后放入线程上下文。
 */
@Data
public class UserInfo {

    /** 用户id */
    private String userId;

    /** 租户id */
    private String tenantId;

    /** 用户名 */
    private String username;
}
