package com.demo.foundation.lock;

import com.demo.foundation.core.locker.ConcurrentKey;

/**
 * 默认 ConcurrentKey 实现。
 * 当无业务字段可用作锁 key 时，以类名+hashCode 兜底。
 */
public class DefaultConcurrentKey implements ConcurrentKey {

    @Override
    public String getConcurrentKey() {
        return this.getClass().getSimpleName() + this.hashCode();
    }
}
