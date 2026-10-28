package com.demo.foundation.web.user;

import com.demo.foundation.domain.user.UserInfo;
import com.demo.foundation.domain.user.UserInfoContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * 登录信息拦截器：从网关注入的请求头读取用户、酒店编码（租户），写入线程上下文；
 * 请求结束时清除，避免线程复用导致的上下文串号。
 */
public class LoginHandlerInterceptor implements HandlerInterceptor, ApplicationContextAware {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(request.getHeader("userId"));
        String username = request.getHeader("username");
        if (username != null && !username.isEmpty()) {
            userInfo.setUsername(URLDecoder.decode(username, StandardCharsets.UTF_8.name()));
        }
        String tenantId = request.getHeader("tenantId");
        if (tenantId != null) {
            userInfo.setTenantId(tenantId);
        }
        UserInfoContext.set(userInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserInfoContext.remove();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    }

    /**
     * 拦截器排除路径：内部接口不走登录信息写入。
     */
    public String[] excludePathPatterns() {
        return new String[]{"/inner/**"};
    }
}
