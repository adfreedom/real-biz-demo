package com.demo.foundation.lock;

import java.lang.annotation.*;

/**
 * 并发过滤（分布式锁）注解。
 * 标注在应用服务方法上，由 ConcurrentInterceptor 切面在方法执行前以 Redisson 加锁。
 * 锁 key 来源：方法第一个参数若为 ConcurrentKey 取其 getConcurrentKey()，否则用参数对象 hashCode。
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConcurrencyFilter {

    /**
     * 获取锁的等待时间（毫秒），超时未拿到锁则抛 ConcurrentException。
     */
    int waitTime() default 100;

    /**
     * 锁租约时间（毫秒），-1 表示 Redisson 看门狗自动续期。
     */
    int leaseTime() default -1;

    /**
     * 锁前缀，默认取 spring.application.name。
     */
    String prefix() default "";
}
