package com.demo.foundation.web.user;

import com.demo.foundation.domain.user.UserContext;
import com.demo.foundation.domain.user.UserInfo;
import com.demo.foundation.domain.user.UserInfoContext;
import com.demo.foundation.domain.user.UserRequestContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录用户参数解析器：对 @LoginUser 标注的 UserContext 及其子类参数，
 * 从线程上下文（LoginHandlerInterceptor 写入）与请求头装配操作人上下文。
 * 与 LoginRequestBodyMethodProcessor（@RequestBody 回填）并行，互不依赖。
 */
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class)
                && UserContext.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        UserRequestContext loginInfo = new UserRequestContext();
        UserInfo userInfo = UserInfoContext.get();
        loginInfo.setUserId(userInfo.getUserId());
        loginInfo.setUserName(userInfo.getUsername());
        loginInfo.setClientIp(NetWorkUtil.getIpAddr(servletRequest));
        loginInfo.setRequestSource(LoginInfoUtils.getRequestSource(servletRequest));
        return loginInfo;
    }
}
