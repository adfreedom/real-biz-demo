package com.demo.business.domain.trade.entity;

import com.demo.foundation.core.exception.BizException;
import com.demo.foundation.mybatis.entities.CreationAuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * 订单支付明细。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Payment extends CreationAuditedEntity {

    public void setPaymentType(String paymentType) {
        if (StringUtils.isBlank(paymentType)) {
            throw new BizException("支付方式不能为空");
        }
        this.paymentType = paymentType;
    }

    /** 订单号 */
    private String orderNo;
    /** 支付单号 */
    private String payNo;
    /** 支付方式编码（alipay/wechat/balance 等） */
    private String paymentType;
    /** 支付金额 */
    private BigDecimal payMoney;
    /** 操作人id */
    private String userId;
    /** 备注 */
    private String remark;
}
