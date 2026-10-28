package com.demo.foundation.web;

import com.demo.foundation.web.user.LoginHandlerInterceptor;
import com.demo.foundation.web.user.LoginUserHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web MVC 装配：注册登录信息拦截器，将网关写入的酒店编码、用户等登录信息
 * 在请求前置阶段落到线程上下文，请求结束后清除。
 * 同时注册 LoginUserHandlerMethodArgumentResolver，支持控制器以 @LoginUser 参数直接获取登录上下文。
 */
@Component
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Bean
    public LoginHandlerInterceptor loginHandlerInterceptor() {
        return new LoginHandlerInterceptor();
    }

    @Bean
    public LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver() {
        return new LoginUserHandlerMethodArgumentResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LoginHandlerInterceptor interceptor = loginHandlerInterceptor();
        registry.addInterceptor(interceptor).excludePathPatterns(interceptor.excludePathPatterns());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserHandlerMethodArgumentResolver());
    }
}
