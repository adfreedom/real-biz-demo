package com.demo.business.facade.trade.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 结算结果：每张销售订单对应一条结果，回显单号、支付与完成状态、金额。
 */
@Data
public class SettleResult {

    private String orderNo;
    private String channel;
    private Boolean settled;
    private Boolean completed;
    private LocalDateTime settledTime;
    private LocalDateTime completedTime;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
}
