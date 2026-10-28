package com.demo.business.application.trade.settle;

/**
 * 结算处理器。实现类由 Spring 收集，按 {@link #getSort()} 升序在结算处理器链中执行。
 */
public interface SettleProcessor extends Comparable<SettleProcessor> {

    /**
     * 处理上下文。
     */
    void process(SettleContext context);

    /**
     * 执行顺序，值小者先执行。
     */
    int getSort();

    @Override
    default int compareTo(SettleProcessor other) {
        return Integer.compare(this.getSort(), other.getSort());
    }
}
