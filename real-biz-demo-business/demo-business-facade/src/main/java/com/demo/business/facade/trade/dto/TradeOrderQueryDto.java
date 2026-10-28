package com.demo.business.facade.trade.dto;

import com.demo.foundation.core.dto.PagedResultRequestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 销售订单分页查询请求。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TradeOrderQueryDto extends PagedResultRequestDto {

    /** 订单号（模糊） */
    private String orderNo;
    /** 会员ID */
    private String memberId;
    /** 销售渠道 */
    private String channel;
    /** 是否已支付 */
    private Boolean settled;
    /** 是否已完成 */
    private Boolean completed;
}
