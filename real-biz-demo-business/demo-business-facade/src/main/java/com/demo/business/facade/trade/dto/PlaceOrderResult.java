package com.demo.business.facade.trade.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 下单结果：每张销售订单对应一条结果，回显单号/组号/时间/商品概要。
 */
@Data
public class PlaceOrderResult {

    private String orderNo;
    private String mergePayGroupId;
    private String channel;
    private LocalDateTime orderTime;
    private String remark;
    private Integer itemCount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private List<String> goodsNames;
}
