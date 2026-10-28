package com.demo.business.domain.trade.repository;

import com.demo.business.domain.trade.entity.MergePayGroup;

/**
 * 合并支付组仓储端口：差异落库与按组号加载。实现位于基础设施层。
 */
public interface MergePayGroupRepository {

    /**
     * 保存合并支付组（主表差量保存）。
     */
    void saveChanges(MergePayGroup group);

    /**
     * 按组号加载合并支付组。
     */
    MergePayGroup findByGroupNo(String groupNo);
}
