package com.demo.foundation.web;

import com.demo.foundation.web.user.LoginRequestBodyMethodProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过 {@link InitializingBean} 调整 Spring MVC 参数解析器顺序：
 * 将 {@link LoginRequestBodyMethodProcessor} 置于链首，保证其在标准
 * {@code RequestResponseBodyMethodProcessor} 之前命中 @RequestBody 请求体，
 * 以便在反序列化后回填网关写入的登录信息。
 */
@Configuration
public class RequestMappingHandlerAdapterWrap implements InitializingBean {

    @Resource
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    public LoginRequestBodyMethodProcessor loginRequestBodyMethodProcessor(List<org.springframework.http.converter.HttpMessageConverter<?>> messageConverters) {
        return new LoginRequestBodyMethodProcessor(messageConverters);
    }

    @Override
    public void afterPropertiesSet() {
        List<HandlerMethodArgumentResolver> argumentResolvers = requestMappingHandlerAdapter.getArgumentResolvers();
        assert argumentResolvers != null;
        List<HandlerMethodArgumentResolver> argumentResolverList = new ArrayList<>(argumentResolvers);
        argumentResolverList.add(0, loginRequestBodyMethodProcessor(requestMappingHandlerAdapter.getMessageConverters()));
        requestMappingHandlerAdapter.setArgumentResolvers(argumentResolverList);
    }
}
