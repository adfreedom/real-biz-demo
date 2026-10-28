package com.demo.foundation.mybatis.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.MySqlDialect;
import com.demo.foundation.mybatis.extension.CustomSqlInjector;
import com.demo.foundation.mybatis.interceptor.EntitySelectInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 自动配置。
 * - PaginationInnerInterceptor：分页支持（订单查询等依赖）
 * - CustomSqlInjector：注入 insertBatchSomeColumn（RepositoryUtils 批量落库依赖）
 * - EntitySelectInterceptor：SELECT 返回的实体自动 monitor()，配合 isModified() 做差量更新
 */
@Configuration
@ConditionalOnClass({MybatisPlusInterceptor.class})
public class MybatisPlusAutoConfiguration {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor(new MySqlDialect());
        pageInterceptor.setMaxLimit(5000L);
        interceptor.addInnerInterceptor(pageInterceptor);
        return interceptor;
    }

    /**
     * 注册为 MyBatis 插件，被 MybatisPlusAutoConfiguration 自动挂到 SqlSessionFactory。
     */
    @Bean
    public Interceptor entitySelectInterceptor() {
        return new EntitySelectInterceptor();
    }

    @Bean
    public MybatisPlusPropertiesCustomizer mybatisPlusPropertiesCustomizer() {
        return properties -> properties.getGlobalConfig().setSqlInjector(new CustomSqlInjector());
    }
}
