package com.demo.foundation.domain;

/**
 * 聚合根基类。
 * 业务领域聚合根（如 SalesOrder）继承此类，与基础设施层 PO 的 Entity 区分。
 */
public abstract class AggregateRoot {

    /**
     * 是否未持久化（新建）。由子类按主键判定。
     */
    public abstract boolean isTransient();
}
