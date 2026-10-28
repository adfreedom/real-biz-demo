package com.demo.business.domain.trade.entity;

import com.demo.foundation.domain.user.UserRequestContext;
import com.demo.foundation.core.exception.BizException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 销售订单聚合根。持有商品行、优惠、支付、费项等集合，
 * 提供加入合并支付组、算价、支付、结算（已支付）、完成（确认收货）等域行为。
 */
@Getter
public class SalesOrder implements Serializable {

    /**
     * 加入合并支付组。把当前订单挂到指定组号下。
     */
    public void joinGroup(MergePayGroup group, String userId) {
        salesOrderEntity.setMergePayGroupId(group.getMergePayGroupEntity().getGroupNo());
        salesOrderEntity.setLastModifiedBy(userId);
    }

    /**
     * 分配订单号与所属合并支付组号，并下推到各子实体，保证聚合内单号一致。
     */
    public void assignOrderNo(String orderNo, String groupNo) {
        salesOrderEntity.setOrderNo(orderNo);
        salesOrderEntity.setMergePayGroupId(groupNo);
        salesOrderEntity.setLastModifiedTime(LocalDateTime.now());
        if (itemList != null) {
            itemList.forEach(i -> i.setOrderNo(orderNo));
        }
        if (discountList != null) {
            discountList.forEach(d -> d.setOrderNo(orderNo));
        }
        if (paymentList != null) {
            paymentList.forEach(p -> p.setOrderNo(orderNo));
        }
        if (feeList != null) {
            feeList.forEach(f -> f.setOrderNo(orderNo));
        }
    }

    /**
     * 重算订单总金额 = 商品小计之和 − 优惠之和 + 费项之和，并写回主实体。
     */
    public void recalculateTotalAmount() {
        BigDecimal itemTotal = sum(itemList, OrderItem::getSubtotal);
        BigDecimal discountTotal = sum(discountList, Discount::getAmount);
        BigDecimal feeTotal = sum(feeList, Fee::getAmount);
        salesOrderEntity.setTotalAmount(itemTotal.subtract(discountTotal).add(feeTotal));
    }

    /**
     * 当前已支付总额（全部支付明细之和）。
     */
    public BigDecimal paidAmount() {
        return sum(paymentList, Payment::getPayMoney);
    }

    private static <T> BigDecimal sum(List<T> list, Function<T, BigDecimal> getter) {
        if (list == null || list.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return list.stream().map(getter).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 追加支付明细。check 为 true 时校验订单未结算；按支付单号去重。
     */
    public void addPayList(SalesOrder other) {
        addPayList(other.getPaymentList(), true);
    }

    public void addPayList(List<Payment> payList, boolean check) {
        if (check && salesOrderEntity.isSettled()) {
            throw new BizException("该订单已结算不允许追加支付");
        }
        if (this.paymentList == null) {
            this.paymentList = new ArrayList<>();
        }
        if (payList == null) {
            return;
        }
        for (Payment payment : payList) {
            if (!containPay(payment.getPayNo())) {
                this.paymentList.add(payment);
            }
        }
    }

    /**
     * 当前订单是否已包含指定支付单号。
     */
    public boolean containPay(String payNo) {
        if (getPaymentList() == null || StringUtils.isEmpty(payNo)) {
            return false;
        }
        return getPaymentList().stream().anyMatch(p -> StringUtils.equals(payNo, p.getPayNo()));
    }

    /**
     * 结算（标记已支付）。合并支付由 {@link MergePayGroup} 校验足额后逐单调用。
     * 订单视为足额已付，已支付金额记为订单总金额。
     */
    public void settle(UserRequestContext userRequestContext) {
        if (salesOrderEntity.isSettled()) {
            throw new BizException("该订单已结算不允许再次结算");
        }
        salesOrderEntity.setPaidAmount(salesOrderEntity.getTotalAmount());
        salesOrderEntity.setSettled(true);
        salesOrderEntity.setSettledTime(LocalDateTime.now());
        salesOrderEntity.setLastModifiedBy(userRequestContext.getUserId());
        salesOrderEntity.setLastModifiedTime(LocalDateTime.now());
    }

    /**
     * 完成订单（确认收货/交易完成）。先置为已支付，再标记完成。
     */
    public void complete(UserRequestContext userRequestContext) {
        if (salesOrderEntity.isCompleted()) {
            throw new BizException("已完成不允许再次完成");
        }
        if (!salesOrderEntity.isSettled()) {
            salesOrderEntity.setPaidAmount(salesOrderEntity.getTotalAmount());
            salesOrderEntity.setSettled(true);
            salesOrderEntity.setSettledTime(LocalDateTime.now());
        }
        salesOrderEntity.setCompleted(true);
        salesOrderEntity.setCompletedTime(LocalDateTime.now());
        salesOrderEntity.setLastModifiedBy(userRequestContext.getUserId());
        salesOrderEntity.setLastModifiedTime(LocalDateTime.now());
    }

    /**
     * 追加备注。override 为 true 时覆盖，否则追加。
     */
    public void setRemark(String remark, String userId, boolean override) {
        if (salesOrderEntity.isSettled()) {
            throw new BizException("该订单已结算不允许修改备注");
        }
        if (override) {
            salesOrderEntity.setRemark(remark);
        } else {
            appendRemark(remark);
        }
        salesOrderEntity.setLastModifiedBy(userId);
        salesOrderEntity.setLastModifiedTime(LocalDateTime.now());
    }

    private void appendRemark(String remark) {
        if (StringUtils.isEmpty(remark)) {
            return;
        }
        if (StringUtils.isEmpty(salesOrderEntity.getRemark())) {
            salesOrderEntity.setRemark(remark);
            return;
        }
        if (salesOrderEntity.getRemark().endsWith(remark)) {
            return;
        }
        salesOrderEntity.setRemark(salesOrderEntity.getRemark() + ";" + remark);
    }

    public List<Payment> getPaymentList() {
        if (paymentList == null) {
            paymentList = new ArrayList<>();
        }
        return paymentList;
    }

    public String getOrderNo() {
        return salesOrderEntity == null ? null : salesOrderEntity.getOrderNo();
    }

    public Boolean isCompleted() {
        return salesOrderEntity == null ? null : salesOrderEntity.isCompleted();
    }

    public Boolean isSettled() {
        return salesOrderEntity == null ? null : salesOrderEntity.isSettled();
    }

    public String getChannel() {
        return salesOrderEntity == null ? null : salesOrderEntity.getChannel();
    }

    public LocalDateTime getOrderTime() {
        return salesOrderEntity == null ? null : salesOrderEntity.getOrderTime();
    }

    public String getMemberId() {
        return salesOrderEntity == null ? null : salesOrderEntity.getMemberId();
    }

    public LocalDateTime getCompletedTime() {
        return salesOrderEntity == null ? null : salesOrderEntity.getCompletedTime();
    }

    public LocalDateTime getSettledTime() {
        return salesOrderEntity == null ? null : salesOrderEntity.getSettledTime();
    }

    public BigDecimal getTotalAmount() {
        return salesOrderEntity == null ? null : salesOrderEntity.getTotalAmount();
    }

    public BigDecimal getPaidAmount() {
        return salesOrderEntity == null ? null : salesOrderEntity.getPaidAmount();
    }

    @Setter
    private SalesOrderEntity salesOrderEntity;

    @Setter
    private List<OrderItem> itemList;

    @Setter
    private List<Discount> discountList;

    @Setter
    private List<Payment> paymentList;

    @Setter
    private List<Fee> feeList;
}
