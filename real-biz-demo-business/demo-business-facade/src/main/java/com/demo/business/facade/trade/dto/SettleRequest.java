package com.demo.business.facade.trade.dto;

import com.demo.foundation.core.locker.ConcurrentKey;
import com.demo.foundation.domain.user.IUserContext;
import lombok.Data;

import java.util.List;

/**
 * 结算请求。携带操作人上下文与待结算的销售订单号列表，
 * 实现 {@link ConcurrentKey} 供分布式锁取键。
 */
@Data
public class SettleRequest implements IUserContext, ConcurrentKey {

    /** 操作人ID */
    private String userId;

    /** 合并支付组号 */
    private String mergePayGroupId;
    /** 待结算的销售订单号列表 */
    private List<String> orderNoList;
    /** 本次合并支付明细 */
    private List<PlaceOrderRequest.PaymentDto> paymentList;
    /** 备注 */
    private String remark;

    @Override
    public String getConcurrentKey() {
        return (orderNoList == null || orderNoList.isEmpty())
                ? String.valueOf(hashCode())
                : String.join(",", orderNoList);
    }
}
