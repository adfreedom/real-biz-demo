package com.demo.foundation.core.locker;

/**
 * 并发键。
 * 被 @ConcurrencyFilter 标注的方法，其第一个参数若实现此接口，
 * 则用 getConcurrentKey() 的值作为分布式锁的 key；否则用参数对象的 hashCode。
 */
public interface ConcurrentKey {

    String getConcurrentKey();
}
