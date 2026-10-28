package com.demo.foundation.lock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式锁自动配置。
 * 依据 spring.data.redis 配置创建单机版 RedissonClient（缺省时容器中无 RedissonClient 则跳过创建，
 * 由使用方自行提供）。ConcurrentInterceptor 依赖此 bean。
 */
@Configuration
public class DistributedLockerAutoConfiguration {

    private final RedisProperties redisProperties;

    public DistributedLockerAutoConfiguration(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    @ConditionalOnClass(RedissonClient.class)
    @ConditionalOnMissingBean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setPassword(redisProperties.getPassword())
                .setConnectionMinimumIdleSize(10)
                .setConnectionPoolSize(100)
                .setIdleConnectionTimeout(600000)
                .setSubscriptionConnectionMinimumIdleSize(10)
                .setSubscriptionConnectionPoolSize(100)
                .setTimeout((int) (redisProperties.getTimeout().getSeconds() * 1000L));
        config.setCodec(new StringCodec());
        config.setThreads(5);
        config.setNettyThreads(5);
        return Redisson.create(config);
    }
}
