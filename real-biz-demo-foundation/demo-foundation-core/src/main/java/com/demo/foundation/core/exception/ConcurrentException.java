package com.demo.foundation.core.exception;

/**
 * 并发异常。
 * 分布式锁获取失败时抛出，全局异常处理器返回"请勿重复操作"。
 */
public class ConcurrentException extends FriendlyException {

    public ConcurrentException(String message) {
        super(message);
    }
}
