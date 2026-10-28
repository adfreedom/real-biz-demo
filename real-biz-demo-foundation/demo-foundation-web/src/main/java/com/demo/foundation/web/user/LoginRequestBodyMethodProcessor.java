package com.demo.foundation.web.user;

import com.demo.foundation.domain.user.IClientContext;
import com.demo.foundation.domain.user.IUserContext;
import com.demo.foundation.domain.user.UserInfo;
import com.demo.foundation.domain.user.UserInfoContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 请求体参数解析器：在标准 {@code @RequestBody} 反序列化之后，对实现了
 * {@link IUserContext} 的请求体回填网关写入的登录信息（用户、酒店、班次、营业日期），
 * 对实现了 {@link IClientContext} 的请求体回填客户端IP与请求来源。
 * 通过在参数解析器链首插入，保证优先于标准 {@link RequestResponseBodyMethodProcessor} 命中。
 */
public class LoginRequestBodyMethodProcessor extends RequestResponseBodyMethodProcessor {

    public LoginRequestBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBody.class)
                && IUserContext.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        Object object = super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        assert servletRequest != null;
        UserInfo userInfo = UserInfoContext.get();
        if (userInfo != null && object instanceof IUserContext) {
            IUserContext loginInfo = (IUserContext) object;
            loginInfo.setUserId(userInfo.getUserId());
            loginInfo.setUserName(userInfo.getUsername());
        }
        if (object instanceof IClientContext) {
            IClientContext clientContext = (IClientContext) object;
            clientContext.setClientIp(NetWorkUtil.getIpAddr(servletRequest));
            clientContext.setRequestSource(LoginInfoUtils.getRequestSource(servletRequest));
        }
        return object;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return false;
    }
}
