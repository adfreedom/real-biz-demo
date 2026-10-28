package com.demo.business.domain.trade.repository;

import com.demo.business.domain.trade.entity.SalesOrder;

import java.util.List;

/**
 * 销售订单仓储端口：聚合差异落库与按单号加载。实现位于基础设施层。
 */
public interface SalesOrderRepository {

    /**
     * 批量保存销售订单聚合（主表 + 商品行/优惠/支付/费项各子集合差量保存）。
     */
    void saveChanges(List<SalesOrder> orders);

    /**
     * 按订单号加载聚合（含全部子集合），用于结算等读改场景。
     */
    List<SalesOrder> findByOrderNos(List<String> orderNos);
}
