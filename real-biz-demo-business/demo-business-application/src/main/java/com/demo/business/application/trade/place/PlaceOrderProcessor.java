package com.demo.business.application.trade.place;

/**
 * 下单处理器。实现类由 Spring 收集，按 {@link #getSort()} 升序在下单处理器链中执行。
 */
public interface PlaceOrderProcessor extends Comparable<PlaceOrderProcessor> {

    /**
     * 处理上下文。
     */
    void process(PlaceOrderContext context);

    /**
     * 执行顺序，值小者先执行。
     */
    int getSort();

    @Override
    default int compareTo(PlaceOrderProcessor other) {
        return Integer.compare(this.getSort(), other.getSort());
    }
}
