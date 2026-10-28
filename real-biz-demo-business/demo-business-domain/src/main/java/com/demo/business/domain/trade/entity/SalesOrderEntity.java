package com.demo.business.domain.trade.entity;

import com.demo.foundation.mybatis.entities.CreationAuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 销售订单持久化数据。
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SalesOrderEntity extends CreationAuditedEntity {

    /** 销售订单号 */
    private String orderNo;
    /** 销售渠道（web/app/miniapp 等） */
    private String channel;
    /** 下单时间 */
    private LocalDateTime orderTime;
    /** 备注 */
    private String remark;
    /** 会员ID */
    private String memberId;
    /** 会员姓名 */
    private String memberName;
    /** 操作人id */
    private String userId;
    /** 合并支付组号 */
    private String mergePayGroupId;
    /** 订单总金额（商品小计 − 优惠 + 费项） */
    private BigDecimal totalAmount;
    /** 已支付金额 */
    private BigDecimal paidAmount;
    /** 是否已支付 */
    private boolean settled;
    /** 支付时间 */
    private LocalDateTime settledTime;
    /** 是否已完成（确认收货/交易完成） */
    private boolean completed;
    /** 完成时间 */
    private LocalDateTime completedTime;
}
