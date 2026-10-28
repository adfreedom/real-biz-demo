package com.demo.business.domain.trade.entity;

import com.demo.foundation.mybatis.entities.CreationAuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 订单费项（运费、手续费、包装费等订单总额构成项）。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Fee extends CreationAuditedEntity {

    /** 订单号 */
    private String orderNo;
    /** 费项类型（shipping/handling/packaging 等） */
    private String type;
    /** 金额 */
    private BigDecimal amount;
    /** 备注 */
    private String remark;
}
