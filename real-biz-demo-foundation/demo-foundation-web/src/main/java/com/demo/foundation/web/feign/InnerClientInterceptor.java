package com.demo.foundation.web.feign;

import com.demo.foundation.domain.user.UserInfo;
import com.demo.foundation.domain.user.UserInfoContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 内部服务间 Feign 调用拦截器：透传 appId / userId / username / traceId，
 * 使下游服务可还原调用方与链路上下文。
 */
public class InnerClientInterceptor implements RequestInterceptor {

    public static final String APP_ID = "appId";
    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    public static final String TRACE_ID = "traceId";

    private final ApplicationContext applicationContext;

    public InnerClientInterceptor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void apply(RequestTemplate template) {
        String appId = applicationContext.getEnvironment().getProperty("spring.application.name");
        if (isNotBlank(appId)) {
            template.header(APP_ID, appId);
        }
        UserInfo user = UserInfoContext.get();
        if (user != null) {
            if (isNotBlank(user.getUserId())) {
                template.header(USER_ID, user.getUserId());
            }
            if (isNotBlank(user.getUsername())) {
                template.header(USERNAME,
                        Base64.getEncoder().encodeToString(user.getUsername().getBytes(StandardCharsets.UTF_8)));
            }
        }
        String traceId = MDC.get(TRACE_ID);
        if (isNotBlank(traceId)) {
            template.header(TRACE_ID, traceId);
        }
    }

    private static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
