package com.demo.business.domain.trade.entity;

import com.demo.foundation.mybatis.entities.CreationAuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 订单优惠。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Discount extends CreationAuditedEntity {

    /** 订单号 */
    private String orderNo;
    /** 优惠类型（满减/优惠券/积分抵扣等） */
    private String type;
    /** 优惠金额 */
    private BigDecimal amount;
    /** 优惠原因 */
    private String reason;
}
