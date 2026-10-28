package com.demo.business.domain.trade.entity;

import com.demo.foundation.mybatis.entities.CreationAuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 订单商品行。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderItem extends CreationAuditedEntity {

    /** 订单号 */
    private String orderNo;
    /** 商品SKU */
    private String skuId;
    /** 商品名称 */
    private String goodsName;
    /** 数量 */
    private Integer quantity;
    /** 单价 */
    private BigDecimal unitPrice;
    /** 小计（单价 × 数量） */
    private BigDecimal subtotal;
}
