package com.demo.business.domain.trade.entity;

import com.demo.foundation.domain.user.UserRequestContext;
import com.demo.foundation.core.exception.BizException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 合并支付组聚合根。联结多张销售订单做合并支付：本次支付明细挂到主单，
 * 校验合并组应支付总额与已支付总额平衡后，逐单标记已支付/完成。
 */
@Data
public class MergePayGroup implements Serializable {

    /**
     * 加入合并支付组。把待合并订单挂到当前组下，主单号取第一单。
     */
    public void joinGroup(List<SalesOrder> joinOrderList, String userId) {
        if (CollectionUtils.isEmpty(joinOrderList)) {
            throw new BizException("订单不能为空");
        }
        SalesOrder order = joinOrderList.get(0);
        String orderNo = order.getSalesOrderEntity().getOrderNo();
        if (orderNo == null) {
            throw new BizException("订单号不允许为空");
        }
        if (mergePayGroupEntity.getPrimaryOrderNo() == null) {
            mergePayGroupEntity.setPrimaryOrderNo(orderNo);
            mergePayGroupEntity.setLastModifiedTime(LocalDateTime.now());
            mergePayGroupEntity.setLastModifiedBy(userId);
        }
        mergePayGroupEntity.setMergeGroup(false);
        for (SalesOrder salesOrder : joinOrderList) {
            if (StringUtils.equals(salesOrder.getSalesOrderEntity().getMergePayGroupId(),
                    this.getMergePayGroupEntity().getGroupNo())) {
                throw new BizException(salesOrder.getOrderNo() + " 订单不能重复加入合并组");
            }
            salesOrder.joinGroup(this, userId);
            if (!StringUtils.equals(salesOrder.getSalesOrderEntity().getOrderNo(),
                    mergePayGroupEntity.getPrimaryOrderNo())) {
                mergePayGroupEntity.setMergeGroup(true);
            }
        }
    }

    /**
     * 设置主单号。
     */
    public void setPrimary(SalesOrder salesOrder, String userId) {
        this.mergePayGroupEntity.setPrimaryOrderNo(salesOrder.getSalesOrderEntity().getOrderNo());
        this.mergePayGroupEntity.setLastModifiedBy(userId);
        this.mergePayGroupEntity.setLastModifiedTime(LocalDateTime.now());
    }

    /**
     * 合并支付结算。本次支付明细挂到主单，校验合并组已支付额 ≥ 应支付额，
     * 足额后逐单标记已支付。
     */
    public void settle(List<SalesOrder> orderList, List<Payment> newPaymentList,
                       UserRequestContext userRequestContext) {
        if (CollectionUtils.isEmpty(orderList)) {
            throw new BizException("结算订单不能为空");
        }
        String groupNo = mergePayGroupEntity.getGroupNo();
        String primaryOrderNo = mergePayGroupEntity.getPrimaryOrderNo();
        SalesOrder primaryOrder = null;
        for (SalesOrder order : orderList) {
            if (!StringUtils.equals(order.getSalesOrderEntity().getMergePayGroupId(), groupNo)) {
                throw new BizException(order.getOrderNo() + " 订单不属于该合并组，不允许结算");
            }
            if (order.getSalesOrderEntity().isSettled()) {
                throw new BizException(order.getOrderNo() + " 订单已结算，请勿重复操作");
            }
            if (StringUtils.equals(order.getOrderNo(), primaryOrderNo)) {
                primaryOrder = order;
            }
        }
        if (primaryOrder == null) {
            primaryOrder = orderList.get(0);
        }
        // 合并支付：本次支付明细统一挂到主单
        if (!CollectionUtils.isEmpty(newPaymentList)) {
            for (Payment payment : newPaymentList) {
                payment.setOrderNo(primaryOrder.getOrderNo());
            }
            primaryOrder.addPayList(newPaymentList, false);
        }
        // 合并组应支付总额与已支付总额
        BigDecimal groupTotal = BigDecimal.ZERO;
        BigDecimal groupPaid = BigDecimal.ZERO;
        for (SalesOrder order : orderList) {
            groupTotal = groupTotal.add(order.getSalesOrderEntity().getTotalAmount());
            groupPaid = groupPaid.add(order.paidAmount());
        }
        if (groupPaid.compareTo(groupTotal) < 0) {
            throw new BizException("合并支付金额不足，应支付 " + groupTotal + " ，已支付 " + groupPaid);
        }
        for (SalesOrder order : orderList) {
            order.settle(userRequestContext);
        }
        mergePayGroupEntity.setLastModifiedBy(userRequestContext.getUserId());
        mergePayGroupEntity.setLastModifiedTime(LocalDateTime.now());
    }

    /**
     * 合并完成：先合并支付结算，再逐单确认收货。
     */
    public void complete(List<SalesOrder> orderList, List<Payment> newPaymentList,
                         UserRequestContext userRequestContext) {
        settle(orderList, newPaymentList, userRequestContext);
        for (SalesOrder order : orderList) {
            if (!Boolean.TRUE.equals(order.isCompleted())) {
                order.complete(userRequestContext);
            }
        }
    }

    private MergePayGroupEntity mergePayGroupEntity;

    /**
     * 当前合并支付组号。
     */
    public String getGroupNo() {
        return mergePayGroupEntity == null ? null : mergePayGroupEntity.getGroupNo();
    }

    /**
     * 分配合并支付组号。
     */
    public void assignGroupNo(String groupNo) {
        mergePayGroupEntity.setGroupNo(groupNo);
    }
}
