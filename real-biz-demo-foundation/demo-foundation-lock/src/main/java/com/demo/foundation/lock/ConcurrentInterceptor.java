package com.demo.foundation.lock;

import com.demo.foundation.core.exception.ConcurrentException;
import com.demo.foundation.core.locker.ConcurrentKey;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 并发拦截切面。
 * 拦截 @ConcurrencyFilter 标注的方法，以 Redisson 分布式锁做并发互斥。
 * 该 bean 需被组件扫描（启动类扫描 com.demo.foundation.lock）。
 */
@Aspect
@Component
public class ConcurrentInterceptor {

    private static final String REDIS_SEPARATOR = ":";

    @Value("${spring.application.name:demo}")
    private String applicationName;

    private final RedissonClient redissonClient;

    public ConcurrentInterceptor(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(com.demo.foundation.lock.ConcurrencyFilter)")
    public Object intercept(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("方法参数不能为空");
        }

        String concurrentCode;
        if (args[0] instanceof ConcurrentKey) {
            ConcurrentKey concurrentKey = (ConcurrentKey) args[0];
            if (!StringUtils.hasLength(concurrentKey.getConcurrentKey())) {
                throw new IllegalArgumentException("concurrentKey 不能为空");
            }
            concurrentCode = concurrentKey.getConcurrentKey();
        } else {
            concurrentCode = args[0].getClass().getName() + args[0].hashCode();
        }

        Class<?> targetClass = point.getTarget().getClass();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = targetClass.getDeclaredMethod(signature.getName(), signature.getParameterTypes());
        ConcurrencyFilter annotation = method.getAnnotation(ConcurrencyFilter.class);

        String prefix = annotation.prefix();
        if (!StringUtils.hasLength(prefix)) {
            prefix = applicationName;
        }
        String lockKey = prefix + REDIS_SEPARATOR + concurrentCode;

        RLock lock = redissonClient.getLock(lockKey);
        if (lock.tryLock(annotation.waitTime(), TimeUnit.MILLISECONDS)) {
            try {
                return point.proceed();
            } finally {
                if (lock.isHeldByCurrentThread() && lock.isLocked()) {
                    lock.unlock();
                }
            }
        }
        throw new ConcurrentException("请勿重复操作");
    }
}
