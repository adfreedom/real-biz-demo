package com.demo.business.facade.trade.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 销售订单查询结果。
 */
@Data
public class TradeOrderDto {

    /** 销售订单号 */
    private String orderNo;
    /** 销售渠道 */
    private String channel;
    /** 下单时间 */
    private LocalDateTime orderTime;
    /** 会员ID */
    private String memberId;
    /** 会员姓名 */
    private String memberName;
    /** 订单总金额 */
    private BigDecimal totalAmount;
    /** 已支付金额 */
    private BigDecimal paidAmount;
    /** 是否已支付 */
    private Boolean settled;
    /** 支付时间 */
    private LocalDateTime settledTime;
    /** 是否已完成 */
    private Boolean completed;
    /** 完成时间 */
    private LocalDateTime completedTime;
    /** 商品行数 */
    private Integer itemCount;
}
